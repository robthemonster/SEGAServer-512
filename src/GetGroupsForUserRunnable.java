import SEGAMessages.GetGroupsForUserRequest;
import SEGAMessages.GetGroupsForUserResponse;

import java.util.List;

public class GetGroupsForUserRunnable implements Runnable {
    private GetGroupsForUserRequest request;

    public GetGroupsForUserRunnable(GetGroupsForUserRequest request) {
        this.request = request;
    }

    @Override
    public void run() {
        List<String> groups = DatabaseManager.getGroupsForUser(request);
        GetGroupsForUserResponse response = new GetGroupsForUserResponse();
        response.setGroups(groups);
        FirebaseManager.sendResponseToClient(response, request.getFirebaseToken());
    }
}
