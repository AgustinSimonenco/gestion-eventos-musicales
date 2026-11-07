package persistencia;

import excepciones.SistemaException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import modelo.Artista;
import modelo.Carrito;
import modelo.Cliente;
import modelo.Compra;
import modelo.DatosSistema;
import modelo.Evento;
import modelo.Organizador;
import modelo.PagoTarjeta;

/**
 * Carga los datos con los que arranca el programa la primera vez, para poder
 * mostrarlo funcionando sin cargar todo a mano (etapa E14).
 * <p>
 * Usa los mismos constructores y métodos que el resto del programa, así que
 * pasan por todas las validaciones del modelo. Las fechas se calculan desde
 * hoy, para que los eventos sean siempre futuros.
 * <p>
 * Usuarios: {@code ana@demo.com} (organizadora), {@code juan@demo.com} y
 * {@code maria@demo.com} (clientes). La contraseña de todos es {@code 1234}.
 */
public class DatosDeEjemplo {

    private static final String CONTRASENIA = "1234";

    /**
     * Agrega los datos de ejemplo. Se llama solo si los datos están vacíos.
     *
     * @param datos datos vacíos del sistema
     * @throws SistemaException si algún dato no pasa las validaciones del modelo
     */
    public static void cargarEn(DatosSistema datos) throws SistemaException {
        Organizador ana = new Organizador("Ana López", "ana@demo.com", CONTRASENIA);
        Cliente juan = new Cliente("Juan Pérez", "juan@demo.com", CONTRASENIA);
        Cliente maria = new Cliente("María Gómez", "maria@demo.com", CONTRASENIA);
        datos.registrarUsuario(ana);
        datos.registrarUsuario(juan);
        datos.registrarUsuario(maria);

        Artista soda = agregarArtista(datos, "Soda Stereo", "Rock");
        Artista charly = agregarArtista(datos, "Charly García", "Rock");
        Artista fito = agregarArtista(datos, "Fito Páez", "Rock");
        Artista mercedes = agregarArtista(datos, "Mercedes Sosa", "Folklore");
        Artista piazzolla = agregarArtista(datos, "Astor Piazzolla", "Tango");

        Evento rock = agregarEvento(datos, "Rock en el Luna", 30, "Luna Park", 200, 25000, ana, soda, charly);
        Evento folklore = agregarEvento(datos, "Noche de folklore", 45, "Teatro Coliseo", 100, 15000.5, ana, mercedes);
        Evento tango = agregarEvento(datos, "Tango íntimo", 20, "Café Tortoni", 2, 18000, ana, piazzolla);
        Evento acustico = agregarEvento(datos, "Fito acústico", 60, "Teatro Ópera", 50, 30000, ana, fito);

        // Una compra normal y otra que agota "Tango íntimo", para que Juan
        // tenga historial y se vea un evento AGOTADO
        comprar(datos, juan, rock, 2);
        comprar(datos, juan, tango, 2);
        // Una compra de un evento que después se cancela (D-003: la compra no
        // se cancela sola, y el historial lo avisa)
        comprar(datos, juan, acustico, 1);
        acustico.cancelar();
        // Se deja un ítem en el carrito de María para mostrar la compra
        maria.getCarrito().agregarItem(folklore, 2);
    }

    private static Artista agregarArtista(DatosSistema datos, String nombre, String genero)
            throws SistemaException {
        Artista artista = new Artista(datos.generarId(), nombre, genero);
        datos.agregarArtista(artista);
        return artista;
    }

    // "diasDesdeHoy" hace que el evento sea futuro cualquier día que se abra el programa
    private static Evento agregarEvento(DatosSistema datos, String nombre, int diasDesdeHoy,
            String lugar, int capacidad, double precio, Organizador organizador,
            Artista... artistas) throws SistemaException {
        List<Artista> lista = new ArrayList<Artista>();
        for (Artista artista : artistas) {
            lista.add(artista);
        }
        Evento evento = new Evento(datos.generarId(), nombre, LocalDate.now().plusDays(diasDesdeHoy),
                lugar, capacidad, precio, organizador, lista);
        datos.agregarEvento(evento);
        return evento;
    }

    // Hace los mismos pasos que ControladorCompras, con una tarjeta de prueba
    private static void comprar(DatosSistema datos, Cliente cliente, Evento evento, int cantidad)
            throws SistemaException {
        Carrito carrito = cliente.getCarrito();
        carrito.agregarItem(evento, cantidad);
        Compra compra = new Compra(datos.generarId(), cliente, carrito.getTotal());
        compra.registrarPago(new PagoTarjeta(carrito.getTotal(), "4509123412341234", cliente.getNombre(),
                12, LocalDate.now().getYear() + 2, "123", 1));
        compra.confirmar(carrito);
        cliente.agregarCompra(compra);
        compra.generarComprobante();
        carrito.vaciar();
    }
}
