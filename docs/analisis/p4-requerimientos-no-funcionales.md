# p4. Requerimientos no funcionales

## Usabilidad
- **RNF01** – La aplicación debe ser simple e intuitiva para usuarios sin conocimientos técnicos.
- **RNF02** – La aplicación debe contar con una **interfaz gráfica** de escritorio desarrollada con Swing.
- **RNF03** – El sistema debe validar los datos ingresados y mostrar mensajes de error claros.

## Persistencia y consistencia
- **RNF04** – La información del sistema (usuarios, eventos, artistas, compras, entradas y pagos) debe **persistirse en archivos**, de modo que se conserve entre ejecuciones.
- **RNF05** – El sistema debe mantener la coherencia entre eventos, entradas, pagos y compras registradas.

## Seguridad
- **RNF06** – Cada usuario solo podrá acceder a las funcionalidades correspondientes a su rol (cliente u organizador).
- **RNF07** – En los comprobantes y listados no se debe mostrar el número completo de tarjeta, solo los últimos 4 dígitos.

## Gestión de errores
- **RNF08** – El sistema debe capturar todos los errores posibles en tiempo de ejecución y clasificarlos mediante una **jerarquía de excepciones propia** (por ejemplo: errores de evento, de compra, de pago y de autenticación). La ejecución no debe arrojar excepciones inesperadas.

## Extensibilidad y mantenibilidad
- **RNF09** – El modelo debe poder extenderse fácilmente, por ejemplo para incorporar nuevos medios de pago o un nuevo mecanismo de almacenamiento, sin modificar las clases existentes (uso de interfaces).
- **RNF10** – Las funcionalidades comunes deberán implementarse evitando duplicación de código mediante herencia, composición e interfaces.
- **RNF11** – La solución deberá desarrollarse siguiendo los principios de Programación Orientada a Objetos, distribuyendo adecuadamente las responsabilidades entre las clases (evitando clases que concentren demasiadas responsabilidades).

## Plataforma e implementación
- **RNF12** – El sistema debe desarrollarse en **Java**, compatible con la **versión 8 o superior**, y compilar sin errores.
- **RNF13** – Solo se utilizarán bibliotecas estándar del JDK; no se usarán frameworks externos que resuelvan la lógica principal de la aplicación.
- **RNF14** – El código debe documentarse con Javadoc y contar con pruebas unitarias en JUnit para las clases principales del modelo.
