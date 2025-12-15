package controller;

import structure.Home;
import structure.Room;
import devices.*;
import interfaces.Controllable;  // ← ADD THIS LINE
import interfaces.EnergyConsumer;
import exceptions.DeviceNotFoundException;

import java.util.List;
/**
 * Central controller for managing the entire smart home system
 */
public class CentralController {
    private Home home;

    public CentralController(Home home) {
        this.home = home;
        System.out.println("\n✓ Central Controller initialized for " + home.getHomeName());
    }

    /**
     * Shows status of all devices in all rooms
     */
    public void showAllDevicesStatus() {
        home.showAllRooms();
    }

    /**
     * Turns on all lights in the home
     */
    public void turnOnAllLights() {
        System.out.println("\n💡 Turning on all lights...");
        List<SmartDevice> allDevices = home.getAllDevices();
        int count = 0;

        for (SmartDevice device : allDevices) {
            if (device instanceof Light) {
                device.turnOn();
                count++;
            }
        }

        System.out.println("✓ " + count + " light(s) turned on");
    }

    /**
     * Turns off all devices in the home
     */
    public void turnOffAllDevices() {
        home.turnOffEverything();
    }

    /**
     * Calculates total energy consumption
     * @return Total energy consumption in watts
     */
    public double getTotalEnergyConsumption() {
        double total = 0;
        List<SmartDevice> allDevices = home.getAllDevices();

        for (SmartDevice device : allDevices) {
            if (device instanceof EnergyConsumer) {
                total += ((EnergyConsumer) device).getEnergyConsumption();
            }
        }

        return total;
    }

    /**
     * Searches for a device by ID and displays its status
     * @param deviceId The device ID to search for
     */
    public void searchDeviceById(String deviceId) {
        try {
            SmartDevice device = home.findDevice(deviceId);
            System.out.println("\n🔍 Device Found:");
            System.out.println("  " + device.getStatus());
        } catch (DeviceNotFoundException e) {
            System.out.println("\n✗ " + e.getMessage());
        }
    }

    /**
     * Finds and returns a device
     * @param deviceId The device ID
     * @return The device
     * @throws DeviceNotFoundException if device doesn't exist
     */
    public SmartDevice findDevice(String deviceId) throws DeviceNotFoundException {
        return home.findDevice(deviceId);
    }

    /**
     * Lists all devices of a specific type
     * @param deviceType The type name (e.g., "Light", "Thermostat")
     */
    public void listDevicesByType(String deviceType) {
        System.out.println("\n📋 Listing all " + deviceType + " devices:");
        List<SmartDevice> allDevices = home.getAllDevices();
        int count = 0;

        for (SmartDevice device : allDevices) {
            if (device.getClass().getSimpleName().equalsIgnoreCase(deviceType)) {
                System.out.println("  • " + device.getStatus());
                count++;
            }
        }

        if (count == 0) {
            System.out.println("  No " + deviceType + " devices found");
        }
    }

    /**
     * Executes a global command on all controllable devices
     * @param command The command to execute
     */
    public void executeGlobalCommand(String command) {
        System.out.println("\n⚡ Executing global command: " + command);
        List<SmartDevice> allDevices = home.getAllDevices();

        for (SmartDevice device : allDevices) {
            if (device instanceof Controllable) {
                ((Controllable) device).executeCommand(command);
            }
        }
    }

    /**
     * Activates energy saving mode
     */
    public void energySavingMode() {
        System.out.println("\n🌱 Activating Energy Saving Mode...");
        List<SmartDevice> allDevices = home.getAllDevices();

        for (SmartDevice device : allDevices) {
            if (device instanceof Light) {
                Light light = (Light) device;
                if (light.isOn()) {
                    light.setBrightness(30); // Reduce brightness
                }
            } else if (device instanceof SmartTV) {
                device.turnOff(); // Turn off TVs
            }
        }

        System.out.println("✓ Energy saving mode activated");
    }

    public Home getHome() {
        return home;
    }
}