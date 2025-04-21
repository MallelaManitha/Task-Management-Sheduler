@echo off
echo Compiling Task Scheduler Application...

:: Create directories if they don't exist
if not exist "bin" mkdir bin
if not exist "lib" mkdir lib

:: Download required libraries if they don't exist
if not exist "lib\javafx-sdk" (
    echo Downloading JavaFX SDK...
    powershell -Command "& {Invoke-WebRequest -Uri 'https://download2.gluonhq.com/openjfx/17.0.2/openjfx-17.0.2_windows-x64_bin-sdk.zip' -OutFile 'javafx-sdk.zip'}"
    powershell -Command "& {Expand-Archive -Path 'javafx-sdk.zip' -DestinationPath 'lib'}"
    move "lib\javafx-sdk-17.0.2" "lib\javafx-sdk"
    del "javafx-sdk.zip"
)

if not exist "lib\sqlite-jdbc-3.36.0.3.jar" (
    echo Downloading SQLite JDBC...
    powershell -Command "& {Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.36.0.3/sqlite-jdbc-3.36.0.3.jar' -OutFile 'lib\sqlite-jdbc-3.36.0.3.jar'}"
)

:: Compile the application
echo Compiling Java files...
javac -d bin -cp "lib\sqlite-jdbc-3.36.0.3.jar;lib\javafx-sdk\lib\*" src\main\java\com\taskscheduler\*.java src\main\java\com\taskscheduler\controller\*.java src\main\java\com\taskscheduler\model\*.java src\main\java\com\taskscheduler\util\*.java

if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

:: Copy resources
echo Copying resources...
xcopy /s /y src\main\resources\* bin\

echo Compilation complete!
echo.
echo To run the application, use: run.bat 