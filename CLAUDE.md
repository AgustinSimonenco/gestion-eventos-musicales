# Instrucciones para Claude

Este archivo lo lee Claude cada vez que trabaja en el proyecto. Define las reglas del proyecto y el **procedimiento nocturno** que Claude ejecuta todos los días a las 00:00.

## Contexto

- **Proyecto:** Trabajo Práctico Integrador de Algoritmos I (2026, 2.º cuatrimestre): Sistema de Gestión de Eventos Musicales.
- **Grupo 3.** Integrantes (usuarios de GitHub): AgustinSimonenco, michuarcondo, simonenconacho, stx-06.
- **Fechas clave:**
  - **27/10:** Seguimiento II (documentación de diseño).
  - **06/11:** entrega final.
  - **10/11:** presentación y defensa oral.
- **Objetivo interno:** todo terminado el **01/11**. Del 02/11 al 05/11 queda margen para atrasos.
- **Defensa:** a cada integrante le toca un caso de uso al azar. Tiene que demostrar la trazabilidad desde la GUI hasta el código, pasando por todos los diagramas, y explicar cualquier línea. Por eso **el código tiene que ser simple, explicable y estar comentado**.
- El enunciado permite desarrollar el software completo con IA.

## Reglas del enunciado (obligatorias)

- Java compatible con **versión 8**. Compilar siempre con `javac --release 8`.
- **Interfaz gráfica obligatoria:** Swing.
- **Persistencia obligatoria:** archivos, solo con la biblioteca estándar.
- **Sin frameworks externos.** La única excepción es JUnit, solo para tests.
- **Jerarquía de excepciones propia**, obligatoria. Toda excepción de negocio hereda de una excepción base del sistema.
- Diseño orientado a objetos con estos atributos de calidad:
  - **extensibilidad**: interfaces, por ejemplo para los medios de pago y la persistencia
  - **mantenibilidad**: sin código duplicado, subsistemas desacoplados
  - **sin God classes**
- La rúbrica premia: más casos de uso, menos clases, más patrones, más interfaces.
- Las vistas UML tienen que ser **consistentes entre sí y con el código**. Cada vista lleva diagrama + descripción: elementos, patrones y justificación de las decisiones.
- El código compila sin errores y la ejecución no lanza excepciones inesperadas.

## Reglas de código

- **Solo lo que se vio en la materia:**
  - Se puede usar: clases, herencia, clases abstractas, interfaces, polimorfismo, excepciones, colecciones (`ArrayList`, `HashMap`, etc.), patrones de diseño, GRASP.
  - **Prohibido:**
    - lambdas, streams, referencias a métodos (`::`), `Optional`, `var`
    - records, `switch` como expresión, text blocks
    - clases anónimas complejas. Para los listeners de Swing, usar clases internas con nombre o que la pantalla implemente `ActionListener`.
- **Arquitectura MVC (Pantalla – Controlador – Modelo):**
  - Las pantallas no tienen lógica de negocio.
  - Los controladores coordinan cada caso de uso.
  - El modelo tiene las reglas.
- **Paquetes:** `modelo`, `vista`, `controlador`, `persistencia`, `excepciones`. Si hace falta, se agregan subpaquetes.
- **Nombres en español**, en camelCase. Clases en PascalCase.
- **Javadoc** en toda clase y todo método público.
- **Comentarios de línea** en castellano simple donde la lógica no sea obvia. Explican el *por qué*, no repiten el código.
- Métodos cortos. Si un método pasa de ~30 líneas, se divide.
- **Estructura del proyecto Java:**
  - `src/` para el código
  - `test/` para los tests
  - `lib/` para el jar de JUnit
  - `bin/` para la salida compilada, ignorada por git
- **Configuración del editor:** `.vscode/settings.json`, porque el equipo usa VS Code y Antigravity con el Extension Pack for Java.
- **No descargar nada de internet.** Si falta una biblioteca (por ejemplo JUnit en `lib/`), se anota como pedido al equipo en la bitácora.

## Ramas y flujo de trabajo

- **`main`:** solo lo aprobado por el equipo. Claude **nunca** commitea directo en `main`. Solo mergea `desarrollo` cuando la bitácora dice `Aprobado: SÍ`.
- **`desarrollo`:** la rama donde Claude trabaja todas las noches. El equipo edita ahí la bitácora para responder.
- **Horario de exclusión:** el equipo no toca el repo entre las 23:30 y las 06:00.
- Nunca usar `git push --force` ni reescribir historia. Nunca borrar bitácoras anteriores.
- Los commits terminan con:
  `Co-Authored-By: Claude Opus 5.5 <noreply@anthropic.com>`

