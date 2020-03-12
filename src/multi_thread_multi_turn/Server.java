package multi_thread_multi_turn;

import javax.net.ServerSocketFactory;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {
        ServerSocketFactory factory = ServerSocketFactory.getDefault();
        try (ServerSocket server = factory.createServerSocket(8888)) {
            System.out.println("服务器已启动，等待客户端连接...");
            // Wait for connections.
            while (true) {
                Socket client = server.accept();
                // Start a new thread for a connection
                Thread t = new Thread(() -> worker_run(client));
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void worker_run(Socket client) {
        try (Socket clientSocket = client) {
            // 获取输入输出流
            DataInputStream d_input = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream d_output = new DataOutputStream(clientSocket.getOutputStream());

            while(client.isConnected()) {
                String msg_from_client = d_input.readUTF();
                if (msg_from_client.equals("end"))
                    break;
                System.out.println("受到来自客户端的信息：" + msg_from_client);
                d_output.writeUTF("我收到你的信息: " + msg_from_client);
            }
            d_input.close();
            d_output.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
