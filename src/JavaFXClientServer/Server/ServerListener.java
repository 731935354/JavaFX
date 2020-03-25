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

    // 构造方法，在ServerListener对象创建时调用
    public ServerListener(int port, String dictPath) {
        this.port = port;
        this.dict = new JsonDict(dictPath);
        // 显示当前词典
        ServerController.getInstance().showDict(dict.toString());
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
                // 总体就干了三件事
                // 1. 读取客户端请求 [对应客户端的发送逻辑]
                // 2. 根据请求进行操作(解析请求，实际逻辑，比如添加单词)
                // 3. 给客户端发送回复 [对应客户端的读取逻辑]

                // 1. 读取客户端消息
                String msg_from_client = d_input.readUTF();  // 例子：add-apple-a kind of fruit
                if (msg_from_client.equals("end"))
                    break;
                System.out.println("收到来自客户端的信息：" + msg_from_client);  // add || word || meaning
                ServerController.getInstance().showText(msg_from_client);

                // 2. 处理客户端请求, 解析request
                String[] results = msg_from_client.split("-");
                String type = results[0];
                String word = results[1];
                String meaning = results[2];

                System.out.println(type);
                System.out.println(word);
                System.out.println(meaning);

                // 添加单词
                if (type.equals("add")) {
                    // 单词太短，拒绝添加，并发回回复
                    if (word.length() < 1) {
                        String response = "word too short";
                        d_output.writeUTF(response);
                        continue;
                    }
                    // this.dict是一个对象，this.dict = x
                    // x.functionName() 是在调用X这个类里面定义的functionName方法
                    boolean addSuccess = this.dict.addWord(word, meaning);

                    // 更新server的词典状态
                    ServerController.getInstance().showDict(this.dict.toString());

                    // 添加单词成功
                    if (addSuccess) {
                        // 3. 构建回复，以发给客户端
                        String response = "add success";
                        // 给客户端发送回复
                        d_output.writeUTF(response);
//                        System.out.println(response);
                    }
                }

                // TODO: 删词，查词
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
