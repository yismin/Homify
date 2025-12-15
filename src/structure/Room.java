package structure;

import devices.SmartDevice;
import exceptions.DeviceNotFoundException;
import exceptions.DuplicateDeviceException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a room containing multiple smart devices
 */
public class Room {
    private String roomName;
    private ArrayList<SmartDevice> devices;

    public Room(String roomName) {
        this.roomName = roomName;
        this.devices = new ArrayList<>();
    }

    /**
     * Adds a device to the room
     * @param device The device to add
     * @throws DuplicateDeviceException if device ID already exists
     */
    public void addDevice(SmartDevice device) throws DuplicateDeviceException {
        // Check for duplicate device ID
        for (SmartDevice d : devices) {
            if (d.getDeviceId().equals(device.getDeviceId())) {
                throw new DuplicateDeviceException(
                        "Device with ID " + device.getDeviceId() + " already exists in " + roomName
                );
            }
        }
        devices.add(device);
        System.out.println("✓ " + device.getDeviceName() + " added to " + roomName);
    }

    /**
     * Removes a device from the room
     * @param deviceId The ID of the device to remove
     * @throws DeviceNotFoundException if device doesn't exist
     */
    public void removeDevice(String deviceId) throws DeviceNotFoundException {
        SmartDevice deviceToRemove = null;
        for (SmartDevice device : devices) {
            if (device.getDeviceId().equals(deviceId)) {
                deviceToRemove = device;
                break;
            }
        }

        if (deviceToRemove == null) {
            throw new DeviceNotFoundException("Device " + deviceId + " not found in " + roomName);
        }

        devices.remove(deviceToRemove);
        System.out.println("✓ " + deviceToRemove.getDeviceName() + " removed from " + roomName);
    }

    /**
     * Finds a device by its ID
     * @param deviceId The device ID to search for
     * @return The device if found
     * @throws DeviceNotFoundException if device doesn't exist
     */
    public SmartDevice findDeviceById(String deviceId) throws DeviceNotFoundException {
        for (SmartDevice device : devices) {
            if (device.getDeviceId().equals(deviceId)) {
                return device;
            }
        }
        throw new DeviceNotFoundException("Device " + deviceId + " not found in " + roomName);
    }

    /**
     * Gets all devices of a specific type
     * @param deviceClass The class of the device type
     * @return List of devices matching the type
     */
    public <T extends SmartDevice> List<T> getDevicesByType(
            Class<T> deviceClass) {
        return devices.stream()
                .filter(deviceClass::isInstance)
                .map(deviceClass::cast)
                .collect(Collectors.toList());
    }
    /**
     * Turns off all devices in the room
     */
    public void turnOffAllDevices() {
        for (SmartDevice device : devices) {
            device.turnOff();
        }
        System.out.println("✓ All devices in " + roomName + " turned off");
    }

    /**
     * Turns on all devices in the room
     */
    public void turnOnAllDevices() {
        for (SmartDevice device : devices) {
            device.turnOn();
        }
        System.out.println("✓ All devices in " + roomName + " turned on");
    }

    /**
     * Displays status of all devices in the room
     */
    public void showAllDevices() {
        System.out.println("\n╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("  " + roomName.toUpperCase());
        System.out.println("╚══════════════════════════════════════════════════════════════════╝");

        if (devices.isEmpty()) {
            System.out.println("  No devices in this room");
        } else {
            for (SmartDevice device : devices) {
                System.out.println("  • " + device.getStatus());
            }
        }
    }

    // Getters
    public String getRoomName() {
        return roomName;
    }

    public ArrayList<SmartDevice> getDevices() {
        return new ArrayList<>(devices); // Return copy for safety
    }

    public int getDeviceCount() {
        return devices.size();
    }}