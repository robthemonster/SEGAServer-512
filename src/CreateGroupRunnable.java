import SEGAMessages.CreateGroupRequest;
import SEGAMessages.CreateGroupResponse;

public class CreateGroupRunnable implements Runnable {

    private CreateGroupRequest request;

    public CreateGroupRunnable(CreateGroupRequest request) {
        this.request = request;
    }

    @Override
    public void run() {
        boolean succeeded = DatabaseManager.createGroup(request);
        CreateGroupResponse response = new CreateGroupResponse();
        response.setSucceeded(succeeded);
        FirebaseManager.sendResponseToClient(response, request.getFirebaseToken());
    }
}
