package com.taskscheduler.util;

import com.taskscheduler.model.Task;
import com.taskscheduler.model.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUtil {
    private static final String DB_URL = "jdbc:sqlite:task_scheduler.db";
    private static Connection conn = null;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(DB_URL);
            createTables();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createTables() throws SQLException {
        // Create Users table
        String createUsersTable = """
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT UNIQUE NOT NULL,
                    password TEXT NOT NULL,
                    email TEXT UNIQUE NOT NULL
                )
                """;

        // Create Tasks table
        String createTasksTable = """
                CREATE TABLE IF NOT EXISTS tasks (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    title TEXT NOT NULL,
                    description TEXT,
                    due_date TEXT NOT NULL,
                    priority TEXT NOT NULL,
                    status TEXT NOT NULL,
                    user_id INTEGER,
                    FOREIGN KEY (user_id) REFERENCES users(id)
                )
                """;

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createUsersTable);
            stmt.execute(createTasksTable);
        }
    }

    // User operations
    public static User authenticateUser(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                return user;
            }
        }
        return null;
    }

    public static void createUser(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getEmail());
            pstmt.executeUpdate();
        }
    }

    // Task operations
    public static void createTask(Task task) throws SQLException {
        String sql = "INSERT INTO tasks (title, description, due_date, priority, status, user_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setString(3, task.getDueDate().format(DATE_FORMATTER));
            pstmt.setString(4, task.getPriority().name());
            pstmt.setString(5, task.getStatus().name());
            pstmt.setInt(6, task.getUserId());
            pstmt.executeUpdate();
        }
    }

    public static List<Task> getTasksByUserId(int userId) throws SQLException {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Task task = new Task();
                task.setId(rs.getInt("id"));
                task.setTitle(rs.getString("title"));
                task.setDescription(rs.getString("description"));
                
                // Parse the date string with error handling
                String dateStr = rs.getString("due_date");
                try {
                    task.setDueDate(LocalDateTime.parse(dateStr, DATE_FORMATTER));
                } catch (DateTimeParseException e) {
                    // If the format doesn't match, try to extract just the date part
                    try {
                        // Try to parse just the date part (before the T)
                        String datePart = dateStr.substring(0, dateStr.indexOf('T'));
                        task.setDueDate(LocalDateTime.parse(datePart + " 00:00:00", DATE_FORMATTER));
                    } catch (Exception ex) {
                        // If all parsing fails, use current date
                        task.setDueDate(LocalDateTime.now());
                    }
                }
                
                task.setPriority(Task.Priority.valueOf(rs.getString("priority")));
                task.setStatus(Task.Status.valueOf(rs.getString("status")));
                task.setUserId(rs.getInt("user_id"));
                tasks.add(task);
            }
        }
        return tasks;
    }

    public static void updateTask(Task task) throws SQLException {
        String sql = "UPDATE tasks SET title = ?, description = ?, due_date = ?, priority = ?, status = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setString(3, task.getDueDate().format(DATE_FORMATTER));
            pstmt.setString(4, task.getPriority().name());
            pstmt.setString(5, task.getStatus().name());
            pstmt.setInt(6, task.getId());
            pstmt.executeUpdate();
        }
    }

    public static void deleteTask(int taskId) throws SQLException {
        String sql = "DELETE FROM tasks WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, taskId);
            pstmt.executeUpdate();
        }
    }
} 