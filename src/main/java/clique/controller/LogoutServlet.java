package clique.controller;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;


public class LogoutServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException{

	   HttpSession session = request.getSession();

	   try {
		   session.invalidate();
	   } catch (Exception e) {
		   System.out.println("Could not invalidate session");
	   }

	   response.sendRedirect("/home");
	}


	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException{
		doGet(request, response);
	}
}
