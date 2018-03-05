import SEGAMessages.CreateUserRequest;

public class CreateUserRunnable implements Runnable {
    private CreateUserRequest request;

    public CreateUserRunnable(CreateUserRequest request) {
        this.request = request;
    }

    @Override
    public void run() {
        boolean created = DatabaseManager.createUser(request);
        System.out.println(created ? "user created" : "user creation failed");
    }
}
