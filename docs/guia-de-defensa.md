# Guía de defensa

Para cada caso de uso: por dónde se entra, qué método del controlador se llama, qué clase del modelo tiene la regla y qué diagrama mirar. En la defensa a cada integrante le toca un caso de uso al azar y tiene que seguirlo desde la GUI hasta el código.

## La idea que sirve para todos los casos de uso

Todos siguen la misma forma. Si entendés uno, entendés los demás.

1. Swing llama a `actionPerformed` de la **pantalla**, porque la pantalla se registró con `addActionListener(this)` (patrón Observer).
2. La pantalla llama a **un** método del **controlador** y le pasa los textos tal cual. No valida nada.
3. El controlador convierte los textos (métodos heredados de `Controlador`) y le pide la operación al **modelo**.
4. El **modelo** decide. Si algo no se puede, lanza una excepción que hereda de `SistemaException`.
5. Si salió bien, el controlador llama a `sesion.guardar()` y le dice a la `VentanaPrincipal` qué mostrar.
6. Si salió mal, un único `catch (SistemaException e)` llama a `ventana.mostrarError(e.getMessage())`.
7. `VentanaPrincipal.mostrar(nombre)` llama a `actualizar()` de la pantalla, que recarga sus datos, y la muestra con el `CardLayout`.

## Trazabilidad

| CU | Pantalla y botón | Controlador | Regla en el modelo | Diagrama |
|---|---|---|---|---|
| CU01 Registrar usuario | `PantallaRegistro` · *Registrar* | `ControladorUsuarios.registrar` | Constructor de `Usuario`, `DatosSistema.registrarUsuario` | [CU01](diseno/secuencias/CU01-registrar-usuario.md) |
| CU02 Iniciar sesión | `PantallaLogin` · *Ingresar* | `ControladorUsuarios.iniciarSesion` | `DatosSistema.autenticar`, `Usuario.getTipo` | [CU02](diseno/secuencias/CU02-iniciar-sesion.md) |
| CU03 Crear evento | `PantallaFormularioEvento` · *Guardar* | `ControladorEventos.crearEvento` | Constructor de `Evento`, `validarDatos` | [CU03](diseno/secuencias/CU03-crear-evento.md) |
| CU04 Modificar evento | `PantallaMisEventos` · *Modificar* → *Guardar* | `elegirParaModificar`, `modificarEvento` | `Evento.verificarVigente`, `Evento.modificar` | [CU04](diseno/secuencias/CU04-modificar-evento.md) |
| CU05 Cancelar evento | `PantallaMisEventos` · *Cancelar evento* | `ControladorEventos.cancelarEvento` | `Evento.cancelar` | [CU05](diseno/secuencias/CU05-cancelar-evento.md) |
| CU06 Asignar artistas | `PantallaMisEventos` · *Artistas del evento* → `PantallaAsignarArtistas` | `elegirParaArtistas`, `asignarArtista`, `quitarArtista` | `Evento.asignarArtista`, `Evento.quitarArtista` | [CU06](diseno/secuencias/CU06-asignar-artistas.md) |
| CU07 Consultar eventos | `PantallaEventosDisponibles` (al abrirse) | `ControladorCarrito.getEventosDisponibles` | `DatosSistema.getEventosDisponibles` | [CU07](diseno/secuencias/CU07-consultar-eventos-disponibles.md) |
| CU08 Consultar disponibilidad | `PantallaEventosDisponibles` · *Ver disponibilidad* | `ControladorCarrito.consultarDisponibilidad` | `Evento.verificarALaVenta`, `generarDetalle` | [CU08](diseno/secuencias/CU08-consultar-disponibilidad.md) |
| CU09 Agregar al carrito | `PantallaEventosDisponibles` · *Agregar al carrito*; `PantallaCarrito` | `agregarAlCarrito`, `cambiarCantidad`, `quitarItem` | `Carrito.agregarItem` → `Evento.verificarDisponibilidad` | [CU09](diseno/secuencias/CU09-agregar-entradas-al-carrito.md) |
| CU10 Confirmar compra | `PantallaCarrito` · *Confirmar compra* | `ControladorCompras.iniciarCompra` | `Carrito.verificarDisponibilidad`, `new Compra` | [CU10, parte 1](diseno/secuencias/CU10-confirmar-compra.md) |
| CU11 Realizar pago | `PantallaPago` · *Pagar* | `pagarConTarjeta`, `pagarConTransferencia`, `cancelarPago` | Constructores de los pagos, `Pago.procesar`, `Compra.registrarPago` | [CU10, partes 2 y 3](diseno/secuencias/CU10-confirmar-compra.md) |
| CU12 Generar comprobante | Automático al pagar; `PantallaHistorial` · *Ver comprobante* | `procesarPago`, `verComprobante` | `Compra.generarComprobante`, `Comprobante.generarTexto` | [CU10, parte 2](diseno/secuencias/CU10-confirmar-compra.md) y [CU13](diseno/secuencias/CU13-consultar-historial.md) |
| CU13 Consultar historial | `PantallaHistorial` (al abrirse) | `ControladorCompras.getHistorial` | `Cliente.getCompras` | [CU13](diseno/secuencias/CU13-consultar-historial.md) |
| CU14 Cancelar compra | `PantallaHistorial` · *Cancelar compra* | `ControladorCompras.cancelarCompra` | `Compra.verificarCancelable`, `Compra.cancelar` | [CU14](diseno/secuencias/CU14-cancelar-compra.md) |
| CU15 Gestionar artistas | `PantallaArtistas` · *Alta*, *Modificar*, *Baja* | `altaArtista`, `modificarArtista`, `bajaArtista` | `DatosSistema.agregarArtista`, `modificarArtista`, `eliminarArtista` | [CU15](diseno/secuencias/CU15-gestionar-artistas.md) |

