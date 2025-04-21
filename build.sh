#!/bin/bash
echo "Compiling Task Scheduler Application..."

# Create directories if they don't exist
mkdir -p bin lib

# Download required libraries if they don't exist
if [ ! -d "lib/javafx-sdk" ]; then
    echo "Downloading JavaFX SDK..."
    wget -O javafx-sdk.zip https://download2.gluonhq.com/openjfx/17.0.2/openjfx-17.0.2_linux-x64_bin-sdk.zip
    unzip -q javafx-sdk.zip -d lib
    mv lib/javafx-sdk-17.0.2 lib/javafx-sdk
    rm javafx-sdk.zip
fi

if [ ! -f "lib/sqlite-jdbc-3.36.0.3.jar" ]; then
    echo "Downloading SQLite JDBC..."
    wget -O lib/sqlite-jdbc-3.36.0.3.jar https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.36.0.3/sqlite-jdbc-3.36.0.3.jar
fi

if [ ! -d "lib/itext7" ]; then
    echo "Downloading iText 7..."
    wget -O lib/itext7-core-7.2.5.zip https://repo1.maven.org/maven2/com/itextpdf/itext7-core/7.2.5/itext7-core-7.2.5.zip
    unzip -q lib/itext7-core-7.2.5.zip -d lib/itext7
    rm lib/itext7-core-7.2.5.zip
fi

# Compile the application
javac -d bin -cp "lib/sqlite-jdbc-3.36.0.3.jar:lib/itext7/*:lib/javafx-sdk/lib/*" src/main/java/com/taskscheduler/*.java src/main/java/com/taskscheduler/controller/*.java src/main/java/com/taskscheduler/model/*.java src/main/java/com/taskscheduler/util/*.java

# Copy resources
cp -r src/main/resources/* bin/

echo "Compilation complete!"
echo ""
echo "To run the application, use: ./run.sh" 