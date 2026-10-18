# Casos de uso

> Forman parte de la Documentación de Análisis. Se cubren 15 casos de uso (mínimo exigido: 3 por integrante = 12).

## Diagrama de casos de uso

```mermaid
flowchart LR
    U(("👤 Usuario"))
    O(("👤 Organizador"))
    C(("👤 Cliente"))

    O -. es un .-> U
    C -. es un .-> U

    subgraph Sistema de Gestión de Eventos Musicales
        CU01([CU01 Registrar usuario])
        CU02([CU02 Iniciar sesión])
        CU03([CU03 Crear evento])
        CU04([CU04 Modificar evento])
        CU05([CU05 Cancelar evento])
        CU06([CU06 Asignar artistas a un evento])
        CU07([CU07 Consultar eventos disponibles])
        CU08([CU08 Consultar disponibilidad de entradas])
        CU09([CU09 Agregar entradas al carrito])
        CU10([CU10 Confirmar compra])
        CU11([CU11 Realizar pago])
        CU12([CU12 Generar comprobante])
        CU13([CU13 Consultar historial de compras])
        CU14([CU14 Cancelar compra])
        CU15([CU15 Gestionar artistas])
    end

    U --- CU01
    U --- CU02
    O --- CU03
    O --- CU04
    O --- CU05
    O --- CU06
    O --- CU15
    C --- CU07
    C --- CU09
    C --- CU10
    C --- CU13
    C --- CU14

    CU08 -. extend .-> CU07
    CU09 -. include .-> CU08
    CU10 -. include .-> CU11
    CU10 -. include .-> CU12
    CU12 -. extend .-> CU13
    CU14 -. extend .-> CU13
```

**Relaciones entre casos de uso**

- Organizador y Cliente heredan de Usuario, por eso ambos pueden registrarse e iniciar sesión.
- **CU08 extiende CU07:** desde el listado de eventos el cliente *puede* (opcionalmente) consultar la disponibilidad detallada de uno.
- **CU09 incluye CU08:** antes de agregar entradas al carrito siempre se verifica que haya disponibilidad.
- **CU10 incluye CU11 y CU12:** confirmar una compra siempre implica pagar y, si el pago se aprueba, generar el comprobante.
- **CU12 extiende CU13:** desde el historial el cliente *puede* volver a ver o descargar el comprobante de una compra.
- **CU14 extiende CU13:** desde el historial el cliente *puede* cancelar una compra.

## Resumen

| ID | Caso de uso | Actor |
|---|---|---|
| CU01 | Registrar usuario | Usuario |
| CU02 | Iniciar sesión | Usuario |
| CU03 | Crear evento | Organizador |
| CU04 | Modificar evento | Organizador |
| CU05 | Cancelar evento | Organizador |
| CU06 | Asignar artistas a un evento | Organizador |
| CU07 | Consultar eventos disponibles | Cliente |
| CU08 | Consultar disponibilidad de entradas | Cliente |
| CU09 | Agregar entradas al carrito | Cliente |
| CU10 | Confirmar compra | Cliente |
| CU11 | Realizar pago | Cliente |
| CU12 | Generar comprobante | Cliente (lo dispara el sistema) |
| CU13 | Consultar historial de compras | Cliente |
| CU14 | Cancelar compra | Cliente |
| CU15 | Gestionar artistas | Organizador |

## Descripción

### CU01 — Registrar usuario
- **Actor:** Usuario
- **Descripción:** Permite registrar un nuevo usuario en el sistema.
- **Flujo principal:**
  1. El usuario ingresa nombre, email, contraseña y tipo (cliente u organizador).
  2. El sistema valida que el email no esté registrado.
  3. El sistema crea el usuario.
- **Alternativo:** 2a. El email ya existe → el sistema informa el error.

### CU02 — Iniciar sesión
- **Actor:** Usuario
- **Descripción:** Permite acceder al sistema con credenciales registradas.
- **Flujo principal:**
  1. El usuario ingresa email y contraseña.
  2. El sistema valida las credenciales.
  3. El sistema muestra el menú según el tipo de usuario.
- **Alternativo:** 2a. Credenciales incorrectas → el sistema informa el error.

### CU03 — Crear evento
- **Actor:** Organizador
- **Descripción:** Registra un nuevo evento indicando nombre, fecha, lugar, capacidad, precio de entrada y al menos un artista.
- **Precondición:** El organizador inició sesión y hay al menos un artista registrado.
- **Flujo principal:**
  1. El organizador ingresa los datos del evento.
  2. El sistema muestra los artistas registrados y el organizador elige uno o más.
  3. El sistema valida los datos.
  4. El sistema crea el evento en estado ACTIVO con los artistas elegidos.