Las clases están en el [p6](diseno/p6-diagrama-de-clases-modelo.md) y el [p7](diseno/p7-diagrama-vista-controlador.md); los cambios de pantalla, en el [p9](diseno/p9-diagrama-de-navegacion.md).

## Patrones: dónde están y cómo explicarlos

| Patrón | Dónde | En una frase |
|---|---|---|
| **MVC** | Paquetes `vista`, `controlador`, `modelo` | Se puede cambiar una pantalla sin tocar las reglas. |
| **Strategy** | `MedioDePago`, usado por `Compra.registrarPago` | `Compra` llama a `procesar()` sin saber si es tarjeta o transferencia. Un medio nuevo no cambia `Compra`. |
| **Template Method** | `Pago.procesar()` con `autorizar()` abstracto | Los pasos están una sola vez en `Pago`; cada subclase dice solo cómo se autoriza. |
| **Observer** | Pantallas que implementan `ActionListener` | El botón avisa a la pantalla, sin conocerla. |
| **DAO** (parecido) | `Persistencia` / `PersistenciaArchivo` | Se puede cambiar cómo se guarda sin tocar el resto. |
| **GRASP Experto** | `Evento.getEntradasDisponibles`, `Carrito.getTotal`, `ItemCarrito.getSubtotal` | Cada cálculo lo hace la clase que tiene los datos. |
| **GRASP Creador** | `Compra` crea `Entrada` y `Comprobante`; `Carrito` crea `ItemCarrito` | Crea el objeto quien lo contiene. |
| **GRASP Controlador** | Un controlador por módulo | Los eventos de la GUI los recibe un controlador, no el modelo. |
| **Polimorfismo** | `Usuario.getTipo`, `Pantalla.actualizar`, `MedioDePago.procesar` | Se llama igual y cada subclase hace lo suyo. |

## Preguntas generales

1. **¿Quién llama a `actionPerformed`?** — *Swing, cuando ocurre el evento. Nuestro código solo se registra con `addActionListener(this)`.*
2. **¿Por qué las pantallas no validan?** — *Porque las reglas tienen que estar en el modelo. Así se cumplen siempre, sin importar desde dónde se llame.*
3. **¿Cómo llega un error del modelo a la pantalla?** — *El modelo lanza una excepción, sube hasta el controlador, y su `catch (SistemaException e)` muestra el mensaje con la ventana. La pantalla nunca ve una excepción.*
4. **¿Por qué excepciones verificadas?** — *Heredan de `Exception`, entonces el compilador obliga a capturarlas. Ninguna se escapa sin control.*
5. **¿Cómo se guardan los datos?** — *Con serialización de Java: `PersistenciaArchivo` escribe el `DatosSistema` entero. Las referencias entre objetos se conservan solas.*
6. **¿Por qué `serialVersionUID`?** — *Para que el archivo guardado se pueda seguir leyendo aunque se cambie la clase.*
7. **¿Por qué `Sesion` y no una clase estática?** — *Es un objeto que comparten todos los controladores: los datos, la persistencia y el usuario actual. Se pasa por constructor.*
8. **¿Por qué una sola ventana?** — *`CardLayout` muestra una pantalla por vez y la navegación queda en un solo lugar.*
9. **¿Cómo se agrega otro medio de pago?** — *Una clase que herede de `Pago` e implemente `autorizar` y `getDescripcion`, un método en `ControladorCompras` y su formulario. `Compra` no cambia.*
10. **¿Por qué los lugares se ocupan después de pagar?** — *Si el pago se rechaza, no hay nada que deshacer. Por eso agregar al carrito no ocupa lugares.*
11. **¿Qué pasa con las compras si se cancela un evento?** — *No se cancelan solas (D-003). El historial lo avisa con "(CANCELADO)" y el cliente la cancela con el CU14.*
12. **La contraseña está sin cifrar, ¿por qué?** — *La seguridad no está en el alcance del trabajo. Lo que sí se cuida es la tarjeta: solo se guardan los últimos 4 dígitos y nunca el código.*
13. **¿Por qué `generarDetalle` y `generarTexto` están en el modelo si arman texto?** — *Es discutible en MVC. Se decidió así porque ese texto usa solo datos del objeto y lo necesitan los controladores, que no usan Swing. La pantalla sigue sin saber cómo se arma.*
14. **¿Cómo se probó?** — *62 tests JUnit de las reglas del modelo y la persistencia (`probar.bat`). Para probar que algo falla se usa `try` / `fail` / `catch`, porque `assertThrows` necesita una lambda.*

## Puntos para mirar con cuidado

- **`Sesion.getCliente()` y `getOrganizador()` hacen un cast.** Es seguro porque, por la navegación (p9), un cliente nunca llega a una pantalla del organizador.
- **`verificarVigente` y `verificarCancelable` se llaman dos veces a propósito.** En el controlador, para no pedir datos o confirmación en vano; en el modelo, para que la regla se cumpla siempre.
- **`actualizarEstado` cambia el estado al listar.** El estado no cambia solo con el tiempo, así que se recalcula cada vez que se muestran los eventos.
- **En `procesarPago` se sale de la pantalla de pago antes de guardar.** La compra ya terminó: si fallara el guardado, el cliente no puede quedar pagando una compra que ya no existe.
