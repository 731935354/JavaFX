package JavaFXClientServer.Server;

import JavaFXClientServer.JsonIO.JsonDict;
import org.json.JSONObject;

import javax.net.ServerSocketFactory;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListener implements Runnable {
    private int port;
    private JsonDict dict;

    public ServerListener(int port, String dictPath) {
        this.port = port;
        this.dict = new JsonDict(dictPath);
    }

    public void run() {
        ServerSocketFactory factory = ServerSocketFactory.getDefault();
        try (ServerSocket server = factory.createServerSocket(this.port)) {
            System.out.println("服务器已在端口"+ this.port +"启动，等待客户端连接...");
            // Wait for connections.
            while (true) {
                System.out.println("已进入循环...");
                Socket client = server.accept();
                System.out.println("收到连接请求，已和客户端建立连接");
                // Start a new thread for a connection
                Thread t = new Thread(() -> worker_run(client));
                t.start();
                System.out.print("已为客户端创建工作线程");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void worker_run(Socket client) {
        try (Socket clientSocket = client) {
            // 获取输入输出流
            DataInputStream d_input = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream d_output = new DataOutputStream(clientSocket.getOutputStream());
            System.out.println("获取客户端输入输出流成功");

            while(client.isConnected()) {
                String msg_from_client = d_input.readUTF();  // 读取客户端消息
                if (msg_from_client.equals("end"))
                    break;
                System.out.println("收到来自客户端的信息：" + msg_from_client);  // add || word || meaning
                ServerController.getInstance().showText(msg_from_client);

                // 处理客户端请求
                String[] results = msg_from_client.split("-");
                String type = results[0];
                String word = results[1];
                String meaning = results[2];
                System.out.println(type);
                System.out.println(word);
                System.out.println(meaning);
                // 添加单词
                if (type.equals("add")) {
                    boolean addSuccess = this.dict.addWord(word, meaning);
                    if (addSuccess) {
                        String response = word + " 已经添加成功";
                        d_output.writeUTF(response);
                        System.out.println(response);
                    }
                }

                // 删词，查词
                else {
                    String response = "我收到你的信息: " + msg_from_client;
                    // 给客户端发回回复
                    d_output.writeUTF(response);
                    System.out.println(response);
                }


            }
            d_input.close();
            d_output.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
