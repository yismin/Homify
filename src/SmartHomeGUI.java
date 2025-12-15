import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import controller.CentralController;
import devices.*;
import structure.Home;
import structure.Room;
import automation.AutomationEngine;
import automation.AutomationRule;
import exceptions.*;

/**
 * Smart Home GUI Application - Improved Version
 */
public class SmartHomeGUI extends Application {
    private CentralController controller;
    private AutomationEngine automationEngine;
    private TextArea logArea;

    // Device references
    private Light livingLight;
    private Light bedroomLight;
    private Light kitchenLight;
    private Thermostat thermostat;
    private SmartTV tv;
    private MotionSensor motionSensor;

    // Status labels for real-time updates
    private Label tvStatusLabel;
    private Label sensorStatusLabel;
    private Label energyStatusLabel;

    @Override
    public void start(Stage primaryStage) {
        initializeSmartHome();

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f5f5;");

        root.setTop(createTitleBar());
        root.setCenter(createMainContent());
        root.setBottom(createLogPanel());

        Scene scene = new Scene(root, 1300, 850);
        primaryStage.setTitle("Smart Home Automation System");
        primaryStage.setScene(scene);
        primaryStage.show();

        log("✅ System Ready! Control your devices using the buttons and sliders.");
        updateEnergyDisplay();
    }

    private void initializeSmartHome() {
        Home myHome = new Home("My Smart Home");

        Room livingRoom = new Room("Living Room");
        Room bedroom = new Room("Bedroom");
        Room kitchen = new Room("Kitchen");

        livingLight = new Light("L001", "Living Room Light", 75);
        bedroomLight = new Light("L002", "Bedroom Light", 50);
        kitchenLight = new Light("L003", "Kitchen Light", 100);
        thermostat = new Thermostat("T001", "Main Thermostat", 22);
        tv = new SmartTV("TV001", "Living Room TV");
        motionSensor = new MotionSensor("S001", "Living Room Sensor");

        try {
            livingRoom.addDevice(livingLight);
            livingRoom.addDevice(thermostat);
            livingRoom.addDevice(tv);
            livingRoom.addDevice(motionSensor);
            bedroom.addDevice(bedroomLight);
            kitchen.addDevice(kitchenLight);

            myHome.addRoom(livingRoom);
            myHome.addRoom(bedroom);
            myHome.addRoom(kitchen);
        } catch (DuplicateDeviceException e) {
            log("Error: " + e.getMessage());
        }

        controller = new CentralController(myHome);

        // Automation setup with CLEAR explanations
        automationEngine = new AutomationEngine();

        // Rule 1: Motion turns on light automatically
        AutomationRule motionRule = new AutomationRule(
                "Motion Light Rule",
                () -> motionSensor.isMotionDetected(),
                () -> {
                    if (!livingLight.isOn()) {
                        livingLight.turnOn();
                        log("🤖 [AUTO] Motion detected → Living room light turned ON automatically!");
                        updateEnergyDisplay();
                    }
                }
        );

        // Rule 2: High energy usage triggers energy saving
        AutomationRule energySavingRule = new AutomationRule(
                "Energy Saving Rule",
                () -> controller.getTotalEnergyConsumption() > 200,
                () -> {
                    log("🤖 [AUTO] High energy (>200W) → Activating energy saving mode!");
                    controller.energySavingMode();
                    updateEnergyDisplay();
                }
        );

        automationEngine.addRule(motionRule);
        automationEngine.addRule(energySavingRule);
    }

    private HBox createTitleBar() {
        HBox titleBar = new HBox();
        titleBar.setAlignment(Pos.CENTER);
        titleBar.setPadding(new Insets(20));
        titleBar.setStyle("-fx-background-color: linear-gradient(to right, #667eea, #764ba2);");

        Label title = new Label("🏠 Smart Home Control Center");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white;");

        titleBar.getChildren().add(title);
        return titleBar;
    }

