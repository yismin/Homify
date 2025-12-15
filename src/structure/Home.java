package structure;

import devices.SmartDevice;
import exceptions.DeviceNotFoundException;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a home containing multiple rooms
 */
public class Home {
    private String homeName;
    private HashMap<String, Room> rooms;

    public Home(String homeName) {
        this.homeName = homeName;
        this.rooms = new HashMap<>();
    }

    /**
     * Adds a room to the home
     * @param room The room to add
     */
    public void addRoom(Room room) {
        rooms.put(room.getRoomName(), room);
        System.out.println("✓ Room '" + room.getRoomName() + "' added to " + homeName);
    }

    /**
     * Removes a room from the home
     * @param roomName The name of the room to remove
     */
    public void removeRoom(String roomName) {
        Room removed = rooms.remove(roomName);
        if (removed != null) {
            System.out.println("✓ Room '" + roomName + "' removed from " + homeName);
        } else {
            System.out.println("✗ Room '" + roomName + "' not found");
        }
    }

    /**
     * Gets a specific room
     * @param roomName The name of the room
     * @return The room object, or null if not found
     */
    public Room getRoom(String roomName) {
        return rooms.get(roomName);
    }

    /**
     * Gets all devices in the entire home
     * @return List of all devices
     */
    public List<SmartDevice> getAllDevices() {
        List<SmartDevice> allDevices = new ArrayList<>();
        for (Room room : rooms.values()) {
            allDevices.addAll(room.getDevices());
        }
        return allDevices;
    }

    /**
     * Finds a device by ID across all rooms
     * @param deviceId The device ID to search for
     * @return The device if found
     * @throws DeviceNotFoundException if device doesn't exist
     */
    public SmartDevice findDevice(String deviceId) throws DeviceNotFoundException {
        for (Room room : rooms.values()) {
            try {
                return room.findDeviceById(deviceId);
            } catch (DeviceNotFoundException e) {
                // Continue searching in other rooms
            }
        }
        throw new DeviceNotFoundException("Device " + deviceId + " not found in any room");
    }

    /**
     * Turns off all devices in all rooms
     */
    public void turnOffEverything() {
        System.out.println("\n🔌 Shutting down all devices in " + homeName + "...");
        for (Room room : rooms.values()) {
            room.turnOffAllDevices();
        }
    }

    /**
     * Displays all rooms and their devices
     */
    public void showAllRooms() {
        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("║  " + homeName.toUpperCase() + " - FULL STATUS");
        System.out.println("╚════════════════════════════════════════════════════════════════════╝");

        if (rooms.isEmpty()) {
            System.out.println("  No rooms in this home");
        } else {
            for (Room room : rooms.values()) {
                room.showAllDevices();
            }
        }

        System.out.println("\n  Total Rooms: " + rooms.size());
        System.out.println("  Total Devices: " + getAllDevices().size());
    }

    // Getters
    public String getHomeName() {
        return homeName;
    }

    public HashMap<String, Room> getRooms() {
        return new HashMap<>(rooms); // Return copy for safety
    }

    public int getRoomCount() {
        return rooms.size();
    }
}