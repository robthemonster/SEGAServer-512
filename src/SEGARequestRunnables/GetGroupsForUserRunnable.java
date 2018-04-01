package SEGARequestRunnables;

import SEGAMessages.GetGroupsForUserRequest;
import SEGAMessages.GetGroupsForUserResponse;
import SEGAServer.DatabaseManager;
import SEGAServer.FirebaseManager;
import SEGAServer.Logger;

public class GetGroupsForUserRunnable extends RequestRunnable {

    public GetGroupsForUserRunnable(GetGroupsForUserRequest request) {
        super(request);
    }

    @Override
    public void run() {
        Logger.debug(request.toString());
        GetGroupsForUserResponse response = DatabaseManager.getGroupsForUser((GetGroupsForUserRequest) request);
        Logger.debug(response.toString());
        FirebaseManager.sendContentThroughFirebase(FirebaseManager.getResponseHttpContent(response, request.getFirebaseToken()));
    }
}
