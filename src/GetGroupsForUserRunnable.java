import SEGAMessages.GetGroupsForUserRequest;
import SEGAMessages.GetGroupsForUserResponse;

public class GetGroupsForUserRunnable extends RequestRunnable {

    public GetGroupsForUserRunnable(GetGroupsForUserRequest request) {
        super(request);
    }

    @Override
    public void run() {
        GetGroupsForUserRequest getGroupsForUserRequest = (GetGroupsForUserRequest) request;
        GetGroupsForUserResponse response = DatabaseManager.getGroupsForUser(getGroupsForUserRequest);
        FirebaseManager.sendContentThroughFirebase(FirebaseManager.getResponseHttpContent(response, request.getFirebaseToken()));
    }
}
