$(document).ready(function(){

	$("input:submit").button();

	if ($("#username_field").val() == "username") {
		$("#username_field").css("color", "grey");
	}
	$("#search_field").css("color", "grey");

	$("#username_field").focus(function(event) {
		if("username" == $("#username_field").val()) {
			$("#username_field").attr("value", "");
			$("#username_field").css("color", "black");	
		}
	});
	$("#username_field").blur(function(event) {
		if($("#username_field").val() == "") {
			$("#username_field").attr("value", "username");
			$("#username_field").css("color", "grey");	
		}
	});
	$("#password_field").focus(function(event) {
		if("password" == $("#password_field").val()) {
			$("#password_field").attr("value", "");
		}
	});
	$("#password_field").blur(function(event) {
		if($("#password_field").val() == "") {
			$("#password_field").attr("value", "password");
		}
	});	
	$("#search_field").focus(function(event) {
		if("search" == $("#search_field").val()) {
			$("#search_field").attr("value", "");
			$("#search_field").css("color", "black");	
		}
	});
	$("#search_field").blur(function(event) {
		if($("#search_field").val() == "") {
			$("#search_field").attr("value", "search");
			$("#search_field").css("color", "grey");	
		}
	});
});
