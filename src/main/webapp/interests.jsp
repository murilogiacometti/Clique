<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ page import="clique.model.core.*" %>

<% User user  = (User)session.getAttribute("user"); %>
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
    <link rel="stylesheet" type="text/css" href="css/interests-search.css" />


	<link type="text/css" href="css/south-street/jquery-ui-1.8.14.custom.css" rel="Stylesheet" />	
	<script type="text/javascript" src="js/jquery-1.5.1.min.js"></script>
	<script type="text/javascript" src="js/jquery-ui-1.8.14.custom.min.js"></script>
	
	<script type="text/javascript" src="js/tray.js"></script>
	<script type="text/javascript" src="js/panel.js"></script>
	<script type="text/javascript" src="js/interests-search.js"></script>

    <title>Clique - interests</title>
</head>

<body>
<div id="main">
	<jsp:include page="components/header.jsp" />

	<div id="picture-dialog">
		<form name="send-picture-form" method="post" action="upload_picture" enctype="multipart/form-data">
			<label><strong>Picture:</strong></label>
            <input type="file" name="picture" id="picture"/>
			<h4 id="picture-message">The image must be in one of the following formats: jpeg, gif, png</h4>
		</form>
	</div>

    <div id="col-top"></div>
    <div id="col" class="box">
		<%@ include file="components/side_panel.jsp"%>
		<div id="interest-search">
			<div id="interest_query" class="ui-widget">
				<h1 id="interest_label">Type your interests!</h1>
				<input name="query" id="interest_input"/>
				<input id="interest-submit" type="button" value="Add"/>
			</div>
			<div id="interest-div">
				<div id="interest-relevance">
					<span>Set the relevance:</span>
					<div id="relevance"></div>
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