import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class ChatServer extends JFrame {

	private JPanel messagePanel;
	private JPanel editPanel;
	private TextArea messageArea;
	public static TextArea logArea;
	private JPanel logPanel;
	private JTextField editTextField;
	private JButton sendButton;
	private final static int SERVERPORT = 8000;
	PrintWriter pw;
	//	private JLabel sendLabel = new JLabel("Send:");

	public ChatServer() {
		// TODO Auto-generated constructor stub
		setLayout(new GridLayout(2, 1));
		messagePanel = new JPanel();
		messagePanel.setLayout(new BorderLayout());
		messageArea = new TextArea(20, 60);
		messagePanel.add(messageArea);
		add(messagePanel);
		editPanel = new JPanel(new FlowLayout());
		editPanel.add(new JLabel("Send text:"));
		editTextField = new JTextField(60);
		editPanel.add(editTextField);
		sendButton = new JButton("enter");
		editPanel.add(sendButton);
		logPanel = new JPanel(new BorderLayout());
		logPanel.add(editPanel, BorderLayout.NORTH);
		logArea = new TextArea();
		logArea.setEditable(false);
		logPanel.add(logArea,BorderLayout.CENTER);
		add(logPanel);

		setResizable(false);
		setTitle("Chat Server");
		pack();
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					ServerSocket serverSocket = new ServerSocket(SERVERPORT);
					logArea.append("Server started at " + new Date() + "\n");
					while (true) {
						Socket socket = serverSocket.accept();
						logArea.append("Start new thread for " + socket.getInetAddress() + " ,Hostname: " + socket.getInetAddress().getHostName()+ "\n");
						HandAClient task = new HandAClient(socket);
						new Thread(task).start();

						pw = new PrintWriter(socket.getOutputStream());

						sendButton.addActionListener(new ActionListener() {						
							@Override
							public void actionPerformed(ActionEvent e) {
								// TODO Auto-generated method stub
								pw.println( new Date() + " => " + "Server: " + editTextField.getText());
								pw.flush();
								messageArea.append( new Date() + " => " + "Server: " + editTextField.getText() + "\n");
								editTextField.setText("");
							}
						});
						
						editTextField.addActionListener(new ActionListener() {					
							@Override
							public void actionPerformed(ActionEvent e) {
								// TODO Auto-generated method stub
								pw.println( new Date() + " => " + "Server: " + editTextField.getText());
								pw.flush();
								messageArea.append( new Date() + " => " + "Server: " + editTextField.getText() + "\n");
								editTextField.setText("");
							}
						});
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();	
	}

	class HandAClient implements Runnable{

		private Socket socket;
		public HandAClient(Socket socket) {
			// TODO Auto-generated constructor stub
			this.socket = socket;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//BufferedReader br= new BufferedReader(new DataInputStream(socket.getInputStream()));
			try {
				//					DataInputStream dis = new DataInputStream(socket.getInputStream());
				//					BufferedReader br = BufferedReader
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String string;
				while ((string = br.readLine()) != null) {
					messageArea.append(new Date() + " => " + socket.getInetAddress().getHostAddress() + " : "+ string + "\n");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				new ChatServer();
				//logArea.append("Server started...\n");
			}
		});

	}

}
