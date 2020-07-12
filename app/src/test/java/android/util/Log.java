package android.util;

/**
 * A convenience test class for mocking Android Log class.
 */
public class Log {

    public static int e(String tag, String msg, Throwable e) {
        System.out.println("ERROR: " + tag + ": " + msg);
        return 0;
    }
}
