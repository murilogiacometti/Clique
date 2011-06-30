package clique.controller;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import clique.model.core.*;
import clique.model.util.*;

import org.hibernate.*;

public class LoginServlet extends HttpServlet {

public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,java.io.IOException {

	String email = request.getParameter("username");
	String password = request.getParameter("password");
	HttpSession session = request.getSession();
	RequestDispatcher rd = null;
	String message = "";

	Session context = HibernateUtil.openContext();

	User user = User.findByEmailPassword(email, password, context);

	HibernateUtil.closeContext(context);

	message = "";

	if (user != null) {
		session.setAttribute("user", user);
	} else {
		message = "Invalid username or password";
	}

		request.setAttribute("message", message);
		rd = request.getRequestDispatcher("/home");
		rd.forward(request, response);
     }

public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
	doGet(request, response);
	}

}

