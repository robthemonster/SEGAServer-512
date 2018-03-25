import java.io.File;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    List<BasicFileAttributes> getDirectoryForGroup(String groupname) {
        ArrayList<BasicFileAttributes> result = new ArrayList<>();
        File directory = new File("groups" + File.separator + groupname);
        if (!directory.exists() || !directory.isDirectory()) {
            if (!directory.mkdir()) {
                //error making directory
                return null;
            }
        }
        if (directory.listFiles() != null) {
            for (File file : directory.listFiles()) {

            }
        }
        return null;
    }

    File getFileFromGroup(String filename, String groupname) {
        return null;
    }

    boolean uploadFileToGroup(File file, String groupname) {
        return false;
    }

    boolean deleteFileFromGroup(String filename, String groupname) {
        return false;
    }
}
