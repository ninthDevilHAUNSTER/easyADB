package exception;

public class ScreenshotNullImageException extends Exception {
    public ScreenshotNullImageException() {
        super();
    }

    public ScreenshotNullImageException(String str) {
        super(str);
    }
}