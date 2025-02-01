package com.upt.HospitalManagementFrontend.ui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import com.upt.HospitalManagement.dto.PatientLimitedDTO;
import com.upt.HospitalManagement.model.Patient;
import com.upt.HospitalManagementFrontend.MainApp;
import com.upt.HospitalManagementFrontend.RestService.RestService;

import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

public class PatientMenu {

    private RestService restService = new RestService(); // Service for API calls
    private TableView<PatientLimitedDTO> tableView = new TableView<>(); // Table to display patient data
    private Stage primaryStage; // Reference to main application stage

    public PatientMenu(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Scene getPatientMenuScene() {
        // Main layout
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        // Set the background image
        Image backgroundImage = new Image(getClass().getResourceAsStream("/Background.png"));
        BackgroundImage bgImage = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, true)
        );
        root.setBackground(new Background(bgImage));

        // Title
        Label titleLabel = new Label("Patient Menu");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // TableView setup
        configureTableView();

        // Buttons
        HBox buttonBar = new HBox(10);
        Button addButton = ButtonFactory.createStyledButton("Add Patient", "Add.png");
        Button viewButton = ButtonFactory.createStyledButton("View Patient", "View.png");
        Button editButton = ButtonFactory.createStyledButton("Edit Patient", "Update.png");
        Button deleteButton = ButtonFactory.createStyledButton("Delete Patient", "Delete.png");
        Button backButton = ButtonFactory.createStyledButton("Back", "Back.png");

        addButton.setOnAction(e -> showAddOrEditPatientForm(null));
        viewButton.setOnAction(e -> handleViewPatient());
        editButton.setOnAction(e -> handleEditPatient());
        deleteButton.setOnAction(e -> handleDeletePatient());
        backButton.setOnAction(e -> primaryStage.setScene(MainApp.getMainMenuScene(primaryStage)));

        buttonBar.getChildren().addAll(addButton, viewButton, editButton, deleteButton, backButton);

        // Layout setup
        root.getChildren().addAll(titleLabel, tableView, buttonBar);

        // Load initial data into the TableView
        loadPatients();

        return new Scene(root, 800, 600);
    }

    // Configure TableView columns and behavior
    private void configureTableView() {
        TableColumn<PatientLimitedDTO, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<PatientLimitedDTO, Integer> ageColumn = new TableColumn<>("Age");
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));

        TableColumn<PatientLimitedDTO, String> contactColumn = new TableColumn<>("Contact");
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));

        tableView.getColumns().addAll(nameColumn, ageColumn, contactColumn);
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
    }

    // Load patients into the TableView
    private void loadPatients() {
        tableView.getItems().clear();
        List<PatientLimitedDTO> patients = restService.getAllPatients();
        tableView.getItems().addAll(patients);
    }

    // Handle "View Patient" action
    private void handleViewPatient() {
        PatientLimitedDTO selectedPatient = tableView.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            Patient fullDetails = restService.getPatientById(selectedPatient.getId());
            if (fullDetails != null) {
                showPatientDetailsDialog(fullDetails);
            } else {
                showAlert("Error", "Could not retrieve patient details.", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("No Selection", "Please select a patient to view.", Alert.AlertType.WARNING);
        }
    }

    // Handle "Edit Patient" action
    private void handleEditPatient() {
        PatientLimitedDTO selectedPatient = tableView.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            showAddOrEditPatientForm(selectedPatient.getId());
        } else {
            showAlert("No Selection", "Please select a patient to edit.", Alert.AlertType.WARNING);
        }
    }

    // Handle "Delete Patient" action
    private void handleDeletePatient() {
        PatientLimitedDTO selectedPatient = tableView.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            boolean confirmed = showConfirmationDialog("Delete Patient", "Are you sure you want to delete this patient?");
            if (confirmed) {
                String response = restService.deletePatient(selectedPatient.getId());
                showAlert("Delete Patient", response, Alert.AlertType.INFORMATION);
                loadPatients(); // Refresh the table
            }
        } else {
            showAlert("No Selection", "Please select a patient to delete.", Alert.AlertType.WARNING);
        }
    }

    // Show form for adding or editing a patient
    private void showAddOrEditPatientForm(Long patientId) {
        Stage formStage = new Stage();
        formStage.setTitle(patientId == null ? "Add Patient" : "Edit Patient");

        Scene formScene = new PatientForm(primaryStage, patientId).getFormScene(() -> {
            loadPatients(); // Refresh table after adding/editing a patient
            formStage.close();
        });

        formStage.setScene(formScene);
        formStage.show();
    }

    // Display detailed patient information in a dialog
    private void showPatientDetailsDialog(Patient patient) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Patient Details");
        alert.setHeaderText("Details for: " + patient.getName());

        // Build the details string directly using concatenation
        String details = "Name: " + patient.getName() + "\n\n" +
                         "Age: " + patient.getAge() + "\n\n" +
                         "Contact: " + patient.getContactNumber() + "\n\n" +
                         "Medical History: " + patient.getMedicalHistory() + "\n\n" +
                         "Surgery Required: " + (patient.isSurgeryRequired() ? "Yes" : "No") + "\n\n" +
                         "Bed Days: " + patient.getBedDays() + "\n\n" +
                         "Surgery Days: " + patient.getSurgeryDays() + "\n\n";

        // Add prescribed medicines details if they exist
        if (patient.getPrescribedMedicines() != null && !patient.getPrescribedMedicines().isEmpty()) {
            details += "Prescribed Medicines:\n";
            for (Entry<String, Integer> entry : patient.getPrescribedMedicines().entrySet()) {
                details += " - " + entry.getKey() + ": " + entry.getValue() + "\n";
            }
        } else {
            details += "No prescribed medicines.\n";
        }

        alert.setContentText(details);
        alert.showAndWait();
    }

    // Show alert dialog
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }

    // Show confirmation dialog
    private boolean showConfirmationDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
        alert.setTitle(title);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.YES;
    }
}