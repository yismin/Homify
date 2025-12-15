package devices;

import interfaces.Controllable;
import interfaces.EnergyConsumer;
import exceptions.InvalidDeviceStateException;

/**
 * Represents a smart TV with channel and volume control
 */
public class SmartTV extends SmartDevice implements Controllable, EnergyConsumer {
    private int currentChannel;
    private int volume;
    private boolean isStreaming;
    private String streamingApp;

    public SmartTV(String deviceId, String deviceName) {
        super(deviceId, deviceName);
        this.currentChannel = 1;
        this.volume = 50;
        this.isStreaming = false;
        this.streamingApp = "none";
    }

    @Override
    public void turnOn() {
        isOn = true;
        System.out.println(deviceName + " turned ON - Channel: " + currentChannel);
    }

    @Override
    public void turnOff() {
        isOn = false;
        isStreaming = false;
        System.out.println(deviceName + " turned OFF");
    }

    @Override
    public String getStatus() {
        String status = String.format("%s | Status: %s | Channel: %d | Volume: %d | Energy: %.2fW",
                deviceName,
                isOn ? "ON" : "OFF",
                currentChannel,
                volume,
                getEnergyConsumption());
        if (isStreaming) {
            status += " | Streaming: " + streamingApp;
        }
        return status;
    }

    public void changeChannel(int channel) {
        if (!isOn) {
            throw new InvalidDeviceStateException("TV must be on to change channel");
        }
        if (channel < 1 || channel > 999) {
            throw new InvalidDeviceStateException("Invalid channel number");
        }
        this.currentChannel = channel;
        this.isStreaming = false;
        System.out.println(deviceName + " changed to channel " + channel);
    }

    public void adjustVolume(int change) {
        if (!isOn) {
            throw new InvalidDeviceStateException("TV must be on to adjust volume");
        }
        volume += change;
        if (volume < 0) volume = 0;
        if (volume > 100) volume = 100;
        System.out.println(deviceName + " volume set to " + volume);
    }

    public void startStreaming(String app) {
        if (!isOn) {
            throw new InvalidDeviceStateException("TV must be on to stream");
        }
        this.isStreaming = true;
        this.streamingApp = app;
        System.out.println(deviceName + " now streaming from " + app);
    }

    public void stopStreaming() {
        this.isStreaming = false;
        this.streamingApp = "none";
        System.out.println(deviceName + " stopped streaming");
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
            case "channel":
                if (parts.length > 1) {
                    changeChannel(Integer.parseInt(parts[1]));
                }
                break;
            case "volumeup":
                adjustVolume(10);
                break;
            case "volumedown":
                adjustVolume(-10);
                break;
            case "stream":
                if (parts.length > 1) {
                    startStreaming(parts[1]);
                }
                break;
            default:
                System.out.println("Unknown command: " + command);
        }
    }

    @Override
    public void setMode(String mode) {
        System.out.println(deviceName + " mode set to " + mode);
    }

    @Override
    public double getEnergyConsumption() {
        if (!isOn) return 0;
        return isStreaming ? 120.0 : 80.0; // More power when streaming
    }

    @Override
    public String getEnergyEfficiencyRating() {
        return "B";
    }

    // Getters
    public int getCurrentChannel() {
        return currentChannel;
    }

    public int getVolume() {
        return volume;
    }

    public boolean isStreaming() {
        return isStreaming;
    }
}