import SEGAMessages.GetUsersForGroupRequest;
import SEGAMessages.GetUsersForGroupResponse;

public class GetUsersForGroupRunnable extends RequestRunnable {
    public GetUsersForGroupRunnable(GetUsersForGroupRequest request) {
        super(request);
    }

    @Override
    public void run() {
        GetUsersForGroupRequest getUsersForGroupRequest = (GetUsersForGroupRequest) request;
        GetUsersForGroupResponse response = DatabaseManager.getUsersForGroup(getUsersForGroupRequest);
        FirebaseManager.sendContentThroughFirebase(FirebaseManager.getResponseHttpContent(response, request.getFirebaseToken()));
    }
}
