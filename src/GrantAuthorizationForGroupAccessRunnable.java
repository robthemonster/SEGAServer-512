import SEGAMessages.GrantAuthorizationForGroupRequest;
import SEGAMessages.GrantAuthorizationForGroupResponse;

public class GrantAuthorizationForGroupAccessRunnable extends RequestRunnable {
    public GrantAuthorizationForGroupAccessRunnable(GrantAuthorizationForGroupRequest request) {
        super(request);
    }

    @Override
    public void run() {
        Logger.debug(request.toString());
        GrantAuthorizationForGroupResponse response = DatabaseManager.grantAuthorizationForGroupAccess((GrantAuthorizationForGroupRequest) request);
        Logger.debug(response.toString());
        FirebaseManager.sendContentThroughFirebase(FirebaseManager.getResponseHttpContent(response, request.getFirebaseToken()));
    }
}
