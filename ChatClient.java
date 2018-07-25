import java.awt.BorderLayout;
import java.awt.FlowLayout;

import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class ChatClient extends JFrame {
	// TODO Auto-generated constructor stub

	public static final String HOST = "localhost";
	private JPanel messagePanel;
	private JPanel editPanel;
	private TextArea messageArea;
	private TextArea logArea;
	private JPanel logPanel;
	private JTextField editTextField;
	private JButton sendButton;
	private JButton connectButton;
	private JButton disConnectButton;
	private JPanel statusPanel;
	private TextField IPAddr;
	private JLabel statusLabel;
	Socket socket;
	//		private JLabel sendLabel = new JLabel("Send:");

	PrintWriter pw;
	BufferedReader bf;

	public ChatClient() {
		// TODO Auto-generated constructor stub
		setLayout(new BorderLayout());
		statusPanel = new JPanel(new FlowLayout());
		statusPanel.add(new JLabel("Server IP Address:"));
		IPAddr = new TextField(25);
		statusPanel.add(IPAddr);
		connectButton = new JButton("Connect");
		disConnectButton = new JButton("Disconnect");
		statusPanel.add(connectButton);
		statusPanel.add(disConnectButton);
		statusLabel = new JLabel("");
		statusPanel.add(statusLabel);
		add(statusPanel,BorderLayout.NORTH);
		messagePanel = new JPanel();
		messageArea = new TextArea(25, 80);
		messagePanel.add(messageArea);
		add(messagePanel,BorderLayout.CENTER);
		editPanel = new JPanel(new FlowLayout());
		editPanel.add(new JLabel("Send text:"));
		editTextField = new JTextField(40);
		editPanel.add(editTextField);
		sendButton = new JButton("enter");
		editPanel.add(sendButton);
		add(editPanel, BorderLayout.SOUTH);


		setResizable(false);
		setTitle("Chat Client");
		pack();
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		if (socket == null) {
			try {
				socket = new Socket(HOST, 8000);
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				System.err.println("server is not online.");
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		new Thread(new Runnable() {	
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					bf = new BufferedReader( new InputStreamReader(socket.getInputStream()));
					String string;
					while ((string = bf.readLine()) != null) {
						messageArea.append(string + "\n");
					}
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();;

		try {
			pw = new PrintWriter(socket.getOutputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		editTextField.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				pw.println(editTextField.getText());
				pw.flush();
				messageArea.append(new Date() + " => "+ socket.getInetAddress().getHostAddress() +" : "+ editTextField.getText()+ "\n");
				editTextField.setText("");
				//System.out.println(socket.isClosed());
			}
		});

		sendButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				pw.println(editTextField.getText());
				pw.flush();
				messageArea.append(new Date() + " => "+ socket.getInetAddress().getHostAddress() +" : "+ editTextField.getText()+ "\n");
				editTextField.setText("");
				//System.out.println(socket.isClosed());
			}
		});
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				new ChatClient();
			}
		});

	}

}
