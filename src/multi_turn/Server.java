package multi_turn;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket socket = new ServerSocket(8888);
            System.out.println("服务器开启，等待客户端连接");

            Socket client_socket = socket.accept();
            System.out.println("客户端连接成功");

            DataInputStream in = new DataInputStream(client_socket.getInputStream());
            DataOutputStream out = new DataOutputStream(client_socket.getOutputStream());

            while(client_socket.isConnected()) {
                String request_from_client = in.readUTF();
                System.out.println("get request: " + request_from_client);

                String response_to_client = request_from_client + " [received]";
                out.writeUTF(response_to_client);
                out.flush();
            }

            in.close();
            out.close();
            client_socket.close();

        } catch (IOException e) {
            e.printStackTrace();  // modified later according to different circumstances
        }
    }
}
