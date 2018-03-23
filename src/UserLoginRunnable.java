import SEGAMessages.UserLoginRequest;
import SEGAMessages.UserLoginResponse;

public class UserLoginRunnable extends RequestRunnable {


    public UserLoginRunnable(UserLoginRequest request) {
        super(request);
    }

    @Override
    public void run() {
        UserLoginRequest userLoginRequest = (UserLoginRequest) request;
        UserLoginResponse response = DatabaseManager.authenticateUser(userLoginRequest);
        FirebaseManager.sendContentThroughFirebase(FirebaseManager.getResponseHttpContent(response, request.getFirebaseToken()));
    }
}
