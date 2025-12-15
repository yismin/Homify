package devices;

import interfaces.Controllable;

/**
 * Represents a motion sensor that detects movement
 */
public class MotionSensor extends SmartDevice implements Controllable {
    private boolean motionDetected;
    private int sensitivity; // 1-10
    private long lastDetectionTime;

    public MotionSensor(String deviceId, String deviceName) {
        super(deviceId, deviceName);
        this.motionDetected = false;
        this.sensitivity = 5;
        this.lastDetectionTime = 0;
    }

    @Override
    public void turnOn() {
        isOn = true;
        System.out.println(deviceName + " activated - Monitoring for motion...");
    }

    @Override
    public void turnOff() {
        isOn = false;
        motionDetected = false;
        System.out.println(deviceName + " deactivated");
    }

    @Override
    public String getStatus() {
        return String.format("%s | Status: %s | Motion: %s | Sensitivity: %d",
                deviceName,
                isOn ? "ACTIVE" : "INACTIVE",
                motionDetected ? "DETECTED" : "None",
                sensitivity);
    }

    public void detectMotion() {
        if (!isOn) {
            System.out.println(deviceName + " is not active");
            return;
        }
        this.motionDetected = true;
        this.lastDetectionTime = System.currentTimeMillis();
        System.out.println(deviceName + " MOTION DETECTED!");
    }

    public void clearMotion() {
        this.motionDetected = false;
        System.out.println(deviceName + " motion cleared");
    }

    public void setSensitivity(int level) {
        if (level < 1 || level > 10) {
            System.out.println("Sensitivity must be between 1 and 10");
            return;
        }
        this.sensitivity = level;
        System.out.println(deviceName + " sensitivity set to " + level);
    }

    @Override
    public void executeCommand(String command) {
        switch (command.toLowerCase()) {
            case "on":
                turnOn();
                break;
            case "off":
                turnOff();
                break;
            case "detect":
                detectMotion();
                break;
            case "clear":
                clearMotion();
                break;
            default:
                System.out.println("Unknown command: " + command);
        }
    }

    @Override
    public void setMode(String mode) {
        System.out.println(deviceName + " mode set to " + mode);
    }

    // Getters
    public boolean isMotionDetected() {
        return motionDetected && isOn;
    }

    public int getSensitivity() {
        return sensitivity;
    }
}