@echo off
setlocal
cd /d "%~dp0"
call mvn clean package
if errorlevel 1 (
 echo Build failed. Check the error above. Use NetBeans Clean and Build if Maven is not on PATH.
 pause
 exit /b 1
)
if not exist dist mkdir dist
copy /y target\PawCare.jar dist\PawCare.jar >nul
echo Build complete. Start with run.bat.
endlocal
