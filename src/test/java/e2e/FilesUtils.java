package e2e;

import java.io.File;
import java.util.Objects;

class FilesUtils {
    static boolean deleteRecursive(File path) {
        boolean deletedSuccessfully = true;
        if (path.isDirectory()) {
            for (File f : Objects.requireNonNull(path.listFiles())) {
                deletedSuccessfully = deletedSuccessfully && deleteRecursive(f);
            }
        }
        return deletedSuccessfully && path.delete();
    }
}