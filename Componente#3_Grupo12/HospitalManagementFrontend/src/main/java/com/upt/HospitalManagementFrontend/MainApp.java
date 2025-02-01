package com.upt.HospitalManagementFrontend;

import com.upt.HospitalManagementFrontend.ui.DoctorMenu;
import com.upt.HospitalManagementFrontend.ui.Earnings;
import com.upt.HospitalManagementFrontend.ui.PatientMenu;
import com.upt.HospitalManagementFrontend.ui.PharmacyMenu;
import com.upt.HospitalManagementFrontend.ui.ButtonFactory;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainApp extends Application {

	// start used here for the main menu 
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setScene(getMainMenuScene(primaryStage));
        primaryStage.setTitle("UPT Hospital Management System");
        primaryStage.show();
    }

    public static Scene getMainMenuScene(Stage primaryStage) {
        BorderPane root = new BorderPane();

        // Set resizable background image 
        Image backgroundImage = new Image(MainApp.class.getResourceAsStream("/HP10.png"));
        if (backgroundImage != null) {
            BackgroundSize backgroundSize = new BackgroundSize(
                    100, 100, true, true, false, true); // Proportional scaling for all screen sizes
            BackgroundImage bgImage = new BackgroundImage(
                    backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER, backgroundSize);
            root.setBackground(new Background(bgImage));
        } else {
            System.out.println("Background image not found.");
        }

        // Top: Title
        Image hospitalImage = new Image(MainApp.class.getResourceAsStream("/hospital.png"));
        ImageView hospitalImageView = new ImageView(hospitalImage);
        hospitalImageView.setFitHeight(50);
        hospitalImageView.setPreserveRatio(true);

        Label title = new Label("Welcome to UPT Hospital", hospitalImageView);
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        title.setId("titleLabel");  // Apply the ID for styling
        title.setGraphicTextGap(10);
        root.setTop(title);
        BorderPane.setAlignment(title, Pos.CENTER);

        // Center: Main content
        GridPane centerPane = new GridPane();
        centerPane.setAlignment(Pos.CENTER);
        centerPane.setVgap(20);
        centerPane.setHgap(20);
        centerPane.setPadding(new Insets(20));

        // Add Welcome Text
        Text welcomeText = new Text("Welcome to UPT Hospital Management System");
        welcomeText.setStyle("-fx-font-size: 18px; -fx-fill: black;");
        centerPane.add(welcomeText, 0, 0, 2, 1);

        // Add menu buttons using the ButtonFactory
        Button patientMenuButton = ButtonFactory.createStyledButton("Manage Patients", "doctors.png");
        Button doctorMenuButton = ButtonFactory.createStyledButton("Doctor Menu", "patient.png");
        Button reportMenuButton = ButtonFactory.createStyledButton("Reports", "report.png");
        Button pharmacyMenuButton = ButtonFactory.createStyledButton("Pharmacy", "pharmacy.png");

        // Set actions for buttons
        patientMenuButton.setOnAction(e -> {
            PatientMenu patientMenu = new PatientMenu(primaryStage);
            primaryStage.setScene(patientMenu.getPatientMenuScene());
        });

        doctorMenuButton.setOnAction(e -> {
            DoctorMenu doctorMenu = new DoctorMenu(primaryStage);
            primaryStage.setScene(doctorMenu.getDoctorMenuScene());
        });

        reportMenuButton.setOnAction(e -> {
            Earnings earnings = new Earnings();
            primaryStage.setScene(earnings.getEarningsScene(primaryStage));
        });

        pharmacyMenuButton.setOnAction(e -> {
            PharmacyMenu pharmacyMenu = new PharmacyMenu(primaryStage);
            primaryStage.setScene(pharmacyMenu.getPharmacyMenuScene());
        });

        // Add buttons to center pane
        centerPane.add(patientMenuButton, 0, 1);
        centerPane.add(doctorMenuButton, 1, 1);
        centerPane.add(reportMenuButton, 0, 2);
        centerPane.add(pharmacyMenuButton, 1, 2);

        root.setCenter(centerPane);

        // Bottom: Placeholder for progress bar
        Label progressBarPlaceholder = new Label("Progress Bar Space (Future Feature)");
        progressBarPlaceholder.setStyle("-fx-font-style: italic; -fx-text-fill: lightgray;");
        root.setBottom(progressBarPlaceholder);
        BorderPane.setAlignment(progressBarPlaceholder, Pos.CENTER);

        Scene scene = new Scene(root, 800, 600);

        return scene;
    }
}