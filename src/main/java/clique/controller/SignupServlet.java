package clique.controller;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import clique.model.core.*;
import clique.model.util.*;

import org.hibernate.*;
import org.hibernate.cfg.*;

public class SignupServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	
		 HttpSession session = request.getSession();
		 String name =  request.getParameter("name");
		 String email = request.getParameter("email");
		 String address =  request.getParameter("address");
		 String typed_password = request.getParameter("typed_password");
		 String facebook = request.getParameter("facebook");

		 Boolean useFacebook = new Boolean(facebook);

	

		 Session context = HibernateUtil.openContext();

		 User user = new User();

		user.setName(name);	
		user.setEmail(email);
		user.setAddress(address);
		user.setPassword(typed_password);
		user.setFacebook(useFacebook);

		System.out.println("1");
		user.save(context);
		System.out.println("2");

		HibernateUtil.closeContext(context);

		RequestDispatcher dispatcher = request.getRequestDispatcher("/");
		dispatcher.forward(request, response);
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
