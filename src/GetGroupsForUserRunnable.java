import SEGAMessages.GetGroupsForUserRequest;
import SEGAMessages.GetGroupsForUserResponse;

public class GetGroupsForUserRunnable extends RequestRunnable {

    public GetGroupsForUserRunnable(GetGroupsForUserRequest request) {
        super(request);
    }

    @Override
    public void run() {
        Logger.debug(request.toString());
        GetGroupsForUserResponse response = DatabaseManager.getGroupsForUser((GetGroupsForUserRequest) request);
        Logger.debug(response.toString());
        FirebaseManager.sendContentThroughFirebase(FirebaseManager.getResponseHttpContent(response, request.getFirebaseToken()));
    }
}
