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
            Socket socket = new Socket(this.ip, this.port);

            out = new DataOutputStream(socket.getOutputStream());
            out.flush();
            in = new DataInputStream(socket.getInputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String sendMsg(String message) {
        try {
            out.writeUTF(message);
            System.out.println("send message");
            return in.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "empty response";
    }
}
