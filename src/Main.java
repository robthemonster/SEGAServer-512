import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args){
        try {
            ServerSocket serverSocket = new ServerSocket(6969);
            Socket socket;
            while (true){
                socket = serverSocket.accept();
                SendGroupNotification sendGroupNotification = new SendGroupNotification(socket);
                new Thread(sendGroupNotification).start();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
