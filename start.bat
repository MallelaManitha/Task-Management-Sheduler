@echo off
echo Task Scheduler Application
echo ========================
echo.

:: Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: Java is not installed or not in PATH.
    echo Please install Java 11 or higher and try again.
    echo.
    pause
    exit /b 1
)

:: Check if build.bat exists
if not exist build.bat (
    echo Error: build.bat not found.
    echo Please make sure you're in the correct directory.
    echo.
    pause
    exit /b 1
)

:: Build the application
echo Building application...
call build.bat

:: Run the application
echo.
echo Starting application...
call run.bat

pause 