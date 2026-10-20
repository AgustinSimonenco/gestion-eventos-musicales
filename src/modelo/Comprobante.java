package modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
     * Arma el texto del comprobante: datos de la compra, una línea por
     * entrada, el total y el medio de pago.
     *
     * @return texto listo para mostrar
     */
    public String generarTexto() {
        DateTimeFormatter formatoFechaHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        StringBuilder texto = new StringBuilder();
        texto.append("Comprobante ").append(numero).append("\n");
        texto.append("Emitido: ").append(fechaEmision.format(formatoFechaHora)).append("\n");
        texto.append("Compra N.º ").append(compra.getNumero()).append("\n");
        texto.append("Cliente: ").append(compra.getCliente().getNombre()).append("\n\n");
        texto.append("Entradas:\n");
        for (Entrada entrada : compra.getEntradas()) {
            texto.append("  ").append(entrada.getCodigo()).append("  ")
                    .append(entrada.getEvento().getNombre()).append("  ")
                    .append(entrada.getEvento().getFecha().format(formatoFecha)).append("  $")
                    .append(String.format("%.2f", entrada.getPrecio())).append("\n");
        }
        texto.append("\nTotal: $").append(String.format("%.2f", compra.getTotal())).append("\n");
        if (compra.getPagoAprobado() != null) {
            texto.append("Pago: ").append(compra.getPagoAprobado().getDescripcion()).append("\n");
        }
        return texto.toString();
    }

    /** @return número del comprobante */
    public String getNumero() {
        return numero;
    }
}
