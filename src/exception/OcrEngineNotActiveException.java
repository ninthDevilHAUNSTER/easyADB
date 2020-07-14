package exception;

public class OcrEngineNotActiveException extends Exception {
    public OcrEngineNotActiveException() {
        super();
    }

    public OcrEngineNotActiveException(String str) {
        super(str);
    }
}
