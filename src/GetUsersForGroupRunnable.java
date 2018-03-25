import SEGAMessages.GetUsersForGroupRequest;
import SEGAMessages.GetUsersForGroupResponse;

public class GetUsersForGroupRunnable extends RequestRunnable {
    public GetUsersForGroupRunnable(GetUsersForGroupRequest request) {
        super(request);
    }

    @Override
    public void run() {
        Logger.debug(request.toString());
        GetUsersForGroupResponse response = DatabaseManager.getUsersForGroup((GetUsersForGroupRequest) request);
        Logger.debug(response.toString());
        FirebaseManager.sendContentThroughFirebase(FirebaseManager.getResponseHttpContent(response, request.getFirebaseToken()));
    }
}
