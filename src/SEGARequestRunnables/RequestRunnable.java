package SEGARequestRunnables;

import SEGAMessages.*;

public abstract class RequestRunnable implements Runnable {
    protected Request request;

    public RequestRunnable(Request request) {
        this.request = request;
    }

    public static RequestRunnable getInstanceOfRunnable(Request request) {
        if (request instanceof CreateUserRequest) {
            return new CreateUserRunnable((CreateUserRequest) request);
        }
        if (request instanceof UserLoginRequest) {
            return new UserLoginRunnable((UserLoginRequest) request);
        }
        if (request instanceof CreateGroupRequest) {
            return new CreateGroupRunnable((CreateGroupRequest) request);
        }
        if (request instanceof GetGroupsForUserRequest) {
            return new GetGroupsForUserRunnable((GetGroupsForUserRequest) request);
        }
        if (request instanceof GetUsersForGroupRequest) {
            return new GetUsersForGroupRunnable((GetUsersForGroupRequest) request);
        }
        if (request instanceof RequestAuthorizationFromGroupRequest) {
            return new RequestAuthorizationFromGroupRunnable((RequestAuthorizationFromGroupRequest) request);
        }
        if (request instanceof GrantAuthorizationForGroupRequest) {
            return new GrantAuthorizationForGroupAccessRunnable((GrantAuthorizationForGroupRequest) request);
        }
        if (request instanceof AddUserToGroupRequest) {
            return new AddUserToGroupRunnable((AddUserToGroupRequest) request);
        }
        if (request instanceof GetFilesForGroupRequest) {
            return new GetFilesForGroupRunnable((GetFilesForGroupRequest) request);
        }
        if (request instanceof DeleteFileFromGroupRequest) {
            return new DeleteFileFromGroupRunnable((DeleteFileFromGroupRequest) request);
        }
        if (request instanceof DeleteUserFromGroupRequest) {
            return new DeleteUserFromGroupRunnable((DeleteUserFromGroupRequest) request);
        }
        return null;
    }
}

