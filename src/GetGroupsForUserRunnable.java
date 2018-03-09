import SEGAMessages.GetGroupsForUserRequest;
import SEGAMessages.GetGroupsForUserResponse;

import java.util.List;

public class GetGroupsForUserRunnable extends RequestRunnable {

    public GetGroupsForUserRunnable(GetGroupsForUserRequest request) {
        super(request);
    }

    @Override
    public void run() {
        GetGroupsForUserRequest getGroupsForUserRequest = (GetGroupsForUserRequest) request;
        List<String> groups = DatabaseManager.getGroupsForUser(getGroupsForUserRequest);
        GetGroupsForUserResponse response = new GetGroupsForUserResponse();
        response.setGroups(groups);
        FirebaseManager.sendResponseToClient(response, getGroupsForUserRequest.getFirebaseToken());
    }
}
