package clique.controller;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import clique.model.core.*;

import javax.mail.*;
import javax.mail.internet.*;

class Emailer { 
	private static Properties config = new Properties();
	private static InputStream is;

	public static void setConfig() {
		config.put("mail.smtp.auth", "true");
		config.put("mail.smtp.starttls.enable", "true");
	}

	public static boolean send(String address, String senderName) {
		setConfig();
		Session session = Session.getInstance(config);
		MimeMessage message = new MimeMessage(session);

		String host = "smtp.gmail.com";
		int port = 587;
		String username = "cliqueappinviter@gmail.com";
		String password = "stayconnected";

		try {
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(address));
			message.setSubject("Join Clique!");
			message.setText("Hi, "+
							"\n\n\t"+senderName+" has invited you to join Clique!" +
							"\n\tCheck it out!" +
							"\n\nCheers!" +
							"\nThe Clique Team."
			);

			Transport transport = session.getTransport("smtp");
			transport.connect(host, port, username, password);

			transport.sendMessage(message, message.getAllRecipients());
			transport.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Could not send email to "+ address);
			return false;
		}

		return true;
	}
}

public class InviteFriendServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		String address = request.getParameter("email");

		String xmlResponse = "<status><return>";

		if (user == null) {
			response.sendRedirect("/home");
		} else {
			String sender = user.getName();
			if (Emailer.send(address, sender)) {
				xmlResponse += "OK</return></status>";
			} else {
				xmlResponse += "ERROR</return></status>";
			}
		}

		response.setContentType("application/xml");
		response.getWriter().write(xmlResponse);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
