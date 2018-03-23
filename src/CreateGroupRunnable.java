import SEGAMessages.CreateGroupRequest;
import SEGAMessages.CreateGroupResponse;

public class CreateGroupRunnable extends RequestRunnable {

    public CreateGroupRunnable(CreateGroupRequest request) {
        super(request);
    }

    @Override
    public void run() {
        CreateGroupRequest createGroupRequest = (CreateGroupRequest) request;
        CreateGroupResponse response = DatabaseManager.createGroup(createGroupRequest);
        FirebaseManager.sendContentThroughFirebase(FirebaseManager.getResponseHttpContent(response, request.getFirebaseToken()));
    }
}
