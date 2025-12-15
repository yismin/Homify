package devices;

import interfaces.Controllable;
import interfaces.EnergyConsumer;
import exceptions.InvalidDeviceStateException;

/**
 * Represents a smart light with brightness control
 */
public class Light extends SmartDevice implements Controllable, EnergyConsumer {
    private int brightness; // 0-100
    private String color;
    private String mode;

    public Light(String deviceId, String deviceName, int initialBrightness) {
        super(deviceId, deviceName);
        this.brightness = initialBrightness;
        this.color = "white";
        this.mode = "normal";
    }

    @Override
    public void turnOn() {
        isOn = true;
        if (brightness == 0) {
            brightness = 100;
        }
        System.out.println(deviceName + " turned ON (Brightness: " + brightness + "%)");
    }

    @Override
    public void turnOff() {
        isOn = false;
        System.out.println(deviceName + " turned OFF");
    }

    @Override
    public String getStatus() {
        return String.format("%s | Status: %s | Brightness: %d%% | Color: %s | Energy: %.2fW",
                deviceName,
                isOn ? "ON" : "OFF",
                brightness,
                color,
                getEnergyConsumption());
    }

    /**
     * Sets the brightness level
     * @param level Brightness level (0-100)
     * @throws InvalidDeviceStateException if brightness is out of range
     */
    public void setBrightness(int level) {
        if (level < 0 || level > 100) {
            throw new InvalidDeviceStateException("Brightness must be between 0 and 100");
        }
        this.brightness = level;
        System.out.println(deviceName + " brightness set to " + level + "%");

        if (level == 0) {
            isOn = false;
        } else if (!isOn) {
            isOn = true;
        }
    }

    public void setColor(String color) {
        this.color = color;
        System.out.println(deviceName + " color changed to " + color);
    }

    public void dim() {
        if (brightness > 10) {
            setBrightness(brightness - 10);
        }
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
            case "dim":
                dim();
                break;
            default:
                System.out.println("Unknown command: " + command);
        }
    }

    @Override
    public void setMode(String mode) {
        this.mode = mode;
        System.out.println(deviceName + " mode set to " + mode);
    }

    @Override
    public double getEnergyConsumption() {
        // LED bulb: approx 10W at full brightness
        return isOn ? (brightness * 0.1) : 0;
    }

    @Override
    public String getEnergyEfficiencyRating() {
        return "A+"; // LED lights are very efficient
    }

    // Getters
    public int getBrightness() {
        return brightness;
    }

    public String getColor() {
        return color;
    }
}