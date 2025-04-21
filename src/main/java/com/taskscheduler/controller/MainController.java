package com.taskscheduler.controller;

import com.taskscheduler.model.Task;
import com.taskscheduler.model.User;
import com.taskscheduler.util.DatabaseUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MainController {
    @FXML private Label userLabel;
    @FXML private TableView<Task> taskTable;
    @FXML private TableColumn<Task, String> titleColumn;
    @FXML private TableColumn<Task, String> descriptionColumn;
    @FXML private TableColumn<Task, LocalDateTime> dueDateColumn;
    @FXML private TableColumn<Task, Task.Priority> priorityColumn;
    @FXML private TableColumn<Task, Task.Status> statusColumn;
    @FXML private ComboBox<Task.Priority> priorityFilter;
    @FXML private ComboBox<Task.Status> statusFilter;
    @FXML private DatePicker dueDateFilter;
    @FXML private TextField searchField;
    @FXML private DatePicker calendarView;

    private User currentUser;
    private ObservableList<Task> tasks;
    private FilteredList<Task> filteredTasks;

    @FXML
    public void initialize() {
        // Initialize table columns
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Initialize filters
        priorityFilter.setItems(FXCollections.observableArrayList(Task.Priority.values()));
        statusFilter.setItems(FXCollections.observableArrayList(Task.Status.values()));

        // Initialize task list
        tasks = FXCollections.observableArrayList();
        filteredTasks = new FilteredList<>(tasks, p -> true);

        // Bind table to filtered list
        taskTable.setItems(filteredTasks);

        // Add search functionality
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateFilters();
        });

        // Add calendar view listener
        calendarView.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                filterByDate(newValue);
            }
        });
    }

    public void setUser(User user) {
        this.currentUser = user;
        userLabel.setText("Welcome, " + user.getUsername());
        loadTasks();
    }

    private void loadTasks() {
        try {
            List<Task> userTasks = DatabaseUtil.getTasksByUserId(currentUser.getId());
            tasks.setAll(userTasks);
        } catch (SQLException e) {
            showError("Error loading tasks: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddTask() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/task_dialog.fxml"));
            Parent root = loader.load();
            TaskDialogController controller = loader.getController();
            controller.setUser(currentUser);

            Stage stage = new Stage();
            stage.setTitle("Add New Task");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadTasks();
        } catch (Exception e) {
            showError("Error adding task: " + e.getMessage());
        }
    }

    @FXML
    private void handleApplyFilters() {
        updateFilters();
    }

    @FXML
    private void handleClearFilters() {
        priorityFilter.setValue(null);
        statusFilter.setValue(null);
        dueDateFilter.setValue(null);
        searchField.clear();
        updateFilters();
    }

    private void updateFilters() {
        filteredTasks.setPredicate(task -> {
            if (task == null) return false;

            boolean matchesPriority = priorityFilter.getValue() == null || 
                                    task.getPriority() == priorityFilter.getValue();
            boolean matchesStatus = statusFilter.getValue() == null || 
                                  task.getStatus() == statusFilter.getValue();
            boolean matchesDate = dueDateFilter.getValue() == null || 
                                task.getDueDate().toLocalDate().equals(dueDateFilter.getValue());
            boolean matchesSearch = searchField.getText().isEmpty() ||
                                  task.getTitle().toLowerCase().contains(searchField.getText().toLowerCase()) ||
                                  task.getDescription().toLowerCase().contains(searchField.getText().toLowerCase());

            return matchesPriority && matchesStatus && matchesDate && matchesSearch;
        });
    }

    private void filterByDate(LocalDate date) {
        filteredTasks.setPredicate(task -> {
            if (task == null) return false;
            return task.getDueDate().toLocalDate().equals(date);
        });
    }

    @FXML
    private void handleExportToPDF() {
        // Temporarily disable PDF export
        showAlert("Export", "PDF export is temporarily disabled.", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void handleExportText() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Tasks to Text");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );

        File file = fileChooser.showSaveDialog(taskTable.getScene().getWindow());
        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("Task List - " + currentUser.getUsername() + "\n\n");
                
                for (Task task : filteredTasks) {
                    writer.write("Title: " + task.getTitle() + "\n");
                    writer.write("Description: " + task.getDescription() + "\n");
                    writer.write("Due Date: " + task.getDueDate().format(DateTimeFormatter.ISO_LOCAL_DATE) + "\n");
                    writer.write("Priority: " + task.getPriority() + "\n");
                    writer.write("Status: " + task.getStatus() + "\n");
                    writer.write("-------------------\n");
                }
            } catch (Exception e) {
                showError("Error exporting to text: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) userLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Task Scheduler - Login");
        } catch (Exception e) {
            showError("Error logging out: " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 