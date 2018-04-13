import SEGAMessages.*;

public class RequestRunnable implements Runnable {
    protected Request request;

    public RequestRunnable(Request request) {
        this.request = request;
    }

    @Override
    public void run() {
        Response response = null;
        Logger.debug(request.toString());
        if (request instanceof CreateUserRequest) {
            response = DatabaseManager.createUser((CreateUserRequest) request);
        }
        if (request instanceof UserLoginRequest) {
            response = DatabaseManager.userLogin((UserLoginRequest) request);
        }
        if (request instanceof CreateGroupRequest) {
            response = DatabaseManager.createGroup((CreateGroupRequest) request);
        }
        if (request instanceof GetGroupsForUserRequest) {
            response = DatabaseManager.getGroupsForUser((GetGroupsForUserRequest) request);
        }
        if (request instanceof GetUsersForGroupRequest) {
            response = DatabaseManager.getUsersForGroup((GetUsersForGroupRequest) request);
        }
        if (request instanceof RequestAuthorizationFromGroupRequest) {
            response = DatabaseManager.requestAuthorizationFromGroup((RequestAuthorizationFromGroupRequest) request);
        }
        if (request instanceof GrantAuthorizationForGroupRequest) {
            response = DatabaseManager.grantAuthorizationForGroup((GrantAuthorizationForGroupRequest) request);
        }
        if (request instanceof AddUserToGroupRequest) {
            response = DatabaseManager.processRequest((AddUserToGroupRequest) request);
        }
        if (request instanceof GetFilesForGroupRequest) {
            response = FileManager.processRequest((GetFilesForGroupRequest) request);
        }
        if (request instanceof DeleteFileFromGroupRequest) {
            response = FileManager.processRequest((DeleteFileFromGroupRequest) request);
        }
        if (request instanceof DeleteUserFromGroupRequest) {
            response = DatabaseManager.processRequest((DeleteUserFromGroupRequest) request);
        }
        if (response != null) {
            Logger.debug(response.toString());
            FirebaseManager.sendContentThroughFirebase(FirebaseManager.getResponseHttpContent(response, request.getFirebaseToken()));
        }
    }
}

