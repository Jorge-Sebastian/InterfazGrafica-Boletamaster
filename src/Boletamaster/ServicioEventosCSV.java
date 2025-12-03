package Boletamaster;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de ServicioEventos que lee los eventos
 * ya cargados en memoria por DataStore.loadAll().
 */
public class ServicioEventosCSV implements IServicioEventos {
	
	private Organizador organizador; // puede ser null
	
	public ServicioEventosCSV() {
		this(null);
	}
	
	public ServicioEventosCSV(Organizador organizador) {
		this.organizador = organizador;
	}

    @Override
    public List<Evento> listarEventos() {
        // Usamos la lista estática que ya maneja el backend
    	
    	// Si nos pasan un organizador, usamos sus eventos
    	if (organizador != null) {
    		return new ArrayList<>(organizador.getEventos());
    	}
    	// Si no, devolvemos todos los eventos del sistema
        return new ArrayList<>(Main.eventos);
    }
}
