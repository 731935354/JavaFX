package single_turn;

import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try {
            // 创建客户端socket建立连接，指定服务器地址和端口
            Socket socket = new Socket("127.0.0.1", 8888);

            // 获取输出流，向服务器端发送信息
            OutputStream out = socket.getOutputStream(); // 字节输出流
            DataOutputStream d_out = new DataOutputStream(socket.getOutputStream());

            // 获取输入流，读取服务器端的响应
            InputStream in = socket.getInputStream();
            DataInputStream d_in = new DataInputStream(socket.getInputStream());

            // 给客户端发送消息
            d_out.writeUTF("用户名：admin;密码：123");
            d_out.flush();
            socket.shutdownOutput();

            // 从服务器读取消息
            String info = d_in.readUTF();
            System.out.println("我是客户端，服务器说：" + info);

            socket.shutdownInput();

            // 关闭资源
            d_in.close();
            in.close();
            d_out.close();
            out.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
