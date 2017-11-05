package view.user;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

import util.Message;
import util.ShowDialog;
import util.StyleUtil;
import chat.Client;

public class MainFrame {

	public JFrame frmWelcome;
	private JTextField title;
	private JTextField chatroom;
	private JTextField user_online;
	private JTextField show_opion;
	public static JTextPane textPane = new JTextPane();
	public static Map<String,UserChat> chatting=new ConcurrentHashMap<String,UserChat>();
	private JTextArea textArea;// 评论发表框
	public static DefaultListModel<String> listModel = new DefaultListModel<String>();
	private Client client;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
				try {
					// 测试用
					Message.init(textPane, listModel);
					
					Client client = new Client("127.0.0.1");
					client.login("陈思明","1020260");
					//client.login("苏天弘", "123456");
					MainFrame window = new MainFrame(client);
					window.frmWelcome.setTitle("欢迎：" + client.username);
					window.frmWelcome.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
//			}
//		});
	}

	/**
	 * Create the application.
	 */
	public MainFrame(Client client) {
		this.client=client;
		initialize();
		StyleUtil.changeStyle(frmWelcome);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmWelcome = new JFrame();
		frmWelcome.setResizable(false);
		frmWelcome.setTitle("welcome");
		frmWelcome.setBounds(100, 100, 614, 563);
		frmWelcome.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmWelcome.getContentPane().setLayout(null);

		title = new JTextField();
		title.setEditable(false);
		title.setBounds(10, 10, 302, 43);
		title.setForeground(Color.RED);
		title.setFont(new Font("华文彩云", Font.PLAIN, 26));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setText("\u6B22\u8FCE\u6B23\u8D4F\u6B4C\u66F2");
		frmWelcome.getContentPane().add(title);
		title.setColumns(10);

		textPane.setEditable(false);
		JScrollPane content_online = new JScrollPane(textPane);
		content_online.setBounds(10, 54, 302, 248);
		frmWelcome.getContentPane().add(content_online);
		// 设置聊天

		JPanel chatting = new JPanel();
		chatting.setBounds(322, 10, 271, 709);
		frmWelcome.getContentPane().add(chatting);
		chatting.setLayout(null);

		chatroom = new JTextField();
		chatroom.setEditable(false);
		chatroom.setForeground(Color.RED);
		chatroom.setFont(new Font("华文新魏", Font.PLAIN, 23));
		chatroom.setHorizontalAlignment(SwingConstants.CENTER);
		chatroom.setBounds(49, 5, 155, 69);
		chatroom.setText("\u804A\u5929\u5BA4");
		chatting.add(chatroom);
		chatroom.setColumns(10);

		JPanel online_user = new JPanel();
		online_user.setBounds(0, 89, 256, 219);
		chatting.add(online_user);
		online_user.setLayout(null);

		user_online = new JTextField();
		user_online.setEditable(false);
		user_online.setBounds(51, 10, 165, 44);
		user_online.setText("\u5F53\u524D\u5728\u7EBF\u7528\u6237");
		user_online.setHorizontalAlignment(SwingConstants.CENTER);
		user_online.setFont(new Font("华文宋体", Font.ITALIC, 18));
		online_user.add(user_online);
		user_online.setColumns(10);

		final JList<String> list = new JList<String>(listModel);
		// 双击时的操作
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					String name=list.getSelectedValue();
					if(name.equals(client.username)){
						ShowDialog.showErrorMessage("不能和自己对话");
					}
					else
					{
						UserChat u=new UserChat(name, client);
						u.chat.setVisible(true);
					}
				}
			}
		});
		JScrollPane listScrollPane = new JScrollPane(list);
		listScrollPane.setBounds(51, 62, 165, 147);
		online_user.add(listScrollPane);

		JPanel show = new JPanel();
		show.setBounds(10, 318, 246, 207);
		chatting.add(show);
		show.setLayout(null);

		show_opion = new JTextField();
		show_opion.setEditable(false);
		show_opion
				.setText("\u8BF4\u8BF4\u5BF9\u6B4C\u66F2\u7684\u770B\u6CD5\u5427");
		show_opion.setBounds(15, 5, 221, 29);
		show.add(show_opion);
		show_opion.setColumns(10);
		textArea = new JTextArea();
		JScrollPane textScrollPane = new JScrollPane(textArea);
		textArea.setLineWrap(true);

		textScrollPane.setBounds(15, 44, 221, 114);
		show.add(textScrollPane);

		JButton submit = new JButton("\u53D1\u9001");
		// 点击发送后的事件
		submit.addActionListener(new ActionListener() {

			boolean sending;

			public void actionPerformed(ActionEvent e) {
				if (!check())
					return;
				if (!sending) {
					sending = true;
					if (client.sendMessage(textArea.getText())) {
						textArea.setText("");
						ShowDialog.showInfoMessage("评论成功");
					}
					sending = false;
				}
			}
		});
		submit.setBounds(58, 168, 123, 29);
		show.add(submit);
		JLabel lblNewLabel = new JLabel("\u672C\u7AD9\u7B80\u4ECB");
		lblNewLabel.setFont(new Font("华文新魏", Font.BOLD, 23));
		lblNewLabel.setBounds(98, 307, 105, 32);
		frmWelcome.getContentPane().add(lblNewLabel);

		JTextPane introduce = new JTextPane();
		introduce.setEditable(false);
		introduce.setFont(new Font("华文中宋", Font.PLAIN, 23));
		introduce
				.setText("\u672C\u7AD9\u4E13\u6CE8\u5BF9\u5404\u79CD\u7C7B\u578B\u6B4C\u66F2\u7684\u6536\u5F55\u4EE5\u53CA\u653E\u6620\uFF0C\u5728\u672C\u7AD9\u60A8\u5C06\u4F53\u9A8C\u5230\u7EDD\u5BF9\u7684\u97F3\u4E50\u76DB\u5BB4\uFF0C\u8FD9\u4E2A\u662F\u4E00\u4E2A\u53EA\u5728\u4E4E\u58F0\u97F3\u7684\u5E73\u53F0   -\u65E0\u654C\u8BA1\u7F51\u5C0F\u7EC4");
		introduce.setBounds(10, 339, 302, 171);
		frmWelcome.getContentPane().add(introduce);
	}

	public boolean check() {
		if (client == null || client.username == null) {
			ShowDialog.showErrorMessage("用户没有登录");
			return false;
		}
		return true;
	}
}
