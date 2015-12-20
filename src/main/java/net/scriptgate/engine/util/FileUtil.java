package net.scriptgate.engine.util;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtil {

    private static final DateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");

    public static File getUniqueFileNameWithTimestamp(File parentDirectory, String extension) {
        String timestamp = timestampFormat.format(new Date());
        int index = 1;

        File result;
        do {
            result = new File(parentDirectory, timestamp + (index == 1 ? "" : "_" + index) + "."+extension);
            ++index;
        } while (result.exists());
        return result;
    }
}
