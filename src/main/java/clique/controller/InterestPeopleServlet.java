package clique.controller;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import clique.model.core.*;
import clique.model.util.*;
import org.hibernate.*;
import java.util.*;

public class InterestPeopleServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
			HttpSession session = request.getSession();
			int id_interest = Integer.parseInt(request.getParameter("id"));
			int max = Integer.parseInt(request.getParameter("max"));
			
			Session context = HibernateUtil.openContext();
			
			RequestDispatcher rd = null;
			Word word = Word.findById(id_interest,context);
			
			ArrayList<PersonWord> people_relation =  word.getPeople(max,false,context);
			ArrayList<PersonWord> users_relation = word.getPeople(max,true,context);
			
			HibernateUtil.closeContext(context);
			
			request.setAttribute("people",people_relation);	
			request.setAttribute("users",users_relation);
			rd = request.getRequestDispatcher("people_interest.jsp");
			rd.forward(request,response);	


	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
