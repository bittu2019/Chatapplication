package mynewchatserver;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javazoom.jl.player.Player;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author HCL
 */
public class Server implements ActionListener{

    JFrame jf;
    JTextArea jta;
    JTextField jtf;
    JScrollPane jsp;
    ServerSocket serverSocket;
    InetAddress inet_Address;
    DataInputStream dis;
    DataOutputStream dos;
    Socket socket;

    Thread thread=new Thread()
    {
        int count=0;
        @Override
        public void run()
        {
            while(true)
            {
                readMessage();
                count++;
                System.out.println(count);
            }
        }
    };
    public Server() {
        jf=new JFrame("Server");
        jf.setSize(600, 500);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Font font=new Font("Arial", 1, 20);
        jta=new JTextArea();
        jta.setFont(font);
        jta.setEditable(false);
        jsp=new JScrollPane(jta);
        jf.add(jsp);
        jtf=new JTextField();
        jtf.setEditable(false);
        jtf.addActionListener(this);
        jf.add(jtf, BorderLayout.SOUTH);
        jf.setVisible(true);
    }
    public void waitingForClient()
    {
        String ipaddress=getIpAddress();
        try {
            serverSocket=new ServerSocket(5050);
            jta.setText("To connect with server, please provide IP Address : "+ipaddress);
            socket=serverSocket.accept();
            jta.setText("connected\n");
            jtf.setEditable(true);
        } catch (Exception e) {
        }
    }
    public String getIpAddress()
    {
        String ip_address="";
        try {
            inet_Address=InetAddress.getLocalHost();
            ip_address=inet_Address.getHostAddress();   
        } catch (Exception e) {
        }
        return ip_address;
    }
     public void readMessage()
    {
        try {
            String mymessage=dis.readUTF();
            showMessage(mymessage);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public void showMessage(String myMessage) {
       jta.append(myMessage+"\n");
       soundplay();
       
    }
    public void sendMessage(String message)
    {
        try {
              dos.writeUTF(message);
              dos.flush();
//              dos.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public void setInputOutput()
    {
        
        try {
             dis=new DataInputStream(socket.getInputStream());
             dos=new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            System.out.println(e);
        }
        thread.start();
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==jtf)
        {
            
            sendMessage(jtf.getText());
            jta.append(jtf.getText()+"\n");
            jtf.setText("");
        }
    }

    private void soundplay() {
        try {
            File file_name=new File("src\\Sound\\tune.mp3");
            FileInputStream fis=new FileInputStream(file_name.getAbsolutePath());
            Player p=new Player(fis);
            p.play();
//            URL soundbyte = new File("C:\\Users\\HCL\\Documents\\NetBeansProjects\\MyChatApplication\\src\\Sound\\mysound.wav").toURI().toURL();
//            java.applet.AudioClip clip = java.applet.Applet.newAudioClip(soundbyte);
//            clip.play();
        } catch (Exception e) {
            System.out.println(e);
        }
}
}