- **Alternativos:**
  - 2a. No hay artistas registrados → se ejecuta CU15 para dar de alta uno.
  - 3a. Datos inválidos o ningún artista elegido → el sistema informa el error.

### CU04 — Modificar evento
- **Actor:** Organizador
- **Descripción:** Actualiza la información de un evento existente.
- **Precondición:** El evento existe, fue creado por ese organizador y no está cancelado.
- **Flujo principal:**
  1. El organizador selecciona un evento.
  2. Modifica los datos deseados.
  3. El sistema valida y guarda los cambios.

### CU05 — Cancelar evento
- **Actor:** Organizador
- **Descripción:** Marca un evento como cancelado para impedir nuevas ventas.
- **Precondición:** El evento existe y fue creado por ese organizador.
- **Flujo principal:**
  1. El organizador selecciona un evento.
  2. Confirma la cancelación.
  3. El sistema cambia el estado del evento a CANCELADO.

### CU06 — Asignar artistas a un evento
- **Actor:** Organizador
- **Descripción:** Asocia o desasocia uno o más artistas a un evento.
- **Precondición:** El evento existe, fue creado por ese organizador y no está cancelado.
- **Flujo principal:**
  1. El organizador selecciona un evento.
  2. El sistema muestra los artistas ya asignados y los artistas registrados.
  3. El organizador selecciona uno o más artistas para asignar.
  4. El sistema asocia los artistas al evento.
- **Alternativos:**
  - 3a. El artista ya está asignado → el sistema lo informa.
  - 3b. El artista no está registrado → se ejecuta CU15 para darlo de alta.
  - 3c. El organizador quiere quitar un artista del evento → el sistema lo desasocia, siempre que el evento conserve al menos un artista.

### CU07 — Consultar eventos disponibles
- **Actor:** Cliente
- **Descripción:** Muestra los eventos activos con entradas disponibles.
- **Flujo principal:**
  1. El cliente solicita el listado.
  2. El sistema muestra los eventos activos con nombre, fecha, lugar, artistas, precio y entradas disponibles.
- **Alternativo:** 2a. No hay eventos activos → el sistema informa que no hay eventos disponibles.

### CU08 — Consultar disponibilidad de entradas
- **Actor:** Cliente
- **Descripción:** Permite conocer la cantidad de entradas disponibles para un evento determinado.
- **Precondición:** El cliente inició sesión y el evento existe.
- **Flujo principal:**
  1. El cliente selecciona un evento.
  2. El sistema verifica el estado del evento.
  3. El sistema calcula las entradas disponibles.
  4. El sistema muestra nombre, fecha, lugar, artistas, precio por entrada y cantidad disponible.
- **Alternativos:**
  - 2a. El evento está CANCELADO → el sistema informa que el evento fue cancelado y no tiene entradas a la venta.
  - 2b. El evento ya se realizó (FINALIZADO) → el sistema informa que el evento ya no está disponible.
  - 3a. No quedan entradas → el sistema muestra el evento como AGOTADO.

### CU09 — Agregar entradas al carrito
- **Actor:** Cliente
- **Descripción:** Permite seleccionar entradas para un evento y agregarlas al carrito de compra.
- **Precondición:** El cliente inició sesión.
- **Postcondición:** El carrito del cliente contiene el ítem (evento + cantidad) con su subtotal.
- **Flujo principal:**
  1. El cliente selecciona un evento.
  2. El cliente indica la cantidad de entradas que quiere.
  3. Se consulta la disponibilidad de entradas.
  4. El sistema agrega el ítem al carrito y calcula el subtotal.
  5. El sistema muestra el carrito actualizado con el total acumulado.
- **Alternativos:**
  - 2a. La cantidad ingresada es 0 o negativa → el sistema informa el error y vuelve a pedirla.
  - 3a. La cantidad supera las entradas disponibles → el sistema informa cuántas quedan y permite ingresar otra cantidad.
  - 5a. El cliente quiere quitar un ítem o cambiar una cantidad → el sistema actualiza el carrito y recalcula el total.

### CU10 — Confirmar compra
- **Actor:** Cliente
- **Descripción:** Permite confirmar las entradas del carrito y generar una operación de compra.
- **Precondición:** El cliente inició sesión y su carrito tiene al menos un ítem.
- **Flujo principal:**
  1. El cliente solicita confirmar la compra.
  2. El sistema muestra el resumen: eventos, cantidades, subtotales y total.
  3. El cliente acepta el resumen.
  4. El sistema verifica que cada evento siga activo y con capacidad suficiente.
  5. Se ejecuta Realizar Pago.
  6. El sistema registra la compra y genera una entrada por cada unidad comprada, asociada al cliente y al evento.
  7. Se genera el comprobante.
  8. El carrito se vacía.
