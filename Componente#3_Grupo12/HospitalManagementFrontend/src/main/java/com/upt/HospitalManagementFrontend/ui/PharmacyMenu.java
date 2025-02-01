package com.upt.HospitalManagementFrontend.ui;

import com.upt.HospitalManagement.dto.Invoice;
import com.upt.HospitalManagement.dto.MedicineDetail;
import com.upt.HospitalManagement.dto.PatientLimitedDTO;
import com.upt.HospitalManagement.model.Medicine;
import com.upt.HospitalManagementFrontend.MainApp;
import com.upt.HospitalManagementFrontend.RestService.RestService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PharmacyMenu {

    private RestService restService = new RestService();
    private Stage primaryStage;

    public PharmacyMenu(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    public Scene getPharmacyMenuScene() {
        // Main layout
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        
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

        // Buttons with icons
        Button listButton = ButtonFactory.createStyledButton("LIST", "list.png");
        Button invoiceButton = ButtonFactory.createStyledButton("INVOICE", "invoice.png");
        Button backButton = ButtonFactory.createStyledButton("Back to Main Menu", "Back.png");

        // Set up the Back Button to go back to the main menu
        backButton.setOnAction(e -> goBackToMainMenu());

        // Set up the List Button to show medicines in a TableView
        listButton.setOnAction(e -> showMedicineList());

        // Set up the Invoice Button to fetch and show invoice details
        invoiceButton.setOnAction(e -> showInvoiceInput());



        // Center the buttons vertically and horizontally on the screen
        VBox buttonBox = new VBox(20, listButton, invoiceButton, backButton);
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);
        buttonBox.setPadding(new Insets(50));  // Padding to push buttons towards the center of the screen

        // Set the button layout to the center of the screen
        root.setCenter(buttonBox);

        return new Scene(root, 800, 600);
    }


    private void showMedicineList() {
        // Set up TableView for displaying medicines
        TableView<Medicine> medicineTable = new TableView<>();
        medicineTable.setEditable(true);

        // Table Columns
        TableColumn<Medicine, String> typeColumn = new TableColumn<>("Medicine Type");
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));

        TableColumn<Medicine, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject());

        TableColumn<Medicine, Double> priceColumn = new TableColumn<>("Price per Unit");
        priceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());

        medicineTable.getColumns().addAll(typeColumn, quantityColumn, priceColumn);

        // Fetch all medicines and populate the table
        List<Medicine> medicines = restService.getAllMedicines();
        if (medicines != null) {
            medicineTable.getItems().setAll(medicines);
        }

        // Add buttons for Add and Restock
        Button addButton =  ButtonFactory.createStyledButton("Add Medicine", "Add.png");
        Button restockButton =  ButtonFactory.createStyledButton("Restock Medicine", "Update.png");
        Button backButton =  ButtonFactory.createStyledButton("Back to Main Menu", "Back.png");

        // Style buttons
        addButton.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 10px 20px;");
        restockButton.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 10px 20px;");
        backButton.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 10px 20px;");

        // Action for Add Medicine
        addButton.setOnAction(e -> showAddMedicineForm());

        // Action for Restock Medicine
        restockButton.setOnAction(e -> showRestockMedicineForm());

        // Action for Back to Main Menu
        backButton.setOnAction(e -> primaryStage.setScene(getPharmacyMenuScene()));
        
     // Add Delete Medicine Button
        Button deleteButton = ButtonFactory.createStyledButton("Delete Medicine", "Delete.png");

        // Style the button
        deleteButton.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 10px 20px;");

        // Action for Delete Medicine
        deleteButton.setOnAction(e -> {
            Medicine selectedMedicine = medicineTable.getSelectionModel().getSelectedItem();
            if (selectedMedicine != null) {
                // Show confirmation dialog
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Delete Medicine");
                confirmationAlert.setHeaderText("Are you sure you want to delete this medicine?");
                confirmationAlert.setContentText("This action cannot be undone.");

                // Wait for user's response
                Optional<ButtonType> result = confirmationAlert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    // If the user confirms, delete the medicine
                    String deleteResult = restService.deleteMedicine(selectedMedicine.getId());
                    showAlert(Alert.AlertType.INFORMATION, "Delete Medicine", deleteResult);
                    showMedicineList(); // Refresh the list after deletion
                } else {
                    // If the user cancels, do nothing
                    showAlert(Alert.AlertType.INFORMATION, "Cancel", "Deletion cancelled.");
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Select Medicine", "Please select a medicine to delete.");
            }
        });
        
       
        
        
     // Layout for the Table and Buttons
        HBox hbox = new HBox(15, addButton, restockButton ,deleteButton,backButton);
        hbox.setPadding(new Insets(20));

        // Layout for the Table and Buttons
        VBox vbox = new VBox(15, medicineTable, hbox);
        vbox.setPadding(new Insets(20));
        
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

        // Switch to the table view scene
        primaryStage.setScene(new Scene(vbox, 800, 600));
    }

    private void showAddMedicineForm() {
        // Form fields
        TextField typeField = new TextField();
        typeField.setPromptText("Enter Medicine Type");

        TextField quantityField = new TextField();
        quantityField.setPromptText("Enter Quantity");

        TextField priceField = new TextField();
        priceField.setPromptText("Enter Price per Unit");

        // Buttons
        Button submitButton = new Button("Submit");
        Button cancelButton = new Button("Cancel");

        submitButton.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        cancelButton.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Submit action
        submitButton.setOnAction(e -> {
            try {
                // Gather user input
                String type = typeField.getText().trim();
                int quantity = Integer.parseInt(quantityField.getText().trim());
                double price = Double.parseDouble(priceField.getText().trim());

                // Validate input
                if (type.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Validation Error", "Medicine type cannot be empty.");
                    return;
                }

                // Create a new Medicine object
                Medicine newMedicine = new Medicine(type, quantity, price);

                // Call the API to add the medicine
                String responseMessage = restService.addMedicine(newMedicine);

                // Display the API response message
                if (responseMessage.toLowerCase().contains("success")) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", responseMessage);
                    showMedicineList(); // Navigate back to the medicine list
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", responseMessage);
                }
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter valid numeric values for quantity and price.");
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred: " + ex.getMessage());
            }
        });

        // Cancel action
        cancelButton.setOnAction(e -> showMedicineList());

        // Layout
        VBox formBox = new VBox(15, typeField, quantityField, priceField, submitButton, cancelButton);
        formBox.setPadding(new Insets(20));
        formBox.setAlignment(javafx.geometry.Pos.CENTER);

        // Set scene
        primaryStage.setScene(new Scene(formBox, 400, 300));
    }

    private void showRestockMedicineForm() {
        // Form fields
        Label idLabel = new Label("Select Medicine ID:");
        ComboBox<Long> medicineComboBox = new ComboBox<>();
        medicineComboBox.setPromptText("Select Medicine");

        // Fetch the list of medicines from the API
        List<Medicine> medicines = restService.getAllMedicines(); // Fetch all medicines

        // Populate ComboBox with the medicine IDs
        medicines.forEach(medicine -> medicineComboBox.getItems().add(medicine.getId()));

        // Quantity field for restocking
        Label quantityLabel = new Label("Enter Quantity to Restock:");
        TextField quantityField = new TextField();
        quantityField.setPromptText("Enter Quantity to Restock");

        // Buttons
        Button submitButton = new Button("Submit");
        Button cancelButton = new Button("Cancel");

        submitButton.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        cancelButton.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Submit action
        submitButton.setOnAction(e -> {
            try {
                // Gather inputs
                Long selectedMedicineId = medicineComboBox.getValue();
                if (selectedMedicineId == null) {
                    showAlert(Alert.AlertType.ERROR, "Validation Error", "Please select a medicine.");
                    return;
                }
                int quantity = Integer.parseInt(quantityField.getText());

                // Call API
                String response = restService.restockMedicine(selectedMedicineId, quantity);

                // Display result
                if (response.contains("success")) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Medicine restocked successfully!");
                    showMedicineList();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to restock medicine! Server response: " + response);
                }
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter a valid quantity.");
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred: " + ex.getMessage());
                ex.printStackTrace(); // Debugging
            }
        });

        // Cancel action
        cancelButton.setOnAction(e -> showMedicineList());

        // Layout
        VBox formBox = new VBox(15, idLabel, medicineComboBox, quantityLabel, quantityField, submitButton, cancelButton);
        formBox.setPadding(new Insets(20));
        formBox.setAlignment(javafx.geometry.Pos.CENTER);

        // Set scene
        primaryStage.setScene(new Scene(formBox, 400, 300));
    }
    
    private void showInvoiceInput() {
    	// Patient Selection (ComboBox for dynamic patient selection)
        Label patientLabel = new Label("Select Patient:");
        ComboBox<Long> patientComboBox = new ComboBox<>();
        patientComboBox.setPromptText("Select Patient");

        // Fetch list of patients from backend and extract IDs
        List<PatientLimitedDTO> patients = restService.getAllPatients();
        patients.forEach(patient -> patientComboBox.getItems().add(patient.getId())); // Populate with patient IDs

        // Invoice Button
        Button fetchInvoiceButton = ButtonFactory.createStyledButton("Fetch Invoice", "invoice.png");
        Button backButton = ButtonFactory.createStyledButton("Back to Main Menu", "Back.png");
        
        // Action for Back to Main Menu
        backButton.setOnAction(e -> primaryStage.setScene(getPharmacyMenuScene()));

     
        // Create a container for the invoice details
        VBox detailsBox = new VBox(15);
        detailsBox.setPadding(new Insets(20));
        detailsBox.setAlignment(javafx.geometry.Pos.CENTER);
        
        
        
        

        // When the button is pressed, fetch the invoice and display it
        fetchInvoiceButton.setOnAction(e -> {
            try {
            	 Long patientId = patientComboBox.getValue();
                Invoice invoice = restService.getInvoice(patientId);

                if (invoice != null) {
                    // Create labels for each field and set their styles
                    Label patientNameLabel = new Label("Patient Name: " + invoice.getPatientName().toUpperCase());
                    patientNameLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #333333;");

                    Label bedDaysLabel = new Label("Bed Days: " + invoice.getBedDays() +"    "+ "Cost: €" + invoice.getBedDayCost()) ;
                   
                    bedDaysLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
                   

                    Label surgeryDaysLabel = new Label("Surgery Days: " + invoice.getSurgeryDays() + "         " + "Cost: €" + invoice.getSurgeryDayCost());
                 
                    surgeryDaysLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
                

                    // Creating the Medicine details section
                    VBox medicineBox = new VBox(10);
                    medicineBox.setAlignment(javafx.geometry.Pos.CENTER);

                    Label medicinesLabel = new Label("Prescribed Medicines:");
                    medicinesLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

                    for (MedicineDetail medicine : invoice.getPrescribedMedicines()) {
                        Label medicineLabel = new Label("Type: " + medicine.getType() +
                                " | Quantity: " + medicine.getQuantity() +
                                " | Price: €" + medicine.getPricePerUnit() +
                                " | Cost: €" + medicine.getCost());
                        medicineLabel.setStyle("-fx-font-size: 16px;");
                        medicineBox.getChildren().add(medicineLabel);
                    }

                    // Total cost at the bottom
                    Label totalCostLabel = new Label("Total Cost: €" + invoice.getTotalCost());
                    totalCostLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: green;");

                    // Add all details to the main detailsBox
                    detailsBox.getChildren().clear();  // Clear previous content
                    detailsBox.getChildren().addAll(patientNameLabel, bedDaysLabel,  surgeryDaysLabel,
                            medicinesLabel, medicineBox, totalCostLabel);

                    // Create a ScrollPane to ensure the content fits if it overflows
                    ScrollPane scrollPane = new ScrollPane(detailsBox);
                    scrollPane.setFitToWidth(true);
                    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

                    // Create the final layout container and set the scene
                    VBox mainBox = new VBox(20, scrollPane);
                    mainBox.setPadding(new Insets(20));
                    mainBox.setAlignment(javafx.geometry.Pos.CENTER);
                    mainBox.getChildren().add(backButton); // Add the back button
                    
                 // Set the background image
                    Image backgroundImage = new Image(getClass().getResourceAsStream("/Background.png"));
                    BackgroundImage bgImage = new BackgroundImage(
                            backgroundImage,
                            BackgroundRepeat.NO_REPEAT,
                            BackgroundRepeat.NO_REPEAT,
                            BackgroundPosition.CENTER,
                            new BackgroundSize(100, 100, true, true, true, true)
                    );
                    mainBox.setBackground(new Background(bgImage));

                    // Action for Back to Main Menu
                    backButton.setOnAction(em -> primaryStage.setScene(getPharmacyMenuScene()));
                    
                    // Set the scene to display the invoice details
                    primaryStage.setScene(new Scene(mainBox, 800, 600));
                } else {
                    detailsBox.getChildren().add(new Label("Invoice not found!"));
                }
            } catch (NumberFormatException ex) {
                detailsBox.getChildren().add(new Label("Invalid Patient ID."));
            }
        });

        // Layout for patient ID input and fetch invoice button
        VBox vbox = new VBox(20, patientComboBox, fetchInvoiceButton, backButton);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(javafx.geometry.Pos.CENTER);
        
     // Set the background image
        Image backgroundImage = new Image(getClass().getResourceAsStream("/HP8.png"));
        BackgroundImage bgImage = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, true)
        );
        vbox.setBackground(new Background(bgImage));

        // Switch to the invoice input scene
        primaryStage.setScene(new Scene(vbox, 800, 600));
    }
 
    
    private void goBackToMainMenu() {
        MainApp mainApp = new MainApp();  // Create an instance of MainApp
        primaryStage.setScene(mainApp.getMainMenuScene(primaryStage));  // Set the Main Menu scene
    }
    
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
   

    
    
}