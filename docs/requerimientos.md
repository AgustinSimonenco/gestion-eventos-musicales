# Requerimientos

## Requerimientos funcionales

### Usuarios
- **RF01** — El sistema debe permitir registrar usuarios (clientes u organizadores) con nombre, email y contraseña.
- **RF02** — El sistema debe permitir iniciar sesión con email y contraseña registrados.
- **RF03** — No puede haber dos usuarios con el mismo email.

### Eventos (Organizador)
- **RF04** — El organizador debe poder crear un evento indicando nombre, fecha, lugar, capacidad y precio de entrada.
- **RF05** — El organizador debe poder modificar los datos de un evento existente.
- **RF06** — El organizador debe poder cancelar un evento; un evento cancelado no admite nuevas ventas.
- **RF07** — El organizador debe poder asignar uno o más artistas a un evento.
- **RF08** — Un organizador solo puede modificar o cancelar los eventos que creó.

### Entradas y pagos (Cliente)
- **RF09** — El cliente debe poder consultar el listado de eventos activos con entradas disponibles.
- **RF10** — El cliente debe poder comprar una o más entradas para un evento.
- **RF11** — No se pueden vender más entradas que la capacidad del evento.
- **RF12** — El cliente debe poder pagar con tarjeta o con transferencia.
- **RF13** — Una entrada queda confirmada solo cuando su pago fue aprobado.
- **RF14** — El cliente debe poder consultar su historial de compras.

## Requerimientos no funcionales

- **RNF01** — Desarrollado en Java aplicando programación orientada a objetos (encapsulamiento, herencia, polimorfismo).
- **RNF02** — Interfaz por consola (a definir si se agrega interfaz gráfica).
- **RNF03** — Los datos se guardan en memoria (a definir si se agrega persistencia en archivos).
- **RNF04** — El sistema debe validar los datos ingresados y mostrar mensajes de error claros.

## Pendientes a definir con el equipo

- ¿Se puede modificar la fecha o la capacidad de un evento que ya vendió entradas?
- Si se cancela un evento, ¿se reintegra el dinero de las entradas vendidas?
- ¿Hay límite de entradas por cliente?
- ¿Habrá distintos tipos o sectores de entrada (general, VIP, campo) con precios distintos?
