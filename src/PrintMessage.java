import SEGAMessages.ClientInfo;

import java.io.*;
import java.net.Socket;

public class PrintMessage implements Runnable {
    private Socket socket;
    public PrintMessage(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            ClientInfo clientInfo = (ClientInfo) inputStream.readObject();
            System.out.println(clientInfo.getFirebaseToken() + " : " + clientInfo.getMessage());
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
