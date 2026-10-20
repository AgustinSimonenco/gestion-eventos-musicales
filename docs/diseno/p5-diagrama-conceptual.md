# p5. Diagrama conceptual


## Diagrama

```mermaid
classDiagram
    direction TB

    class Usuario {
        nombre
        email
        contraseña
    }
    class Organizador
    class Cliente

    class Evento {
        nombre
        fecha
        lugar
        capacidad
        precioEntrada
        estado
    }
    class Artista {
        nombre
        generoMusical
    }

    class Carrito
    class ItemCarrito {
        cantidad
    }

    class Compra {
        fecha
        total
        estado
    }
    class Entrada {
        codigo
        precio
        estado
    }

    class Pago {
        monto
        fecha
        estado
    }
    class PagoTarjeta {
        titular
        ultimos4Digitos
        cuotas
    }
    class PagoTransferencia {
        cbuOrigen
        numeroOperacion
    }

    class Comprobante {
        numero
        fechaEmision
    }

    Usuario <|-- Organizador
    Usuario <|-- Cliente
    Pago <|-- PagoTarjeta
    Pago <|-- PagoTransferencia

    Organizador "1" -- "0..*" Evento : organiza
    Organizador "1" -- "0..*" Artista : registra
    Evento "0..*" -- "1..*" Artista : participa en

    Cliente "1" -- "1" Carrito : tiene
    Carrito "1" -- "0..*" ItemCarrito : contiene
    ItemCarrito "0..*" -- "1" Evento : refiere a

    Cliente "1" -- "0..*" Compra : realiza
    Compra "1" -- "1..*" Entrada : incluye
    Entrada "0..*" -- "1" Evento : es para
    Compra "1" -- "1..*" Pago : se paga con
    Compra "1" -- "0..1" Comprobante : genera
```

## Descripción de los conceptos

| Concepto | Qué representa |
|---|---|
| **Usuario** | Persona registrada en el sistema. Puede ser Organizador o Cliente. |
| **Evento** | Recital con fecha, lugar, capacidad máxima y precio por entrada. Su estado puede ser activo, agotado, cancelado o finalizado. |
| **Carrito** | Selección temporal de entradas que el cliente todavía no compró. |
| **ItemCarrito** | Una línea del carrito: un evento y la cantidad de entradas elegidas. |
| **Compra** | Operación confirmada por un cliente. Agrupa las entradas compradas en un mismo momento. Puede estar confirmada o cancelada. |
| **Entrada** | Derecho de acceso a un evento, con un código único. Puede estar válida o anulada. |
| **Pago** | Registro del pago de una compra. Puede ser con tarjeta o por transferencia, y estar aprobado o rechazado. |
