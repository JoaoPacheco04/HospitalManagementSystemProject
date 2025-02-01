package com.upt.HospitalManagementFrontend.ui;

import java.util.List;

import com.upt.HospitalManagement.dto.DoctorLimitedDTO;
import com.upt.HospitalManagement.dto.PatientLimitedDTO;
import com.upt.HospitalManagementFrontend.RestService.RestService;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class BookAppointmentDialog {

    public static void showDialog(Stage primaryStage, RestService restService) {
        // Create the dialog box
        Stage dialog = new Stage();
        dialog.initOwner(primaryStage);
        dialog.setTitle("Book Appointment");

        // Set up the layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        // Doctor Selection (ComboBox for dynamic doctor selection)
        Label doctorLabel = new Label("Select Doctor:");
        ComboBox<Long> doctorComboBox = new ComboBox<>();
        doctorComboBox.setPromptText("Select Doctor");

        // Fetch list of doctors from backend and extract IDs
        List<DoctorLimitedDTO> doctors = restService.getAllDoctors();
        doctors.forEach(doctor -> doctorComboBox.getItems().add(doctor.getId())); // Populate with doctor IDs

        // Patient Selection (ComboBox for dynamic patient selection)
        Label patientLabel = new Label("Select Patient:");
        ComboBox<Long> patientComboBox = new ComboBox<>();
        patientComboBox.setPromptText("Select Patient");

        // Fetch list of patients from backend and extract IDs
        List<PatientLimitedDTO> patients = restService.getAllPatients();
        patients.forEach(patient -> patientComboBox.getItems().add(patient.getId())); // Populate with patient IDs

    
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

        // Result label to show the appointment status
        Label appointmentStatusLabel = new Label();

        // Button to book the appointment
        Button bookAppointmentButton = new Button("Book Appointment");

        // Button Action to book the appointment
        bookAppointmentButton.setOnAction(e -> {
            Long doctorId = doctorComboBox.getValue();
            Long patientId = patientComboBox.getValue();
            String date = datePicker.getValue() != null ? datePicker.getValue().toString() : "";
            String shift = (shiftToggleGroup.getSelectedToggle() != null) ? shiftToggleGroup.getSelectedToggle().getUserData().toString() : "";

            // Validate inputs
            if (doctorId == null || patientId == null || date.isEmpty() || shift.isEmpty()) {
                appointmentStatusLabel.setText("Please fill in all fields.");
                return;
            }

            try {
                // Call the method to book the appointment
                String response = restService.bookAppointment(doctorId, patientId, date, shift);

                // Display success or failure message
                if (response.contains("Failed")) {
                    appointmentStatusLabel.setText("Failed to book appointment.");
                } else {
                    appointmentStatusLabel.setText("Appointment booked successfully!");
                }
            } catch (Exception ex) {
                appointmentStatusLabel.setText("Error occurred: " + ex.getMessage());
            }
        });

        // Add components to the grid
        grid.add(doctorLabel, 0, 0);
        grid.add(doctorComboBox, 1, 0);
        grid.add(patientLabel, 0, 1);
        grid.add(patientComboBox, 1, 1);
        grid.add(dateLabel, 0, 2);
        grid.add(datePicker, 1, 2);
        grid.add(shiftLabel, 0, 3);
        grid.add(morningShift, 1, 3);
        grid.add(afternoonShift, 1, 4);
        grid.add(eveningShift, 1, 5);
        grid.add(bookAppointmentButton, 1, 6);
        grid.add(appointmentStatusLabel, 1, 7);

        // Set the scene and show the dialog
        Scene scene = new Scene(grid, 500, 400);
        dialog.setScene(scene);
        dialog.show();
    }
}