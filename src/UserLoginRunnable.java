import SEGAMessages.UserLoginRequest;
import SEGAMessages.UserLoginResponse;

public class UserLoginRunnable implements Runnable {

    private UserLoginRequest request;

    public UserLoginRunnable(UserLoginRequest request) {
        this.request = request;
    }

    @Override
    public void run() {
        boolean authenticated = DatabaseManager.authenticateUser(request);
        System.out.println(authenticated ? "user successfully authenticated" : "user not authenticated");
        UserLoginResponse response = new UserLoginResponse();
        response.setSucceeded(authenticated);
        FirebaseManager.sendResponseToClient(response, request.getFirebaseToken());
    }
}
