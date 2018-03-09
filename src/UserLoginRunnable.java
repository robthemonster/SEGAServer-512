import SEGAMessages.UserLoginRequest;
import SEGAMessages.UserLoginResponse;

public class UserLoginRunnable extends RequestRunnable {


    public UserLoginRunnable(UserLoginRequest request) {
        super(request);
    }

    @Override
    public void run() {
        UserLoginRequest userLoginRequest = (UserLoginRequest) request;
        boolean authenticated = DatabaseManager.authenticateUser(userLoginRequest);
        System.out.println(authenticated ? "user successfully authenticated" : "user not authenticated");
        UserLoginResponse response = new UserLoginResponse();
        response.setSucceeded(authenticated);
        response.setUsername(userLoginRequest.getUsername());
        FirebaseManager.sendResponseToClient(response, userLoginRequest.getFirebaseToken());
    }
}
