import SEGAMessages.AddUserToGroupRequest;
import SEGAMessages.AddUserToGroupResponse;

public class AddUserToGroupRunnable extends RequestRunnable {
    public AddUserToGroupRunnable(AddUserToGroupRequest request) {
        super(request);
    }

    @Override
    public void run() {
        AddUserToGroupRequest addUserToGroupRequest = (AddUserToGroupRequest) request;
        AddUserToGroupResponse response = DatabaseManager.addUserToGroup(addUserToGroupRequest);
        FirebaseManager.sendContentThroughFirebase(FirebaseManager.getResponseHttpContent(response, request.getFirebaseToken()));
    }
}
