#!/bin/bash
echo "Task Scheduler Application"
echo "========================"
echo

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "Error: Java is not installed or not in PATH."
    echo "Please install Java 11 or higher and try again."
    echo
    exit 1
fi

# Check if build.sh exists
if [ ! -f "build.sh" ]; then
    echo "Error: build.sh not found."
    echo "Please make sure you're in the correct directory."
    echo
    exit 1
fi

# Make scripts executable
chmod +x build.sh run.sh

# Build the application
echo "Building application..."
./build.sh

# Run the application
echo
echo "Starting application..."
./run.sh 