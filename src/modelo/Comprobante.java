package modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Constancia de una compra pagada (CU12). Arma el texto que se le muestra al
 * cliente.
 */
public class Comprobante implements Serializable {

    private static final long serialVersionUID = 1L;

    private String numero;
    private LocalDateTime fechaEmision;
    private Compra compra;

    /**
     * Crea el comprobante. Solo lo crea {@link Compra#generarComprobante()}.
     *
     * @param numero número del comprobante, por ejemplo "C-15"
     * @param compra compra pagada
     */
    public Comprobante(String numero, Compra compra) {
        this.numero = numero;
        this.compra = compra;
        this.fechaEmision = LocalDateTime.now();
    }

    /**
     * Arma el texto del comprobante con lo que pide el CU12: número, fecha,
     * cliente, detalle por evento, código de cada entrada, total y medio de
     * pago. Se arma cada vez que se muestra, así una compra cancelada lo dice.
     *
     * @return texto listo para mostrar
     */
    public String generarTexto() {
        DateTimeFormatter formatoFechaHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        StringBuilder texto = new StringBuilder();
        texto.append("Comprobante ").append(numero).append("\n");
        texto.append("Emitido: ").append(fechaEmision.format(formatoFechaHora)).append("\n");
        texto.append("Compra N.º ").append(compra.getNumero()).append("\n");
        texto.append("Cliente: ").append(compra.getCliente().getNombre())
                .append(" (").append(compra.getCliente().getEmail()).append(")\n\n");
        agregarDetalle(texto);
        texto.append("Total pagado: $").append(String.format("%.2f", compra.getTotal())).append("\n");
        if (compra.getPagoAprobado() != null) {
            texto.append("Pago: ").append(compra.getPagoAprobado().getDescripcion()).append("\n");
        }
        if (compra.getEstado() == EstadoCompra.CANCELADA) {
            texto.append("\nCOMPRA CANCELADA: entradas anuladas y total reintegrado.\n");
        }
        return texto.toString();
    }

    // Agrega un bloque por evento. Las entradas de un mismo evento están
    // seguidas, porque la compra las crea ítem por ítem.
    private void agregarDetalle(StringBuilder texto) {
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        List<Entrada> entradas = compra.getEntradas();
        int i = 0;
        while (i < entradas.size()) {
            Evento evento = entradas.get(i).getEvento();
            double precio = entradas.get(i).getPrecio();
            String codigos = "";
            int cantidad = 0;
            while (i < entradas.size() && entradas.get(i).getEvento() == evento) {
                codigos = codigos + " " + entradas.get(i).getCodigo();
                cantidad = cantidad + 1;
                i = i + 1;
            }
            texto.append(evento.getNombre()).append(" - ").append(evento.getFecha().format(formatoFecha))
                    .append(" - ").append(evento.getLugar()).append("\n");
            texto.append("  ").append(cantidad).append(" x $").append(String.format("%.2f", precio))
                    .append(" = $").append(String.format("%.2f", cantidad * precio)).append("\n");
            texto.append("  Entradas:").append(codigos).append("\n\n");
        }
    }

    /** @return número del comprobante */
    public String getNumero() {
        return numero;
    }
}
