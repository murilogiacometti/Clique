<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ page isELIgnored = "false" %>
<%
	String name = (String) request.getParameter("name");
	if (name == null) {
		name = "";
	}
	String email = (String) request.getParameter("email");
	if (email == null) {
		email = "";
	}
	boolean logged = false;
	if (request.getParameter("user") != null) {
		logged = true;
	}	
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <meta http-equiv="content-language" content="en" />
    <link rel="stylesheet" type="text/css" href="css/reset.css" />
    <link rel="stylesheet" type="text/css" href="css/main.css" />
    <link rel="stylesheet" type="text/css" href="css/style.css" />
    <link rel="stylesheet" type="text/css" href="css/signup_form.css" />

	<link type="text/css" href="css/south-street/jquery-ui-1.8.14.custom.css" rel="Stylesheet" />	
	<script type="text/javascript" src="js/jquery-1.5.1.min.js"></script>
	<script type="text/javascript" src="js/jquery-ui-1.8.14.custom.min.js"></script>
	<script type="text/javascript" src="js/jquery.validate.js"></script>

	<script type="text/javascript" src="js/signup_form_validation.js"></script>
	<script type="text/javascript" src="js/tray.js"></script>

    <title>Clique - signup!</title>
</head>
<body>
<div id="main">
	<jsp:include page="components/header.jsp">
		<jsp:param name="login" value="<%= logged %>"/>
	</jsp:include>
    <!-- Promo -->
    <div id="col-top"></div>
    <div id="col" class="box">
        <div id="col-browser"><img src="images/init_image.png" width="255" height="177" alt="" /></div>      
        <div id="col-text">
            <h2 id="slogan"><span></span>Sign up.</h2>
			<%@ include file="components/signup.jsp"%>
        </div> <!-- /col-text -->    
    </div> <!-- /col -->
    <div id="col-bottom"></div>
    <hr class="noscreen" />
	<%@ include file="components/footer.jsp"%>
</div> <!-- /main -->
</body>
</html>
