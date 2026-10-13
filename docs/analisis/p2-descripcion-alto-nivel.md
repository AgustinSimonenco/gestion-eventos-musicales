# p2. Descripción de alto nivel del sistema

## ¿Qué es?

El Sistema de Gestión de Eventos Musicales es una **aplicación de escritorio en Java con interfaz gráfica** que conecta a dos tipos de usuarios:

- **Organizadores**, que publican recitales, les asignan artistas y los administran.
- **Clientes**, que buscan recitales, compran entradas a través de un carrito, las pagan y consultan sus compras.
- **Vista:** muestra la información y captura las acciones del usuario. No contiene lógica de negocio.
- **Controladores:** reciben las acciones de la vista, coordinan cada caso de uso y devuelven el resultado a la vista.
- **Modelo:** contiene las reglas del negocio (por ejemplo, que no se vendan más entradas que la capacidad del evento).
- **Persistencia:** aísla el guardado en archivos del resto del sistema, para poder cambiar el mecanismo de almacenamiento sin modificar el modelo.

Los errores (capacidad agotada, pago rechazado, datos inválidos, etc.) se representan con una **jerarquía de excepciones propia**: el modelo las lanza y los controladores las capturan para mostrar un mensaje claro en la vista.

## Flujo típico de uso

1. Un **organizador** inicia sesión, crea un recital (nombre, fecha, lugar, capacidad y precio) y le asigna artistas.
2. Un **cliente** inicia sesión, consulta los eventos disponibles y agrega entradas al carrito.
3. El cliente confirma la compra y paga con tarjeta o transferencia.
4. Si el pago se aprueba, el sistema genera las entradas y el comprobante, y descuenta la disponibilidad del evento.
5. El cliente puede ver luego su historial de compras o cancelar una compra antes de la fecha del evento.


