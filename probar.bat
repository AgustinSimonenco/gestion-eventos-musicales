@echo off
rem Compila el programa y los tests, y ejecuta los tests con JUnit.
call .\compilar.bat
if %ERRORLEVEL% neq 0 exit /b %ERRORLEVEL%
if exist bin-test rmdir /s /q bin-test
mkdir bin-test
dir /s /b test\*.java > fuentes-test.txt
javac --release 8 -encoding UTF-8 -Xlint:-options -cp "bin;lib\junit-platform-console-standalone-1.11.4.jar" -d bin-test @fuentes-test.txt
set RESULTADO=%ERRORLEVEL%
del fuentes-test.txt
if %RESULTADO% neq 0 (
    echo Hubo errores de compilacion en los tests.
    exit /b %RESULTADO%
)
java -Dfile.encoding=UTF-8 -jar lib\junit-platform-console-standalone-1.11.4.jar execute --class-path "bin;bin-test" --scan-class-path --details=summary
