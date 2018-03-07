import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.gson.JsonObject;

import java.io.*;
import java.util.Arrays;
import java.util.Base64;

public class FirebaseManager {
    private static final GenericUrl SEND_MESSAGE_URL = new GenericUrl("https://fcm.googleapis.com/v1/projects/distributed-authentication/messages:send");
    private static final String GET_TOPICS_URL = "https://iid.googleapis.com/iid/info/";
    private static final String API_KEY = "AIzaSyD7HDiQmdC-gvTkWGMH6If5RwappgAlK4M";

    private static String getGoogleCredentials() throws IOException {
        String[] scopes = {"https://www.googleapis.com/auth/firebase.messaging", "https://www.googleapis.com/auth/cloud-platform"};
        GoogleCredential credential = GoogleCredential
                .fromStream(new FileInputStream("privatekey" + File.separator + "key.json"))
                .createScoped(Arrays.asList(scopes));
        credential.refreshToken();
        return credential.getAccessToken();
    }

    public static void sendResponseToClient(Serializable response, String firebaseToken) {
        HttpTransport transport = new NetHttpTransport();
        try {
            HttpRequest request = transport.createRequestFactory()
                    .buildPostRequest(SEND_MESSAGE_URL, getResponseHttpContent(response, firebaseToken));
            request.getHeaders().setAuthorization("Bearer " + getGoogleCredentials());
            request.getHeaders().setContentType("application/json");
            HttpResponse httpResponse = request.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static HttpContent getResponseHttpContent(Serializable response, String firebaseToken) throws IOException {
        JsonObject requestBody = new JsonObject();
        JsonObject message = new JsonObject();
        JsonObject data = new JsonObject();
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        ObjectOutputStream outputStream = new ObjectOutputStream(byteArray);
        outputStream.writeObject(response);
        outputStream.close();
        data.addProperty("serializedMessage", Base64.getEncoder().encodeToString(byteArray.toByteArray()));
        message.add("data", data);
        message.addProperty("token", firebaseToken);
        requestBody.add("message", message);
        return ByteArrayContent.fromString("application/json", requestBody.toString());
    }

}
