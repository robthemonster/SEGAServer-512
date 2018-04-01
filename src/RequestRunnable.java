import SEGAMessages.*;

public class RequestRunnable implements Runnable {
    protected Request request;

    public RequestRunnable(Request request) {
        this.request = request;
    }

    @Override
    public void run() {
        Response response = null;
        if (request instanceof CreateUserRequest) {
            response = DatabaseManager.processRequest((CreateUserRequest) request);
        }
        if (request instanceof UserLoginRequest) {
            response = DatabaseManager.processRequest((UserLoginRequest) request);
        }
        if (request instanceof CreateGroupRequest) {
            response = DatabaseManager.processRequest((CreateGroupRequest) request);
        }
        if (request instanceof GetGroupsForUserRequest) {
            response = DatabaseManager.processRequest((GetGroupsForUserRequest) request);
        }
        if (request instanceof GetUsersForGroupRequest) {
            response = DatabaseManager.processRequest((GetUsersForGroupRequest) request);
        }
        if (request instanceof RequestAuthorizationFromGroupRequest) {
            response = DatabaseManager.processRequest((RequestAuthorizationFromGroupRequest) request);
        }
        if (request instanceof GrantAuthorizationForGroupRequest) {
            response = DatabaseManager.processRequest((GrantAuthorizationForGroupRequest) request);
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

