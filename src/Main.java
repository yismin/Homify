import controller.CentralController;
import devices.*;
import structure.Home;
import structure.Room;
import automation.AutomationEngine;
import automation.AutomationRule;
import exceptions.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("===================================");
        System.out.println("  SMART HOME AUTOMATION SIMULATOR  ");
        System.out.println("===================================\n");

        // Create the home
        Home myHome = new Home("My Smart Home");

        // Create rooms
        Room livingRoom = new Room("Living Room");
        Room bedroom = new Room("Bedroom");
        Room kitchen = new Room("Kitchen");

        // Create devices
        Light livingLight = new Light("L001", "Living Room Light", 75);
        Light bedroomLight = new Light("L002", "Bedroom Light", 50);
        Thermostat livingThermostat = new Thermostat("T001", "Main Thermostat", 22);
        SmartTV tv = new SmartTV("TV001", "Living Room TV");
        MotionSensor motionSensor = new MotionSensor("S001", "Living Room Sensor");
        Light kitchenLight = new Light("L003", "Kitchen Light", 100);

        // Add devices to rooms
        try {
            livingRoom.addDevice(livingLight);
            livingRoom.addDevice(livingThermostat);
            livingRoom.addDevice(tv);
            livingRoom.addDevice(motionSensor);

            bedroom.addDevice(bedroomLight);

            kitchen.addDevice(kitchenLight);

            // Add rooms to home
            myHome.addRoom(livingRoom);
            myHome.addRoom(bedroom);
            myHome.addRoom(kitchen);

        } catch (DuplicateDeviceException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Create central controller
        CentralController controller = new CentralController(myHome);

        // Create automation engine
        AutomationEngine automationEngine = new AutomationEngine();

        // Add automation rules
        AutomationRule motionRule = new AutomationRule(
                "Motion Light Rule",
                () -> motionSensor.isMotionDetected(),
                () -> {
                    if (!livingLight.isOn()) {
                        livingLight.turnOn();
                        System.out.println("[AUTOMATION] Motion detected! Living room light turned on.");
                    }
                }
        );

        AutomationRule energySavingRule = new AutomationRule(
                "Energy Saving Rule",
                () -> controller.getTotalEnergyConsumption() > 200,
                () -> {
                    System.out.println("[AUTOMATION] High energy consumption detected! Reducing usage...");
                    livingLight.setBrightness(30);
                    tv.turnOff();
                }
        );

        automationEngine.addRule(motionRule);
        automationEngine.addRule(energySavingRule);

        // Demo menu
        runDemo(controller, automationEngine, motionSensor, myHome);
    }

    private static void runDemo(CentralController controller, AutomationEngine engine,
                                MotionSensor sensor, Home home) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n========== SMART HOME MENU ==========");
            System.out.println("1. Show all devices status");
            System.out.println("2. List all device IDs");
            System.out.println("3. Turn on all lights");
            System.out.println("4. Turn off all devices");
            System.out.println("5. Show energy consumption");
            System.out.println("6. Control specific device");
            System.out.println("7. Simulate motion detection");
            System.out.println("8. Run automation rules");
            System.out.println("9. Search device by ID");
            System.out.println("10. Exit");
            System.out.print("Choose option: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                switch (choice) {
                    case 1:
                        controller.showAllDevicesStatus();
                        break;
                    case 2:
                        listAllDeviceIds(controller);
                        break;
                    case 3:
                        controller.turnOnAllLights();
                        break;
                    case 4:
                        controller.turnOffAllDevices();
                        break;
                    case 5:
                        System.out.println("\n=== ENERGY REPORT ===");
                        System.out.printf("Total Energy Consumption: %.2f watts\n",
                                controller.getTotalEnergyConsumption());
                        break;
                    case 6:
                        controlSpecificDevice(controller, scanner, home);
                        break;
                    case 7:
                        System.out.println("\n[SIMULATION] Motion detected in living room!");
                        sensor.detectMotion();
                        engine.evaluateRules();
                        break;
                    case 8:
                        System.out.println("\n[AUTOMATION] Evaluating all rules...");
                        engine.evaluateRules();
                        break;
                    case 9:
                        System.out.print("Enter device ID: ");
                        String id = scanner.nextLine();
                        controller.searchDeviceById(id);
                        break;
                    case 10:
                        System.out.println("\nShutting down Smart Home System...");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid option!");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                scanner.nextLine(); // clear buffer
            }
        }

        scanner.close();
    }

    private static void controlSpecificDevice(CentralController controller,
                                              Scanner scanner, Home home) {
        System.out.print("Enter device ID: ");
        String deviceId = scanner.nextLine();

        try {
            SmartDevice device = controller.findDevice(deviceId);

            if (device instanceof Light) {
                Light light = (Light) device;
                System.out.println("\n=== LIGHT CONTROL ===");
                System.out.println("1. Turn On");
                System.out.println("2. Turn Off");
                System.out.println("3. Set Brightness");
                System.out.println("4. Set Color");
                System.out.println("5. Dim Light (-10%)");
                System.out.print("Choose: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                switch (choice) {
                    case 1:
                        light.turnOn();
                        break;
                    case 2:
                        light.turnOff();
                        break;
                    case 3:
                        System.out.print("Enter brightness (0-100): ");
                        int brightness = scanner.nextInt();
                        light.setBrightness(brightness);
                        break;
                    case 4:
                        System.out.print("Enter color (e.g., white, warm, blue): ");
                        String color = scanner.nextLine();
                        light.setColor(color);
                        break;
                    case 5:
                        light.dim();
                        System.out.println("Light dimmed by 10%");
                        break;
                    default:
                        System.out.println("Invalid choice!");
                }
            } else if (device instanceof Thermostat) {
                Thermostat thermostat = (Thermostat) device;
                System.out.println("\n=== THERMOSTAT CONTROL ===");
                System.out.println("1. Turn On");
                System.out.println("2. Turn Off");
                System.out.println("3. Set Temperature");
                System.out.println("4. Set Mode");
                System.out.println("5. Schedule Task");
                System.out.println("6. Cancel Scheduled Task");
                System.out.print("Choose: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                switch (choice) {
                    case 1:
                        thermostat.turnOn();
                        break;
                    case 2:
                        thermostat.turnOff();
                        break;
                    case 3:
                        System.out.print("Enter temperature (10-35°C): ");
                        int temp = scanner.nextInt();
                        thermostat.setTemperature(temp);
                        break;
                    case 4:
                        System.out.print("Enter mode (heat/cool/auto): ");
                        String mode = scanner.nextLine();
                        thermostat.setMode(mode);
                        break;
                    case 5:
                        System.out.print("Enter time (HH:MM): ");
                        String time = scanner.nextLine();
                        System.out.print("Enter action (e.g., settemp 24): ");
                        String action = scanner.nextLine();
                        thermostat.scheduleTask(time, action);
                        break;
                    case 6:
                        System.out.print("Enter task time to cancel (HH:MM): ");
                        String taskId = scanner.nextLine();
                        thermostat.cancelScheduledTask(taskId);
                        break;
                    default:
                        System.out.println("Invalid choice!");
                }
            } else if (device instanceof SmartTV) {
                SmartTV tv = (SmartTV) device;
                System.out.println("\n=== SMART TV CONTROL ===");
                System.out.println("1. Turn On");
                System.out.println("2. Turn Off");
                System.out.println("3. Change Channel");
                System.out.println("4. Volume Up");
                System.out.println("5. Volume Down");
                System.out.println("6. Start Streaming");
                System.out.println("7. Stop Streaming");
                System.out.print("Choose: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                switch (choice) {
                    case 1:
                        tv.turnOn();
                        break;
                    case 2:
                        tv.turnOff();
                        break;
                    case 3:
                        System.out.print("Enter channel (1-999): ");
                        int channel = scanner.nextInt();
                        tv.changeChannel(channel);
                        break;
                    case 4:
                        tv.adjustVolume(10);
                        break;
                    case 5:
                        tv.adjustVolume(-10);
                        break;
                    case 6:
                        System.out.print("Enter app name (Netflix/YouTube/etc): ");
                        String app = scanner.nextLine();
                        tv.startStreaming(app);
                        break;
                    case 7:
                        tv.stopStreaming();
                        break;
                    default:
                        System.out.println("Invalid choice!");
                }
            } else if (device instanceof MotionSensor) {
                // ← ADDED THIS ENTIRE SECTION
                MotionSensor sensor = (MotionSensor) device;
                System.out.println("\n=== MOTION SENSOR CONTROL ===");
                System.out.println("1. Turn On (Activate)");
                System.out.println("2. Turn Off (Deactivate)");
                System.out.println("3. Detect Motion");
                System.out.println("4. Clear Motion");
                System.out.println("5. Set Sensitivity");
                System.out.print("Choose: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        sensor.turnOn();
                        break;
                    case 2:
                        sensor.turnOff();
                        break;
                    case 3:
                        sensor.detectMotion();
                        break;
                    case 4:
                        sensor.clearMotion();
                        break;
                    case 5:
                        System.out.print("Enter sensitivity (1-10): ");
                        int sensitivity = scanner.nextInt();
                        sensor.setSensitivity(sensitivity);
                        break;
                    default:
                        System.out.println("Invalid choice!");
                }
            } else {
                System.out.println("Unknown device type!");
            }

        } catch (DeviceNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void listAllDeviceIds(CentralController controller) {
        System.out.println("\n========== ALL DEVICE IDs ==========");
        System.out.println("L001  - Living Room Light");
        System.out.println("L002  - Bedroom Light");
        System.out.println("L003  - Kitchen Light");
        System.out.println("T001  - Main Thermostat");
        System.out.println("TV001 - Living Room TV");
        System.out.println("S001  - Living Room Sensor");
        System.out.println("====================================");
    }
}