    private ScrollPane createMainContent() {
        HBox mainLayout = new HBox(15);
        mainLayout.setPadding(new Insets(20));

        // Left column
        VBox leftColumn = new VBox(15);
        leftColumn.getChildren().addAll(
                createLightsSection(),
                createThermostatSection()
        );

        // Right column
        VBox rightColumn = new VBox(15);
        rightColumn.getChildren().addAll(
                createTVSection(),
                createMotionSensorSection(),
                createAutomationSection(),
                createGlobalSection()
        );

        mainLayout.getChildren().addAll(leftColumn, rightColumn);
        HBox.setHgrow(leftColumn, Priority.ALWAYS);
        HBox.setHgrow(rightColumn, Priority.ALWAYS);

        ScrollPane scrollPane = new ScrollPane(mainLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: white;");
        return scrollPane;
    }

    private VBox createLightsSection() {
        VBox box = new VBox(15);
        box.setPadding(new Insets(15));
        box.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-width: 2px; -fx-border-radius: 10px; -fx-background-radius: 10px;");

        Label header = new Label("💡 Smart Lights");
        header.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        box.getChildren().addAll(
                header,
                createLightControl("Living Room", livingLight),
                createLightControl("Bedroom", bedroomLight),
                createLightControl("Kitchen", kitchenLight)
        );

        return box;
    }

    private HBox createLightControl(String name, Light light) {
        HBox box = new HBox(15);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-background-color: #f9f9f9; -fx-border-radius: 5px; -fx-background-radius: 5px;");

        Label label = new Label(name);
        label.setPrefWidth(120);
        label.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        Button onBtn = new Button("ON");
        onBtn.setPrefWidth(70);
        onBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px;");
        onBtn.setOnAction(e -> {
            light.turnOn();
            log("💡 " + name + " light turned ON");
            updateEnergyDisplay();
        });

        Button offBtn = new Button("OFF");
        offBtn.setPrefWidth(70);
        offBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px;");
        offBtn.setOnAction(e -> {
            light.turnOff();
            log("💡 " + name + " light turned OFF");
            updateEnergyDisplay();
        });

        Label brightLabel = new Label("Brightness:");
        brightLabel.setStyle("-fx-font-size: 12px;");

        Slider slider = new Slider(0, 100, light.getBrightness());
        slider.setPrefWidth(180);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setMajorTickUnit(25);

        Label valueLabel = new Label(light.getBrightness() + "%");
        valueLabel.setPrefWidth(50);
        valueLabel.setStyle("-fx-font-weight: bold;");

        slider.valueProperty().addListener((obs, old, newVal) -> {
            light.setBrightness(newVal.intValue());
            valueLabel.setText(newVal.intValue() + "%");
            log("💡 " + name + " brightness: " + newVal.intValue() + "%");
            updateEnergyDisplay();
        });

        box.getChildren().addAll(label, onBtn, offBtn, brightLabel, slider, valueLabel);
        return box;
    }

    private VBox createThermostatSection() {
        VBox box = new VBox(15);
        box.setPadding(new Insets(15));
        box.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-width: 2px; -fx-border-radius: 10px; -fx-background-radius: 10px;");

        Label header = new Label("🌡️ Thermostat");
        header.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        HBox controls = new HBox(15);
        controls.setAlignment(Pos.CENTER_LEFT);
        controls.setPadding(new Insets(10));
        controls.setStyle("-fx-background-color: #f9f9f9; -fx-border-radius: 5px; -fx-background-radius: 5px;");

        Button onBtn = new Button("ON");
        onBtn.setPrefWidth(70);
        onBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px;");
        onBtn.setOnAction(e -> {
            thermostat.turnOn();
            log("🌡️ Thermostat turned ON");
            updateEnergyDisplay();
        });

        Button offBtn = new Button("OFF");
        offBtn.setPrefWidth(70);
        offBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px;");
        offBtn.setOnAction(e -> {
            thermostat.turnOff();
            log("🌡️ Thermostat turned OFF");
            updateEnergyDisplay();
        });

        Label tempLabel = new Label("Temperature:");
        tempLabel.setStyle("-fx-font-size: 12px;");

        Slider tempSlider = new Slider(10, 35, thermostat.getTargetTemperature());
        tempSlider.setPrefWidth(200);
        tempSlider.setShowTickMarks(true);
        tempSlider.setShowTickLabels(true);
        tempSlider.setMajorTickUnit(5);

        Label valueLabel = new Label(thermostat.getTargetTemperature() + "°C");
        valueLabel.setPrefWidth(60);
        valueLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        tempSlider.valueProperty().addListener((obs, old, newVal) -> {
            thermostat.setTemperature(newVal.intValue());
            valueLabel.setText(newVal.intValue() + "°C");
            log("🌡️ Temperature set to " + newVal.intValue() + "°C");
            updateEnergyDisplay();
        });

        controls.getChildren().addAll(onBtn, offBtn, tempLabel, tempSlider, valueLabel);
        box.getChildren().addAll(header, controls);

        return box;
    }

    private VBox createTVSection() {
        VBox box = new VBox(15);
        box.setPadding(new Insets(15));
        box.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-width: 2px; -fx-border-radius: 10px; -fx-background-radius: 10px;");

        Label header = new Label("📺 Smart TV");
        header.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // TV Status Display
        tvStatusLabel = new Label("Status: OFF | Channel: 1 | Volume: 50");
        tvStatusLabel.setStyle("-fx-font-size: 13px; -fx-padding: 10; -fx-background-color: #f0f0f0; -fx-border-radius: 5px; -fx-background-radius: 5px;");

        // Power Controls
        HBox powerControls = new HBox(10);
        powerControls.setAlignment(Pos.CENTER_LEFT);
        powerControls.setPadding(new Insets(10));

        Button onBtn = new Button("⚡ Power ON");
        onBtn.setPrefWidth(120);
        onBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        onBtn.setOnAction(e -> {
            tv.turnOn();
            updateTVStatus();
            log("📺 TV turned ON");
            updateEnergyDisplay();
        });

        Button offBtn = new Button("⚡ Power OFF");
        offBtn.setPrefWidth(120);
        offBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold;");
        offBtn.setOnAction(e -> {
            tv.turnOff();
            updateTVStatus();
            log("📺 TV turned OFF");
            updateEnergyDisplay();
        });

        powerControls.getChildren().addAll(onBtn, offBtn);

        // Volume Controls
        HBox volumeControls = new HBox(10);
        volumeControls.setAlignment(Pos.CENTER_LEFT);
        volumeControls.setPadding(new Insets(10));

        Label volLabel = new Label("Volume:");
        volLabel.setStyle("-fx-font-weight: bold;");

        Button volUpBtn = new Button("🔊 Vol +");
        volUpBtn.setPrefWidth(90);
        volUpBtn.setOnAction(e -> {
            try {
                tv.adjustVolume(10);
                updateTVStatus();
                log("📺 Volume increased");
            } catch (Exception ex) {
                log("❌ Turn TV on first!");
            }
        });

        Button volDownBtn = new Button("🔉 Vol -");
        volDownBtn.setPrefWidth(90);
        volDownBtn.setOnAction(e -> {
            try {
                tv.adjustVolume(-10);
                updateTVStatus();
                log("📺 Volume decreased");
            } catch (Exception ex) {
                log("❌ Turn TV on first!");
            }
        });

        volumeControls.getChildren().addAll(volLabel, volUpBtn, volDownBtn);

        // Channel Controls
        HBox channelControls = new HBox(10);
        channelControls.setAlignment(Pos.CENTER_LEFT);
        channelControls.setPadding(new Insets(10));

        Label channelLabel = new Label("Channel:");
        channelLabel.setStyle("-fx-font-weight: bold;");

        TextField channelField = new TextField();
        channelField.setPromptText("1-999");
        channelField.setPrefWidth(80);

        Button changeChannelBtn = new Button("📡 Change Channel");
        changeChannelBtn.setOnAction(e -> {
            try {
                int ch = Integer.parseInt(channelField.getText());
                tv.changeChannel(ch);
                updateTVStatus();
                log("📺 Changed to channel " + ch);
                channelField.clear();
            } catch (NumberFormatException ex) {
                log("❌ Enter a valid channel number (1-999)");
            } catch (Exception ex) {
                log("❌ " + ex.getMessage());
            }
        });

        channelControls.getChildren().addAll(channelLabel, channelField, changeChannelBtn);

        box.getChildren().addAll(header, tvStatusLabel, powerControls, volumeControls, channelControls);
        return box;
    }

    private VBox createMotionSensorSection() {
        VBox box = new VBox(15);
        box.setPadding(new Insets(15));
        box.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-width: 2px; -fx-border-radius: 10px; -fx-background-radius: 10px;");

        Label header = new Label("📡 Motion Sensor (For Automation)");
        header.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Explanation
        Label explanation = new Label(
                "💡 This sensor triggers automation:\n" +
                        "When motion is detected → Living room light turns ON automatically!"
        );
        explanation.setWrapText(true);
        explanation.setStyle("-fx-font-size: 12px; -fx-background-color: #E3F2FD; -fx-padding: 10; -fx-border-radius: 5px; -fx-background-radius: 5px;");

        sensorStatusLabel = new Label("Status: INACTIVE | No motion detected");
        sensorStatusLabel.setStyle("-fx-font-size: 13px; -fx-padding: 10; -fx-background-color: #f0f0f0; -fx-border-radius: 5px; -fx-background-radius: 5px;");

        HBox controls = new HBox(10);
        controls.setAlignment(Pos.CENTER_LEFT);
        controls.setPadding(new Insets(10));

        Button activateBtn = new Button("✅ Activate Sensor");
        activateBtn.setPrefWidth(150);
        activateBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        activateBtn.setOnAction(e -> {
            motionSensor.turnOn();
            updateSensorStatus();
            log("📡 Motion sensor activated (ready to detect)");
        });

        Button simulateBtn = new Button("🏃 SIMULATE MOTION");
        simulateBtn.setPrefWidth(170);
        simulateBtn.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-weight: bold;");
        simulateBtn.setOnAction(e -> {
            motionSensor.detectMotion();
            updateSensorStatus();
            log("🏃 Motion detected! Checking automation rules...");
            automationEngine.evaluateRules();
            updateEnergyDisplay();
        });

        controls.getChildren().addAll(activateBtn, simulateBtn);

        box.getChildren().addAll(header, explanation, sensorStatusLabel, controls);
        return box;
    }

    private VBox createAutomationSection() {
        VBox box = new VBox(15);
        box.setPadding(new Insets(15));
        box.setStyle("-fx-background-color: white; -fx-border-color: #9C27B0; -fx-border-width: 2px; -fx-border-radius: 10px; -fx-background-radius: 10px;");

        Label header = new Label("🤖 Automation Rules (Smart Features)");
        header.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextArea rulesInfo = new TextArea(
                "ACTIVE AUTOMATION RULES:\n\n" +
                        "1. 🏃 Motion Detection → Auto Light\n" +
                        "   IF motion is detected\n" +
                        "   THEN turn on living room light automatically\n\n" +
                        "2. ⚡ Energy Saving → Auto Reduce\n" +
                        "   IF total energy usage > 200 watts\n" +
                        "   THEN reduce brightness & turn off TV automatically\n\n" +
                        "💡 TIP: Try 'Simulate Motion' to see automation work!"
        );
        rulesInfo.setEditable(false);
        rulesInfo.setPrefHeight(160);
        rulesInfo.setWrapText(true);
        rulesInfo.setStyle("-fx-font-size: 12px; -fx-font-family: 'Courier New';");

        Button evaluateBtn = new Button("🔄 Check & Run Automation Now");
        evaluateBtn.setPrefWidth(250);
        evaluateBtn.setPrefHeight(40);
        evaluateBtn.setStyle("-fx-background-color: #9C27B0; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px;");
        evaluateBtn.setOnAction(e -> {
            log("🤖 Checking all automation rules...");
            automationEngine.evaluateRules();
            updateEnergyDisplay();
        });

        HBox buttonBox = new HBox(evaluateBtn);
        buttonBox.setAlignment(Pos.CENTER);

        box.getChildren().addAll(header, rulesInfo, buttonBox);
        return box;
    }

    private VBox createGlobalSection() {
        VBox box = new VBox(15);
        box.setPadding(new Insets(15));
        box.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-width: 2px; -fx-border-radius: 10px; -fx-background-radius: 10px;");

        Label header = new Label("🌍 Quick Controls");
        header.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        energyStatusLabel = new Label("⚡ Total Energy: 0.00 W");
        energyStatusLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 10; -fx-background-color: #FFF3E0; -fx-border-radius: 5px; -fx-background-radius: 5px;");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        Button allLightsOn = new Button("💡 All Lights ON");
        allLightsOn.setPrefHeight(45);
        allLightsOn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px;");
        allLightsOn.setMaxWidth(Double.MAX_VALUE);
        allLightsOn.setOnAction(e -> {
            controller.turnOnAllLights();
            log("💡 All lights turned ON");
            updateEnergyDisplay();
        });

        Button allOff = new Button("🔌 Turn Off Everything");
        allOff.setPrefHeight(45);
        allOff.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px;");
        allOff.setMaxWidth(Double.MAX_VALUE);
        allOff.setOnAction(e -> {
            controller.turnOffAllDevices();
            updateTVStatus();
            updateSensorStatus();
            log("🔌 All devices turned OFF");
            updateEnergyDisplay();
        });

        Button showStatus = new Button("📊 Show Full Status");
        showStatus.setPrefHeight(45);
        showStatus.setStyle("-fx-background-color: #607D8B; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px;");
        showStatus.setMaxWidth(Double.MAX_VALUE);
        showStatus.setOnAction(e -> {
            controller.showAllDevicesStatus();
            log("📊 Full status shown in console");
        });

        grid.add(allLightsOn, 0, 0);
        grid.add(allOff, 0, 1);
        grid.add(showStatus, 0, 2);

        box.getChildren().addAll(header, energyStatusLabel, grid);
        return box;
    }

    private VBox createLogPanel() {
        VBox box = new VBox(5);
        box.setPadding(new Insets(15));
        box.setStyle("-fx-background-color: #263238;");

        Label title = new Label("📋 Activity Log (What's Happening)");
        title.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");

        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setPrefHeight(100);
        logArea.setStyle(
                "-fx-control-inner-background: #37474F; " +
                        "-fx-text-fill: #00FF00; " +
                        "-fx-font-family: 'Courier New'; " +
                        "-fx-font-size: 12px;"
        );

        box.getChildren().addAll(title, logArea);
        return box;
    }

    private void updateTVStatus() {
        String status = tv.isOn() ? "ON" : "OFF";
        String info = String.format("Status: %s | Channel: %d | Volume: %d",
                status, tv.getCurrentChannel(), tv.getVolume());
        tvStatusLabel.setText(info);
    }

    private void updateSensorStatus() {
        String status = motionSensor.isOn() ? "ACTIVE" : "INACTIVE";
        String motion = motionSensor.isMotionDetected() ? "🏃 MOTION DETECTED!" : "No motion";
        sensorStatusLabel.setText(String.format("Status: %s | %s", status, motion));
    }

    private void updateEnergyDisplay() {
        double energy = controller.getTotalEnergyConsumption();
        energyStatusLabel.setText(String.format("⚡ Total Energy: %.2f W", energy));
    }

    private void log(String message) {
        javafx.application.Platform.runLater(() -> {
            String timestamp = java.time.LocalTime.now().format(
                    java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")
            );
            logArea.appendText("[" + timestamp + "] " + message + "\n");
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}