import SEGAMessages.CreateUserRequest;
import SEGAMessages.CreateUserResponse;

public class CreateUserRunnable implements Runnable {
    private CreateUserRequest request;

    public CreateUserRunnable(CreateUserRequest request) {
        this.request = request;
    }

    @Override
    public void run() {
        boolean created = DatabaseManager.createUser(request);
        System.out.println(created ? "user created" : "user creation failed");
        CreateUserResponse response = new CreateUserResponse();
        response.setSucceeded(created);
        FirebaseManager.sendResponseToClient(response, request.getFirebaseToken());
    }
}
