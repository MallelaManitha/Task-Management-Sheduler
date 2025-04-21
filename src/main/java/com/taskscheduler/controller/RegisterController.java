package com.taskscheduler.controller;

import com.taskscheduler.model.User;
import com.taskscheduler.util.DatabaseUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class RegisterController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private TextField emailField;
    @FXML
    private Label messageLabel;

    @FXML
    private void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String email = emailField.getText();

        // Validation
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()) {
            messageLabel.setText("Please fill in all fields");
            return;
        }

        if (!password.equals(confirmPassword)) {
            messageLabel.setText("Passwords do not match");
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            messageLabel.setText("Invalid email format");
            return;
        }

        try {
            User user = new User(username, password, email);
            DatabaseUtil.createUser(user);
            
            // Return to login screen
            handleBackToLogin();
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                messageLabel.setText("Username or email already exists");
            } else {
                messageLabel.setText("Error: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleBackToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Task Scheduler - Login");
        } catch (Exception e) {
            messageLabel.setText("Error: " + e.getMessage());
        }
    }
} 