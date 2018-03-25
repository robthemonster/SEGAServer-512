import SEGAMessages.UploadFileToGroupRequest;
import SEGAMessages.UploadFileToGroupResponse;

public class UploadFileToGroupRunnable extends RequestRunnable {
    public UploadFileToGroupRunnable(UploadFileToGroupRequest request) {
        super(request);
    }

    @Override
    public void run() {
        Logger.debug(request.toString());
        UploadFileToGroupResponse response = FileManager.uploadFileToGroup((UploadFileToGroupRequest) request);
        Logger.debug(response.toString());
        FirebaseManager.sendContentThroughFirebase(FirebaseManager.getResponseHttpContent(response, request.getFirebaseToken()));
    }
}
