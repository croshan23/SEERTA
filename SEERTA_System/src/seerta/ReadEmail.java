package seerta;
import java.util.*;
import java.io.*;
import javax.mail.*;
import javax.mail.event.*;
import javax.mail.internet.*;                     
/**
 *
 * @author Roshan Chaudhary
 */
public class ReadEmail {

    public ReadEmail(String ed, String us, String pa) {

	String protocol = "pop3";

	try {
	    // Get a Properties object
	    Properties props = System.getProperties();

		props.put("mail.pop.host", ed);
        props.put("mail.pop.socketFactory.port", 995);
		props.put("mail.pop.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.pop.auth", true);
		props.put("mail.pop.port", 995);

	    // Get a Session object
	    Session session = Session.getInstance(props, null);
	    session.setDebug(false);

	    // Get a Store object
                Store store = session.getStore(protocol);

		// Connect
                store.connect(ed, us, pa);

	    // Open the Folder
                Folder folder = store.getFolder("inbox");

	    // try to open read/write and if that fails try read-only
                try {
                    folder.open(Folder.READ_WRITE);
                } catch (MessagingException ex) {
                    folder.open(Folder.READ_ONLY);
                }

	    int totalMessages = folder.getMessageCount();
            int newMessages = folder.getNewMessageCount();

            System.out.println("User Authentication Completed\n");
			System.out.println("Total messages = " + totalMessages);
            System.out.println("New messages = " + newMessages);
            System.out.println("-------------------------------");

            // Attributes & Flags for all messages ..
            Message[] message = folder.getMessages();
            System.out.println(message.length);

            int x = message.length;
            int y = x-1;

            int sn = 1;

            for (int i = y; i >=0; i--) {
                
                String date;
                String from;
                String subject;
                String content;

                System.out.println("------------ Message " + (i+1) + " ------------");

                StringBuilder d = new StringBuilder();
                d.append(message[i].getSentDate());
                date = d.toString();
                System.out.println("SentDate : "+date);

                StringBuilder f = new StringBuilder();
                f.append(message[i].getFrom()[0]);
                String fr = f.toString();
                String fro = fr.replace("<","");
                from = fro.replace(">","");
                System.out.println("from : "+from);

                StringBuilder s = new StringBuilder();
                s.append(message[i].getSubject());
                subject = s.toString();
                System.out.println("Subject : "+subject);

                System.out.print("Message : ");
                InputStream stream = message[i].getInputStream();

                StringBuilder sb = new StringBuilder();
                while (stream.available() != 0) {
                     char msg = (char) stream.read();
                     sb.append(msg);
                }
                content = sb.toString();
                System.out.println("msg "+content);
                System.out.println();

                System.out.println("-------------COMMUNICATING WIHT OTHER FILE-------------\n");

                MailToSpeech mts = new MailToSpeech(sn, newMessages, totalMessages, date, from, subject, content);
                sn = sn+1;
                System.out.println(sn);
            }//for

            folder.close(true);
            store.close();
	}//try

        catch (Exception ex) {
	    System.out.println("Oops, got exception! " + ex.getMessage());
	    ex.printStackTrace();
	    System.exit(1);
	}

    }
}


