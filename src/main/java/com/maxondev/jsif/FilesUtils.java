package com.maxondev.jsif;

import java.io.File;
import java.util.Objects;

public class FilesUtils {
    static public boolean deleteRecursive(File path) {
        boolean deletedSuccessfully = true;
        if (path.isDirectory()) {
            for (File f : Objects.requireNonNull(path.listFiles())) {
                deletedSuccessfully = deletedSuccessfully && deleteRecursive(f);
            }
        }
        return deletedSuccessfully && path.delete();
    }
}