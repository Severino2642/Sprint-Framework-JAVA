package mg.itu.framework.sprint.utils;

public class Utils {
    public static String getFileName(String fileName, String extension) {
        return fileName.substring(0, (fileName.length() - extension.length()) - 1);
    }
}
