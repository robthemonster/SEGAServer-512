package SEGARequestRunnables;

import SEGAMessages.GetFilesForGroupRequest;
import SEGAMessages.GetFilesForGroupResponse;
import SEGAServer.FileManager;
import SEGAServer.FirebaseManager;
import SEGAServer.Logger;

public class GetFilesForGroupRunnable extends RequestRunnable {
    public GetFilesForGroupRunnable(GetFilesForGroupRequest request) {
        super(request);
    }

    @Override
    public void run() {
        Logger.debug(request.toString());
        GetFilesForGroupResponse response = FileManager.getFilesForGroup((GetFilesForGroupRequest) request);
        Logger.debug(response.toString());
        FirebaseManager.sendContentThroughFirebase(FirebaseManager.getResponseHttpContent(response, request.getFirebaseToken()));
    }
}
