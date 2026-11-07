# Manual de uso

## Cómo abrir el programa

Hace falta un JDK (8 o más nuevo).

| Qué querés hacer | Windows | Linux / Mac / Git Bash |
|---|---|---|
| Compilar (siempre lo primero) | `compilar.bat` | `./compilar.sh` |
| **Abrir para la demo, con los datos de ejemplo** | `ejecutar-demo.bat` | `./ejecutar-demo.sh` |
| Abrir conservando lo que ya se cargó | `ejecutar.bat` | `./ejecutar.sh` |
| Ejecutar los tests | `probar.bat` | `./probar.sh` |
| Generar el Javadoc en `javadoc/` | `generar-javadoc.bat` | `./generar-javadoc.sh` |

> ⚠️ `ejecutar-demo` **borra** `datos.dat` antes de abrir. Sirve para empezar la demo siempre igual. Si querés conservar lo cargado, usá `ejecutar`.

Los datos de ejemplo también se cargan solos con `ejecutar` si todavía no hay ningún dato guardado.

## Usuarios de ejemplo

La contraseña de todos es `1234`.

| Email | Rol | Qué tiene |
|---|---|---|
| `ana@demo.com` | Organizadora | Los 4 eventos de ejemplo |
| `juan@demo.com` | Cliente | 3 compras en el historial |
| `maria@demo.com` | Cliente | Sin compras, con 2 entradas en el carrito |

| Evento | Estado | Para qué sirve en la demo |
|---|---|---|
| Rock en el Luna | Activo | Compra normal |
| Noche de folklore | Activo | Está en el carrito de María |
| Tango íntimo | Agotado | Mostrar un evento sin lugar. Si Juan cancela su compra, se libera |
| Fito acústico | Cancelado | Aparece en el historial de Juan con "(CANCELADO)" |

## Datos para probar los pagos

El pago es simulado: no se conecta con ningún banco.

| Medio | Se aprueba | Se rechaza |
|---|---|---|
| Tarjeta | `4509123412341234` | Cualquier número terminado en `0000`, por ejemplo `4509123412340000` |
| Transferencia | Número de operación `5001` | Cualquier número terminado en `000`, por ejemplo `5000` |

Para la tarjeta: titular cualquiera, vencimiento mes `12` y año `2030`, código `123`. Para la transferencia: CBU `0123456789012345678901`.

## Organizador

Al entrar se ve **Mis eventos**, con la tabla de sus eventos.

- **Nuevo:** crea un evento. Se eligen los artistas con Ctrl + clic. La fecha se escribe `dd/mm/aaaa` y tiene que ser futura.
- **Modificar:** elegí una fila y cambiá los datos. La capacidad no puede ser menor a lo vendido.
- **Cancelar evento:** pide confirmación. Ya no se pueden comprar entradas; las compras hechas no se cancelan solas.
- **Artistas del evento:** a la izquierda están los del evento y a la derecha todo el catálogo. *Asignar al evento* y *Quitar del evento* trabajan de a uno. El evento siempre conserva al menos un artista.
- **Gestionar artistas:** alta, modificación y baja del catálogo. No se puede dar de baja un artista que está en un evento activo o agotado.

## Cliente

Al entrar se ve **Eventos disponibles**.

- **Ver disponibilidad:** muestra el detalle del evento elegido.
- **Agregar al carrito:** elegí una fila, escribí la cantidad y tocá el botón.
- **Carrito:** muestra los ítems y el total. Se puede cambiar la cantidad, quitar un ítem, seguir comprando o confirmar.
- **Confirmar compra:** muestra un resumen. Si lo aceptás, se pasa al pago.
- **Pago:** elegí tarjeta o transferencia. Si se rechaza, podés reintentar; *Cancelar pago* vuelve al carrito.
- **Comprobante:** aparece solo al pagar. Después se puede volver a ver desde *Mis compras*.
- **Mis compras:** el historial. *Ver comprobante* y *Cancelar compra* trabajan sobre la fila elegida. Se puede cancelar hasta el día anterior al evento.

## Guion para mostrar el software (unos 5 minutos)

Abrir con `ejecutar-demo.bat`.

1. **Validación del login.** Entrar con `juan@demo.com` y una contraseña mal: aparece el error. Entrar bien: se ve la pantalla del cliente.
2. **Evento agotado (CU07, CU08).** Elegir *Tango íntimo* → *Ver disponibilidad*: dice "0 (AGOTADO)". Escribir cantidad 1 → *Agregar al carrito*: dice que no quedan entradas.
3. **Carrito (CU09).** Elegir *Rock en el Luna*, cantidad 2 → *Agregar al carrito*. Elegir la fila, escribir 3 → *Cambiar cantidad*: el total cambia.
4. **Pago rechazado y aprobado (CU10, CU11, CU12).** *Confirmar compra* → *Sí*. Pagar con la tarjeta terminada en `0000`: se rechaza y se queda en la pantalla. Cambiar a `4509123412341234` → *Pagar*: aparece el comprobante. La tabla ahora tiene 3 entradas menos.
5. **Historial y cancelación (CU13, CU14).** *Mis compras*: se ve la compra nueva. Elegir *Tango íntimo* → *Cancelar compra* → *Sí*. *Volver*: Tango ya no está agotado.
6. **Organizador.** *Cerrar sesión* y entrar con `ana@demo.com`. Tocar *Nuevo* y crear un evento (CU03). Elegir un evento → *Artistas del evento* → asignar uno (CU06). Elegir otro → *Cancelar evento* (CU05).
7. **Persistencia.** Cerrar el programa y abrirlo con `ejecutar.bat`, no con `ejecutar-demo`: todo sigue igual.
