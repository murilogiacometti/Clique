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

	<script type="text/javascript" src="http://code.jquery.com/jquery-latest.js"></script>
	<script type="text/javascript" src="js/tray.js"></script>

    <title>Clique</title>
</head>

<body>
<div id="main">
	<jsp:include page="components/header.jsp">
		<jsp:param name="login" value="false" />
	</jsp:include>

    <!-- Promo -->
    <div id="col-top"></div>
    <div id="col" class="box">
        <div id="col-browser"><img src="images/init_image.png" width="255" height="177" alt="" /></div> 
        <div id="col-text">
            <h2 id="slogan"><span></span>Stay connected!</h2>
			<p>A clique is an inclusive group of people who share common interests, views, purposes, patterns of behavior, or ethnicity.</p> 
			<p>A clique as a reference group can be either normative or comparative. Membership in a clique is typically exclusive, and qualifications for membership may be social or essential to the nature of the clique.</p>
        </div> <!-- /col-text -->    
	    <%@ include file="components/start_signup.jsp"%>
    </div> <!-- /col -->
    <div id="col-bottom"></div>
    <hr class="noscreen" />
	<%@ include file="components/footer.jsp"%>
</div> <!-- /main -->
</body>
</html>
