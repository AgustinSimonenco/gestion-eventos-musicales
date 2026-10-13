# p3. Requerimientos funcionales

**RF01** – Registrarse
El sistema debe permitir registrar nuevos usuarios, indicando si son clientes u organizadores. No puede haber dos usuarios con el mismo email. *(CU01)*

**RF02** – Iniciar sesión
El sistema debe permitir autenticar usuarios mediante correo electrónico y contraseña. *(CU02)*

**RF03** – Crear evento
El organizador debe poder registrar nuevos eventos musicales indicando nombre, fecha, lugar, capacidad y precio de entrada. *(CU03)*

**RF04** – Modificar evento
El organizador debe poder actualizar la información de los eventos que creó. *(CU04)*

**RF05** – Cancelar evento
El organizador debe poder cancelar eventos activos. Un evento cancelado no admite nuevas ventas. *(CU05)*

**RF06** – Asignar artistas a un evento
El organizador debe poder asociar y desasociar artistas a un evento. *(CU06)*

**RF07** – Consultar eventos disponibles
Los clientes deben poder visualizar los eventos activos y disponibles. *(CU07)*

**RF08** – Consultar disponibilidad de entradas
Los clientes deben poder consultar la cantidad de entradas disponibles de cada evento. *(CU08)*

**RF09** – Agregar entrada al carrito
Los clientes deben poder seleccionar entradas, agregarlas a un carrito de compra, modificar cantidades y quitar ítems. *(CU09)*

**RF10** – Confirmar compra
Los clientes deben poder confirmar una compra utilizando las entradas seleccionadas. *(CU10)*

**RF11** – Realizar pago
El sistema debe permitir registrar pagos mediante distintos medios de pago (tarjeta y transferencia). Una compra queda confirmada solo si su pago fue aprobado. *(CU11)*

**RF12** – Generar comprobante
El sistema debe generar un comprobante asociado a una compra realizada correctamente. *(CU12)*

**RF13** – Consultar historial de compras
Los clientes deben poder visualizar las compras realizadas anteriormente y sus comprobantes. *(CU13)*

**RF14** – Controlar capacidad del evento
El sistema debe impedir la emisión de entradas cuando la capacidad máxima del evento haya sido alcanzada. La disponibilidad se verifica al agregar al carrito y nuevamente al confirmar la compra. *(CU09, CU10)*

**RF15** – Cancelar compra
Los clientes deben poder cancelar una compra antes de la fecha del evento. Las entradas se anulan, se libera su capacidad y se registra el reintegro. *(CU14)*

**RF16** – Gestionar artistas
El organizador debe poder dar de alta, modificar y dar de baja artistas. *(CU15)*

## Pendientes

- Si se cancela un evento, ¿se reintegra automáticamente el dinero de las entradas vendidas?
- ¿Hay límite de entradas por cliente?
- ¿Habrá distintos tipos o sectores de entrada (general, VIP, campo) con precios distintos? *(Por ahora queda fuera del alcance, ver p1.)*
