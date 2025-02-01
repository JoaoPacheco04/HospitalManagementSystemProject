package com.upt.HospitalManagementFrontend.ui;

/*
 * 
 * This the class that designs all the buttons in the application
 */
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ButtonFactory {

    public static Button createStyledButton(String text, String iconPath) {
        Button button = new Button(text);
        button.setGraphic(createIcon(iconPath));

        // Set a fixed width and height for the button to avoid resizing issues
        button.setPrefWidth(150); 
        button.setPrefHeight(40); // Set a preferred height for the button

        // Style without padding, and keeping a consistent size
        button.setStyle("-fx-background-color: white; " +
                        "-fx-border-color: black; " +
                        "-fx-border-width: 2; " +
                        "-fx-background-radius: 15; " +
                        "-fx-font-size: 14px; " +
                        "-fx-cursor: hand; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 2, 0, 0, 1);");

        // Hover effect without altering the size or padding
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: lightblue; " +
                                                      "-fx-border-color: darkgray; " +
                                                      "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 3, 0, 0, 1);"));

        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: white; " +
                                                     "-fx-border-color: black; " +
                                                     "-fx-border-width: 2; " +
                                                     "-fx-background-radius: 15; " +
                                                     "-fx-font-size: 14px; " +
                                                     "-fx-cursor: hand; " +
                                                     "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 2, 0, 0, 1);"));

        return button;
    }

    private static ImageView createIcon(String iconPath) {
        Image icon = new Image(ButtonFactory.class.getResourceAsStream("/" + iconPath));
        ImageView imageView = new ImageView(icon);
        imageView.setFitWidth(20); // Set icon width
        imageView.setFitHeight(20); // Set icon height
        return imageView;
    }
}