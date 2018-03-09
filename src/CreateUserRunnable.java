import SEGAMessages.CreateUserRequest;
import SEGAMessages.CreateUserResponse;

public class CreateUserRunnable extends RequestRunnable {


    public CreateUserRunnable(CreateUserRequest request) {
        super(request);
    }

    @Override
    public void run() {
        CreateUserRequest createUserRequest = (CreateUserRequest) request;
        boolean created = DatabaseManager.createUser(createUserRequest);
        System.out.println(createUserRequest.getUsername() + " " + (created ? "user created" : "user creation failed"));
        CreateUserResponse response = new CreateUserResponse();
        response.setSucceeded(created);
        FirebaseManager.sendResponseToClient(response, createUserRequest.getFirebaseToken());
    }
}
