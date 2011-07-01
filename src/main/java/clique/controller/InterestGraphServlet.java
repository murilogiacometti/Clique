package clique.controller;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import clique.model.util.*;
import org.hibernate.*;
import java.util.*;
import clique.model.core.*;


public class InterestGraphServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
			HttpSession session = request.getSession();	
			String xml = "";
			int id_user, number_max;
			try {
				
				id_user = Integer.parseInt(request.getParameter("id"));
				number_max = Integer.parseInt(request.getParameter("max")); 

				int relevance = 0;
				ArrayList<PersonWord> people = null; 
				xml = "<edges>";
				String name = null;
				String interest = null;
				int id_people = 0;
				int number_people = 0;

				Session context = HibernateUtil.openContext();
	
				Person user = Person.findById(new Integer(id_user),context);
				ArrayList<PersonWord> user_interest_relevance = user.getMostPopularWords(number_max,context);

				for (int i = 0; i < user_interest_relevance.size(); i++){
					
					people = user_interest_relevance.get(i).getWord().getPeople(number_max,context);
					number_people = people.size();
					
					for (int j = 0; j < number_people; j++){
						
						relevance = people.get(j).getScore();
						name = people.get(j).getPerson().getName();
						interest = user_interest_relevance.get(j).getWord().getWord();
						id_people = people.get(j).getPerson().getId();	

						if (id_people != id_people) {
							xml += "<edge><person> <name>" + name + "</name>";
							xml += "<id>" + id_people + "</id></person>";
							xml += "<interest>" + interest + "</interest>";
							xml += "<relevance>" + relevance + "</relevance></edge>";	
						}
					
					}	
				}

				for (int i = 0; i < user_interest_relevance.size(); i++){
					
					name = user.getName();
					interest = user_interest_relevance.get(i).getWord().getWord();
					relevance = user_interest_relevance.get(i).getScore();
					xml += "<edge><person><name>" + name + "</name>";
					xml += "<id>"+ id_user + "</id>";
					xml += "</person>";
					xml += "<interest>" + interest + "</interest></edge>";	
				}
				
				xml += "</edges>";

				HibernateUtil.closeContext(context);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("No valid parameters given: 	" + request.getParameter("id") + ", " + request.getParameter("max"));
			}
			
			response.setContentType("application/xml");
			response.getWriter().write(xml);

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
