import java.io.*;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;

class Emailer { 
	private static Properties config = new Properties();

	static {
		fetchConfig();
	}

	public static void fetchConfig() {
		InputStream is = null;
		try {
			is = new FileInputStream("src/main/webapp/WEB-INF/mail.properties");
			config.load(is);

		} catch (Exception e) {
			System.out.println("Could not load mail.properties file");
		}

		if (is != null) {
			try {
				is.close();
			} catch (Exception e) {}
		}
	}

	public static void refreshConfig() {
		config.clear();
		fetchConfig();
	}

	public static boolean send(String address, String senderName) {
		Session session = Session.getInstance(config);
		MimeMessage message = new MimeMessage(session);

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
			String host = config.getProperty("mail.smtp.host");
			int port = (new Integer(config.getProperty("mail.smtp.port"))).intValue();
			String username = config.getProperty("mail.smtp.user");
			String password = config.getProperty("mail.smtp.password");

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

public class EmailTest { 

	public static void main(String[] args) {  
		
		String address = "renato.mascosta@gmail.com";
		String sender = "Murilo";
		String xmlResponse = "<status><return>";

			if (Emailer.send(address, sender)) {
				xmlResponse += "OK</return></status>";
			} else {
				xmlResponse += "ERROR</return></status>";
			}
		System.out.println(xmlResponse);
	}

}
