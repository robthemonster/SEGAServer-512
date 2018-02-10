import SEGAMessages.ClientInfo;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.gson.JsonObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Arrays;

public class PrintMessage implements Runnable {
    private Socket socket;
    public PrintMessage(Socket socket) {
        this.socket = socket;
    }

    private static String getAuthorization() throws IOException {
        String[] scopes = {"https://www.googleapis.com/auth/firebase.messaging", "https://www.googleapis.com/auth/cloud-platform"};
        GoogleCredential credential = GoogleCredential
                .fromStream(new FileInputStream("private key\\key.json"))
                .createScoped(Arrays.asList(scopes));
        credential.refreshToken();
        return credential.getAccessToken();
    }

    private static String getContent(String firebaseToken) {
        JsonObject content = new JsonObject();
        JsonObject message = new JsonObject();
        JsonObject notification = new JsonObject();
        notification.addProperty("title", "test");
        notification.addProperty("body", "test body");
        message.add("notification", notification);
        message.addProperty("token", firebaseToken);
        content.add("message", message);
        return content.toString();
    }

    @Override
    public void run() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            ClientInfo clientInfo = (ClientInfo) inputStream.readObject();
            System.out.println(clientInfo.getFirebaseToken() + " : " + clientInfo.getMessage());
            sendMessageReceivedNotification(clientInfo.getFirebaseToken());
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void sendMessageReceivedNotification(String firebaseToken) {
        String url = "https://fcm.googleapis.com/v1/projects/distributed-authentication/messages:send";
        HttpTransport transport = new NetHttpTransport();
        try {
            JsonFactory jsonFactory = new JacksonFactory();
            HttpRequest request = transport.createRequestFactory()
                    .buildPostRequest(new GenericUrl(url), ByteArrayContent.fromString("application/json", getContent(firebaseToken)));
            request.getHeaders().setAuthorization("Bearer " + getAuthorization());
            request.getHeaders().setContentType("application/json");
            HttpResponse response = request.execute();
            System.out.println(response.isSuccessStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
