Tests JUnit 5 de las reglas del modelo y de la persistencia.

Se ejecutan con `probar.bat` (Windows) o `./probar.sh` (Linux, Mac, Git Bash). El jar de JUnit está en `lib/`.

Para probar que algo falla se usa `try` / `fail` / `catch` y no `assertThrows`, porque `assertThrows` necesita una lambda y las reglas del proyecto no las permiten.
