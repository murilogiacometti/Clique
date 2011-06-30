package clique.controller;

import java.io.*;
import javax.servlet.*;
import javax.imageio.*;
import javax.imageio.stream.*;
import javax.servlet.http.*;
import clique.model.core.*;
import java.util.*;
import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

import clique.model.util.*;
import org.hibernate.*;


class ImageIdentifier {

	public static String getFormat(Object o){
		try {
			ImageInputStream iis =  ImageIO.createImageInputStream(o);
			Iterator i = ImageIO.getImageReaders(iis);

			if (!i.hasNext()) return null;
			
			ImageReader reader = (ImageReader)i.next();
			iis.close();
			
			return reader.getFormatName();
		} catch (Exception e) {
			return null;
		}
	}
}

public class UploadPictureServlet extends HttpServlet {

	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();

		User user = (User)  session.getAttribute("user");
		int id = user.getId();
		FileItem picture = null;
		List form = null;
		

		if (user == null) { 
			response.sendRedirect("/home");
		} else {

			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			if (isMultipart){
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				try {
					form = upload.parseRequest(request);
				} catch (FileUploadException e) {
					response.sendRedirect("/home");
				}
				
				Iterator itr = form.iterator();
				
				while (itr.hasNext()) {
					
				       	FileItem image = (FileItem) itr.next();

					if (image.getFieldName().equals("picture")) {
					picture = image;
					}
				}
			}

			File cliquedir = new File("/tmp/clique");

			if (!cliquedir.isDirectory()) 
				cliquedir.mkdir();

			File tmpFile = new File("/tmp/clique/tmp-" + picture.getName());
			try {
				picture.write(tmpFile);
			} catch (Exception e) {
				System.out.println("Could not upload temp file "+ picture.getName());
			}
		
			String pictureformat = ImageIdentifier.getFormat(tmpFile);

			if (user.getImageType() != null) {
				File currentPicture = new File("/tmp/clique/" + user.getId() + "." + user.getImageType());
				if (currentPicture.isFile()) {
					try {
						currentPicture.delete();
					} catch (Exception e) {
						System.out.println("Could not remove old user image: " + currentPicture.getName());
					}
				}
			}

			Session context = HibernateUtil.openContext();

			user.setImageType(pictureformat);
			user.merge(context);

			HibernateUtil.closeContext(context);

			File picturefile = new File("/tmp/clique/" + Integer.toString(id) + "." + pictureformat);
			tmpFile.renameTo(picturefile);

			response.sendRedirect("home");

		}	
	}	

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
