package com.upt.HospitalManagementFrontend.ui;



import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;

import com.upt.HospitalManagement.dto.Invoice;
import com.upt.HospitalManagement.dto.MedicineDetail;
import com.upt.HospitalManagement.dto.PatientLimitedDTO;
import com.upt.HospitalManagementFrontend.MainApp;
import com.upt.HospitalManagementFrontend.RestService.RestService;

public class Earnings {

    private RestService restService = new RestService(); 

    public Scene getEarningsScene(Stage primaryStage) {
        // Create a container for the total earnings and patient invoices
        VBox reportBox = new VBox(20);
        reportBox.setAlignment(Pos.BOTTOM_CENTER);
        
    

        // Create label for displaying total earnings
        Label earningsLabel = new Label();
        earningsLabel.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 2; -fx-padding: 10;-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: green;");

        // Fetch all patients from the API
        List<PatientLimitedDTO> patients = restService.getAllPatients();

        double totalEarnings = 0.0; // Variable to accumulate total earnings

        // VBox to hold patient invoice details
        VBox patientDetailsBox = new VBox(20);
        patientDetailsBox.setAlignment(Pos.CENTER_LEFT);

        for (PatientLimitedDTO patient : patients) {
            // Fetch the invoice for each patient
            Invoice invoice = restService.getInvoice(patient.getId());

            if (invoice != null) {
                // Create labels for each field in the invoice
                Label patientNameLabel = new Label("Patient Name: " + invoice.getPatientName().toUpperCase());
                patientNameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

                Label bedDaysLabel = new Label("Bed Days: " + invoice.getBedDays() + "      " + "Cost: €" + invoice.getBedDayCost());
               
                bedDaysLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
               

                Label surgeryDaysLabel = new Label("Surgery Days: " + invoice.getSurgeryDays() + "      " + "Cost: €" + invoice.getSurgeryDayCost());
         
                surgeryDaysLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
               

                // Medicine details section
                VBox medicineBox = new VBox(10);
                medicineBox.setAlignment(Pos.CENTER_LEFT);

                Label medicinesLabel = new Label("Prescribed Medicines:");
                medicinesLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

                for (MedicineDetail medicine : invoice.getPrescribedMedicines()) {
                    Label medicineLabel = new Label("Type: " + medicine.getType() +
                            " | Quantity: " + medicine.getQuantity() +
                            " | Price: €" + medicine.getPricePerUnit() +
                            " | Cost: €" + medicine.getCost());
                    medicineLabel.setStyle("-fx-font-size: 16px;");
                    medicineBox.getChildren().add(medicineLabel);
                }

                // Total cost for this patient
                Label totalCostLabel = new Label("Total Cost: €" + invoice.getTotalCost() + "\n =======================================================");
                totalCostLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: green;");

                // Accumulate the total earnings
                totalEarnings += invoice.getTotalCost();

                // Add the patient details to the patientDetailsBox
                patientDetailsBox.getChildren().addAll(patientNameLabel, bedDaysLabel, 
                        surgeryDaysLabel, medicinesLabel, medicineBox, totalCostLabel);
            }
        }

        // Set the total earnings label at the bottom
        earningsLabel.setText("Total Earnings: €" + totalEarnings);

        // Create a ScrollPane to ensure all content fits if it overflows
        ScrollPane scrollPane = new ScrollPane(patientDetailsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        // Add total earnings and scrollable invoices to the report box
        reportBox.getChildren().addAll(scrollPane,earningsLabel);

        // Create a Back button
        Button backButton = ButtonFactory.createStyledButton("Back to Main Menu", "Back.png");
        backButton.setOnAction(em -> primaryStage.setScene(MainApp.getMainMenuScene(primaryStage)));

        // Final layout containing the back button and report details
        VBox finalLayout = new VBox(20, reportBox, backButton);
        finalLayout.setAlignment(Pos.CENTER);
        
        // Set the background image
        Image backgroundImage = new Image(getClass().getResourceAsStream("/Background.png"));
        BackgroundImage bgImage = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, true)
        );
        finalLayout.setBackground(new Background(bgImage));
        
       

        return new Scene(finalLayout, 800, 600);
    }
}