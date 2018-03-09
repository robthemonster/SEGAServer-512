import SEGAMessages.*;

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
            if (object instanceof Request) {
                RequestRunnable requestRunnable = getRequestRunnable((Request) object);
                new Thread(requestRunnable).start();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static RequestRunnable getRequestRunnable(Request request) {
        if (request instanceof CreateUserRequest) {
            return new CreateUserRunnable((CreateUserRequest) request);
        }
        if (request instanceof UserLoginRequest) {
            return new UserLoginRunnable((UserLoginRequest) request);
        }
        if (request instanceof CreateGroupRequest) {
            return new CreateGroupRunnable((CreateGroupRequest) request);
        }
        if (request instanceof GetGroupsForUserRequest) {
            return new GetGroupsForUserRunnable((GetGroupsForUserRequest) request);
        }
        if (request instanceof GetUsersForGroupRequest) {
            return new GetUsersForGroupRunnable((GetUsersForGroupRequest) request);
        }
        return null;
    }
}

