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
    //初始化socket
    public AudioClient(String name) {
        try {
        	this.name=name;
            socket = new Socket("localhost", 8899);//localhost可修改为连接服务端的IP地址
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void run() {
        MP3Player mp3Player = null;
        try{
        fout = socket.getOutputStream();
        pw = new PrintWriter(socket.getOutputStream(),true);//输出流
        }
        catch(Exception e)
        {
        	System.out.println("连接出现问题");
        }
        pw.println(name);
        try {
            din = new DataInputStream(socket.getInputStream());
            mp3Player=new MP3Player(din);
            mp3Player.start();
			while(play);//一直播放 直到选择结束
			mp3Player.stop();
        } catch (Exception ex) {
            System.out.println("服务器已关闭");
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
        		System.out.println("关闭出错");
        	}
        }
    }
    

}
