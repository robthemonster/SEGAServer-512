import SEGAMessages.GetFileFromGroupRequest;
import SEGAMessages.GetFileFromGroupResponse;

public class GetFileFromGroupRunnable extends RequestRunnable {
    public GetFileFromGroupRunnable(GetFileFromGroupRequest request) {
        super(request);
    }

    @Override
    public void run() {
        Logger.debug(request.toString());
        GetFileFromGroupResponse response = FileManager.getFileFromGroup((GetFileFromGroupRequest) request);
        Logger.debug(response.toString());
        FirebaseManager.sendContentThroughFirebase(FirebaseManager.getResponseHttpContent(response, request.getFirebaseToken()));
    }
}
