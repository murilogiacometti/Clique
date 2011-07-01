package controller;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import clique.model.core.*;
import clique.model.util.*;
import org.hibernate.*;


public class InterestSearchServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = Session.getSession();

		String  query = request.getParameter("query");
		
		Session context = HibernateUtil.openContext();
	
		ArrayList <Word> words  = Word.match(query,10,session);  

		HibernateUtil.closeContext(context);
		
		String xml = "<interests>";
		
		for (int i = 0; i < interest.size(); i++){
			xml += "<interest>" + words.get(i).getWord() + "</interest>";
		}
		
		xml += "</interests>";
		
		response.setContentType("application/xml");
		reponse.getWriter().write(xml);

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
