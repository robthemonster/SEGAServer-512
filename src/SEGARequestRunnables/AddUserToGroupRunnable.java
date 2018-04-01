package SEGARequestRunnables;

import SEGAMessages.AddUserToGroupRequest;
import SEGAMessages.AddUserToGroupResponse;
import SEGAServer.DatabaseManager;
import SEGAServer.FirebaseManager;
import SEGAServer.Logger;

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
