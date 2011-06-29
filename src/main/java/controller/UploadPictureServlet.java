package clique.controller;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
//import model.*;

public class UploadPictureServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.setSession();
		
		int id = Integer.parseInt(request.getParameter("id"));
		FileItem Picture = null;
		List image = null;
		try {
			image = upload.parseRequest(request);
		} catch (FileUploadException e) {
			response.sendRedirect("");
		}

		Iterator itr = image.iterator();

		while (itr.hasNext()) {






	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
