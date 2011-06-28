<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
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

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <meta http-equiv="content-language" content="en" />
    <link rel="stylesheet" media="screen,projection" type="text/css" href="css/reset.css" />
    <link rel="stylesheet" media="screen,projection" type="text/css" href="css/main.css" />
    <link rel="stylesheet" media="screen,projection" type="text/css" href="css/style.css" />
    <link rel="stylesheet" media="print" type="text/css" href="css/print.css" />

	<script type="text/javascript" src="http://code.jquery.com/jquery-latest.js"></script>
	<script type="text/javascript" src="js/jquery.validate.js"></script>
	<script type="text/javascript" src="js/signup_form_validation.js"></script>
			
    <title>Clique</title>
</head>

<body>
<div id="main">
	<jsp:include page="components/header.jsp">
		<jsp:param name="login" value='true' />
	</jsp:include>

    <!-- Promo -->
    <div id="col-top"></div>
    <div id="col" class="box">
        
        <!-- Screenshot in browser (replace tmp/browser.gif) -->
        <div id="col-browser"><a href="#"><img src="images/init_image.png" width="255" height="177" alt="" /></a></div> 
        
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
