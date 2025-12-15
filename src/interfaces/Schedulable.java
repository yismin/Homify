package interfaces;
/**
 * Interface for devices that can be scheduled
 */
public interface Schedulable {
    /**
     * Schedules a task for the device
     * @param time Time to execute the task (format: "HH:MM")
     * @param action Action to perform
     */
    void scheduleTask(String time, String action);

    /**
     * Cancels a scheduled task
     * @param taskId ID of the task to cancel
     */
    void cancelScheduledTask(String taskId);
}