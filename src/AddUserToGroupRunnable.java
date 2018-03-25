import SEGAMessages.AddUserToGroupRequest;
import SEGAMessages.AddUserToGroupResponse;

public class AddUserToGroupRunnable extends RequestRunnable {
    public AddUserToGroupRunnable(AddUserToGroupRequest request) {
        super(request);
    }

    @Override
    public void run() {
        Logger.debug(request.toString());
        AddUserToGroupResponse response = DatabaseManager.addUserToGroup((AddUserToGroupRequest) request);
        Logger.debug(response.toString());
        FirebaseManager.sendContentThroughFirebase(FirebaseManager.getResponseHttpContent(response, request.getFirebaseToken()));
    }
}
