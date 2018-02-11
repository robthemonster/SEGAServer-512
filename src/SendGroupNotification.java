import SEGAMessages.GroupNotification;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class SendGroupNotification implements Runnable {
    private Socket socket;

    public SendGroupNotification(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            GroupNotification groupNotification = (GroupNotification) inputStream.readObject();
            FirebaseManager.sendGroupNotification(groupNotification);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }
}
