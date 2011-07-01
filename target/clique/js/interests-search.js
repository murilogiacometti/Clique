$(document).ready(function(){
	var relevance = 5;	
	//items = ["icmc", "usp", "eesc","eeee", "computação", "engenharia"];
	$("#interest_input").autocomplete({
		//source: items,
		source: function(req, add) {
            var interests = [];
			$.ajax({
				async: false,
			  	type: "POST",
				url: "search_interest",
				dataType: "xml",
        		data: "query=" + req.term,
			  	success: function(xml) {
					$(xml).find('interest').each(function() {
	        			interests.push(this.text());
					});
				 }
			 });
			 add(interests);
		},
		minLength: 2
	});
	$("#interest-submit").click(function() {
		$.ajax({
			async: false,
		  	type: "POST",
			url: "add_interest",
			dataType: "xml",
    		data: "words=" + req.term + "&relevances=" + relevance,
		  	success: function(xml) {
				window.location = '/clique/main.jsp';
			 }
		 });
	})
	
	$( "#relevance" ).slider({ value:5, min: 0, max: 10, step: 1,
		stop: function( event, ui ) {
			relevance = $( ".selector" ).slider( "option", "value" );
		}
	});
});