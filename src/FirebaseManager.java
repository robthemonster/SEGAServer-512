import SEGAMessages.GroupNotification;
import SEGAMessages.TestResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.util.*;

public class FirebaseManager {
    private static final GenericUrl SEND_MESSAGE_URL = new GenericUrl("https://fcm.googleapis.com/v1/projects/distributed-authentication/messages:send");
    private static final String GET_TOPICS_URL = "https://iid.googleapis.com/iid/info/";
    private static final String API_KEY = "AIzaSyD7HDiQmdC-gvTkWGMH6If5RwappgAlK4M";

    public static String getGoogleCredentials() throws IOException {
        String[] scopes = {"https://www.googleapis.com/auth/firebase.messaging", "https://www.googleapis.com/auth/cloud-platform"};
        GoogleCredential credential = GoogleCredential
                .fromStream(new FileInputStream("privatekey" + File.separator + "key.json"))
                .createScoped(Arrays.asList(scopes));
        credential.refreshToken();
        return credential.getAccessToken();
    }

    public static void sendTestResponseToClient(String firebaseToken) {
        HttpTransport transport = new NetHttpTransport();
        try {
            HttpRequest request = transport.createRequestFactory()
                    .buildPostRequest(SEND_MESSAGE_URL, getTestResponseContent(firebaseToken));
            request.getHeaders().setAuthorization("Bearer " + getGoogleCredentials());
            request.getHeaders().setContentType("application/json");
            HttpResponse response = request.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendGroupNotification(GroupNotification groupNotification) {
        HttpTransport transport = new NetHttpTransport();
        try {
            HttpRequest request = transport.createRequestFactory()
                    .buildPostRequest(SEND_MESSAGE_URL, getGroupNotificationContent(groupNotification));
            request.getHeaders().setAuthorization("Bearer " + getGoogleCredentials());
            request.getHeaders().setContentType("application/json");
            HttpResponse response = request.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getTopics(String firebaseInstanceToken) {
        HttpTransport transport = new NetHttpTransport();
        GenericUrl url = new GenericUrl(GET_TOPICS_URL + "/" + firebaseInstanceToken);
        url.put("details", true);
        try {
            HttpRequest request = transport.createRequestFactory()
                    .buildGetRequest(url);
            request.getHeaders().setAuthorization("key=" + API_KEY);
            HttpResponse response = request.execute();
            return getTopicsListFromResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<String>();
    }

    private static List<String> getTopicsListFromResponse(HttpResponse response) throws IOException {
        JsonObject content = new JsonParser().parse(new InputStreamReader(response.getContent())).getAsJsonObject();
        JsonObject rel = content.getAsJsonObject("rel");
        JsonObject topics = rel.getAsJsonObject("topics");
        ArrayList<String> ret = new ArrayList<>();
        for (Map.Entry<String, JsonElement> e : topics.entrySet()) {
            ret.add(e.getKey());
        }
        return ret;
    }

    private static HttpContent getGroupNotificationContent(GroupNotification groupNotification) {
        JsonObject requestBody = new JsonObject();
        JsonObject message = new JsonObject();
        JsonObject android = new JsonObject();
        JsonObject notification = new JsonObject();
        notification.addProperty("title", groupNotification.getTopicName());
        notification.addProperty("body", groupNotification.getMessage());
        notification.addProperty("sound", "default");
        android.add("notification", notification);
        message.add("android", android);
        message.addProperty("topic", groupNotification.getTopicName());
        requestBody.add("message", message);
        return ByteArrayContent.fromString("application/json", requestBody.toString());
    }

    private static HttpContent getTestResponseContent(String firebaseToken) throws IOException {
        JsonObject requestBody = new JsonObject();
        JsonObject message = new JsonObject();
        JsonObject data = new JsonObject();
        TestResponse response = new TestResponse();
        response.setMessageBody("fuk");
        response.setPayload(" TOP SECRET MEMES. ");
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
