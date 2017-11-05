package view;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import util.ShowDialog;
import util.StyleUtil;
import chat.AdminClient;

public class Adminenter {

	private JFrame frame;
	private JTextField username;
	private JPasswordField password;
	
	private AdminClient client;
	private JTextField ip;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Adminenter window = new Adminenter();
					window.frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Adminenter() {
		initialize();
		StyleUtil.changeStyle(frame);
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setSize(369, 266);
		
		frame.setTitle("\u7BA1\u7406\u5458\u767B\u5F55");
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel label = new JLabel("\u8D26\u53F7");
		label.setBounds(24, 59, 81, 21);
		frame.getContentPane().add(label);
		
		username = new JTextField();
		username.setBounds(115, 56, 194, 27);
		frame.getContentPane().add(username);
		username.setColumns(10);
		
		JLabel lblEwLabel = new JLabel("\u7BA1\u7406\u5458\u767B\u5F55");
		lblEwLabel.setFont(new Font("方正舒体", Font.BOLD, 25));
		lblEwLabel.setBounds(92, 18, 136, 31);
		frame.getContentPane().add(lblEwLabel);
		
		JLabel label_1 = new JLabel("\u5BC6\u7801");
		label_1.setBounds(24, 96, 81, 21);
		frame.getContentPane().add(label_1);
		
		JButton button = new JButton("\u767B\u5F55");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(ip.getText()==null||ip.getText().equals(""))
					ShowDialog.showErrorMessage("服务器ip地址不能为空");
				else
				{
					try {
						client=new AdminClient(ip.getText());
					} catch (Exception e2) {
						ShowDialog.showErrorMessage("服务器地址不正确或服务器未启动");
					}
				}
				if(client.login(username.getText().trim(), password.getText().trim()))
				{
					//登录成功后
				}
				else
				{
					ShowDialog.showErrorMessage("账号或密码错误");
					
				}
				
			}
		});
		button.setBounds(22, 167, 123, 29);
		frame.getContentPane().add(button);
		
		JButton button_1 = new JButton("\u6E05\u7A7A");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		button_1.setBounds(186, 167, 123, 29);
		frame.getContentPane().add(button_1);
		password = new JPasswordField();
		password.setBounds(115, 93, 194, 27);
		frame.getContentPane().add(password);
		
		JLabel label_2 = new JLabel("\u670D\u52A1\u5668\u5730\u5740");
		label_2.setBounds(24, 133, 81, 21);
		frame.getContentPane().add(label_2);
		
		ip = new JTextField();
		ip.setColumns(10);
		ip.setBounds(115, 130, 194, 27);
		frame.getContentPane().add(ip);
	}
}
