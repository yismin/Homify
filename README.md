# Homify - Smart Home Automation Simulator

A comprehensive Java-based smart home management system demonstrating advanced Object-Oriented Programming principles through intelligent device control and automation.

---

## Team Members

- **Yasmine Tbessi**
- **Chaima Chammaa**
- **Yassmine Majoul**
- **Elaa Marco**

---

## Project Overview

Homify is a virtual home management system that controls multiple smart devices across different rooms. It features dual user interfaces (GUI and CLI), intelligent automation rules, and real-time energy monitoring.

### **Key Features**
- **Dual Interface:** JavaFX GUI + Command-Line Interface
- **4 Smart Device Types:** Lights, Thermostats, Smart TVs, Motion Sensors
- **Intelligent Automation:** Rule-based IF-THEN automation engine
- **Energy Monitoring:** Real-time consumption tracking and optimization
- **Multi-Room Architecture:** Scalable home/room structure
- **Exception Handling:** Robust error management with custom exceptions

---

## Technologies & Concepts

### **Core Technologies**
- **Language:** Java 8+
- **GUI Framework:** JavaFX
- **Architecture:** Object-Oriented Programming

### **OOP Concepts Demonstrated**
- Abstraction (Abstract classes)
- Inheritance (Device hierarchy)
- Polymorphism (Interface implementations)
- Encapsulation (Private fields, public methods)
- Exception Handling (3 custom exceptions)
- Collections Framework (ArrayList, HashMap)

---

## Project Structure

```
src/
├── devices/              # Smart device implementations
│   ├── SmartDevice.java       
│   ├── Light.java
│   ├── Thermostat.java
│   ├── SmartTV.java
│   └── MotionSensor.java
├── interfaces/           # Device interfaces
│   ├── Controllable.java
│   ├── EnergyConsumer.java
│   └── Schedulable.java
├── structure/           # Home architecture
│   ├── Home.java
│   └── Room.java
├── controller/          # Central management
│   └── CentralController.java
├── automation/          # Automation engine
│   ├── AutomationEngine.java
│   └── AutomationRule.java
├── exceptions/          # Custom exceptions
│   ├── DeviceNotFoundException.java
│   ├── DuplicateDeviceException.java
│   └── InvalidDeviceStateException.java
├── Main.java            # CLI interface
└── SmartHomeGUI.java    # JavaFX GUI
```

---

## How to Run

### **Prerequisites**
- Java JDK 8 or higher
- JavaFX SDK (for GUI)

### **Option 1: Run GUI Interface**
```bash
javac SmartHomeGUI.java
java SmartHomeGUI
```

### **Option 2: Run CLI Interface**
```bash
javac Main.java
java Main
```

---

## Usage

### **GUI Interface**
- Control lights with sliders and buttons
- Adjust thermostat temperature
- Manage TV channels and volume
- Simulate motion detection
- Monitor real-time energy consumption
- Trigger automation rules

### **CLI Interface**
1. Show all devices status
2. Turn on all lights
3. Turn off all devices
4. Show energy consumption
5. Control specific device (by ID)
6. Simulate motion detection
7. Run automation rules
8. Search device by ID
9. Schedule thermostat tasks

---

## Automation Rules

### **Rule 1: Motion-Activated Lighting**
- **IF:** Motion is detected in living room
- **THEN:** Automatically turn on living room light

### **Rule 2: Energy Saving Mode**
- **IF:** Total energy consumption exceeds 200W
- **THEN:** Automatically reduce brightness to 30% and turn off TV

---

## Smart Devices

### **Light**
- Adjustable brightness (0-100%)
- Color customization
- Energy consumption: ~10W at full brightness

### **Thermostat**
- Temperature range: 10-35°C
- Modes: heat, cool, auto
- Scheduling capabilities
- Energy consumption: 50-150W

### **Smart TV**
- Channel selection (1-999)
- Volume control (0-100)
- Streaming services integration
- Energy consumption: 80-120W

### **Motion Sensor**
- Motion detection
- Adjustable sensitivity (1-10)
- Automation trigger

---

## Key Features

### **Exception Handling**
- `DeviceNotFoundException` - Thrown when device search fails
- `DuplicateDeviceException` - Prevents duplicate device IDs
- `InvalidDeviceStateException` - Validates device operations

### **Energy Management**
- Real-time consumption tracking
- Per-device energy reporting
- Automatic optimization via automation rules
- Energy efficiency ratings

### **Scalability**
- Add unlimited devices
- Create multiple rooms
- Extensible device types
- Flexible automation rules

---

## Technical Highlights

- **Abstract Classes:** `SmartDevice` with 3 abstract methods
- **Concrete Implementations:** 4 device types
- **Interfaces:** 3 fully implemented (Controllable, EnergyConsumer, Schedulable)
- **Design Patterns:** Observer pattern for automation, Factory-like device creation
- **Lambda Expressions:** Used in automation rule definitions
- **Collections:** ArrayList for device lists, HashMap for room/task management

---

## License

This project was created for educational purposes as part of an Object-Oriented Programming course.

---

## Acknowledgments

Special thanks to our professor Ameni Azzouz for providing the project guidelines and requirements that helped us build this comprehensive smart home automation system.

---

**Built by Team Homify**
