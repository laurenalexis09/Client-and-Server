import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ClassClient extends JFrame{

	public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {

		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		Socket netSock = null;
		int port = 0;
		String ips = " ";
		JPanel myPanel = new JPanel();

		JTextField portNum = new JTextField(10);
		JTextField ip = new JTextField(10);

		UIManager.put("OptionPane.background", new Color(204, 204, 255));
		UIManager.put("Panel.background", new Color(204, 204, 255));
		UIManager.put("OptionPane.okButtonText", "Submit");

		myPanel.setBackground(new Color(204, 204, 255));
		myPanel.add(new JLabel("Enter the IP Address: "));
		myPanel.add(ip);
		myPanel.add(Box.createHorizontalStrut(15));
		myPanel.add(new JLabel("Enter the port number: "));
		myPanel.add(portNum);

		int result = JOptionPane.showConfirmDialog(null, myPanel, "Welcome to the client. Enter the IP address and port you want to connect to.", JOptionPane.PLAIN_MESSAGE);

		if (result == JOptionPane.OK_OPTION) {
			String ports = portNum.getText();
			port = Integer.parseInt(ports);
			ips = ip.getText();
		}

		if(result == JOptionPane.CLOSED_OPTION) {
			return;
		}

		UIManager.put("OptionPane.okButtonText", "Okay");

		try {
			netSock = new Socket(ips, port);
		} catch (UnknownHostException e) {
			//	e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Unknown Host Error!", "An error has occcured", JOptionPane.PLAIN_MESSAGE);
			return;
		} catch (IOException e) {
			//	e.printStackTrace();
			JOptionPane.showMessageDialog(null, " Unknown port: "+port, "An error has occured", JOptionPane.PLAIN_MESSAGE);
			return;
		}
		JOptionPane.showMessageDialog(null,"Connected successfully to: "+"\n"+netSock.toString(), "Connected! [client side]", JOptionPane.PLAIN_MESSAGE);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy HH.mm.ss");
		LocalDateTime now = LocalDateTime.now();
		String currentTimeDate = dtf.format(now);
		
		PrintWriter fileOut = new PrintWriter(new FileOutputStream("ClientSide Info "+currentTimeDate+".txt"));
		
		InetAddress address = InetAddress.getLocalHost(); 
		String ipss = address.getHostAddress();
		ipss.toString();
		
		fileOut.println("Connection information: ");
		fileOut.println("Connected IP address: "+netSock.getInetAddress()+" |  Local IP address: "+ipss);
		fileOut.println("Connected port: "+ netSock.getPort()  + " | Local port: "+netSock.getLocalPort());
		fileOut.println("");
		fileOut.println("Socket information: ");
		fileOut.println(netSock.toString());
		System.out.println();

		UIManager.put("OptionPane.okButtonText", "Send Message");

		PrintWriter out;
		out = new PrintWriter(netSock.getOutputStream(), true);

		BufferedReader sockIn;
		sockIn = new BufferedReader(new InputStreamReader(netSock.getInputStream()));

		if(port == 80) {
			out.println("GET / HTTP/1.1");
			out.println();
			int ch;
			String text;
			String output = "H";

			File file = new File("bad.html");
			PrintWriter writer = new PrintWriter(file);

			while ((ch=sockIn.read())>0) {
				if (ch=='&') break;
				while ((ch=sockIn.read())>0) {
					String s = (char)ch + sockIn.readLine();
					output = output + s +"\n";
				}
				text = JOptionPane.showInputDialog(output + "\nEnter your message: ");
				out.println(text);
			}
			writer.write(output);
			writer.close();
			fileOut.close();
		}

		else {
			int ch = ' ';
			String text;
			String output ="Messages recieved from the server:\n ";


			while((ch=sockIn.read())>0) {
				if(ch == '&') break;
				String s = (char)ch + sockIn.readLine();
				output = output + s +"\n";

				text = JOptionPane.showInputDialog(output + "\nEnter your message: ");
				out.println(text);

			}
			fileOut.close();
		}
		UIManager.put("OptionPane.okButtonText", "Okay");
		JOptionPane.showMessageDialog(null, "Connection closed on port " + port, "End of connection to "+ips, JOptionPane.PLAIN_MESSAGE);
		try {
			netSock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}

	}
}
