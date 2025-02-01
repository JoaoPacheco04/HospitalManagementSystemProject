package com.upt.HospitalManagementFrontend.ui;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;



import com.toedter.calendar.JCalendar;
import com.upt.HospitalManagement.model.Appointment;
import com.upt.HospitalManagement.model.Doctor;
import com.upt.HospitalManagementFrontend.RestService.RestService;

import java.util.List;

public class AppointmentScene {

    private RestService restService = new RestService();
    private Stage primaryStage;// Using RestService class to make API calls
    
    public AppointmentScene(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Scene createAppointmentScene() {
    	
    	
       
        // Create buttons for different actions
        Button checkAvailabilityButton =  ButtonFactory.createStyledButton("Check Availability", "CheckAvailability.png");
        Button bookAppointmentButton = ButtonFactory.createStyledButton("Book Appointment", "BookApp.png");
        Button getAllDoctorsButton =ButtonFactory.createStyledButton("View All Appointments", "patient.png");
        Button backButton = ButtonFactory.createStyledButton("Back", "Back.png");
        
        backButton.setOnAction(e -> {
            DoctorMenu doctorMenu = new DoctorMenu(primaryStage);
            primaryStage.setScene(doctorMenu.getDoctorMenuScene()); // Go back to the Doctor Menu
        });

        //Setting actions for each button

        checkAvailabilityButton.setOnAction(e -> {
            // Open the DoctorAvailabilityDialog to allow the user to check doctor availability
            DoctorAvailabilityDialog.showDialog(primaryStage, restService);
        });
        // Action for Book Appointment button
        bookAppointmentButton.setOnAction(e -> {
        	BookAppointmentDialog.showDialog(primaryStage, restService);
        });

        // Action for Get All Doctors button
        getAllDoctorsButton.setOnAction(e -> {
            try {
                // Call the backend API to fetch all doctors with appointments
                List<Doctor> doctors = restService.getAllDoctorsWithAppointments();
                
                // Display the fetched data in a TableView
                displayDoctorAppointments(doctors);
            } catch (Exception ex) {
                showErrorAlert("Error", "Failed to fetch doctor appointments. Please try again.");
                ex.printStackTrace();
            }          
        });
        
        // Create the layout for the scene
        VBox layout = new VBox(20, checkAvailabilityButton, bookAppointmentButton, getAllDoctorsButton,backButton);
        layout.setAlignment(javafx.geometry.Pos.CENTER);
        layout.setPadding(new Insets(50)); // Padding to push buttons towards the center
        
        // Set the background image
        Image backgroundImage = new Image(getClass().getResourceAsStream("/HP8.png"));
        BackgroundImage bgImage = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, true)
        );
        layout.setBackground(new Background(bgImage));

        // Return the scene with the layout
        return new Scene(layout, 800, 600);
    }
    
    
    // Method to Display the Appointment count of each doctor in a TableView
    private void displayDoctorAppointments(List<Doctor> doctors) {
        Stage stage = new Stage();
        stage.setTitle("Doctor Appointments");

        // TableView for Doctors
        TableView<Doctor> doctorTable = new TableView<>();
        TableColumn<Doctor, String> nameCol = new TableColumn<>("Doctor Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Doctor, String> specializationCol = new TableColumn<>("Specialization");
        specializationCol.setCellValueFactory(new PropertyValueFactory<>("specialization"));

        TableColumn<Doctor, Integer> appointmentsCountCol = new TableColumn<>("Appointments Count");
        appointmentsCountCol.setCellValueFactory(cellData -> 
            new SimpleIntegerProperty(cellData.getValue().getAppointments().size()).asObject()
        );

        doctorTable.getColumns().addAll(nameCol, specializationCol, appointmentsCountCol);

        // Add doctor data to the table
        doctorTable.getItems().addAll(doctors);

        // Nested TableView or Button for Appointments
        doctorTable.setRowFactory(tv -> {
            TableRow<Doctor> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Doctor selectedDoctor = row.getItem();
                    showAppointmentsForDoctor(selectedDoctor);
                }
            });
            return row;
        });

        VBox layout = new VBox(10, doctorTable);
        layout.setPadding(new Insets(10));
        Scene scene = new Scene(layout, 800, 600);
        stage.setScene(scene);
        stage.show();
        
        
        // Set the background image
        Image backgroundImage = new Image(getClass().getResourceAsStream("/Background.png"));
        BackgroundImage bgImage = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, true)
        );
        layout.setBackground(new Background(bgImage));
    }
    
    // Method to Show all appointments of a given Doctor
    private void showAppointmentsForDoctor(Doctor doctor) {
        Stage stage = new Stage();
        stage.setTitle("Appointments for " + doctor.getName());

        TableView<Appointment> appointmentTable = new TableView<>();
        TableColumn<Appointment, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Appointment, String> patientNameCol = new TableColumn<>("Patient Name");
        patientNameCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getPatient().getName())
        );

        TableColumn<Appointment, String> shiftCol = new TableColumn<>("Shift");
        shiftCol.setCellValueFactory(new PropertyValueFactory<>("shift"));

        TableColumn<Appointment, String> medicalHistoryCol = new TableColumn<>("Medical History");
        medicalHistoryCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getPatient().getMedicalHistory())
        );

        appointmentTable.getColumns().addAll(dateCol, patientNameCol, shiftCol, medicalHistoryCol);

        // Add appointments to the table
        appointmentTable.getItems().addAll(doctor.getAppointments());

        VBox layout = new VBox(10, appointmentTable);
        layout.setPadding(new Insets(10));
        Scene scene = new Scene(layout, 600, 400);
        stage.setScene(scene);
        stage.show();
    }
   
    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}