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
			String  relevance = request.getParameter("relevances");
		
			String[] words = wordString.split(",");
				
			String[] relevances = relevance.split(",");
			
			Session context = HibernateUtil.openContext();
			
			for (int i = 0; i < words.length; i++) {
				System.out.println(words[i] + "\n" + relevances[i]);
				Word word = new Word(words[i],context);
				word.save(context);
				user.add(word,new Integer(Integer.parseInt(relevances[i])),context);
				user.merge(context);
			}

			HibernateUtil.closeContext(context);

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
