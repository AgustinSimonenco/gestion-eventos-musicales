# Diagrama de clases inicial

Versión INICIAL de las clases principales de la propuesta: Usuario, Cliente, Organizador, Evento, Artista, Entrada, Pago, PagoTarjeta y PagoTransferencia.

Para esta versión, se hace uso de los casos de uso: CU01, CU02, CU03, CU04 y CU05.
<img width="605" height="418" alt="image" src="https://github.com/user-attachments/assets/a5fc9c5d-d01d-44dc-a018-ad1983d73c96" />

Relaciones básicas:
<img width="429" height="347" alt="image" src="https://github.com/user-attachments/assets/da8cf108-d3c0-44b1-b198-8ef9fb1d16e6" />



```mermaid
classDiagram
    direction TB

    class Usuario {
        <<abstract>>
        -int id
        -String nombre
        -String email
        -String contrasenia
        +validarCredenciales(email, contrasenia) boolean
        +getNombre() String
        +getEmail() String
    }

    class Cliente {
        -List~Entrada~ entradas
        +comprarEntrada(evento, cantidad, pago) List~Entrada~
        +getHistorialCompras() List~Entrada~
    }

    class Organizador {
        -List~Evento~ eventos
        +crearEvento(nombre, fecha, lugar, capacidad, precio) Evento
        +modificarEvento(evento, ...) void
        +cancelarEvento(evento) void
        +asignarArtista(evento, artista) void
    }

    class Evento {
        -int id
        -String nombre
        -LocalDate fecha
        -String lugar
        -int capacidad
        -double precioEntrada
        -EstadoEvento estado
        -List~Artista~ artistas
        -List~Entrada~ entradasVendidas
        +agregarArtista(artista) void
        +cancelar() void
        +estaDisponible() boolean
        +getEntradasDisponibles() int
        +venderEntrada(cliente, pago) Entrada
    }

    class EstadoEvento {
        <<enumeration>>
        ACTIVO
        AGOTADO
        CANCELADO
        FINALIZADO
    }

    class Artista {
        -int id
        -String nombre
        -String generoMusical
        +getNombre() String
    }

    class Entrada {
        -int codigo
        -LocalDateTime fechaCompra
        -double precio
        -Evento evento
        -Cliente cliente
        -Pago pago
        +getEvento() Evento
        +estaConfirmada() boolean
    }

    class Pago {
        <<abstract>>
        -int id
        -double monto
        -LocalDateTime fecha
        -EstadoPago estado
        +procesar()* boolean
        +getEstado() EstadoPago
    }

    class EstadoPago {
        <<enumeration>>
        PENDIENTE
        APROBADO
        RECHAZADO
    }

    class PagoTarjeta {
        -String numeroTarjeta
        -String titular
        -int cuotas
        +procesar() boolean
    }

    class PagoTransferencia {
        -String cbu
        -String numeroComprobante
        +procesar() boolean
    }

    Usuario <|-- Cliente
    Usuario <|-- Organizador
    Pago <|-- PagoTarjeta
    Pago <|-- PagoTransferencia

    Organizador "1" --> "0..*" Evento : organiza
    Evento "0..*" o-- "1..*" Artista : participan
    Evento "1" *-- "0..*" Entrada : vende
    Cliente "1" --> "0..*" Entrada : compra
    Entrada "1..*" --> "1" Pago : se paga con
    Evento --> EstadoEvento
    Pago --> EstadoPago
```

## Decisiones de diseño

- **Usuario** es abstracta: nunca se instancia un "usuario" genérico, siempre es Cliente u Organizador. Los dos heredan el registro y el inicio de sesión.
- **Pago** es abstracta y `procesar()` es abstracto: cada medio de pago lo implementa a su manera (**polimorfismo**). Agregar un medio nuevo (por ejemplo MercadoPago) no obliga a cambiar el resto del sistema.
- **Evento – Artista** es una **agregación**: el artista existe aunque se borre el evento, y un artista puede tocar en varios eventos.
- **Evento – Entrada** es una **composición**: la entrada no tiene sentido sin su evento.
- **Un pago puede cubrir varias entradas** (compra de 3 entradas = 1 pago).
- Se usan **enums** para los estados en lugar de Strings, así se evitan valores inválidos.
- **Evento** es experto en su capacidad (GRASP *Experto*): sabe cuántas entradas quedan y si puede vender, por eso `venderEntrada()` y `estaDisponible()` están ahí.

## Para revisar más adelante

- Agregar una clase controladora (por ejemplo `SistemaEventos`) que guarde las listas de usuarios, eventos y artistas y coordine los casos de uso (GRASP *Controlador*).
- Evaluar una clase `Compra` que agrupe varias entradas con un solo pago, si el historial se complica.
- Si se agregan sectores (VIP, campo, general), crear una clase `Sector` con su propia capacidad y precio.
