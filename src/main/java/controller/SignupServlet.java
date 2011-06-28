package controller;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
//import model.*;

public class SignupServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	
	 HttpSession session = session.getSession();
	 String email = request.getParameter("email");
	 String name =  request.getParameter("name");
	 String address =  request.getParameter("address");
	 String typed_password = request.getParameter("typed_password");
	 String facebook = request.getParameter("facebook");

	 User user = new User();

	user.setEmail(email);
	user.setName(name);	
	user.setAddress(address);
	user.setPassword(typed_password);
	
	user.set();







		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
