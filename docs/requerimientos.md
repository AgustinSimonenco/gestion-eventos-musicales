# Requerimientos

## Requerimientos funcionales

### 
**RF01** – Registrarse
El sistema debe permitir registrar nuevos usuarios.

**RF02** – Iniciar sesión
El sistema debe permitir autenticar usuarios mediante correo electrónico y contraseña.

**RF03** – Crear evento
El organizador debe poder registrar nuevos eventos musicales.

**RF04** – Modificar evento
El organizador debe poder actualizar la información de eventos existentes.

**RF05** – Cancelar evento
El organizador debe poder cancelar eventos activos.

**RF06** – Gestionar artistas
El organizador debe poder asociar y desasociar artistas a un evento.

**RF07** – Consultar eventos disponibles
Los clientes deben poder visualizar los eventos activos y disponibles.

**RF08** – Consultar disponibilidad de entradas
Los clientes deben poder consultar la cantidad de entradas disponibles de cada evento.

**RF09** – Agregar entrada al carrito
Los clientes deben poder seleccionar entradas y agregarlas a un carrito de compra.

**RF10** – Confirmar compra
Los clientes deben poder confirmar una compra utilizando las entradas seleccionadas.

**RF11** – Realizar pago
El sistema debe permitir registrar pagos mediante distintos medios de pago.

**RF12** – Generar comprobante
El sistema debe generar un comprobante asociado a una compra realizada correctamente.

**RF13** – Consultar historial de compras
Los clientes deben poder visualizar las compras realizadas anteriormente.

**RF14** – Controlar capacidad del evento
El sistema debe impedir la emisión de entradas cuando la capacidad máxima del evento haya sido alcanzada.

## Requerimientos no funcionales

- **RFN01** - La aplicación debe ser simple e intuitiva para usuarios sin conocimientos técnicos.
- **RNF02** - El sistema deberá mantener coherencia entre eventos, entradas, pagos y compras registradas.
- **RNF03** - Los datos se guardan en memoria.
- **RNF04** - El sistema debe validar los datos ingresados y mostrar mensajes de error claros.
- **RFN05** - Cada usuario solo podrá acceder a las funcionalidades correspondientes a su rol (cliente u organizador).
- **RFN06** - Las funcionalidades comunes deberán implementarse evitando duplicación de código mediante herencia, composición e interfaces.
- **RFN07** - La solución deberá desarrollarse siguiendo los principios de Programación Orientada a Objetos para facilitar futuras modificaciones.


## Pendientes 

- Si se cancela un evento ¿se reintegra el dinero de las entradas vendidas?
- ¿Hay límite de entradas por cliente?
- ¿Habrá distintos tipos o sectores de entrada (general, VIP, campo) con precios distintos?
