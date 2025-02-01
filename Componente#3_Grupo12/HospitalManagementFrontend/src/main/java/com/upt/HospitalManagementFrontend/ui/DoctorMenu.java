package com.upt.HospitalManagementFrontend.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.upt.HospitalManagement.dto.DoctorLimitedDTO;
import com.upt.HospitalManagementFrontend.MainApp;
import com.upt.HospitalManagementFrontend.RestService.RestService;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DoctorMenu {

    private Stage primaryStage;
    private RestService restService;  // Instance of the RestService

    public DoctorMenu(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.restService = new RestService(); // Initialize the RestService object
    }


    // Method to return the Doctor Menu Scene
    public Scene getDoctorMenuScene() {
        BorderPane root = new BorderPane();
        
     // Set the background image
        Image backgroundImage = new Image(getClass().getResourceAsStream("/HP8.png"));
        BackgroundImage bgImage = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, true)
        );
        root.setBackground(new Background(bgImage));

        // Top: Title for Doctor Menu
        Button backButton = ButtonFactory.createStyledButton("Back to Main Menu", "Back.png");
        backButton.setStyle("-fx-font-size: 16px;");
        backButton.setOnAction(e -> primaryStage.setScene(MainApp.getMainMenuScene(primaryStage)));

        Label titleLabel = new Label("Doctor Menu");
        titleLabel.setStyle("-fx-background-color: white; -fx-border-color: white; -fx-border-width: 2; -fx-padding: 10;-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: black;");
        root.setTop(titleLabel);
        BorderPane.setAlignment(titleLabel, Pos.CENTER);

        root.setBottom(backButton);
        BorderPane.setAlignment(backButton, Pos.CENTER);

        // Center: Content
        GridPane centerPane = new GridPane();
        centerPane.setAlignment(Pos.CENTER);
        centerPane.setVgap(20);
        centerPane.setHgap(20);
        centerPane.setPadding(new Insets(20));

        // Buttons for listing doctors and appointments
        Button listDoctorsButton = ButtonFactory.createStyledButton("List Doctors", "list.png");
        listDoctorsButton.setPrefSize(150, 50);
        listDoctorsButton.setOnAction(e -> showDoctorTable());  // Call the method to show doctor table

        Button listAppointmentsButton =  ButtonFactory.createStyledButton("Appointments", "Appointment.png");
        listAppointmentsButton.setPrefSize(150, 50);
        listAppointmentsButton.setOnAction(e -> {
            // Logic for listing appointments (can open a new scene or display information)
            System.out.println("Appointments button clicked");

            // Assuming you're switching to the AppointmentScene
            AppointmentScene appointmentScene = new AppointmentScene(primaryStage);
            Scene appointmentScene2 = appointmentScene.createAppointmentScene();
            primaryStage.setScene(appointmentScene2);
        });
        
        
        // Add buttons to the center pane
        centerPane.add(listDoctorsButton, 0, 1);
        centerPane.add(listAppointmentsButton, 1, 1);

        root.setCenter(centerPane);

        return new Scene(root, 800, 600);
    }

 // Method to show the TableView of doctors
    private void showDoctorTable() {
        // Get the list of doctors from RestService
        List<DoctorLimitedDTO> doctors = restService.getAllDoctors();

        // Create the TableView
        TableView<DoctorLimitedDTO> doctorTable = new TableView<>();
        doctorTable.setPrefWidth(600);

        // Configure TableView columns and behavior
        TableColumn<DoctorLimitedDTO, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<DoctorLimitedDTO, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<DoctorLimitedDTO, String> specializationColumn = new TableColumn<>("Specialization");
        specializationColumn.setCellValueFactory(new PropertyValueFactory<>("specialization"));

        // Add columns to the TableView
        doctorTable.getColumns().addAll(idColumn, nameColumn, specializationColumn);

        // Set the data in the TableView
        doctorTable.getItems().addAll(doctors);

        // Create buttons for Add, Delete, and Back
        Button addButton = ButtonFactory.createStyledButton("Add Doctor", "Add.png");
        Button deleteButton =  ButtonFactory.createStyledButton("Delete Doctor", "Delete.png");
        Button backButton = ButtonFactory.createStyledButton("Back", "Back.png");
        
      

        // Set button actions
        backButton.setOnAction(e -> primaryStage.setScene(getDoctorMenuScene()));  // Back to Doctor Menu

        addButton.setOnAction(e -> showAddDoctorDialog(doctorTable));

        deleteButton.setOnAction(e -> {
            DoctorLimitedDTO selectedDoctor = doctorTable.getSelectionModel().getSelectedItem();
            if (selectedDoctor != null) {
                restService.deleteDoctor(selectedDoctor.getId());
                doctorTable.getItems().remove(selectedDoctor);  // Remove from TableView after deleting
            }
        });
        
     // Create a hBox to hold the table and buttons
        HBox hbox = new HBox(20);
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(addButton, deleteButton, backButton);
        
        
        

        // Create a VBox to hold the table and buttons
        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(doctorTable, hbox);
        
     // Set the background image
        Image backgroundImage = new Image(getClass().getResourceAsStream("/Background.png"));
        BackgroundImage bgImage = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, true)
        );
        vbox.setBackground(new Background(bgImage));

        // Create a scene for the doctor table and buttons
        Scene doctorTableScene = new Scene(vbox, 800, 600);

        // Set the new scene for the primary stage
        primaryStage.setScene(doctorTableScene);
    }
 /// Method to create the scene for adding a doctor
    private void showAddDoctorDialog(TableView<DoctorLimitedDTO> doctorTable) {
        // Create a new stage for the dialog
        Stage addDoctorStage = new Stage();
        addDoctorStage.setTitle("Add New Doctor");

        // Create the form to input the doctor's name and specialization
        GridPane form = new GridPane();
        form.setVgap(10);
        form.setHgap(10);

        // Doctor name field
        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter doctor's name");

        // Specialization field
        Label specializationLabel = new Label("Specialization:");
        TextField specializationField = new TextField();
        specializationField.setPromptText("Enter specialization");
        
        
        
        Button submitButton = new Button("Add Doctor");
        submitButton.setOnAction(e -> {
            String name = nameField.getText();
            String specialization = specializationField.getText();

            if (name.isEmpty() || specialization.isEmpty()) {
                showError("Both fields must be filled in!");
            } else {
                // Prepare the doctor data
                DoctorLimitedDTO doctor = new DoctorLimitedDTO(null, name, specialization);  // ID is null as backend will handle it

                // Add doctor using the RestService (blocking call)
                DoctorLimitedDTO responseDoctor = restService.addDoctor(doctor);

                // Check if the response contains the updated doctor with ID
                if (responseDoctor != null && responseDoctor.getId() != null) {
                    Platform.runLater(() -> {
                        doctorTable.getItems().add(responseDoctor);  // Add to the TableView with updated ID
                        addDoctorStage.close();  // Close the dialog
                    });
                } else {
                    showError("Failed to add doctor: Doctor ID not returned.");
                }
            }
        });
        
        
        // Add fields to the form
        form.add(nameLabel, 0, 0);
        form.add(nameField, 1, 0);
        form.add(specializationLabel, 0, 1);
        form.add(specializationField, 1, 1);
        form.add(submitButton, 1, 2);

        // Create a scene for the form
        Scene scene = new Scene(form, 400, 250);
        addDoctorStage.setScene(scene);
        addDoctorStage.show();
    }

    // Method to show error message (e.g., if the fields are empty or an error occurs)
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}