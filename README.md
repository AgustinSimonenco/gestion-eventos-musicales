# Sistema de Gestión de Eventos Musicales

Trabajo Práctico Integrador — **Algoritmos I** (2026, 2.º cuatrimestre)

Aplicación de escritorio para administrar recitales: gestión de eventos, asignación de artistas, venta de entradas y registro de pagos. Centraliza la administración de eventos y la interacción con los usuarios.

> 🚧 Etapa actual: documentación de diseño terminada (E1 a E3). Desde el 20/10 empieza la implementación.

## Actores

| Actor | Qué puede hacer |
|---|---|
| **Organizador** | Crear, modificar y cancelar eventos. Gestionar artistas y asignarlos a eventos. |
| **Cliente** | Consultar eventos, agregar entradas al carrito, comprar y pagar, ver su historial y cancelar compras. |

## Documentación

### Análisis (Seguimiento I)

| Punto | Documento |
|---|---|
| p1 | [Objetivo y alcance](docs/analisis/p1-objetivo-y-alcance.md) |
| p2 | [Descripción de alto nivel del sistema](docs/analisis/p2-descripcion-alto-nivel.md) |
| p3 | [Requerimientos funcionales](docs/analisis/p3-requerimientos-funcionales.md) |
| p4 | [Requerimientos no funcionales](docs/analisis/p4-requerimientos-no-funcionales.md) |
| — | [Casos de uso](docs/analisis/casos-de-uso.md) |

### Diseño (Seguimiento II)

| Punto | Documento |
|---|---|
| p5 | [Diagrama conceptual](docs/diseno/p5-diagrama-conceptual.md) *(multiplicidades de Compra corregidas el 19/10)* |
| p6 | [Diagrama de clases del modelo](docs/diseno/p6-diagrama-de-clases-modelo.md) *(aprobado el 17/10, ajustado el 19/10)* |
| p7 | [Diagrama de vista y controlador](docs/diseno/p7-diagrama-vista-controlador.md) *(aprobado el 18/10, ajustado el 19/10)* |
| p8 | [Diagramas de secuencia de 5 casos de uso](docs/diseno/p8-diagramas-de-secuencia.md) *(versión E3, pendiente de aprobación)* |
| p9 | [Diagrama de navegación de la GUI](docs/diseno/p9-diagrama-de-navegacion.md) *(aprobado el 18/10)* |

## Tecnologías

- Java 8 o superior (POO)
- Swing para la interfaz gráfica
- Persistencia en archivos (biblioteca estándar)
- JUnit para pruebas y Javadoc para documentación
- UML con Mermaid (los diagramas se ven directamente en GitHub)

## Forma de trabajo

- [PLAN.md](PLAN.md): etapas y fechas pactadas.
- [bitacora/](bitacora/): qué se hizo cada día, por qué, y las decisiones que el equipo tiene que responder.
- [CLAUDE.md](CLAUDE.md): reglas del proyecto y procedimiento nocturno que sigue Claude.
- Rama `desarrollo`: avances diarios, pendientes de revisión. Rama `main`: lo aprobado por el equipo.
- El equipo no toca el repo entre las 23:30 y las 06:00.
