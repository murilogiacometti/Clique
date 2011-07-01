<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ page import="clique.model.core.*" %>
<%@ page import="java.util.*" %>


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
    <link rel="stylesheet" type="text/css" href="css/cloud.css" />


	<link type="text/css" href="css/south-street/jquery-ui-1.8.14.custom.css" rel="Stylesheet" />	
	<script type="text/javascript" src="js/jquery-1.5.1.min.js"></script>
	<script type="text/javascript" src="js/jquery-ui-1.8.14.custom.min.js"></script>
	<script type="text/javascript" src="js/arbor.js"></script>
	<script type="text/javascript" src="js/arbor-tween.js"></script>
	
	<script type="text/javascript" src="js/tray.js"></script>
	<script type="text/javascript" src="js/cloud.js"></script>

    <title>Clique - <% if (user != null) out.println(user.getName()); %></title>
</head>

<body>
<div id="main">
	<jsp:include page="components/header.jsp" />

    <div id="col-top"></div>
    <div id="col" class="box">
		<h1> What we (probably) know about you: </h1>
		<h4> Click the word related with you!</h4>
		<div id="tag-cloud">
		<%
			HashMap interests = (HashMap)request.getAttribute("interests");
			Set keys = interests.keySet();
			Iterator i = keys.iterator();
			while(i.hasNext()) {
				String key = (String) (((Map.Entry) i.next()).getKey());
				int value = ((Integer) interests.getKey(key)).intValue();
		%>
			<span class="interest-word" id="score<%= value %>">
				<%= key %>
				<input class="relevance-field" type="hidden" value="<%= value %>"/>
			</span>
			<!--<span class="interest-word score6">javascript</span>
				<span class="interest-word score8">bottom</span>
				<span class="interest-word score9">about</span>
				<span class="interest-word score7">main</span>
				<span class="interest-word score10">people</span>
				<span class="interest-word score6">javascript</span>
				<span class="interest-word score10">people</span>
				<span class="interest-word score7">main</span>
				<span class="interest-word score9">about</span>
				<span class="interest-word score8">bottom</span> -->
		<%  
			}
		%>
		</div>
		<div id="register-words-div"><input type="button" id="register-words" value="Register"/></div>
    </div> <!-- /col -->
    <div id="col-bottom"></div>
    <hr class="noscreen" />
	<%@ include file="components/footer.jsp"%>
</div> <!-- /main -->
</body>
</html>