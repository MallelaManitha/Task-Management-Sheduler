package com.taskscheduler.controller;

import com.taskscheduler.model.Task;
import com.taskscheduler.model.User;
import com.taskscheduler.util.DatabaseUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TaskDialogController {
    @FXML private TextField titleField;
    @FXML private TextArea descriptionField;
    @FXML private DatePicker dueDatePicker;
    @FXML private ComboBox<Task.Priority> priorityComboBox;
    @FXML private ComboBox<Task.Status> statusComboBox;
    @FXML private Label messageLabel;

    private User currentUser;
    private Task taskToEdit;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @FXML
    public void initialize() {
        priorityComboBox.setItems(FXCollections.observableArrayList(Task.Priority.values()));
        statusComboBox.setItems(FXCollections.observableArrayList(Task.Status.values()));
        
        // Set default values
        priorityComboBox.setValue(Task.Priority.MEDIUM);
        statusComboBox.setValue(Task.Status.PENDING);
        dueDatePicker.setValue(LocalDate.now());
    }

    public void setUser(User user) {
        this.currentUser = user;
    }

    public void setTaskToEdit(Task task) {
        this.taskToEdit = task;
        if (task != null) {
            titleField.setText(task.getTitle());
            descriptionField.setText(task.getDescription());
            dueDatePicker.setValue(task.getDueDate().toLocalDate());
            priorityComboBox.setValue(task.getPriority());
            statusComboBox.setValue(task.getStatus());
        }
    }

    @FXML
    private void handleSave() {
        String title = titleField.getText().trim();
        String description = descriptionField.getText().trim();
        LocalDate dueDate = dueDatePicker.getValue();
        Task.Priority priority = priorityComboBox.getValue();
        Task.Status status = statusComboBox.getValue();

        // Validation
        if (title.isEmpty()) {
            messageLabel.setText("Please enter a title");
            return;
        }

        if (dueDate == null) {
            messageLabel.setText("Please select a due date");
            return;
        }

        try {
            Task task;
            if (taskToEdit != null) {
                task = taskToEdit;
                task.setTitle(title);
                task.setDescription(description);
                task.setDueDate(LocalDateTime.of(dueDate, LocalTime.of(12, 0)));
                task.setPriority(priority);
                task.setStatus(status);
                DatabaseUtil.updateTask(task);
            } else {
                task = new Task(title, description, 
                              LocalDateTime.of(dueDate, LocalTime.of(12, 0)),
                              priority, status, currentUser.getId());
                DatabaseUtil.createTask(task);
            }

            closeDialog();
        } catch (Exception e) {
            messageLabel.setText("Error saving task: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.close();
    }
} 