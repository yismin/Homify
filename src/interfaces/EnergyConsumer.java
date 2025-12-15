package interfaces;
/**
 * Interface for devices that consume energy
 */
public interface EnergyConsumer {
    /**
     * Gets the current energy consumption of the device
     * @return Energy consumption in watts
     */
    double getEnergyConsumption();
    /**
     * Gets the energy efficiency rating of the device
     * @return Efficiency rating (A+ to F)
     */
    String getEnergyEfficiencyRating();
}