import SEGAMessages.CreateGroupRequest;
import SEGAMessages.CreateGroupResponse;

public class CreateGroupRunnable extends RequestRunnable {

    public CreateGroupRunnable(CreateGroupRequest request) {
        super(request);
    }

    @Override
    public void run() {
        Logger.debug(request.toString());
        CreateGroupResponse response = DatabaseManager.createGroup((CreateGroupRequest) request);
        Logger.debug(response.toString());
        FirebaseManager.sendContentThroughFirebase(FirebaseManager.getResponseHttpContent(response, request.getFirebaseToken()));
    }
}
