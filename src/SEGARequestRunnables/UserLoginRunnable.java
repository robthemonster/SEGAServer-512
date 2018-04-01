package SEGARequestRunnables;

import SEGAMessages.UserLoginRequest;
import SEGAMessages.UserLoginResponse;
import SEGAServer.DatabaseManager;
import SEGAServer.FirebaseManager;
import SEGAServer.Logger;

public class UserLoginRunnable extends RequestRunnable {

    public UserLoginRunnable(UserLoginRequest request) {
        super(request);
    }

    @Override
    public void run() {
        Logger.debug(request.toString());
        UserLoginResponse response = DatabaseManager.authenticateUser((UserLoginRequest) request);
        Logger.debug(response.toString());
        FirebaseManager.sendContentThroughFirebase(FirebaseManager.getResponseHttpContent(response, request.getFirebaseToken()));
    }
}
