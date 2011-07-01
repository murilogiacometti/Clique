<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ page import="clique.model.core.*" %>
<%@ page import="org.hibernate.*" %>
<%@ page import="java.util.*" %>

<% 
	User user  = (User)session.getAttribute("user"); 
	Session context = (Session) request.getAttribute("context");
	String interest = (String) request.getAttribute("interest");	
%>
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
    <link rel="stylesheet" type="text/css" href="css/general_search.css" />


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
			<h1 id="interest_label">People with interest:<%= interest %></h1>

			<h2 class="search-title">Users</h2>
			<div class="search" id="user_search">
			<%
				ArrayList<Person> users = (ArrayList<Person>)request.getAttribute("users");
				for(int i = 0; i < users.size(); i++) {
					ArrayList<PersonWord> words = users.get(i).getMostPopularWords(5, true, context);
				%>
					<div class="interest_item">
						<div class="search_img">
							<a href="/clique/profile?id=<%=users.get(i).getId()%>">
								<img width="45" height="55" src="/clique/download_picture?id=<%= users.get(i).getId() %>"/>
							</a>
						</div>
						<a href="/clique/profile?id=<%=users.get(i).getId()%>">
							<h3 class="search_name"><%=users.get(i).getName()%></h3>
						</a>
						<p><% for(int j = 0; j < words.size(); j++) {%>
							<%= words.get(j).getWord() %>
						<%}%></p>
					</div>
				<%}
			%>
			</div>
			<h2 class="search-title">People</h2>
			<div class="search" id="people_search">
			<%
				ArrayList<Person> people = (ArrayList<Person>)request.getAttribute("people");
				for(int i = 0; i < people.size(); i++) {
					ArrayList<PersonWord> words = people.get(i).getMostPopularWords(5, false, context);
			%>
					<div class="interest_item">
						<div class="search_img">
							<a href="/clique/profile?id=<%=people.get(i).getId()%>">
								<img width="45" height="55" src="/clique/download_picture?id=<%= people.get(i).getId()%>"/>
							</a>
						</div>
						<a href="/clique/profile?id=<%=people.get(i).getId()%>">
							<h3 class="search_name"><%=people.get(i).getName()%></h3>
						</a>
						<p><% for(int j = 0; j < words.size(); j++) {%>
							<%= words.get(j).getWord() %>
						<%}%></p>
					</div>
				<%}
			%>
			</div>
		</div>		
    </div> <!-- /col -->
    <div id="col-bottom"></div>
    <hr class="noscreen" />
	<%@ include file="components/footer.jsp"%>
</div> <!-- /main -->
</body>
</html>