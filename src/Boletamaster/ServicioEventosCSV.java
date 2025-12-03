package Boletamaster;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de ServicioEventos que lee los eventos
 * ya cargados en memoria por DataStore.loadAll().
 */
public class ServicioEventosCSV implements IServicioEventos {

    @Override
    public List<Evento> listarEventos() {
        // Usamos la lista estática que ya maneja el backend
        return new ArrayList<>(Main.eventos);
    }
}
