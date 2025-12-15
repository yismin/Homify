package exceptions;

/**
 * Exception thrown when a device operation is invalid for current state
 */
public class InvalidDeviceStateException extends RuntimeException {
    public InvalidDeviceStateException(String message) {
        super(message);
    }
}