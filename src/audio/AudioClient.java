package audio;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import util.ShowDialog;

/**
 * Created by zqq3436 on 2017/3/22.
 */
public class AudioClient extends Thread {
    public Socket socket;
    private DataInputStream din = null;
    private OutputStream fout;
    private PrintWriter pw;
    private String name;
    public boolean play=true;
    //��ʼ��socket
    public AudioClient(String name) {
        try {
        	this.name=name;
            socket = new Socket("localhost", 8899);//localhost���޸�Ϊ���ӷ���˵�IP��ַ
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void run() {
        MP3Player mp3Player = null;
        try{
        fout = socket.getOutputStream();
        pw = new PrintWriter(socket.getOutputStream(),true);//�����
        }
        catch(Exception e)
        {
        	System.out.println("���ӳ�������");
        }
        pw.println(name);
        try {
            din = new DataInputStream(socket.getInputStream());
            mp3Player=new MP3Player(din);
            mp3Player.start();
			while(play);//һֱ���� ֱ��ѡ�����
			mp3Player.stop();
        } catch (Exception ex) {
            System.out.println("�������ѹر�");
            mp3Player.stop();
        } finally {
        	try{
            if (fout != null)
               fout.close();
            if (din != null)
                din.close();
            if(pw!=null)
        		pw.close();
            if (socket != null)
                socket.close();
        	}
        	catch(Exception e)
        	{
        		System.out.println("�رճ���");
        	}
        }
    }
    

}
