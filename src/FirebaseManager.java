import SEGAMessages.GroupNotification;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FirebaseManager {
    private static GenericUrl sendMessageUrl = new GenericUrl("https://fcm.googleapis.com/v1/projects/distributed-authentication/messages:send");
    private static String getTopicsUrl = "https://iid.googleapis.com/iid/info/";
    public static String getAuthorization() throws IOException {
        String[] scopes = {"https://www.googleapis.com/auth/firebase.messaging", "https://www.googleapis.com/auth/cloud-platform"};
        GoogleCredential credential = GoogleCredential
                .fromStream(new FileInputStream("privatekey" + File.separator + "key.json"))
                .createScoped(Arrays.asList(scopes));
        credential.refreshToken();
        return credential.getAccessToken();
    }

    public static void sendGroupNotification(GroupNotification groupNotification) {
        HttpTransport transport = new NetHttpTransport();
        try {
            HttpRequest request = transport.createRequestFactory()
                    .buildPostRequest(sendMessageUrl, getGroupNotificationContent(groupNotification));
            request.getHeaders().setAuthorization("Bearer " + getAuthorization());
            request.getHeaders().setContentType("application/json");
            HttpResponse response = request.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getTopics(String firebaseInstanceToken) {
        HttpTransport transport = new NetHttpTransport();
        GenericUrl url = new GenericUrl(getTopicsUrl + "/" + firebaseInstanceToken);
        url.put("details", true);
        try {
            HttpRequest request = transport.createRequestFactory()
                    .buildGetRequest(url);
            request.getHeaders().setAuthorization("key="); //TODO: get server key
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

}
