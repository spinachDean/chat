package view.user;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import util.Message;
import util.StyleUtil;
import chat.Client;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class UserChat {

	public JFrame chat;
	public JTextPane jTextPane;
	public String chatName;
	private JTextArea jTextArea;
	private Client client;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client client = new Client("127.0.0.1");
					client.login("陈思明","1020260");
					//client.login("苏天弘", "123456");
					UserChat window = new UserChat("苏天弘",client);
					//UserChat window = new UserChat("陈思明",client);
					window.chat.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UserChat(String chatName,Client client) {
		this.client=client;
		this.chatName=chatName;
		initialize();
		StyleUtil.changeStyle(chat);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		chat = new JFrame();
		chat.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				chat.setVisible(false);
			}
		});
		
		chat.setTitle(chatName);
		chat.setBounds(0,0, 500, 500);
		chat.getContentPane().setLayout(null);
		jTextPane=Message.getJTextPane(chatName);
		JScrollPane jsp=new JScrollPane(jTextPane);
	
		jsp.setBounds(10, 34, 464, 263);
		chat.getContentPane().add(jsp);
		JPanel selfuser = new JPanel();
		selfuser.setBounds(0, 298, 484, 164);
		chat.getContentPane().add(selfuser);
		selfuser.setLayout(null);
		
		JButton btnQuit = new JButton("quit");
		btnQuit.setBounds(298, 133, 70, 25);
		selfuser.add(btnQuit);
		//发送框
		jTextArea = new JTextArea();
		JButton send = new JButton("send");
		send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String msg=jTextArea.getText();
				client.
				sendMessage(msg,chatName);
				Document doc=jTextPane.getDocument();
				try {
					doc.insertString(doc.getLength(),"你：\n",StyleUtil.getNameStyle());
					doc.insertString(doc.getLength(),msg+"\n",null);
				} catch (BadLocationException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
			}
		});
		send.setBounds(404, 133, 70, 25);
		selfuser.add(send);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 35, 464, 88);
		selfuser.add(scrollPane);
		scrollPane.setViewportView(jTextArea);
	}
}
