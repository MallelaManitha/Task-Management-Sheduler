#!/bin/bash
echo "Starting Task Scheduler Application..."

# Run the application
java --module-path "lib/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml -cp "bin:lib/sqlite-jdbc-3.36.0.3.jar:lib/itext7/*" com.taskscheduler.Main

echo "Application closed." 