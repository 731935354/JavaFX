package single_turn;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {

        try {
            //创建一个服务器socket，即serversocket,指定绑定的端口，并监听此端口
            ServerSocket serverSocket = new ServerSocket(8888);
            //调用accept()方法开始监听，等待客户端的连接
            System.out.println("***服务器即将启动，等待客户端的连接***");
            Socket client_socket = serverSocket.accept();  //

            //获取输入流，并读入客户端的信息
            InputStream in = client_socket.getInputStream(); //字节输入流
            DataInputStream d_in = new DataInputStream(client_socket.getInputStream());

            // 获取输出流，向客户端发送信息
            OutputStream out = client_socket.getOutputStream();
            DataOutputStream d_out = new DataOutputStream(client_socket.getOutputStream());

            String info = d_in.readUTF();  // 读取字符串，一定要与客户端发送函数一致
            System.out.println("我是服务器，客户端说：" + info);

            client_socket.shutdownInput(); //关闭输入流

            // 给客户端发送消息
            d_out.writeUTF("欢迎您! ");
            d_out.flush();

            client_socket.shutdownOutput();

            //关闭资源
            out.close();
            d_out.close();
            out.close();

            in.close();
            d_in.close();

            client_socket.close();
            serverSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}