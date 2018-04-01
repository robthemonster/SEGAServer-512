package SEGARequestRunnables;

import SEGAMessages.DeleteFileFromGroupRequest;
import SEGAMessages.DeleteFileFromGroupResponse;
import SEGAServer.FileManager;
import SEGAServer.FirebaseManager;
import SEGAServer.Logger;

public class DeleteFileFromGroupRunnable extends RequestRunnable {
    public DeleteFileFromGroupRunnable(DeleteFileFromGroupRequest request) {
        super(request);
    }

    @Override
    public void run() {
        Logger.debug(request.toString());
        DeleteFileFromGroupResponse response = FileManager.deleteFileFromGroup((DeleteFileFromGroupRequest) request);
        Logger.debug(response.toString());
        FirebaseManager.sendContentThroughFirebase(FirebaseManager.getResponseHttpContent(response, request.getFirebaseToken()));
    }
}
