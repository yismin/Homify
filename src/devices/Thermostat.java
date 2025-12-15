package devices;

import interfaces.Controllable;
import interfaces.EnergyConsumer;
import interfaces.Schedulable;
import exceptions.InvalidDeviceStateException;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a smart thermostat with temperature control
 */
public class Thermostat extends SmartDevice implements Controllable, EnergyConsumer, Schedulable {
    private int currentTemperature;
    private int targetTemperature;
    private String mode; // "heat", "cool", "auto"
    private Map<String, String> scheduledTasks;

    public Thermostat(String deviceId, String deviceName, int initialTemp) {
        super(deviceId, deviceName);
        this.currentTemperature = initialTemp;
        this.targetTemperature = initialTemp;
        this.mode = "auto";
        this.scheduledTasks = new HashMap<>();
    }

    @Override
    public void turnOn() {
        isOn = true;
        System.out.println(deviceName + " turned ON - Mode: " + mode);
    }

    @Override
    public void turnOff() {
        isOn = false;
        System.out.println(deviceName + " turned OFF");
    }

    @Override
    public String getStatus() {
        return String.format("%s | Status: %s | Current: %d°C | Target: %d°C | Mode: %s | Energy: %.2fW",
                deviceName,
                isOn ? "ON" : "OFF",
                currentTemperature,
                targetTemperature,
                mode,
                getEnergyConsumption());
    }

    public void setTemperature(int temperature) {
        if (temperature < 10 || temperature > 35) {
            throw new InvalidDeviceStateException("Temperature must be between 10°C and 35°C");
        }
        this.targetTemperature = temperature;
        System.out.println(deviceName + " target temperature set to " + temperature + "°C");

        // Simulate temperature adjustment
        adjustTemperature();
    }

    private void adjustTemperature() {
        if (!isOn) return;

        if (currentTemperature < targetTemperature) {
            currentTemperature++;
            System.out.println(deviceName + " heating... Current: " + currentTemperature + "°C");
        } else if (currentTemperature > targetTemperature) {
            currentTemperature--;
            System.out.println(deviceName + " cooling... Current: " + currentTemperature + "°C");
        } else {
            System.out.println(deviceName + " temperature reached target.");
        }
    }

    @Override
    public void executeCommand(String command) {
        String[] parts = command.split(" ");
        switch (parts[0].toLowerCase()) {
            case "on":
                turnOn();
                break;
            case "off":
                turnOff();
                break;
            case "settemp":
                if (parts.length > 1) {
                    setTemperature(Integer.parseInt(parts[1]));
                }
                break;
            case "setmode":
                if (parts.length > 1) {
                    setMode(parts[1]);
                }
                break;
            default:
                System.out.println("Unknown command: " + command);
        }
    }

    @Override
    public void setMode(String mode) {
        if (mode.equals("heat") || mode.equals("cool") || mode.equals("auto")) {
            this.mode = mode;
            System.out.println(deviceName + " mode set to " + mode);
        } else {
            throw new InvalidDeviceStateException("Invalid mode. Use: heat, cool, or auto");
        }
    }

    @Override
    public double getEnergyConsumption() {
        if (!isOn) return 0;

        // Higher consumption when actively heating/cooling
        if (currentTemperature != targetTemperature) {
            return mode.equals("heat") ? 150.0 : 120.0;
        }
        return 50.0; // Idle consumption
    }

    @Override
    public String getEnergyEfficiencyRating() {
        return "A";
    }

    @Override
    public void scheduleTask(String time, String action) {
        scheduledTasks.put(time, action);
        System.out.println(deviceName + " scheduled: " + action + " at " + time);
    }

    @Override
    public void cancelScheduledTask(String taskId) {
        scheduledTasks.remove(taskId);
        System.out.println("Scheduled task " + taskId + " cancelled for " + deviceName);
    }

    // Getters
    public int getCurrentTemperature() {
        return currentTemperature;
    }

    public int getTargetTemperature() {
        return targetTemperature;
    }

    public String getMode() {
        return mode;
    }
}