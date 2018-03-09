import SEGAMessages.Request;

public abstract class RequestRunnable implements Runnable {
    protected Request request;

    public RequestRunnable(Request request) {
        this.request = request;
    }
}
