import SEGAMessages.CreateUserRequest;
import SEGAMessages.CreateUserResponse;

public class CreateUserRunnable extends RequestRunnable {


    public CreateUserRunnable(CreateUserRequest request) {
        super(request);
    }

    @Override
    public void run() {
        CreateUserRequest createUserRequest = (CreateUserRequest) request;
        CreateUserResponse response = DatabaseManager.createUser(createUserRequest);
        FirebaseManager.sendResponseToClient(response, createUserRequest.getFirebaseToken());
    }
}
