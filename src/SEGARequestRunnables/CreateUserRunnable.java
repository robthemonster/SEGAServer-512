package SEGARequestRunnables;

import SEGAMessages.CreateUserRequest;
import SEGAMessages.CreateUserResponse;
import SEGAServer.DatabaseManager;
import SEGAServer.FirebaseManager;
import SEGAServer.Logger;

public class CreateUserRunnable extends RequestRunnable {

    public CreateUserRunnable(CreateUserRequest request) {
        super(request);
    }

    @Override
    public void run() {
        Logger.debug(request.toString());
        CreateUserResponse response = DatabaseManager.createUser((CreateUserRequest) request);
        Logger.debug(response.toString());
        FirebaseManager.sendContentThroughFirebase(FirebaseManager.getResponseHttpContent(response, request.getFirebaseToken()));
    }
}
