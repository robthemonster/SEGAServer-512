import SEGAMessages.CreateUserRequest;

public class CreateUserRunnable implements Runnable {
    private CreateUserRequest request;

    public CreateUserRunnable(CreateUserRequest request) {
        this.request = request;
    }

    @Override
    public void run() {
        DatabaseManager.createUser(request);
    }
}
