@echo off
rem Compila todo el codigo de src/ en bin/ con Java 8.
if exist bin rmdir /s /q bin
mkdir bin
dir /s /b src\*.java > fuentes.txt
javac --release 8 -encoding UTF-8 -Xlint:-options -d bin @fuentes.txt
set RESULTADO=%ERRORLEVEL%
del fuentes.txt
if %RESULTADO% neq 0 (
    echo Hubo errores de compilacion.
    exit /b %RESULTADO%
)
echo Compilado en bin/
