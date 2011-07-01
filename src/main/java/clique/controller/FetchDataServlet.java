package clique.controller;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import clique.robot.*;
import clique.model.core.*;

public class FetchDataServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String query = request.getParameter("query");
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		File stop_words = new File("/tmp/clique/stop_words");

		if (user == null) {
			response.sendRedirect("/home");
		} else {
			try {
				
				if (!stop_words.isDirectory()) {
					ServletContext context = getServletContext();
					InputStream is = context.getResourceAsStream("/WEB-INF/stop_words");

					OutputStream out = new FileOutputStream(stop_words);
					byte buf[] = new byte[1024];

					int len;
					while ((len = is.read(buf)) > 0) {
						out.write(buf, 0, len);
					}

					out.close();
					is.close();
				}
			} catch (Exception e) {
				System.out.println("Could not save stop_words file to temp dir");
			}

			try {
				
				HashMap keywords = Robot.search(query, 5, stop_words);
				request.setAttribute("interests", keywords);
				
				response.sendRedirect("define_interests.jsp");
			} catch (Exception e) {
				System.out.println("Robot could not search query " + query);
				response.sendRedirect("/home");
			}

		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
