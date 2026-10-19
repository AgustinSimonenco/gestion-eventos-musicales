# p8. Diagramas de secuencia

Un diagrama de secuencia muestra **qué objetos participan en un caso de uso y en qué orden se llaman**, desde que el usuario toca un botón hasta que ve el resultado. Sirve para la defensa: es el "hilo" que une la pantalla (p7 y p9) con las reglas del modelo (p6).

## Los 5 casos de uso elegidos

Elegidos el 17/10 (D-005, aprobado). Entre los cinco aparecen todos los patrones y todas las capas del sistema.

| CU | Diagrama | Por qué se eligió |
|---|---|---|
| CU02 | [Iniciar sesión](secuencias/CU02-iniciar-sesion.md) | Polimorfismo de `Usuario`: `getTipo()` decide a qué pantalla se va. |
| CU03 | [Crear evento](secuencias/CU03-crear-evento.md) | Conversión de datos en el controlador, validaciones en el modelo y jerarquía de excepciones. |
| CU09 | [Agregar entradas al carrito](secuencias/CU09-agregar-entradas-al-carrito.md) | Incluye CU08: control de capacidad con `SinDisponibilidadException`. |
| CU10 | [Confirmar compra](secuencias/CU10-confirmar-compra.md) (con CU11 y CU12) | Strategy (`MedioDePago`), Template Method (`Pago.procesar`), GRASP Creador (`Compra` crea `Entrada` y `Comprobante`). |
| CU14 | [Cancelar compra](secuencias/CU14-cancelar-compra.md) | Anula entradas y libera capacidad: varios objetos del modelo cambian juntos. |

## Cómo leer los diagramas

- **Flecha llena (`->>`):** un objeto llama un método de otro. Si la flecha vuelve al mismo objeto, es un método propio (por ejemplo `convertirEntero`).
- **Flecha punteada (`-->>`):** lo que devuelve el método.
- **Flecha punteada con cruz (`--x`):** el método **lanza una excepción** en lugar de devolver algo.
- **`alt` / `else`:** caminos distintos según una condición. Cada camino lleva entre paréntesis el flujo alternativo del caso de uso que representa (por ejemplo "(3a)").
- **`loop`:** se repite por cada elemento de una lista.
- **Notas:** aclaraciones que no son llamadas.
- Para que los diagramas se puedan leer, se omiten los *getters* triviales y lo que pasa dentro de Swing.

## Lo que se repite en todos

Los cinco siguen la misma forma, la del [p7](p7-diagrama-vista-controlador.md#3-controladores):

1. La **pantalla** recibe el clic en `actionPerformed` y llama a **un** método del controlador, con los textos tal cual.
2. El **controlador** convierte los textos (métodos heredados de `Controlador`), le pide la operación al **modelo** y, si todo sale bien, llama a `Sesion.guardar()` y le dice a la `VentanaPrincipal` qué mostrar.
3. Si el modelo lanza una excepción (cualquier subclase de `SistemaException`), sube sin que nadie la capture **hasta el controlador**, que tiene un único `catch (SistemaException e)` y llama a `ventana.mostrarError(e.getMessage())`.
4. Cuando la ventana muestra una pantalla con `mostrar(nombre)`, antes le llama `actualizar()` para que recargue sus datos.

## Consistencia con los otros diagramas

- Cada clase de los diagramas está en el p6 (modelo) o en el p7 (vista y controlador).
- Cada método llamado está en el p6 o en el p7. Los que hubo que agregar al hacer estos diagramas se agregaron el 19/10 (ver la bitácora de ese día): `Compra.verificarCancelable`, los *getters* de `Evento` y `Compra`, `Carrito.buscarItem` y, en el p7, `ControladorCompras.procesarPago` y `armarResumen` y las constantes de nombres de pantalla de `VentanaPrincipal`.
- Cada cambio de pantalla coincide con una flecha del [p9](p9-diagrama-de-navegacion.md).
