package view.admin;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;

import util.StyleUtil;
import chat.AdminClient;
import chat.Client;

public class AdminFrame {

	private JFrame frame;
	private Client client;
	/**
	 * Launch the application.
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException, IOException {
		//测试用

		AdminFrame window = new AdminFrame();
		window.frame.setVisible(true);
	}

	/**
	 * Create the application.
	 */
	public AdminFrame() {
		initialize();
		StyleUtil.changeStyle(frame);
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setSize(369, 266);
		frame.setTitle("管理页面");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton uploadbtn = new JButton("上传音乐");
		uploadbtn.setBounds(10, 10, 93, 23);
		frame.getContentPane().add(uploadbtn);
		JFileChooser filechooser = new JFileChooser();//创建文件选择器
        filechooser.setCurrentDirectory(new File("."));//设置当前目录
        filechooser.setAcceptAllFileFilterUsed(false);
        filechooser.setFileFilter(new FileFilter() {
			
			@Override
			public String getDescription() {
				return "只允许mp3音乐";
			}
			
			@Override
			public boolean accept(File f) {
			    if (f.isDirectory())return true;
			    return f.getName().endsWith(".mp3");
			}
		});
        int returnVal = filechooser.showOpenDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            System.out.println("You chose to open this file: " + filechooser.getSelectedFile().getName());
            System.out.println(filechooser.getSelectedFile().getAbsolutePath());

        }
	}
}
