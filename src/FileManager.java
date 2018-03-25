import SEGAMessages.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FileManager {

    private static final String groupSubfolder = "." + File.separator + "groups";

    public static GetFilesForGroupResponse getFilesForGroup(GetFilesForGroupRequest request) {
        GetFilesForGroupResponse response = new GetFilesForGroupResponse();
        ArrayList<FileAttributes> result = new ArrayList<>();
        if (!DatabaseManager.userIsInGroup(request.getUsername(), request.getGroupname())) {
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
                result.add(fileAttributes);
            }
        }
        response.setFiles(result);
        return response;
    }

    public static GetFileFromGroupResponse getFileFromGroup(GetFileFromGroupRequest request) {
        GetFileFromGroupResponse response = new GetFileFromGroupResponse();
        if (!DatabaseManager.userIsInGroup(request.getUsername(), request.getGroupname())) {
            response.setErrorMessage("User is not in that group.");
            return response;
        }
        File file = new File(groupSubfolder + File.separator + request.getGroupname() + File.separator + request.getFilename());
        if (file.exists()) {
            try {
                response.setFile(FileUtils.readFileToByteArray(file));
            } catch (IOException e) {
                e.printStackTrace();
                Logger.debug(e.getMessage());
                response.setErrorMessage("Error writing file to response");
            }
        } else {
            response.setErrorMessage("File does not exist");
        }
        return response;
    }

    public static UploadFileToGroupResponse uploadFileToGroup(UploadFileToGroupRequest request) {
        UploadFileToGroupResponse response = new UploadFileToGroupResponse();
        if (!DatabaseManager.userIsInGroup(request.getUsername(), request.getGroupname())) {
            response.setErrorMessage("User is not in that group.");
            return response;
        }
        try {
            File file = new File(groupSubfolder + File.separator + request.getGroupname() + File.separator + request.getFilename());
            if (!file.getParentFile().exists()) {
                if (!file.getParentFile().mkdirs()) {
                    Logger.debug("WHAT HAPPENED");
                }
            }
            if (file.createNewFile()) {
                FileUtils.writeByteArrayToFile(file, request.getFile());
                response.setSucceeded(true);
            } else {
                response.setErrorMessage("Error creating file");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Logger.debug(e.getMessage());
            response.setErrorMessage("Error uploading file.");
            response.setSucceeded(false);
        }
        return response;
    }

    public static DeleteFileFromGroupResponse deleteFileFromGroup(DeleteFileFromGroupRequest request) {
        DeleteFileFromGroupResponse response = new DeleteFileFromGroupResponse();
        if (!DatabaseManager.userIsInGroup(request.getUsername(), request.getGroupname())) {
            response.setErrorMessage("User is not in that group.");
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
