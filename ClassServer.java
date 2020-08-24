import java.awt.Color;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ClassServer {

	public static void main(String[] args) throws Exception {

		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		UIManager.put("OptionPane.background", new Color(204, 204, 255));
		UIManager.put("Panel.background", new Color(204, 204, 255));

		int socket = 1503;

		ServerSocket listen;
		Socket client;
		String text = "a";

		int n1 =  (int)((Math.random()*50)+1);
		int n2 =  (int)((Math.random()*50)+1);
		int n3 =  (int)((Math.random()*50)+1);
		int n4 =  (int)((Math.random()*50)+1);
		int n5 =  (int)((Math.random()*50)+1);

		UIManager.put("OptionPane.okButtonText", "Okay");

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy HH.mm.ss");
		LocalDateTime now = LocalDateTime.now();
		String currentTimeDate = dtf.format(now);

		PrintWriter fileOut = new PrintWriter(new FileOutputStream("ServerClient Conversation "+currentTimeDate+".txt"));

		listen = new ServerSocket(socket);

		JOptionPane.showMessageDialog(null, listen.toString()+"\n Listening for a new connection", "Establishing connection...", JOptionPane.PLAIN_MESSAGE);

		client = listen.accept();
		InetAddress address = InetAddress.getLocalHost(); 
		String ip = address.getHostAddress();
		ip.toString();
		String cli = client.toString();
		

		fileOut.println("Connection information: ");
		fileOut.println("Connected IP address: "+client.getInetAddress()+" | Local IP address: "+ip);
		fileOut.println("Connected port: "+ client.getPort()  + " | Local port: "+client.getLocalPort());
		fileOut.println();
		fileOut.println("Socket information:");
		fileOut.println(cli);
		fileOut.println();
		fileOut.println("Conversation: ");
		

		PrintWriter out = new PrintWriter(client.getOutputStream(), true);

		JOptionPane.showMessageDialog(null,"Connection accepted from: \n"+client.toString(), "Connection established! [server side]", JOptionPane.PLAIN_MESSAGE);

		BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		 DataOutputStream toClient = new DataOutputStream(client.getOutputStream());

		String output = "Messages recieved from the client:\n";
		out.println( "Congratulations, you have connected to Lauren's server! Current lucky numbers: "+n1+" "+n2+" "+n3+" "+n4+" "+n5);


		UIManager.put("OptionPane.okButtonText", "Send Message");

		while(text != null) {
			now = LocalDateTime.now();
			currentTimeDate = dtf.format(now);

			if(text.length() > 0) {
				if(text.charAt(0)=='&') break;
				if(text.equals(null)) {
					break;
				}
			}
			String s = in.readLine();
			String b = s;
			output = output + s +"\n";
			text = JOptionPane.showInputDialog(output+"\nEnter your message: ");
			out.println(text);
			//text = in.readLine();			

			b = s.toUpperCase() + '\n';
			toClient.writeBytes(b);
			
			fileOut.println(currentTimeDate);
			fileOut.println("Client said: "+s);
			fileOut.println("Server said: "+text);
			fileOut.println("Server packet read: "+b);
			fileOut.println("\n");
		}


		client.close();
		listen.close();
		fileOut.close();
	}
	
}
