@echo off
rem Abre el programa con los datos de ejemplo, borrando lo que se haya cargado antes.
if exist datos.dat del datos.dat
java -cp bin Main
