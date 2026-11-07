# CU13 — Consultar historial de compras · Diagrama de secuencia

[← Volver al p8](../p8-diagramas-de-secuencia.md) · Caso de uso: [CU13](../../analisis/casos-de-uso.md#cu13--consultar-historial-de-compras) (lo extienden [CU12](../../analisis/casos-de-uso.md#cu12--generar-comprobante) y [CU14](CU14-cancelar-compra.md))

El cliente toca *Mis compras* y ve una tabla con sus compras. Desde ahí puede ver el comprobante de una (CU12) o cancelarla (CU14).

## Diagrama

```mermaid
sequenceDiagram
    actor U as Persona (cliente)
    participant PE as PantallaEventosDisponibles
    participant P as PantallaHistorial
    participant V as VentanaPrincipal
    participant C as ControladorCompras
    participant S as Sesion
    participant Cl as Cliente
    participant Co as Compra
    participant Cb as Comprobante

    U->>+PE: toca Mis compras
    PE->>+V: mostrar(HISTORIAL)
    V->>+P: actualizar()
    P->>+C: getHistorial()
    C->>+S: getCliente()
    S-->>-C: cliente
    C->>+Cl: getCompras()
    Cl-->>-C: compras
    C-->>-P: compras
    P->>P: comprasMostradas = compras
    loop por cada compra
        P->>+Co: getNumero(), getFechaHora(), getNombresEventos(), getEntradas(), getTotal(), getPagoAprobado().getEstado(), getEstado()
        Co-->>-P: datos de la compra
        P->>P: modeloTabla.addRow(...)
    end
    alt no tiene compras (2a)
        P->>P: etiquetaAviso = "Todavía no hiciste ninguna compra."
    end
    deactivate P
    deactivate V
    deactivate PE

    U->>+P: elige una compra y toca Ver comprobante (CU12)
    P->>+C: verComprobante(compra)
    C->>+Co: generarComprobante()
    Note right of Co: devuelve el que ya se creó al pagar
    Co-->>-C: comprobante
    C->>+Cb: generarTexto()
    Cb-->>-C: texto
    C->>+V: mostrarTexto("Comprobante", texto)
    deactivate V
    deactivate C
    deactivate P
```

## Paso a paso

1. *Mis compras* solo cambia de pantalla, así que `PantallaEventosDisponibles` llama directo a `ventana.mostrar(HISTORIAL)`.
2. **`PantallaHistorial.actualizar()`** le pide las compras a `ControladorCompras.getHistorial()`, que se las pide al cliente que inició sesión.
3. La pantalla guarda la lista en `comprasMostradas` y arma una fila por compra: número, fecha de compra, eventos, cantidad de entradas, total, estado del pago y estado de la compra.
4. **2a:** si la lista está vacía, lo dice en la misma pantalla con una etiqueta.
5. *Ver comprobante* toma la compra de la fila elegida y llama a `verComprobante`. **`Compra.generarComprobante()`** devuelve el comprobante que se creó al pagar, sin generar otro número. **`Comprobante.generarTexto()`** arma el texto, que se muestra en un diálogo.
6. *Cancelar compra* está dibujado en el [CU14](CU14-cancelar-compra.md).

## Elementos, patrones y decisiones

| Elemento | Qué muestra |
|---|---|
| **GRASP Experto** | El cliente tiene sus compras; la compra sabe qué eventos tiene y arma los nombres; el comprobante arma su texto. |
| **Polimorfismo** | `compra.getPagoAprobado()` devuelve un `MedioDePago`: la pantalla no sabe si fue tarjeta o transferencia. |
| **Herencia** | `PantallaHistorial` hereda de `Pantalla` y usa `crearTabla`, `formatearFecha` y `formatearPrecio`. |

- **Una compra por fila, no una entrada por fila.** Una compra puede tener muchas entradas. La cantidad se muestra en la tabla, y los códigos de cada entrada están en el comprobante.
- **Si el organizador canceló un evento, el historial lo aclara** con "(CANCELADO)" al lado del nombre. La compra no se cancela sola (D-003): el cliente decide si la cancela.
- **Solo aparecen compras confirmadas o canceladas.** La compra entra al historial recién cuando se paga (`Cliente.agregarCompra`).
- Este caso de uso no guarda nada: solo consulta.
