# Plan de desarrollo

Claude trabaja todas las noches a las 00:00 en la rama `desarrollo`, siguiendo el [procedimiento de CLAUDE.md](CLAUDE.md). Al terminar deja una bitácora en [`bitacora/`](bitacora/). El equipo la revisa durante el día, responde las decisiones y aprueba o pide correcciones.

## Fechas clave

| Fecha | Qué |
|---|---|
| **mar 27/10** | Seguimiento II: documentación de diseño, "punto A al punto I" *(a confirmar con el profesor)* |
| vie 30/10 | Recuperatorio de Java |
| **dom 01/11** | **Objetivo interno: todo terminado** |
| 02/11 – 05/11 | Margen para atrasos e imprevistos |
| **vie 06/11** | **Entrega final del TPI** |
| **mar 10/11** | Presentación y defensa (grupos 1 a 3) |

## Etapas

Las fechas son las de la noche en que Claude ejecuta la etapa: "17/10" significa a las 00:00 del 17/10.

### Diseño (para el Seguimiento II)

- [ ] **E1 · 17/10 — Diagrama de clases del modelo (p6)**
  - Rehacer el p6 para que sea consistente con el p5: Carrito, ItemCarrito, Compra, Entrada, Comprobante y Pago con sus subclases.
  - Agregar la interfaz `MedioDePago`, la interfaz de persistencia y la jerarquía de excepciones.
  - Indicar los patrones usados.
  - Proponer en la bitácora el reparto de casos de uso por integrante (mínimo 3 cada uno) y los 5 casos de uso para el p8.
- [ ] **E2 · 18/10 — Vista y controlador (p7) + navegación (p9)**
  - Pantallas y controladores con sus relaciones con el modelo.
  - Diagrama de navegación entre pantallas según el rol.
- [ ] **E3 · 19/10 — Diagramas de secuencia (p8)**
  - Los 5 casos de uso elegidos, con su descripción.
  - Revisar la consistencia de p5 a p9 y actualizar el README.

### Implementación

- [ ] **E4 · 20/10 — Esqueleto del proyecto**
  - Estructura `src/`, `test/`, `lib/` y `.vscode/settings.json`.
  - Scripts para compilar y ejecutar.
  - `Main`.
  - Jerarquía de excepciones.
  - Clases base del modelo.
  - Interfaz de persistencia con su implementación en archivos.
  - Ventana principal vacía.
- [ ] **E5 · 21/10 — Usuarios:** CU01 Registrarse, CU02 Iniciar sesión, menú según el rol.
- [ ] **E6 · 22/10 — Artistas y eventos:** CU15 Gestionar artistas, CU03 Crear evento.
- [ ] **E7 · 23/10 — Eventos (etapa liviana: es el día del parcial):** CU04 Modificar evento, CU05 Cancelar evento.
- [ ] **E8 · 24/10 — Asignación y consulta:** CU06 Asignar artistas, CU07 Consultar eventos, CU08 Consultar disponibilidad.
- [ ] **E9 · 25/10 — Carrito:** CU09 Agregar entradas al carrito (modificar y quitar ítems).
- [ ] **E10 · 26/10 — Compra y pago:** CU10 Confirmar compra, CU11 Realizar pago (tarjeta y transferencia).
- [ ] **E11 · 27/10 — Comprobante e historial:** CU12 Generar comprobante, CU13 Consultar historial. Revisar que los diagramas coincidan con el código para el Seguimiento II.
- [ ] **E12 · 28/10 — Cancelación:** CU14 Cancelar compra. Aplicar la devolución del profesor en el Seguimiento II.

### Cierre

- [ ] **E13 · 29/10 — Pruebas:** tests JUnit de las reglas del modelo (capacidad, pagos, cancelaciones, validaciones).
- [ ] **E14 · 30/10 — Robustez:**
  - Revisar que todas las pantallas capturen las excepciones y muestren mensajes claros.
  - Validaciones de datos.
  - Datos de ejemplo para la demo.
- [ ] **E15 · 31/10 — Documentación de uso:** Javadoc generado, manual de uso y README final.
- [ ] **E16 · 01/11 — Revisión final y guía de defensa:**
  - Revisar la consistencia de todos los diagramas con el código.
  - Escribir `docs/guia-de-defensa.md`, con la trazabilidad de cada caso de uso: pantalla → controlador → modelo → diagramas.

### Margen (02/11 – 05/11)

Para recuperar atrasos y aplicar correcciones. Si no hay atrasos:

- [ ] Material de apoyo para la presentación (contenidos recomendados por el enunciado).
- [ ] Mejoras menores pedidas por el equipo.

## Pendientes de aclarar

- [ ] ¿Qué significa "Seguimiento II: punto A al punto I"? Agustín le pregunta al profesor. Cuando se sepa, se ajustan las etapas.
- [x] JUnit disponible: `lib/junit-platform-console-standalone-1.11.4.jar` (JUnit 5, compatible con Java 8).
