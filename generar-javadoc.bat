@echo off
rem Genera la documentacion Javadoc del codigo en la carpeta javadoc\.
if exist javadoc rmdir /s /q javadoc
dir /s /b src\*.java > fuentes.txt
javadoc -encoding UTF-8 -charset UTF-8 -docencoding UTF-8 -quiet -Xdoclint:none -d javadoc @fuentes.txt
del fuentes.txt
