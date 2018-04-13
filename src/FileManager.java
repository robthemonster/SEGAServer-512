import SEGAMessages.*;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;

public class FileManager {

    private static final String groupSubfolder = "." + File.separator + "groups";

    public static GetFilesForGroupResponse getFilesForGroup(GetFilesForGroupRequest request) {
        GetFilesForGroupResponse response = new GetFilesForGroupResponse();
        ArrayList<FileAttributes> result = new ArrayList<>();
        if (DatabaseManager.userIsNotInGroup(request.getUsername(), request.getGroupname())) {
            response.setFiles(result);
            response.setErrorMessage("User is not in that group.");
            return response;
        }
        File directory = new File(groupSubfolder + File.separator + request.getGroupname());
        if (!directory.exists()) {
            if (!directory.mkdir()) {
                //error making directory
                response.setFiles(result);
                response.setErrorMessage("Error creating directory");
                return response;
            }
        }
        if (directory.listFiles() != null) {
            for (File file : directory.listFiles()) {
                FileAttributes fileAttributes = new FileAttributes();
                fileAttributes.setFileName(file.getName());
                fileAttributes.setFileSize(file.length());
                fileAttributes.setFileType(FilenameUtils.getExtension(file.getName()));
                result.add(fileAttributes);
            }
        }
        response.setFiles(result);
        return response;
    }

    public static DeleteFileFromGroupResponse deleteFileFromGroup(DeleteFileFromGroupRequest request) {
        DeleteFileFromGroupResponse response = new DeleteFileFromGroupResponse();
        if (DatabaseManager.userIsNotInGroup(request.getUsername(), request.getGroupname())) {
            response.setErrorMessage("User is not in that group.");
            return response;
        }
        if (!DatabaseManager.isTokenCorrect(request.getGroupname(), request.getToken())) {
            response.setErrorMessage("Cannot delete. Access denied");
            return response;
        }
        File file = new File(groupSubfolder + File.separator + request.getGroupname() + File.separator + request.getFilename());
        if (file.exists()) {
            response.setSucceeded(file.delete());
        } else {
            response.setErrorMessage("File does not exist");
        }
        return response;

    }
}
