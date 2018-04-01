package SEGARequestRunnables;

import SEGAMessages.DeleteUserFromGroupRequest;
import SEGAMessages.DeleteUserFromGroupResponse;
import SEGAServer.DatabaseManager;
import SEGAServer.FirebaseManager;
import SEGAServer.Logger;

public class DeleteUserFromGroupRunnable extends RequestRunnable {

    public DeleteUserFromGroupRunnable(DeleteUserFromGroupRequest request) {
        super(request);
    }

    @Override
    public void run() {
        DeleteUserFromGroupResponse response = DatabaseManager.deleteUserFromGroup((DeleteUserFromGroupRequest) request);
        Logger.debug(response.toString());
        FirebaseManager.sendContentThroughFirebase(FirebaseManager.getResponseHttpContent(response, request.getFirebaseToken()));
    }
}
