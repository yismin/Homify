package exceptions;

/**
 * Exception thrown when attempting to add a device with duplicate ID
 */
public class DuplicateDeviceException extends Exception {
    public DuplicateDeviceException(String message) {
        super(message);
    }
}