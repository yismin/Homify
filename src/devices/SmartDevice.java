package devices;

/**
 * Abstract base class for all smart devices in the home automation system.
 */
public abstract class SmartDevice {
    protected String deviceId;
    protected String deviceName;
    protected boolean isOn;

    /**
     * Constructor for SmartDevice
     * @param deviceId Unique identifier for the device
     * @param deviceName Human-readable name for the device
     */
    public SmartDevice(String deviceId, String deviceName) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.isOn = false;
    }

    /**
     * Turns the device on
     */
    public abstract void turnOn();

    /**
     * Turns the device off
     */
    public abstract void turnOff();

    /**
     * Gets the current status of the device
     * @return String representation of device status
     */
    public abstract String getStatus();

    // Getters
    public String getDeviceId() {
        return deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public boolean isOn() {
        return isOn;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s", deviceId, deviceName, isOn ? "ON" : "OFF");
    }
}