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
	private InputStream in;//������
	private boolean login = false;// �Ƿ��¼��
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
	//Ⱥ����Ϣ
	public void sendMessage(String msg)
	{
		pw.println(msg);
	}
	public void sendMessage(String msg,String toUsername)
	{
		pw.println(toUsername+"<>"+msg);
	}
	//�رշ�����
	public void closeConnect()
	{
		closeAudio();
		try {
			clientsSocket.close();
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
	}
    //�ر�����
    public void closeAudio()
    {
    	audioClient.stop();
    }
    //��������
    public void openAudio()
    {
    	if(login)
    		audioClient=new AudioClient(username);//������������
    }
	public void run()
	{
		System.out.println("�ȴ���¼");
		while (!login)
			{
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
			};// ��û�е�¼�ɹ�ʱ�Ͳ���ִ������
		System.out.println("��¼�ɹ�");
		audioClient=new AudioClient(username);//������������
		new ReadServer();// �����������
	}
	//����Ա�ϴ����ֵ����ַ�����
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
	//����Ա��¼
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
			// TODO �Զ����ɵ� catch ��
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
						System.out.println("����������:"+content);
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
			System.out.println("�˺Ż��������");
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
