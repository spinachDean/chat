package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import util.Message;
import audio.AudioClient;

public class Client extends Thread {

	// private static String serverIp = "127.0.0.1";
	private static int serverPort = 8898;
	private Socket clientsSocket; // plays key role
	private PrintWriter pw; // for send data
	private InputStream in;//读入流
	private boolean login = false;// 是否登录了
	private BufferedReader reader;
	public String username;
	public AudioClient audioClient;
	public Client(String serverIp) throws UnknownHostException, IOException {
			clientsSocket = new Socket(serverIp, serverPort);
			pw = new PrintWriter(clientsSocket.getOutputStream(), true);
			reader = new BufferedReader(new InputStreamReader(
					clientsSocket.getInputStream()));
			start();
	}
	//群发消息
	public boolean sendMessage(String msg)
	{
		try{
		msg=msg.replaceAll("(\r\n|\r|\n|\n\r)","<hr>");//将内容的回车转换为<br>
		pw.println(msg);
		}
		catch(Exception e){
		return false;}
		return true;
	}
	public void sendMessage(String msg,String toUsername)
	{
		pw.println(toUsername+"<>"+msg.replaceAll("(\r\n|\r|\n|\n\r)","<hr>"));
	}
	//关闭服务器
	public void closeConnect()
	{
		closeAudio();
		try {
			clientsSocket.close();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
    //关闭音乐
    public void closeAudio()
    {
    	audioClient.stop();
    }
    //连接音乐
    public void openAudio()
    {
    	if(login)
    		audioClient=new AudioClient(username);//连接语音服务
    }
	public void run()
	{
		System.out.println("等待登录");
		while (!login)
			{
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			};// 当没有登录成功时就不会执行下面
		System.out.println("登录成功");
		audioClient=new AudioClient(username);//连接语音服务
		new ReadServer();// 开启聊天服务
	}
	//用户登录
	public boolean login(String username, String password) {
		pw.println(Message.login(username, password));
		try {
			if (reader.readLine().equals("success")) {
				login = true;
				this.username=username;
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return false;
	}
	public boolean register(String username, String password) {
		pw.println(Message.register(username, password));
		try {
			if (reader.readLine().equals("success")) {
				login = true;
				this.username=username;
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return false;
	}

	//读取信息的进程
	class ReadServer extends Thread {

		public ReadServer() {
			try {

				start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void run() {
			try {
				while (true) {
					String content = reader.readLine();
					if (content != null||!"".equals(content)) {
						System.out.println("服务器发来:"+content);
						Message.parseMessage(content);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public static void main(String[] args) throws Exception {
		Client client = new Client("127.0.0.1");
		Scanner sc = new Scanner(System.in);
		System.out.println("username:");
		String username = sc.next();
		System.out.println("password");
		String password = sc.next();
		if (client.login(username, password))
			System.out.println(client.login);
		else
			System.out.println("账号或密码错误");
		while(true)
		{
			String content=sc.next();
//			if(content.equals("close"))
//				client.closeAudio();
//			else if(content.equals("start"))
//				client.openAudio();
//			else
			client.sendMessage(content);
		}
	}
}
