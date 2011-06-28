package controller;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
//import model.*;

public class LoginServlet extends HttpServlet {

public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,java.io.IOException {

	String user = request.getParameter("username");
	String password = request.getParameter("password");
	HttpSession session = request.getSession();
	RequestDispatcher rd = null;
	String message = "";


	if (true) {
		rd = request.getRequestDispatcher("/main.jsp");
		rd.forward(request, response);
	
	} else {
		rd = request.getRequestDispatcher("/init.jsp");	
		rd.forward(request, response);
		message = "Invalid username or password";
	}

	request.setAttribute("message", message);
     }

public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
	doGet(request, response);
	}

}

