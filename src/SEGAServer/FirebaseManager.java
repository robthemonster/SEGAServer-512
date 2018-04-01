package SEGAServer;

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

    public static void sendContentThroughFirebase(HttpContent content) {
        HttpTransport transport = new NetHttpTransport();
        try {
            HttpRequest request = transport.createRequestFactory()
                    .buildPostRequest(SEND_MESSAGE_URL, content);
            request.getHeaders().setAuthorization("Bearer " + getGoogleCredentials());
            request.getHeaders().setContentType("application/json");
            HttpResponse httpResponse = request.execute();
        } catch (IOException e) {
            Logger.debug(e.getMessage());
            e.printStackTrace();
        }
    }

    public static HttpContent getResponseHttpContent(Serializable response, String firebaseToken) {
        JsonObject requestBody = new JsonObject();
        JsonObject message = new JsonObject();
        JsonObject data = new JsonObject();
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(byteArray);
            outputStream.writeObject(response);
            outputStream.close();
        } catch (IOException e) {
            Logger.debug(e.getMessage());
            e.printStackTrace();
        }
        data.addProperty("serializedMessage", Base64.getEncoder().encodeToString(byteArray.toByteArray()));
        message.add("data", data);
        message.addProperty("token", firebaseToken);
        requestBody.add("message", message);
        return ByteArrayContent.fromString("application/json", requestBody.toString());
    }

    public static HttpContent getAuthorizationRequestNotification(String requestor, String username, String groupName, String firebaseToken) {
        JsonObject requestBody = new JsonObject();
        JsonObject message = new JsonObject();
        JsonObject androidConfig = new JsonObject();
        JsonObject androidNotification = new JsonObject();
        JsonObject data = new JsonObject();
        data.addProperty("groupname", groupName);
        data.addProperty("username", username);
        data.addProperty("requestor", requestor);
        androidConfig.add("data", data);
        androidConfig.addProperty("ttl", "60s");
        androidNotification.addProperty("title", groupName + " Authorization Request");
        androidNotification.addProperty("body", requestor + ", a user in " + groupName + " is requesting your approval. Tap to grant.");
        androidNotification.addProperty("click_action", "SEGAClient.GRANTAUTH");
        androidNotification.addProperty("sound", "default");
        androidConfig.add("notification", androidNotification);
        message.add("android", androidConfig);
        message.addProperty("token", firebaseToken);
        requestBody.add("message", message);
        return ByteArrayContent.fromString("application/json", requestBody.toString());
    }
}