## Procedimiento nocturno (00:00)

Ejecutar en orden. La fecha de hoy es la del momento de ejecución, en formato `AAAA-MM-DD`.

### 1. Sincronizar

```bash
git fetch origin
git checkout desarrollo
git pull origin desarrollo
```

Si hay conflictos que no se pueden resolver de forma segura, se detiene todo. Se escribe la bitácora de hoy explicando el problema, se commitea y se hace push.

### 2. Leer la bitácora anterior

Es el archivo más reciente en `bitacora/`, sin contar `PLANTILLA.md`. Hay que leer:

- **Aprobación:** el campo `**Aprobado:**`.
- **Decisiones pendientes (D-xxx)** con su `**Respuesta del equipo:**`.
- **Pedidos del equipo.**

Además, revisar si quedaron días anteriores sin respuesta. La bitácora de hoy los lista en "Estado de aprobaciones".

### 3. Actuar según la aprobación

- **`SÍ`:** primero aplicar las respuestas a las decisiones y los pedidos. Después mergear a `main`:

  ```bash
  git checkout main
  git pull origin main
  git merge --no-ff desarrollo -m "Aprobado: avance del AAAA-MM-DD"
  git push origin main
  git checkout desarrollo
  ```

- **`NO`:** aplicar **primero** las correcciones pedidas en "Comentarios" y en "Pedidos del equipo". **Después** igualmente seguir con la etapa del plan que corresponde a hoy, para no atrasar. No se mergea a `main`.
- **Vacío** (nadie respondió): no se mergea. Se sigue con la etapa de hoy y se anota en la bitácora que el día anterior quedó pendiente de aprobación.
  - Si se acumulan **3 días seguidos sin respuesta**, la etapa de hoy se limita a mejoras menores y a revisar la consistencia. Se avisa con un mensaje claro al principio de la bitácora.

### 4. Aplicar las decisiones

- **Decisión respondida:** se aplica lo que dijo el equipo.
- **Decisión sin responder:** se aplica la opción marcada como "Si no hay respuesta" y se registra como "aplicada por defecto". El equipo la puede revertir después.

### 5. Hacer la etapa de hoy

Buscar en `PLAN.md` la etapa con la fecha de hoy. Si hay etapas anteriores sin terminar, primero se termina la más vieja.

Cada etapa se considera **terminada** cuando cumple todo esto:

1. El código compila con `javac --release 8` sin errores.
2. Los tests existentes pasan, si JUnit está disponible.
3. Los diagramas afectados están actualizados para que sigan siendo consistentes con el código:
   - p6: modelo
   - p7: vista y controlador
   - p9: navegación
4. Cada caso de uso implementado tiene su diagrama de secuencia en `docs/diseno/secuencias/CUxx-nombre.md`. Los 5 elegidos para el p8 se enlazan desde `docs/diseno/p8-diagramas-de-secuencia.md`.
5. Hay Javadoc y comentarios según las reglas de código.
6. En `PLAN.md` está marcada la casilla de la etapa.

Si algo no se puede terminar, se commitea lo que compila, se explica en la bitácora qué falta, y esa parte pasa a la etapa siguiente. **Nunca** se commitea código que no compila.

### 6. Escribir la bitácora de hoy

Crear `bitacora/AAAA-MM-DD.md` copiando `bitacora/PLANTILLA.md` y completando todas las secciones. Reglas:

- Explicar **qué** se hizo y **por qué**, en castellano simple, pensando en estudiantes que lo tienen que defender.
- Listar todas las decisiones arbitrarias que tomó Claude.
- **Decisiones pendientes:**
  - Numeración global (D-001, D-002, …), continuando la última usada.
  - Cada una con opciones, recomendación y qué se hará si no hay respuesta.
  - Solo decisiones que realmente importan. Lo que se puede decidir solo, Claude lo decide y lo anota en "Decisiones tomadas".
- La sección "Para la defensa" es obligatoria: explica el recorrido de lo implementado, desde la pantalla hasta el modelo, y da preguntas posibles con su respuesta.
- Dejar vacíos los campos que completa el equipo.

### 7. Commitear y publicar

- Hacer commits chicos y descriptivos en `desarrollo` (por ejemplo, uno para el código y otro para los diagramas y la bitácora).
- Después: `git push origin desarrollo`.

### Límites

- No tocar nada fuera de este repositorio.
- No cambiar la configuración del repo en GitHub: colaboradores, visibilidad, ramas protegidas.
- No descargar archivos ni instalar nada.
- Ante una situación no prevista, se elige la opción más conservadora y se explica en la bitácora.
