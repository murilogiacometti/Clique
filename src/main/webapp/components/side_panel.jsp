<div id="control-panel">
	<div class="panel" id="user-panel">
		<h4><span><%= user.getName() %></span></h4>
	    <div class="panel-img" title="Change your profile picture" id="user-img-container"><img src="/clique/download_picture?id=<%= user.getId() %>" width="125" height="150" /></div>
        <div class="panel_details">
			interests 
			interests
			interests
			interests
        </div> <!-- /col-right-text -->
	</div>
    <div class="panel" id="invite-panel">
		<h4><span>Invite a friend!</span></h4>
        <div class="panel_details">
			<input id="invite-field" name="email" type="text" value="Type the email here"/>
			<input type="button" id="invite-submit" value="OK"/>
			<h5 id="invite-message"></h5>
        </div>
	</div>
</div>