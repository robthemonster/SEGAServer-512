import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args){
        try {
            ServerSocket serverSocket = new ServerSocket(6969);
            Socket socket;
            while (true){
                socket = serverSocket.accept();
                PrintMessage printMessage = new PrintMessage(socket);
                new Thread(printMessage).start();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
