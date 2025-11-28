package Boletamaster;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Organizador extends Cliente {
    private ArrayList<Evento> listaEventos;

    public Organizador(String id, String login, String password) {
        super(id, login, password);
        listaEventos = new ArrayList<>();
    }

    public Evento crearEvento(String id, String nombre, Venue v, LocalDate fecha, LocalTime hora, String tipo) {
        if (v != null && !v.reservarFecha(fecha)) {
            System.out.println("El venue ya tiene un evento ese día.");
            return null;
        }
        Evento e = new Evento(id, nombre, v, fecha, hora, tipo);
        listaEventos.add(e);
        return e;
    }

    public Localidad crearLocalidad(Evento e, String id, String nombre, double precio, boolean numerada, int aforo) {
        Localidad l = new Localidad(id, nombre, precio, numerada, aforo, e);
        if (e != null) e.agregarLocalidad(l);
        return l;
    }

    public Oferta crearOferta(Localidad l, String id, double descuento) {
        Oferta o = new Oferta(id, descuento);
        o.localidad = l;         
        l.agregarOferta(o);
        return o;
    }

    public String verGanancias() {
        if (listaEventos.isEmpty()) return "No tienes eventos.";

        double ingresosOrganizador = 0.0;
        double ingresosPlataforma  = 0.0;
        double totalTransado       = 0.0;

        StringBuilder sbDetalle = new StringBuilder();
        for (Evento e : listaEventos) {
            double orgEv = 0.0, platEv = 0.0, totEv = 0.0;
            int vendidosEv = 0;

            for (Tiquete t : Main.inventario) {
                Localidad l = t.getLocalidad();
                if (l == null || l.getEvento() != e) continue;

                String st = t.getEstado();
                if (!"VENDIDO".equals(st) && !"TRANSFERIDO".equals(st)) continue;

                double base  = t.getPrecio();
                double cobro = t.calcularPrecioTotal(Main.admin);
                double fee   = cobro - base;

                orgEv      += base;
                platEv     += fee;
                totEv      += cobro;
                vendidosEv += 1;
            }

            ingresosOrganizador += orgEv;
            ingresosPlataforma  += platEv;
            totalTransado       += totEv;

            sbDetalle.append(String.format(
                "- %s: vendidos=%d | org=$%,.0f | plataforma=$%,.0f | total=$%,.0f%n",
                e.getNombre(), vendidosEv, orgEv, platEv, totEv
            ));
        }

        String resumen = String.format(
            "Ganancias organizador: $%,.0f%nIngresos plataforma: $%,.0f%nTotal transado: $%,.0f%n",
            ingresosOrganizador, ingresosPlataforma, totalTransado
        );
        return resumen + sbDetalle;
    }

    public ArrayList<Evento> getEventos() { return listaEventos; }

    public String[] toCsv(){ return new String[]{ getId(), getLogin(), getPassword() }; }
    public static Organizador fromCsv(String[] r){
        if (r.length<3) return null;
        return new Organizador(r[0], r[1], r[2]);
    }
}