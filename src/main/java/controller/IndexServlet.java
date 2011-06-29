package clique.controller;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class IndexServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		HttpSession session = request.getSession();
		RequestDispatcher rd = null; 

		if (session.getAttribute("user") != null) {
			rd = request.getRequestDispatcher("/main.jsp");
			rd.forward(request,response);
		} else {
			rd = request.getRequestDispatcher("/init.jsp");
			rd.forward(request,response);
		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
