package multi_thread_multi_turn;

import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try {
            // 创建客户端socket建立连接，指定服务器地址和端口
            Socket socket = new Socket("127.0.0.1", 8888);

            // 获取输入输出流
            DataInputStream d_in = new DataInputStream(socket.getInputStream());
            DataOutputStream d_out = new DataOutputStream(socket.getOutputStream());

            while (true) {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("请输入消息");
                String tx = br.readLine();
                d_out.writeUTF(tx);
                if (tx.equals("end")) {
                    break;
                }
                String response = d_in.readUTF();
                System.out.println("受到服务器回复：" + response);

            }
        } catch (IOException e) {
            e.printStackTrace();;
        }
    }
}
