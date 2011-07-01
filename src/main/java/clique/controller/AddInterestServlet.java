package clique.controller;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import clique.model.core.*;
import org.hibernate.*;
import clique.model.util.*;
 



public class AddInterestServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			
			HttpSession session = request.getSession();

			User user = (User) session.getAttribute("user");
			String wordString = request.getParameter("words");
			int  relevance = Integer.parseInt(request.getParameter("relevancias"));
			
			Word word = new Word(wordString);
			
			Session context = HibernateUtil.openContext();
			
			word.save(context);
			user.add(word,new Integer(relevance),context);
			user.merge(context);

			HibernateUtil.closeContext(context);			
				

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
