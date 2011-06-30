$(document).ready(function(){
	$("#invite-submit").button();
	
	$('.panel-img').hover(
		function() {
			$(this).css('cursor','pointer');
		}, 
		function() {
			$(this).css('cursor','default');
		}
	});
	
	//UPLOAD
	var sendPicture = function() {
		var pic_name = $("#picture").val();
		$("#picture-message").css("color", "grey");
		var parse_pic_name = /.*[.](gif|jpeg|jpg|png)/
		if(!pic_name.match(parse_pic_name)) {
			$("#picture-message").css("color", "red");
		} else {
			$("#picture-message").css("color", "grey");
			$("#send-picture-form").submit();
			$("#user-img").attr("src", "/clique/download_picture");
			$("#picture-dialog").dialog('close');
		}
	}
	
	$("#picture-dialog").dialog({
		autoOpen:false, 
		buttons:{
			"OK":sendPicture,
			"Cancel": function() {
				$("#picture-dialog").dialog('close');
			}
		}
	});
	
	$("#user-img-container").click(function(event) {
		$("#picture-dialog").dialog('open');
	});
	
	//INVITE
	$("#invite-submit").click(function(event) {
		var email = $("#invite-field").val();
		if (email.match(/.+@.+[.].+/)) {	
			$.ajax({
			   	type: "POST",
				url: "invite",
				dataType: "xml",
               	data: "email=" + email,
			   	success: function(xml){
					var status = $(xml).find('status').text();
					if (status == "OK") {
						$("#invite-message").html("Invitation sent!").css("color", "green");			   
					} else {
						$("#invite-message").html("An error ocurred, please try again.").css("color", "red");			   
					}
				}
			});
		} else {
			$("#invite-message").html("Not a valid email.").css("color", "red");
		}
	});
	
	//FIELD
	if ($("#invite-field").val() == "Type the email here") {
		$("#invite-field").css("color", "grey");
	}
	$("#invite-field").css("color", "grey");

	$("#invite-field").focus(function(event) {
		if("Type the email here" == $("#invite-field").val()) {
			$("#invite-field").attr("value", "").css("color", "black");	
		}
	});
	$("#invite-field").blur(function(event) {
		if($("#invite-field").val() == "") {
			$("#invite-field").attr("value", "Type the email here").css("color", "grey");	
		}
	});
});