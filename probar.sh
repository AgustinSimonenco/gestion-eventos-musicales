#!/bin/sh
# Compila el programa y los tests, y ejecuta los tests con JUnit.
./compilar.sh || exit 1
rm -rf bin-test
mkdir bin-test
find test -name "*.java" > fuentes-test.txt
# El separador del classpath es ; en Windows (Git Bash) y : en Linux y Mac
SEP=":"
case "$(uname -s)" in MINGW*|MSYS*|CYGWIN*) SEP=";" ;; esac
JUNIT=lib/junit-platform-console-standalone-1.11.4.jar
javac --release 8 -encoding UTF-8 -Xlint:-options -cp "bin$SEP$JUNIT" -d bin-test @fuentes-test.txt
RESULTADO=$?
rm fuentes-test.txt
if [ $RESULTADO -ne 0 ]; then
    echo "Hubo errores de compilacion en los tests."
    exit $RESULTADO
fi
java -Dfile.encoding=UTF-8 -jar $JUNIT execute --class-path "bin$SEP""bin-test" --scan-class-path --details=summary
