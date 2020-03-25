package JavaFXClientServer.Client;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientListener implements Runnable {
    private String ip;
    private int port;

    static DataInputStream in;
    static DataOutputStream out;

    public ClientListener(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void run() {
        try {
            System.out.println("试图连接服务器 ip: " + this.ip + " port: " + this.port + " ...");
            Socket socket = new Socket(this.ip, this.port);
            System.out.println("成功连接服务器");

            System.out.println("获取输入输出流...");
            out = new DataOutputStream(socket.getOutputStream());
            out.flush();
            in = new DataInputStream(socket.getInputStream());
            System.out.println("获取输入输出流成功");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* 给服务器发送消息，(严格的一发一收形式，需要与服务器的逻辑相对应) */
    public static String sendMsg(String message) {
        try {
            out.writeUTF(message);  // 给服务器发送request [对应服务器读取逻辑]
            System.out.println("向服务器发送信息: " + message);
            return in.readUTF();  // 从服务器读取response [对应服务器发送逻辑]
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "empty response";
    }
}
