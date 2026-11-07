#!/bin/sh
# Genera la documentacion Javadoc del codigo en la carpeta javadoc/.
rm -rf javadoc
find src -name "*.java" > fuentes.txt
javadoc -encoding UTF-8 -charset UTF-8 -docencoding UTF-8 -quiet -Xdoclint:none -d javadoc @fuentes.txt
RESULTADO=$?
rm fuentes.txt
exit $RESULTADO
