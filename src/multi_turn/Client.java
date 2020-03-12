package multi_turn;

import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try {
            // server IP, server port
            Socket socket = new Socket("127.0.0.1", 8888);

            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            while(true) {
                // 读取键盘输入
                System.out.println("请输入消息");
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String keyboard_input = br.readLine();

                if (keyboard_input.equals("end"))
                    break;

                // 给服务器发消息
                out.writeUTF(keyboard_input);
                // 接受服务器消息
                String response = in.readUTF();
                System.out.println("接到服务器消息：" + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
