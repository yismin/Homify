package interfaces;
public interface Controllable {
    /**
     * Executes a command on the device
     * @param command The command to execute
     */
    void executeCommand(String command);

    /**
     * Sets the operating mode of the device
     * @param mode The mode to set
     */
    void setMode(String mode);
}