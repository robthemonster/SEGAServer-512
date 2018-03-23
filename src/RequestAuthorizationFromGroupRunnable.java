import SEGAMessages.RequestAuthorizationFromGroupRequest;
import SEGAMessages.RequestAuthorizationFromGroupResponse;

public class RequestAuthorizationFromGroupRunnable extends RequestRunnable {
    public RequestAuthorizationFromGroupRunnable(RequestAuthorizationFromGroupRequest request) {
        super(request);
    }

    @Override
    public void run() {
        RequestAuthorizationFromGroupRequest requestAuthorizationFromGroupRequest = (RequestAuthorizationFromGroupRequest) request;
        RequestAuthorizationFromGroupResponse response = DatabaseManager.authorizedByGroup(requestAuthorizationFromGroupRequest);
        FirebaseManager.sendContentThroughFirebase(FirebaseManager.getResponseHttpContent(response, request.getFirebaseToken()));
    }
}
