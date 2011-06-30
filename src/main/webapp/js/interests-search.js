$(document).ready(function(){
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
	$( "#relevance" ).slider({
		value:5,
		min: 0,
		max: 10,
		step: 1,
		slide: function( event, ui ) {
			//$( "#amount" ).val( "$" + ui.value );
		}
	});
});