package customExceptions;

public class FileSystemTypeException extends  RuntimeException {
    public FileSystemTypeException(String message) {
        super(message);
    }
}
