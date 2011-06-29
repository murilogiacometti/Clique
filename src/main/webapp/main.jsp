<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="content-type" content="text/html;charset=utf-8" />
    <meta http-equiv="content-language" content="en" />
    <link rel="stylesheet" type="text/css" href="css/reset.css" />
    <link rel="stylesheet" type="text/css" href="css/main.css" />
    <link rel="stylesheet" type="text/css" href="css/style.css" />
    <link rel="stylesheet" type="text/css" href="css/start_signup_form.css" />
    <link rel="stylesheet" type="text/css" href="css/start_signup_form.css" />
    <link rel="stylesheet" type="text/css" href="css/panel.css" />

	<link type="text/css" href="css/ui-lightness/jquery-ui-1.8.14.custom.css" rel="Stylesheet" />	
	<script type="text/javascript" src="js/jquery-1.5.1.min.js"></script>
	<script type="text/javascript" src="js/jquery-ui-1.8.14.custom.min.js"></script>
	<script type="text/javascript" src="js/tray.js"></script>
	<script type="text/javascript" src="js/panel.js"></script>

    <title>Clique - main</title>
</head>

<body>
<div id="main">
	<jsp:include page="components/header.jsp">
		<jsp:param name="login" value="false" />
	</jsp:include>

	<div id="picture-dialog">
		<form name="send-picture-form" method="post" action="upload_picture" enctype="multipart/form-data">
			<label><strong>Picture:</strong></label>
            <input type="file" name="picture" id="picture"/>
			<h4 id="picture-message">The image must be in one of the following formats: jpeg, gif, png</h4>
		</form>
	</div>

    <div id="col-top"></div>
    <div id="col" class="box">
        <div id="control-panel">
			<div class="panel" id="user-panel">
				<h4><span>Joaozinho</span></h4>
			    <div class="panel-img" id="user-img-container"><img src="/clique/download_picture" width="125" height="150" /></div>
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
					<input type="button" id="signup-submit" value="OK"/>
					<h5 id="invite-message"></h5>
		        </div>
			</div>
		</div>
    </div> <!-- /col -->
    <div id="col-bottom"></div>
    <hr class="noscreen" />
	<%@ include file="components/footer.jsp"%>
</div> <!-- /main -->
</body>
</html>