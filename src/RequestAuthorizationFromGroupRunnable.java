import SEGAMessages.RequestAuthorizationFromGroupRequest;
import SEGAMessages.RequestAuthorizationFromGroupResponse;

public class RequestAuthorizationFromGroupRunnable extends RequestRunnable {
    public RequestAuthorizationFromGroupRunnable(RequestAuthorizationFromGroupRequest request) {
        super(request);
    }

    @Override
    public void run() {
        RequestAuthorizationFromGroupRequest requestAuthorizationFromGroupRequest = (RequestAuthorizationFromGroupRequest) request;
        String groupName = requestAuthorizationFromGroupRequest.getGroupName();
        String username = requestAuthorizationFromGroupRequest.getUsername();
        String firebaseToken = requestAuthorizationFromGroupRequest.getFirebaseToken();
        boolean succeeded = DatabaseManager.authorizedByGroup(groupName);
        System.out.println(groupName + " authorization: " + succeeded);
        RequestAuthorizationFromGroupResponse requestAuthorizationFromGroupResponse = new RequestAuthorizationFromGroupResponse();
        requestAuthorizationFromGroupResponse.setSucceeded(succeeded);
        FirebaseManager.sendResponseToClient(requestAuthorizationFromGroupResponse, firebaseToken);
    }
}
