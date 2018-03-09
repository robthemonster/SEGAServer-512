import SEGAMessages.GetUsersForGroupRequest;
import SEGAMessages.GetUsersForGroupResponse;

import java.util.List;

public class GetUsersForGroupRunnable extends RequestRunnable {
    public GetUsersForGroupRunnable(GetUsersForGroupRequest request) {
        super(request);
    }

    @Override
    public void run() {
        GetUsersForGroupRequest getUsersForGroupRequest = (GetUsersForGroupRequest) request;
        List<String> users = DatabaseManager.getUsersForGroup(getUsersForGroupRequest);
        GetUsersForGroupResponse response = new GetUsersForGroupResponse();
        response.setGroupname(getUsersForGroupRequest.getGroupname());
        response.setUsers(users);
        FirebaseManager.sendResponseToClient(response, getUsersForGroupRequest.getFirebaseToken());
    }
}
