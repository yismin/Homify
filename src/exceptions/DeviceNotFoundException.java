package exceptions;

/**
 * Exception thrown when a device cannot be found
 */
public class DeviceNotFoundException extends Exception {
    public DeviceNotFoundException(String message) {
        super(message);
    }
}