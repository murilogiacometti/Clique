<%
	String name = (String) request.getParameter("name");
	if (name == null) {
		name = "";
	}
	String email = (String) request.getParameter("email");
	if (email == null) {
		email = "";
	}
%>

<form id="signup_form" action="signup" method="post">
  	<fieldset>  
		<br /><br />
		<div class="form_line">
			<div class="form_label"><label>Name:</label></div>
			<div class="form_input"><input id="start_signup_name" name="name" value="<%= name %>" type="text" /></div>
			<div class="message_field"></div>
		</div>
		<div class="form_line">
			<div class="form_label"><label>E-mail:</label></div>
			<div class="form_input"><input id="start_signup_email" name="email" value="<%= email %>"type="text" /></div>
			<div class="message_field"></div>
		</div>
		<div class="form_line">
			<div class="form_label"><label>Address:</label></div>
			<div class="form_input"><input id="start_signup_address" name="address" type="text" /></div>
			<div class="message_field"></div>
		</div>
		<div class="form_line">
			<div class="form_label"><label>Choose a password:</label></div>
			<div class="form_input"><input id="signup_typed_password" name="typed_password" type="password" /></div>
			<div class="message_field"></div>
		</div>
		<div class="form_line">
			<div class="form_label"><label>Re-enter password:</label></div>
			<div class="form_input"><input id="signup_retyped_password" name="retyped_password" type="password" /></div>
			<div class="message_field"></div>
		</div>
		
		<div id="facebook"><input type="checkbox" name="facebook" value="true" /> Use my Facebook account:
		<a href="#"><img src="images/facebook.gif"/></a></div>
		<div id="form_submit"><input id="signup_submit" type="submit" value="Sign up" disabled/></div>
	</fieldset>  
</form>
