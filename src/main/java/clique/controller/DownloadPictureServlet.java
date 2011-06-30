package clique.controller;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import clique.model.core.*;
import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

import org.hibernate.*;
import clique.model.util.*;


public class DownloadPictureServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		HttpSession session = request.getSession();
		int id_user = Integer.parseInt(request.getParameter("id"));
		
		Session context = HibernateUtil.openContext();
		User user = (User) Person.findById(new Integer(id_user), context);
		HibernateUtil.closeContext(context);
		
		
		if (user == null) {
			response.sendRedirect("/home");
		} else {

			try {
				String pictureformat = user.getImageType();
				
				File picturefile;
				
				if (pictureformat == null) {
					picturefile = new File("/tmp/clique/default.jpg");
					pictureformat = "jpg";

				} else {
					picturefile = new File("/tmp/clique/" + id_user + "." + pictureformat);
				}
				
				byte[] buf = new byte [1024];
				if (pictureformat == "jpg"){
					response.setContentType("image/jpeg");
				} else {	
					response.setContentType("image/" + pictureformat);
				}
				
				response.setContentLength((int)picturefile.length());
				
				FileInputStream in = new FileInputStream (picturefile);
				
				int count = 0;
				OutputStream os = response.getOutputStream();
				
				while ((count = in.read(buf)) >= 0) {
					os.write(buf, 0, count);
				}

				in.close();
				os.close();

			} catch (Exception e) {
				response.sendRedirect("/home");
				e.printStackTrace();
			}
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
