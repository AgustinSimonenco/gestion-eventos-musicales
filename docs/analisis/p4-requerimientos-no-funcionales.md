# p4. Requerimientos no funcionales

## Usabilidad
- **RNF01** – La aplicación debe ser simple e intuitiva para usuarios sin conocimientos técnicos.
- **RNF02** – El sistema debe validar los datos ingresados y mostrar mensajes de error claros.

## Persistencia y consistencia
- **RNF03** – La información del sistema (usuarios, eventos, artistas, compras, entradas y pagos) debe **persistirse en archivos**, de modo que se conserve entre ejecuciones.
- **RNF04** – El sistema debe mantener la coherencia entre eventos, entradas, pagos y compras registradas.

## Seguridad
- **RNF06** – Cada usuario solo podrá acceder a las funcionalidades correspondientes a su rol (cliente u organizador).
  
## Gestión de errores
- **RNF07** – El sistema debe capturar todos los errores posibles en tiempo de ejecución y clasificarlos mediante una **jerarquía de excepciones propia** (por ejemplo: errores de evento, de compra, de pago y de autenticación). 

## Extensibilidad y mantenibilidad
- **RNF08** – El modelo debe poder extenderse fácilmente, mediante uso de interfaces.
- **RNF09** – Las funcionalidades comunes deberán implementarse evitando duplicación de código mediante herencia, composición e interfaces.
- **RNF10** – La solución deberá desarrollarse siguiendo los principios de Programación Orientada a Objetos.

## Plataforma e implementación
- **RNF11** – El sistema debe desarrollarse en **Java** **versión 8 o superior**.
