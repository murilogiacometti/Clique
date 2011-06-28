<form id="signup_form" action="signup" method="post">
	<br /><br />
	<label>E-mail:</label>
	<input id="start_signup_email" name="email" value="<%= email %>"type="text" /><br /><br />
	<label>Name:</label>
	<input id="start_signup_name" name="name" value="<%= name %>" type="text" /><br /><br />
	<label>Address:</label>
	<input id="start_signup_address" name="address" type="text" /><br /><br />
	<label>Choose a password:</label>
	<input id="signup_typed_password" name="typed_password" type="password" /><br /><br />
	<label>Re-enter password:</label>
	<input id="signup_retyped_password" name="retyped_password" type="password" /><br /><br />
	<input type="checkbox" name="facebook" value="true" /> Use my Facebook account:
	<a href="#"><img src="images/facebook.gif"/></a> <br /><br />
	<input id="signup_submit" type="submit" value="Sign up" disabled/>
</form>