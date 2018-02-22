import SEGAMessages.ClientInfo;
import SEGAMessages.CreateUserRequest;
import SEGAMessages.GroupNotification;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args){
        System.out.println("SEGA SERVER HAS BEGUN");
        try {
            ServerSocket serverSocket = new ServerSocket(6969);
            Socket socket;
            while (true){
                socket = serverSocket.accept();
                handleMessage(socket);
                socket.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private static void handleMessage(Socket socket) {
        try {
            ObjectInputStream stream = new ObjectInputStream(socket.getInputStream());
            Object object = stream.readObject();
            if (object instanceof GroupNotification) {
                SendGroupNotification runnable = new SendGroupNotification((GroupNotification) object);
                new Thread(runnable).start();
                return;
            }
            if (object instanceof CreateUserRequest) {
                CreateUserRunnable runnable = new CreateUserRunnable((CreateUserRequest) object);
                new Thread(runnable).start();
                return;
            }
            if (object instanceof ClientInfo) {

                return;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
