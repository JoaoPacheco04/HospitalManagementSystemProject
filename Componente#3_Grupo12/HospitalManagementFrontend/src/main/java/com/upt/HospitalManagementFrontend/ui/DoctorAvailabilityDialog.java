package com.upt.HospitalManagementFrontend.ui;

import com.upt.HospitalManagement.dto.DoctorLimitedDTO;
import com.upt.HospitalManagementFrontend.RestService.RestService;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import java.util.List;

public class DoctorAvailabilityDialog {

    public static void showDialog(Stage primaryStage, RestService restService) {
        // Create the dialog box
        Stage dialog = new Stage();
        dialog.initOwner(primaryStage);
        dialog.setTitle("Check Doctor Availability");

        // Set up the layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        // Doctor Selection (ComboBox for dynamic doctor selection)
        Label doctorLabel = new Label("Select Doctor:");
        ComboBox<Long> doctorComboBox = new ComboBox<>();
        doctorComboBox.setPromptText("Select Doctor");

        // Fetch list of doctors from backend and populate ComboBox
        List<DoctorLimitedDTO> doctors = restService.getAllDoctors();
        doctors.forEach(doctor -> doctorComboBox.getItems().add(doctor.getId()));

        // Date Selection (Using DatePicker)
        Label dateLabel = new Label("Select Date:");
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Select Date");

        // Shift Selection (Radio Buttons for Morning, Afternoon, Evening)
        Label shiftLabel = new Label("Select Shift:");
        ToggleGroup shiftToggleGroup = new ToggleGroup();
        RadioButton morningShift = new RadioButton("Morning");
        morningShift.setToggleGroup(shiftToggleGroup);
        morningShift.setUserData("MORNING");
        RadioButton afternoonShift = new RadioButton("Afternoon");
        afternoonShift.setToggleGroup(shiftToggleGroup);
        afternoonShift.setUserData("AFTERNOON");
        RadioButton eveningShift = new RadioButton("Evening");
        eveningShift.setToggleGroup(shiftToggleGroup);
        eveningShift.setUserData("EVENING");

        // Availability label to show the result
        Label availabilityLabel = new Label();

        // Button to check availability
        Button checkAvailabilityButton = new Button("Check Availability");

        // Button Action to check the availability
        checkAvailabilityButton.setOnAction(e -> {
            Long doctorId = doctorComboBox.getValue();
            String date = datePicker.getValue() != null ? datePicker.getValue().toString() : "";
            String shift = (shiftToggleGroup.getSelectedToggle() != null) ? shiftToggleGroup.getSelectedToggle().getUserData().toString() : "";

            // Validate inputs
            if (doctorId == null || date.isEmpty() || shift.isEmpty()) {
                availabilityLabel.setText("Please fill in all fields.");
                return;
            }

            try {
                boolean available = restService.checkDoctorAvailability(doctorId, date, shift);

                if (available) {
                    availabilityLabel.setText("Doctor is available for " + shift + " shift on " + date);
                } else {
                    availabilityLabel.setText("Doctor is not available for " + shift + " shift on " + date);
                }
            } catch (Exception ex) {
                availabilityLabel.setText("Error checking availability.");
            }
        });

        // Add components to the grid
        grid.add(doctorLabel, 0, 0);
        grid.add(doctorComboBox, 1, 0);
        grid.add(dateLabel, 0, 1);
        grid.add(datePicker, 1, 1);
        grid.add(shiftLabel, 0, 2);
        grid.add(morningShift, 1, 2);
        grid.add(afternoonShift, 1, 3);
        grid.add(eveningShift, 1, 4);
        grid.add(checkAvailabilityButton, 1, 5);
        grid.add(availabilityLabel, 1, 6);

        // Set the scene and show the dialog
        Scene scene = new Scene(grid, 500, 400);
        dialog.setScene(scene);
        dialog.show();
    }
}