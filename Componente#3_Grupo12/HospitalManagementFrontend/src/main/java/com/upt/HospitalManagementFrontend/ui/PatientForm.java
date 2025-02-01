package com.upt.HospitalManagementFrontend.ui;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.upt.HospitalManagement.model.Medicine;
import com.upt.HospitalManagementFrontend.RestService.RestService;

public class PatientForm {

    private RestService restService = new RestService(); // For API calls
    private Long patientId; // ID for editing an existing patient
    private Stage primaryStage; // Reference to main stage
    private Runnable onFormClose; // Callback for refreshing data after form submission

    public PatientForm(Stage primaryStage, Long patientId) {
        this.primaryStage = primaryStage;
        this.patientId = patientId;
    }

    public Scene getFormScene(Runnable onFormClose) {
        this.onFormClose = onFormClose;

        // Main layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);

        // Input fields
        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();

        Label ageLabel = new Label("Age:");
        TextField ageField = new TextField();

        Label historyLabel = new Label("Medical History:");
        TextField historyField = new TextField();

        Label contactLabel = new Label("Contact Number:");
        TextField contactField = new TextField();

        CheckBox surgeryCheckbox = new CheckBox("Is Surgery Required?");
        TextField bedDaysField = new TextField();
        TextField surgeryDaysField = new TextField();
        bedDaysField.setPromptText("Bed Days");
        surgeryDaysField.setPromptText("Surgery Days");
        bedDaysField.setDisable(true);
        surgeryDaysField.setDisable(true);

        surgeryCheckbox.setOnAction(e -> {
            boolean isChecked = surgeryCheckbox.isSelected();
            bedDaysField.setDisable(!isChecked);
            surgeryDaysField.setDisable(!isChecked);
        });

        // Medicines Section
        Label medicineSectionLabel = new Label("Prescribed Medicines:");
        TextField numMedicinesField = new TextField();
        numMedicinesField.setPromptText("Enter number of medicines");

        VBox medicinesVBox = new VBox();
        medicinesVBox.setSpacing(10); 

        Button generateFieldsButton = new Button("Generate Medicine Fields"); //
        generateFieldsButton.setOnAction(e -> {
            try {
                int numMedicines = Integer.parseInt(numMedicinesField.getText());

                medicinesVBox.getChildren().clear(); // Clear previous fields

                // Dynamically generate input fields for each medicine
                for (int i = 0; i < numMedicines; i++) {
                    HBox medicineHBox = new HBox();
                    medicineHBox.setSpacing(10);

                    // ComboBox for selecting medicine type
                    ComboBox<String> medicineComboBox = new ComboBox<>();
                    medicineComboBox.setPromptText("Select Medicine Type");

                    // Fetch the list of medicines from the API (returns a List<Medicine>)
                    List<Medicine> response = restService.getAllMedicines();

                    // Parse the response to extract the medicine types
                    List<String> medicineTypes = parseMedicineResponse(response);

                    // Update the ComboBox with the available medicine types
                    medicineComboBox.getItems().setAll(medicineTypes);

                    // Quantity field for the medicine
                    TextField medicineQuantityField = new TextField();
                    medicineQuantityField.setPromptText("Quantity");

                    // Add the ComboBox and quantity field to the HBox
                    medicineHBox.getChildren().addAll(medicineComboBox, medicineQuantityField);

                    // Add the HBox (representing the current medicine input) to the VBox
                    medicinesVBox.getChildren().add(medicineHBox);
                }

            } catch (NumberFormatException ex) {
                showAlert("Error", "Please enter a valid number for the number of medicines.", Alert.AlertType.ERROR);
            }
        });

        // Submit button
        Button submitButton = new Button(patientId == null ? "Add Patient" : "Update Patient");
        submitButton.setOnAction(e -> {
            try {
                String name = nameField.getText();
                int age = Integer.parseInt(ageField.getText());
                String medicalHistory = historyField.getText();
                String contactNumber = contactField.getText();
                boolean isSurgeryRequired = surgeryCheckbox.isSelected();
                int bedDays = isSurgeryRequired ? Integer.parseInt(bedDaysField.getText()) : 0;
                int surgeryDays = isSurgeryRequired ? Integer.parseInt(surgeryDaysField.getText()) : 0;

                // Create a map for the medicines selected by the user
                Map<String, Integer> medicines = new HashMap<>();
                for (Node node : medicinesVBox.getChildren()) {
                    if (node instanceof HBox) {
                        HBox medicineHBox = (HBox) node;
                        ComboBox<String> medicineComboBox = (ComboBox<String>) medicineHBox.getChildren().get(0);
                        TextField medicineQuantityField = (TextField) medicineHBox.getChildren().get(1);

                        String selectedMedicine = medicineComboBox.getValue();
                        String quantityText = medicineQuantityField.getText();
                        if (selectedMedicine != null && !quantityText.isEmpty()) {
                            int quantity = Integer.parseInt(quantityText);
                            medicines.put(selectedMedicine, quantity);
                        }
                    }
                }

                Map<String, Object> patientData = new HashMap<>();
                patientData.put("name", name);
                patientData.put("age", age);
                patientData.put("medicalHistory", medicalHistory);
                patientData.put("contactNumber", contactNumber);
                patientData.put("prescribedMedicines", medicines);
                patientData.put("isSurgeryRequired", isSurgeryRequired);
                patientData.put("bedDays", bedDays);
                patientData.put("surgeryDays", surgeryDays);

                String response = patientId == null
                        ? restService.addPatient(patientData)
                        : restService.updatePatient(patientId, patientData);

                showAlert("Success", response, Alert.AlertType.INFORMATION);
                onFormClose.run();

            } catch (NumberFormatException ex) {
                showAlert("Error", "Invalid input! Please check your entries.", Alert.AlertType.ERROR);
            } catch (Exception ex) {
                showAlert("Error", "An error occurred: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        // Layout setup
        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(ageLabel, 0, 1);
        grid.add(ageField, 1, 1);
        grid.add(historyLabel, 0, 2);
        grid.add(historyField, 1, 2);
        grid.add(contactLabel, 0, 3);
        grid.add(contactField, 1, 3);
        grid.add(surgeryCheckbox, 0, 4);
        grid.add(bedDaysField, 0, 5);
        grid.add(surgeryDaysField, 1, 5);
        grid.add(medicineSectionLabel, 0, 6);
        grid.add(numMedicinesField, 1, 6);
        grid.add(generateFieldsButton, 2, 6);
        grid.add(medicinesVBox, 1, 7);
        grid.add(submitButton, 1, 8);

        return new Scene(grid, 600, 600);
    }

    private List<String> parseMedicineResponse(List<Medicine> response) {
        // Extract and trim the medicine type, ensuring clean output
        return response.stream()
                .map(medicine -> {
                    String type = medicine.getType();
                    return type != null ? type.trim() : null;
                })
                .filter(type -> type != null && !type.isEmpty())
                .collect(Collectors.toList());
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
}