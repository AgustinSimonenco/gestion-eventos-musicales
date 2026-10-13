# p1. Objetivo y alcance

## Objetivo

Se propone un software como herramienta que permite administrar recitales. Se enfoca en la gestión de eventos, la asignación de artistas, la venta de entradas y el registro de pagos.

El desarrollo de un Sistema de Gestión de Eventos Musicales permite centralizar la administración de eventos y la interacción con los usuarios. Con este, se puede gestionar eventos musicales, administrar artistas y organizadores, controlar la venta de entradas y registrar pagos.

Hay dos actores principales:

- **Organizador:** crea y administra eventos y artistas.
- **Cliente:** consulta eventos, compra entradas, las paga y puede cancelar sus compras.

## Alcance

Como alcance, contemplamos:

- Registro y autenticación de usuarios, con acceso según su rol (cliente u organizador).
- Administración de eventos musicales: alta, modificación y cancelación.
- Administración de artistas y su asignación a eventos.
- Consulta de eventos disponibles y de la disponibilidad de entradas.
- Compra de entradas mediante un carrito de compras.
- Registro de pagos con tarjeta o transferencia.
- Generación de comprobantes de compra.
- Consulta del historial de compras.
- Cancelación de compras por parte del cliente.
- Interfaz gráfica de escritorio.
- Persistencia de la información en archivos, para que los datos se conserven entre ejecuciones.

## Límites (fuera del alcance)

Para acotar el desarrollo, el sistema **no** contempla:

- Pagos reales: el procesamiento de tarjetas y transferencias es **simulado**; no hay conexión con bancos ni pasarelas de pago.
- Reintegros de dinero reales: al cancelar una compra solo se registra el reintegro.
- Sectores, ubicaciones o asientos numerados: todas las entradas de un evento tienen el mismo precio.
- Envío de emails o notificaciones fuera del sistema.
- Uso simultáneo desde varias computadoras (es una aplicación de escritorio de un solo puesto).
- Recuperación de contraseña.
