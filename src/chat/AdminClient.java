package chat;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import util.Message;
import audio.AudioClient;
import bean.Music;

public class AdminClient extends Thread {

	// private static String serverIp = "127.0.0.1";
	private static int serverPort = 8898;
	private Socket clientsSocket; // plays key role
	private PrintWriter pw; // for send data
	private InputStream in;//读入流
	private boolean login = false;// 是否登录了
	private BufferedReader reader;
	public String username;
	public AudioClient audioClient;
	
	public AdminClient(String serverIp) throws UnknownHostException, IOException {
			clientsSocket = new Socket(serverIp, serverPort);
			pw = new PrintWriter(clientsSocket.getOutputStream(), true);
			reader = new BufferedReader(new InputStreamReader(
					clientsSocket.getInputStream()));
			start();
	}
	//群发消息
	public void sendMessage(String msg)
	{
		pw.println(msg);
	}
	public void sendMessage(String msg,String toUsername)
	{
		pw.println(toUsername+"<>"+msg);
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
	//管理员上传音乐到音乐服务器
	public boolean sendMusic(Music music) throws Exception
	{
		Socket socket=audioClient.socket;
		 DataOutputStream  dout = new DataOutputStream(socket.getOutputStream());
		 FileInputStream fin = new FileInputStream(music.getFile());
		 byte[] sendByte = new byte[1024];
		 int length;
		 dout.writeUTF(music.getName()+"-"+music.getSonger());
		 while((length = fin.read(sendByte, 0, sendByte.length))>0){
             dout.write(sendByte,0,length);
             dout.flush();
         }
		 if (dout != null)
             dout.close();
         if (fin != null)
             fin.close();
         if (socket != null)
             socket.close();
		return true;
	}
	//管理员登录
	public boolean login(String username, String password) {
		username="<admin>"+username;
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
					if (content != null) {
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
		AdminClient client = new AdminClient("127.0.0.1");
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
