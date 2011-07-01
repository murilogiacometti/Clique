$(document).ready(function(){
	var words = [];
	var relevances = [];
	
	$(".interest-word").click(function(e) {
		if ($(this).hasClass("selected-interest-word")) {
			$(this).removeClass("selected-interest-word");
			var index = words.indexOf($(this).text());
			words.splice(index, 1);
			relevances.splice(index, 1);
			console.log($(this).text());
		} else {
			$(this).addClass("selected-interest-word");
			words.push($(this).text());
			relevances.push($("input", this).val());
			console.log($(this).text());
		}
		console.log(words);		
		console.log(relevances);		
	});
	
	$("#register-words").button();
	$("#register-words").click(function(e) {
		$.ajax({
			async: false,
		  	type: "POST",
			url: "add_interest",
    		data: "words=" + words.join(",") + "&relevances=" + relevances.join(","),
		  	success: function(xml) {
				window.location = '/clique/main.jsp';
			}
		 });
	})	
});