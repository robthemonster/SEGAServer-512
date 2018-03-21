import SEGAMessages.GrantAuthorizationForGroupRequest;
import SEGAMessages.GrantAuthorizationForGroupResponse;

public class GrantAuthorizationForGroupAccessRunnable extends RequestRunnable {
    public GrantAuthorizationForGroupAccessRunnable(GrantAuthorizationForGroupRequest request) {
        super(request);
    }

    @Override
    public void run() {
        GrantAuthorizationForGroupRequest grantAuthorizationForGroupRequest = (GrantAuthorizationForGroupRequest) request;
        GrantAuthorizationForGroupResponse response = DatabaseManager.grantAuthorizationForGroupAccess(grantAuthorizationForGroupRequest);
        FirebaseManager.sendResponseToClient(response, request.getFirebaseToken());
    }
}
