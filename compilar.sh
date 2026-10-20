#!/bin/sh
# Compila todo el codigo de src/ en bin/ con Java 8.
rm -rf bin
mkdir bin
find src -name "*.java" > fuentes.txt
javac --release 8 -encoding UTF-8 -Xlint:-options -d bin @fuentes.txt
RESULTADO=$?
rm fuentes.txt
if [ $RESULTADO -ne 0 ]; then
    echo "Hubo errores de compilacion."
    exit $RESULTADO
fi
echo "Compilado en bin/"
