# CU04 — Modificar evento · Diagrama de secuencia

[← Volver al p8](../p8-diagramas-de-secuencia.md) · Caso de uso: [CU04](../../analisis/casos-de-uso.md#cu04--modificar-evento)

El organizador elige un evento en "Mis eventos", toca *Modificar*, corrige los datos en el formulario y toca *Guardar*. Pasa por **dos pantallas**, por eso el controlador recuerda el evento en `eventoSeleccionado`.

## Diagrama

```mermaid
sequenceDiagram
    actor O as Persona (organizador)
    participant PM as PantallaMisEventos
    participant PF as PantallaFormularioEvento
    participant C as ControladorEventos
    participant S as Sesion
    participant E as Evento
    participant PE as Persistencia
    participant V as VentanaPrincipal

    O->>+PM: elige un evento de la tabla y toca Modificar
    PM->>PM: actionPerformed(e) y getEventoElegido()
    PM->>+C: elegirParaModificar(evento)
    C->>+S: getOrganizador()
    S-->>-C: organizador
    alt el evento se puede modificar
        C->>+E: verificarOrganizador(organizador)
        deactivate E
        C->>+E: verificarVigente("modificar")
        deactivate E
        C->>C: eventoSeleccionado = evento
        C->>+V: mostrar(FORMULARIO_EVENTO)
        V->>+PF: actualizar()
        PF->>+C: getEventoSeleccionado()
        C-->>-PF: evento
        Note over PF: modo modificar: llena los campos y oculta la lista de artistas
        deactivate PF
        deactivate V
    else cancelado o finalizado
        C->>+E: verificarOrganizador(organizador)
        deactivate E
        C->>+E: verificarVigente("modificar")
        E--x-C: EventoException
        C->>+V: mostrarError(e.getMessage())
        deactivate V
        Note over PM,V: se queda en Mis eventos
    end
    deactivate C
    deactivate PM

    O->>+PF: corrige los datos y toca Guardar
    PF->>+C: modificarEvento(nombre, fecha, lugar, capacidad, precio)
    C->>C: convertirFecha / convertirEntero / convertirDecimal
    alt datos válidos
        C->>+E: modificar(nombre, fecha, lugar, capacidad, precio)
        E->>E: verificarVigente, validarDatos, capacidad >= vendidas
        E->>E: guardarDatos y actualizarCupo
        deactivate E
        C->>+S: guardar()
        S->>+PE: guardar(datos)
        deactivate PE
        deactivate S
        C->>+V: mostrarMensaje("Se guardaron los cambios del evento.")
        deactivate V
        C->>+V: mostrar(MIS_EVENTOS)
        deactivate V
    else dato inválido o capacidad menor a lo vendido
        C->>+E: modificar(nombre, fecha, lugar, capacidad, precio)
        E->>E: verificarVigente, validarDatos, capacidad >= vendidas
        E--x-C: DatosInvalidosException / EventoException
        C->>+V: mostrarError(e.getMessage())
        deactivate V
        Note over PF,V: se queda en el formulario con lo escrito
    end
    deactivate C
    deactivate PF
```

## Paso a paso

1. **`PantallaMisEventos`** toma la fila elegida y busca el `Evento` en `eventosMostrados`. Si no hay fila elegida, muestra "Elegí un evento de la tabla." y no llama al controlador.
2. **`ControladorEventos.elegirParaModificar`** controla dos cosas **antes** de abrir el formulario: que el evento sea del organizador (`verificarOrganizador`) y que no esté cancelado ni finalizado (`verificarVigente`). Así no se completa un formulario para nada.
3. Guarda el evento en `eventoSeleccionado` y muestra el formulario. `PantallaFormularioEvento.actualizar()` ve que hay un evento seleccionado, pasa a modo modificar, llena los campos y oculta la lista de artistas (los artistas se cambian en el CU06).
4. Al tocar *Guardar*, el formulario llama a `modificarEvento` con los textos. El controlador los convierte y le pide al evento que se modifique.
5. **`Evento.modificar`** vuelve a controlar que esté vigente, valida los datos con el mismo método que el constructor (`validarDatos`) y no deja bajar la capacidad por debajo de las entradas vendidas. Después actualiza el estado: si con la nueva capacidad no quedan lugares, pasa a `AGOTADO`, y al revés.
6. Si todo está bien, se guarda y se vuelve a "Mis eventos" con la tabla recargada.

## Elementos, patrones y decisiones

| Elemento | Qué muestra |
|---|---|
| **Estado en el controlador** | `eventoSeleccionado` une las dos pantallas del caso de uso. Si es `null`, el formulario está en modo crear (CU03). |
| **GRASP Experto** | Las reglas (vigente, datos válidos, capacidad) están en `Evento`. |
| **Sin código repetido** | El mismo formulario sirve para crear y modificar; `validarDatos` lo usan el constructor y `modificar`. |

- **`verificarVigente` se llama dos veces** (en `elegirParaModificar` y dentro de `modificar`). La primera es para avisar temprano; la segunda es la que protege la regla, aunque alguien llame a `modificar` desde otro lado.
