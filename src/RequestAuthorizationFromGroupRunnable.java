import SEGAMessages.RequestAuthorizationFromGroupRequest;
import SEGAMessages.RequestAuthorizationFromGroupResponse;

public class RequestAuthorizationFromGroupRunnable extends RequestRunnable {
    public RequestAuthorizationFromGroupRunnable(RequestAuthorizationFromGroupRequest request) {
        super(request);
    }

    @Override
    public void run() {
        Logger.debug(request.toString());
        RequestAuthorizationFromGroupResponse response = DatabaseManager.authorizedByGroup((RequestAuthorizationFromGroupRequest) request);
        Logger.debug(response.toString());
        FirebaseManager.sendContentThroughFirebase(FirebaseManager.getResponseHttpContent(response, request.getFirebaseToken()));
    }
}