- **Alternativos:**
  - 1a. El carrito está vacío → el sistema informa que no hay entradas para comprar.
  - 3a. El cliente no acepta el resumen → se realizan modificaciones.
  - 4a. Un evento fue cancelado o ya no tiene capacidad suficiente → el sistema informa qué ítem tiene problemas y vuelve al carrito sin cobrar.
  - 5a. El pago es rechazado → no se registra la compra ni se generan entradas; el carrito se conserva para reintentar.

### CU11 — Realizar pago
- **Actor:** Cliente
- **Descripción:** Concreta el pago de una compra mediante distintos medios (tarjeta o transferencia).
- **Precondición:** Se está confirmando una compra y se conoce el monto total.
- **Flujo principal:**
  1. El sistema muestra el monto a pagar y los medios de pago disponibles.
  2. El cliente elige el medio de pago.
  3. El cliente ingresa los datos correspondientes:
     - **Tarjeta:** número, titular, vencimiento, código de seguridad y cantidad de cuotas.
     - **Transferencia:** CBU/alias de origen y número de comprobante.
  4. El sistema valida el formato de los datos.
  5. El sistema procesa el pago.
  6. El sistema registra el pago como APROBADO e informa el resultado.
- **Alternativos:**
  - 4a. Datos con formato inválido (por ejemplo, tarjeta vencida o con dígitos de menos) → el sistema informa el error y vuelve a pedirlos.
  - 5a. El pago falla → el sistema lo registra como RECHAZADO, informa el motivo y permite reintentar con otro medio o cancelar.
  - 2a. El cliente cancela el pago → se vuelve a CU10 sin registrar la compra.

### CU12 — Generar comprobante
- **Actor:** Cliente (lo dispara automáticamente el sistema al aprobarse el pago)
- **Descripción:** Permite emitir un comprobante asociado a una compra confirmada.
- **Precondición:** Existe una compra con pago APROBADO.
- **Flujo principal:**
  1. El sistema genera un número de comprobante único.
  2. El sistema arma el comprobante con:
     - número de comprobante y fecha/hora de la compra,
     - datos del cliente (nombre y email),
     - detalle por evento: nombre, fecha, lugar, cantidad, precio unitario y subtotal,
     - código de cada entrada,
     - total pagado y medio de pago (en tarjeta, solo los últimos 4 dígitos).
  3. El sistema muestra el comprobante al cliente.
  4. El sistema guarda el comprobante asociado a la compra.
- **Alternativos:**
  - 1a. La compra no tiene un pago aprobado → el sistema no genera el comprobante e informa el error.

### CU13 — Consultar historial de compras
- **Actor:** Cliente
- **Descripción:** Muestra todas las entradas adquiridas anteriormente.
- **Flujo principal:**
  1. El cliente solicita su historial.
  2. El sistema muestra las entradas con evento, fecha de compra, precio y estado del pago.
- **Alternativo:** 2a. El cliente no tiene compras → el sistema informa que el historial está vacío.

### CU14 — Cancelar compra
- **Actor:** Cliente
- **Descripción:** Permite cancelar una compra realizada, anulando sus entradas.
- **Precondición:** El cliente inició sesión y tiene al menos una compra confirmada.
- **Flujo principal:**
  1. El cliente selecciona una compra desde su historial.
  2. El sistema verifica que la fecha del evento no haya pasado.
  3. El sistema muestra el detalle y el monto a reintegrar, y pide confirmación.
  4. El cliente confirma la cancelación.
  5. El sistema anula las entradas de la compra y libera su capacidad en el evento.
  6. El sistema marca la compra como CANCELADA y registra el reintegro.
- **Alternativos:**
  - 2a. El evento ya se realizó → el sistema informa que la compra no se puede cancelar.
  - 2b. La compra ya estaba cancelada → el sistema lo informa.
  - 4a. El cliente no confirma → no se realiza ningún cambio.

### CU15 — Gestionar artistas
- **Actor:** Organizador
- **Descripción:** Permite dar de alta, modificar y dar de baja artistas.
- **Precondición:** El organizador inició sesión.
- **Flujo principal:**
  1. El organizador accede al listado de artistas.
  2. Elige dar de alta un artista nuevo e ingresa nombre y género musical.
  3. El sistema valida los datos.
  4. El sistema registra el artista.
- **Alternativos:**
  - 2a. Elige modificar un artista → edita sus datos y el sistema los guarda.
  - 2b. Elige dar de baja un artista → el sistema lo elimina, siempre que no esté asignado a un evento activo.
  - 3a. Datos inválidos o artista ya existente → el sistema informa el error.
