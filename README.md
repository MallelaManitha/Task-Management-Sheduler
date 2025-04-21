# Task Scheduler Desktop Application

A modern desktop application for managing tasks with features like task creation, editing, filtering, and export capabilities. Built with Java and JavaFX.

## Features

- **User Authentication**
  - Secure login and registration system
  - SQLite database for user data persistence

- **Task Management**
  - Create, edit, and delete tasks
  - Task properties:
    - Title
    - Description
    - Due date
    - Priority (High, Medium, Low)
    - Status (Pending, In Progress, Completed)

- **Task Filtering**
  - Filter by priority
  - Filter by status
  - Filter by due date
  - Search by text in title or description

- **Calendar View**
  - Visual calendar for task date selection
  - Click on dates to filter tasks for that day

- **Export Capabilities**
  - Export tasks to text file
  - Modern and intuitive user interface

## Requirements

- Java 11 or higher
- SQLite JDBC driver (automatically downloaded)
- JavaFX (automatically downloaded)

## Setup

### Windows

1. Clone or download the repository
2. Run the application with a single command:
```
start.bat
```

This will automatically:
- Check if Java is installed
- Download required dependencies
- Build the application
- Run the application

Alternatively, you can:
1. Build the application:
```
build.bat
```
2. Run the application:
```
run.bat
```

### Linux/Mac

1. Clone or download the repository
2. Make the scripts executable:
```
chmod +x start.sh build.sh run.sh
```
3. Run the application with a single command:
```
./start.sh
```

Alternatively, you can:
1. Build the application:
```
./build.sh
```
2. Run the application:
```
./run.sh
```

## Usage

1. **Login/Register**
   - Create a new account using the registration form
   - Login with your credentials

2. **Managing Tasks**
   - Click "Add Task" to create a new task
   - Fill in the task details (title, description, due date, priority, status)
   - Use the filters on the left to find specific tasks
   - Click on a task to edit or delete it

3. **Calendar View**
   - Use the calendar on the right to view tasks for specific dates
   - Click on a date to filter tasks for that day

4. **Exporting Tasks**
   - Use the "Export to Text" button to save your tasks as a text file
   - Choose the save location and file name

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── taskscheduler/
│   │           ├── Main.java              # Application entry point
│   │           ├── controller/            # UI controllers
│   │           │   ├── LoginController.java
│   │           │   ├── MainController.java
│   │           │   ├── RegisterController.java
│   │           │   └── TaskDialogController.java
│   │           ├── model/                 # Data models
│   │           │   ├── Task.java
│   │           │   └── User.java
│   │           └── util/                  # Utility classes
│   │               └── DatabaseUtil.java
│   └── resources/
│       └── fxml/                          # UI layout files
│           ├── login.fxml
│           ├── main.fxml
│           ├── register.fxml
│           └── task_dialog.fxml
bin/                                       # Compiled classes
lib/                                       # External libraries
```

## Troubleshooting

If you encounter any issues:

1. **Java not found**
   - Make sure you have Java 11 or higher installed
   - Verify Java is in your system PATH

2. **Application won't start**
   - Check that all required libraries were downloaded correctly
   - Look for error messages in the console

3. **Database errors**
   - The application creates a SQLite database file in the project directory
   - Make sure the application has write permissions in the directory

4. **Date format errors**
   - The application uses a specific date format (yyyy-MM-dd HH:mm:ss)
   - If you see date parsing errors, try restarting the application

## Future Enhancements

- PDF export functionality
- Task categories or tags
- Task reminders/notifications
- Data visualization for task statistics
- User preferences

## License

This project is licensed under the MIT License - see the LICENSE file for details. 