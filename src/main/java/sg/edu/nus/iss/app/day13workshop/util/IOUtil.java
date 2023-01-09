package sg.edu.nus.iss.app.day13workshop.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IOUtil {
    // Create a logger for the IOUtil class
    private static final Logger logger = LoggerFactory.getLogger(IOUtil.class);

    /**
     * Creates a new directory at the specified path. If the directory is successfully created,
     * it sets the POSIX file permissions of the directory to "rwxrwx---" if the operating system
     * is not Windows.
     * @param path The path of the directory to be created.
     */
    public static void createDir(String path) {
         // Create a new File object for the specified path
        File dir = new File(path);
        // Create the directory using the mkdirs method of the File class
        boolean isDirCreated = dir.mkdirs();
        // Log whether the directory was created
        logger.info("dir created > " + isDirCreated);
        // If the directory was successfully created
        if (isDirCreated) {
            // Get the name of the operating system the code is running on
            String osName = System.getProperty("os.name");
            // If the operating system is not Windows
            if (!osName.contains("Windows")) {
                try {
                    // Set the POSIX file permissions of the directory to "rwxrwx---"
                    String perm = "rwxrwx---";
                    Set<PosixFilePermission> permissions = PosixFilePermissions.fromString(perm);
                    Files.setPosixFilePermissions(dir.toPath(), permissions);
                } catch (IOException e) {
                    // Log any errors that occurred
                    logger.error("Error ", e);
                }
            }
        }
    }
}
