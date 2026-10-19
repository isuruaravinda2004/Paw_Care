@echo off
setlocal
cd /d "%~dp0"
where java >nul 2>nul
if errorlevel 1 (
 echo Install JDK 17 or newer, reopen the terminal, and try again.
 pause
 exit /b 1
)
if not exist config.properties copy config.example.properties config.properties >nul
set "PAWCARE_JAR=dist\PawCare.jar"
if exist target\PawCare.jar set "PAWCARE_JAR=target\PawCare.jar"
if not exist "%PAWCARE_JAR%" (
 echo Runnable JAR not found. Open the project in NetBeans and choose Clean and Build.
 pause
 exit /b 1
)
java -jar "%PAWCARE_JAR%"
if errorlevel 1 pause
endlocal
