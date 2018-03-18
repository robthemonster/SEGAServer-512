import SEGAMessages.GrantAuthorizationForGroupRequest;
import SEGAMessages.GrantAuthorizationForGroupResponse;

public class GrantAuthorizationForGroupAccessRunnable extends RequestRunnable {
    public GrantAuthorizationForGroupAccessRunnable(GrantAuthorizationForGroupRequest request) {
        super(request);
    }

    @Override
    public void run() {
        GrantAuthorizationForGroupRequest grantAuthorizationForGroupRequest = (GrantAuthorizationForGroupRequest) request;
        boolean succeeded = DatabaseManager.grantAuthorizationForGroupAccess(grantAuthorizationForGroupRequest);
        GrantAuthorizationForGroupResponse response = new GrantAuthorizationForGroupResponse();
        response.setSucceded(succeeded);
        FirebaseManager.sendResponseToClient(response, request.getFirebaseToken());
    }
}
