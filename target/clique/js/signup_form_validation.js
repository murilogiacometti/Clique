$(document).ready(function(){
	$("#signup_submit").attr("disabled", false);
    $("#signup_form").validate({
		rules: {
		    email: {
				required: true,
				email: true
			},
			name: "required",
			address: "required",
			typed_password: "required",
			retyped_password: {
				required: true,
				equalTo: "#signup_typed_password"
			}
		}
	});
});