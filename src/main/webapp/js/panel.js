$(document).ready(function(){
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
	$("#signup-submit").click(function(event) {
		var email = $("#invite-field").val();
		alert(email);
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
	})
});