<%@ page isELIgnored = "false" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>

<%
	String message = (String) request.getParameter("message");
%>

<!-- HEADER -->
<div id="header">
    <h1 id="logo"><a href="/clique" title="Homepage"><img src="tmp/logo.gif" alt="" /></a></h1>
    <hr class="noscreen" />
</div> 
<!-- /HEADER -->
<c:if test="${message != null}">
	<div id="message" class="box">
		<p><%= message %></p>
	</div>
</c:if>
<!-- TRAY -->
<div id="tray">
	<!-- SEARCH -->
	<div id="search" class="box">
	    <form action="query" method="get">
	        <div class="box">
	            <div id="search-input"><span class="noscreen">Search:</span><input type="text" size="30" name="query" value="Search" /></div>
	            <div id="search-submit"><input type="submit" value="Seach" /></div>
	        </div>
	    </form>
	</div>
	<!-- /SEARCH -->

	<!-- LOGIN -->
	<c:choose>
		<c:when test="${param.login eq 'false'}" >
			<form id="login_form" action="login" method="post">
				<div id="username" class="box">
			        <div id="username-input"><span class="noscreen">Username:</span><input type="text" size="30" name="username" value="username"/></div>
				</div>
				<div id="password" class="box">
			        <div id="password-input"><span class="noscreen">Password:</span><input type="password" size="30" name="password" value="password" /></div>
				</div>
				<div id="submit" class="box">
			        <div id="login-submit"><input type="submit" value="OK" /></div>
			    </div>
			</form>

		</c:when>
	    <c:otherwise>
			<div id="logout" class="box">
				<div id="logout_link"><a href="logout">Log out</a></div>
			</div>
		</c:otherwise>
	</c:choose>
	<!-- /LOGIN -->
		
	<hr class="noscreen" />
</div>
<!-- /TRAY -->