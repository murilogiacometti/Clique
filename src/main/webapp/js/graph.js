(function($){
	var nodes = {};

  var Renderer = function(canvas){
		var interests = {};
	  var dom = $(canvas);
    var canvas = $(canvas).get(0);
    var ctx = canvas.getContext("2d");
    var particleSystem;

    var _vignette = null;
    var selected = null,
        nearest = null,
        _mouseP = null;


    var that = {
	
      init:function(system){
        //
        // the particle system will call the init function once, right before the
        // first frame is to be drawn. it's a good place to set up the canvas and
        // to pass the canvas size to the particle system
        //
        // save a reference to the particle system for use in the .redraw() loop
        particleSystem = system

        // inform the system of the screen dimensions so it can map coords for us.
        // if the canvas is ever resized, screenSize should be called again with
        // the new dimensions
        particleSystem.screenSize(canvas.width, canvas.height) 
        particleSystem.screenPadding(80) // leave an extra 80px of whitespace per side
        
        // set up some event handlers to allow for node-dragging
        that.initMouseHandling()
      },
      
      redraw:function(){
        // 
        // redraw will be called repeatedly during the run whenever the node positions
        // change. the new positions for the nodes can be accessed by looking at the
        // .p attribute of a given node. however the p.x & p.y values are in the coordinates
        // of the particle system rather than the screen. you can either map them to
        // the screen yourself, or use the convenience iterators .eachNode (and .eachEdge)
        // which allow you to step through the actual node objects but also pass an
        // x,y point in the screen's coordinate system
        // 
        ctx.fillStyle = "white"
        ctx.fillRect(0,0, canvas.width, canvas.height)
        
        particleSystem.eachEdge(function(edge, pt1, pt2){
          // edge: {source:Node, target:Node, length:#, data:{}}
          // pt1:  {x:#, y:#}  source position in screen coords
          // pt2:  {x:#, y:#}  target position in screen coords

          // draw a line from pt1 to pt2
          ctx.strokeStyle = "rgba(124,252,0, .333)"
          ctx.lineWidth = 3;
          ctx.beginPath();
          ctx.moveTo(pt1.x, pt1.y);
          ctx.lineTo(pt2.x, pt2.y);
          ctx.stroke();
		 			ctx.fillStyle = "#ffffff"; // text color

        })
        particleSystem.eachNode(function(node, pt){
					var img;
					var w = 30;
					if (node.data.type == "person") {
						img = new Image();
						img.src = 'images/fabio1.gif';
						ctx.drawImage(img, pt.x-img.width/2, pt.y-img.height/2);
					} else {
						var word = node.data.desc;
						ctx.fillStyle = "rgba(124,252,0,1)"
	
						ctx.font = "bold 20px sans-serif";
						var i = ctx.measureText(word.replace(/\s/g, "\u0020"));          
	
						ctx.beginPath();
						ctx.fillRect(pt.x - i.width/2 - 20, pt.y, i.width + 35,w)
					  ctx.closePath();
					  ctx.stroke();
					  ctx.fill();

						ctx.fillStyle = "#0000ff"; // text color					
						ctx.fillText(word, pt.x-i.width/2, pt.y+20);
						
					}
        })    			
      },
      
      initMouseHandling:function(){
        // no-nonsense drag and drop (thanks springy.js)
        var dragged = null;
		
        // set up a handler object that will initially listen for mousedowns then
        // for moves and mouseups while dragging
        var handler = {
					moved:function(e){
						var i;
            var pos = $(canvas).offset();
            _mouseP = arbor.Point(e.pageX-pos.left, e.pageY-pos.top)
            nearest = particleSystem.nearest(_mouseP);

           if (!nearest.node) return false

           selected = (nearest.distance < 50 && nearest.node.data.type == "person") ? nearest : null
           if (selected){
           }
           else{

           }
            
            return false
          },
          clicked:function(e){
						var i;
            var pos = $(canvas).offset();
            _mouseP = arbor.Point(e.pageX-pos.left, e.pageY-pos.top)
            nearest = particleSystem.nearest(_mouseP);
            dragged = particleSystem.nearest(_mouseP);

           	if (!nearest.node) return false

           	selected = (nearest.distance < 50 && nearest.node.data.type == "person") ? nearest : null

           //se arrastar nao acessar
						if (selected && selected.node.data.type == "person"){
							window.location = '/clique/init.jsp';
						} else {
           		window.location = '/clique/main.jsp';
						}
	
            // while we're dragging, don't let physics move the node
      		  nearest.node.fixed = true

 						$(canvas).unbind('mousemove', handler.moved);
            $(canvas).bind('mousemove', handler.dragged)
            $(window).bind('mouseup', handler.dropped)
												
            return false
          },
   
       	  dragged:function(e){
            var pos = $(canvas).offset();
            var s = arbor.Point(e.pageX-pos.left, e.pageY-pos.top)

            if (dragged && dragged.node !== null){
              var p = particleSystem.fromScreen(s)
              dragged.node.p = p
            }

            return false
          },

          dropped:function(e){
            if (dragged===null || dragged.node===undefined) return
            if (dragged.node !== null) dragged.node.fixed = false
            dragged.node.tempMass = 1000
            dragged = null

            $(canvas).bind('mousemove', handler.moved);
            $(canvas).unbind('mousemove', handler.dragged)
            $(window).unbind('mouseup', handler.dropped)
            _mouseP = null
            return false
          }
        }
        
        // start listening
        $(canvas).mousedown(handler.clicked);
        $(canvas).bind('mousemove', handler.moved);

      },
      
    }

    return that
  }    

  $(document).ready(function(){
		var i, j;
    var sys = arbor.ParticleSystem(1000, 600, 0.5) // create the system with sensible repulsion/stiffness/friction
    sys.parameters({gravity:true}) // use center-gravity to make the graph settle nicely (ymmv)
    sys.renderer = Renderer("#graph") // our newly created renderer will have its .init() method called shortly by sys...

		$.ajax({
			type: "POST",
			url: "interest_graph",
			/*context: sys,*/
			dataType: "xml",
      data: "id=" /*+usuario*/,
			success: function(xml){
				$(xml).find('edge').each( function(){	
					var node = {}
					node["nodeName"] = $(this).find("id");
					node["data"]["type"] = "person";
					node["data"]["interests"] = node["data"]["interests"]?node["data"]["interests"].push($(this).find("interest").text()):[];
					nodes[$(this).find("id")] = node;
					
					sys.addNode($(this).find("id").text(), {type:"person", name:$(this).find("name").text()});
					sys.addNode($(this).find("interest").text(), {type:"interest"});									
					sys.addEdge($(this).find("id").text(), $(this).find("interest").text());				
				});
			}
		});		
  })
})(this.jQuery);

/*
	<edges>
		<edge>
			<person>
				<name>nome</name>
				<id>1234654</id>
			</person>
			<interest>palavra</interest>
		</edge>
	</edges>

*/