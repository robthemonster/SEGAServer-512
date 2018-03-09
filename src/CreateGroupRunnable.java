import SEGAMessages.CreateGroupRequest;
import SEGAMessages.CreateGroupResponse;

public class CreateGroupRunnable extends RequestRunnable {

    public CreateGroupRunnable(CreateGroupRequest request) {
        super(request);
    }

    @Override
    public void run() {
        CreateGroupRequest createGroupRequest = (CreateGroupRequest) request;
        boolean succeeded = DatabaseManager.createGroup(createGroupRequest);
        CreateGroupResponse response = new CreateGroupResponse();
        response.setSucceeded(succeeded);
        FirebaseManager.sendResponseToClient(response, createGroupRequest.getFirebaseToken());
    }
}
