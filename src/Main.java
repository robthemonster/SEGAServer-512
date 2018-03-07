import SEGAMessages.CreateGroupRequest;
import SEGAMessages.CreateUserRequest;
import SEGAMessages.GetGroupsForUserRequest;
import SEGAMessages.UserLoginRequest;

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
            if (object instanceof CreateUserRequest) {
                CreateUserRunnable runnable = new CreateUserRunnable((CreateUserRequest) object);
                new Thread(runnable).start();
                return;
            }
            if (object instanceof UserLoginRequest) {
                UserLoginRunnable runnable = new UserLoginRunnable((UserLoginRequest) object);
                new Thread(runnable).start();
                return;
            }
            if (object instanceof CreateGroupRequest) {
                CreateGroupRunnable runnable = new CreateGroupRunnable((CreateGroupRequest) object);
                new Thread(runnable).start();
                return;
            }
            if (object instanceof GetGroupsForUserRequest) {
                GetGroupsForUserRunnable runnable = new GetGroupsForUserRunnable((GetGroupsForUserRequest) object);
                new Thread(runnable).start();
                return;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
