package Boletamaster;

import Boletamaster.csv.Csv;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class DataStore {

    private static final String DIR            = "data/";
    private static final String F_ADMIN        = DIR + "administradores.csv";
    private static final String F_CLIENTE      = DIR + "clientes.csv";
    private static final String F_ORG          = DIR + "organizadores.csv";
    private static final String F_VENUE        = DIR + "venues.csv";
    private static final String F_VENUE_FECHAS = DIR + "venue_fechas.csv";
    private static final String F_EVENTO       = DIR + "eventos.csv";
    private static final String F_LOCALIDAD    = DIR + "localidades.csv";
    private static final String F_OFERTA       = DIR + "ofertas.csv";
    private static final String F_TIQ          = DIR + "tiquetes.csv";
    private static final String F_PAGO         = DIR + "pagos.csv";
    private static final String F_PAGO_ITEMS   = DIR + "pago_items.csv";
    private static final String F_PAQ          = DIR + "paquetes.csv";
    private static final String F_PAQ_ITEMS    = DIR + "paquete_items.csv";

    public void loadAll() {
        Main.usuarios.clear();
        Main.eventos.clear();
        Main.localidades.clear();
        Main.inventario.clear();
        Main.pagos.clear();

        // admin
        for (String[] r : Csv.readAll(F_ADMIN, true)) {
            Administrador a = Administrador.fromCsv(r);
            if (a != null) Main.admin = a;
        }
        if (Main.admin == null) Main.admin = new Administrador("A1","admin","123");

        // clientes
        for (String[] r : Csv.readAll(F_CLIENTE, true)) {
            Cliente c = Cliente.fromCsv(r);
            if (c != null) Main.usuarios.add(c);
        }
        // organizadores
        for (String[] r : Csv.readAll(F_ORG, true)) {
            Organizador o = Organizador.fromCsv(r);
            if (o != null) Main.usuarios.add(o);
        }

        // venues
        Map<String, Venue> venues = new HashMap<>();
        for (String[] r : Csv.readAll(F_VENUE, true)) {
            Venue v = Venue.fromCsv(r);
            if (v != null) venues.put(v.getId(), v);
        }
        // fechas de venue
        for (String[] r : Csv.readAll(F_VENUE_FECHAS, true)) {
            String id = s(r,0); String f = s(r,1);
            Venue v = venues.get(id);
            if (v != null && !f.isBlank()) v.reservarFecha(java.time.LocalDate.parse(f));
        }

        // eventos
        Map<String, Evento> eventosById = new LinkedHashMap<>();
        for (String[] r : Csv.readAll(F_EVENTO, true)) {
            Evento e = Evento.fromCsv(r, venues);
            if (e != null) { eventosById.put(e.getId(), e); Main.eventos.add(e); }
        }

        // localidades
        Map<String, Localidad> locById = new LinkedHashMap<>();
        for (String[] r : Csv.readAll(F_LOCALIDAD, true)) {
            Localidad l = Localidad.fromCsv(r, eventosById);
            if (l != null) { locById.put(l.getId(), l); Main.localidades.add(l); }
        }

        // ofertas
        for (String[] r : Csv.readAll(F_OFERTA, true)) {
            Oferta o = Oferta.fromCsv(r, locById);
            if (o != null && o.localidad != null) o.localidad.agregarOferta(o);
        }

        // tiquetes
        Map<String, Tiquete> tiqById = new LinkedHashMap<>();
        for (String[] r : Csv.readAll(F_TIQ, true)) {
            Tiquete t = Tiquete.fromCsv(r, locById, this::findClienteById);
            if (t != null) { tiqById.put(t.getId(), t); Main.inventario.add(t); }
        }

        // pagos
        Map<Integer, Pago> pagoById = new LinkedHashMap<>();
        int maxPagoId = 0;
        for (String[] r : Csv.readAll(F_PAGO, true)) {
            Pago p = Pago.fromCsv(r);
            if (p != null) {
                pagoById.put(p.getIdPago(), p);
                maxPagoId = Math.max(maxPagoId, p.getIdPago());
            }
        }
        // pago_items
        for (String[] r : Csv.readAll(F_PAGO_ITEMS, true)) {
            int idPago = parseI(s(r,0),0);
            String idT = s(r,1);
            Pago p = pagoById.get(idPago);
            Tiquete t = tiqById.get(idT);
            if (p != null && t != null) p.agregarItem(t);
        }
        Pago.setNextId(maxPagoId+1);
        Main.pagos.addAll(pagoById.values());

        // paquetes 
        Map<String, PaqueteTiquetes> paqById = new LinkedHashMap<>();
        for (String[] r : Csv.readAll(F_PAQ, true)) {
            PaqueteTiquetes paq = PaqueteTiquetes.fromCsv(r);
            if (paq != null) paqById.put(paq.getId(), paq);
        }
        for (String[] r : Csv.readAll(F_PAQ_ITEMS, true)) {
            String idP = s(r,0), idT = s(r,1);
            PaqueteTiquetes p = paqById.get(idP);
            Tiquete t = tiqById.get(idT);
            if (p != null && t != null) p.agregar(t);
        }

        for (Usuario u : Main.usuarios) {
            if (u instanceof Organizador) {
                Organizador org = (Organizador) u;
                org.getEventos().clear();
                org.getEventos().addAll(Main.eventos); // simple: todos los eventos
            }
        }
    }

    // Guardar
    public void saveAll() {
        // admin
        List<String[]> adminRows = new ArrayList<>();
        adminRows.add(Main.admin.toCsv());
        Csv.writeAll(F_ADMIN, "id,login,password,porcentajeServicio,cuotaEmision", adminRows);

        // clientes
        List<String[]> clienteRows = Main.usuarios.stream()
                .filter(u -> u instanceof Cliente && !(u instanceof Organizador))
                .map(u -> ((Cliente)u).toCsv())
                .collect(Collectors.toList());
        Csv.writeAll(F_CLIENTE, "id,login,password,saldo", clienteRows);

        // organizadores
        List<String[]> orgRows = Main.usuarios.stream()
                .filter(u -> u instanceof Organizador)
                .map(u -> ((Organizador)u).toCsv())
                .collect(Collectors.toList());
        Csv.writeAll(F_ORG, "id,login,password", orgRows);

        // venues y fechas
        List<String[]> venueRows = new ArrayList<>();
        List<String[]> venueFechas = new ArrayList<>();
        for (Evento e : Main.eventos) {
            Venue v = e.getVenue();
            if (v != null) {
                venueRows.add(v.toCsv());
                for (LocalDate f : v.getFechasOcupadas()) {
                    venueFechas.add(new String[]{ v.getId(), f.toString() });
                }
            }
        }
        Map<String,String[]> uniqV = new LinkedHashMap<>();
        for (String[] r : venueRows) uniqV.put(r[0], r);
        List<String[]> venueRowsDedup = new ArrayList<>(uniqV.values());
        Csv.writeAll(F_VENUE, "id,nombre,ubicacion,capacidad,restricciones", venueRowsDedup);
        Csv.writeAll(F_VENUE_FECHAS, "venueId,fecha", venueFechas);

       
        List<String[]> eventoRows = Main.eventos.stream()
                .map(Evento::toCsv).collect(Collectors.toList());
        Csv.writeAll(F_EVENTO, "id,nombre,tipo,estado,fechaISO,horaISO,venueId", eventoRows);

        
        List<String[]> locRows = Main.localidades.stream()
                .map(Localidad::toCsv).collect(Collectors.toList());
        Csv.writeAll(F_LOCALIDAD, "id,nombre,precioBase,numerada,aforo,vendidos,eventoId", locRows);

       
        List<String[]> offerRows = new ArrayList<>();
        for (Localidad l : Main.localidades)
            for (Oferta o : l.getOfertas()) offerRows.add(o.toCsv());
        Csv.writeAll(F_OFERTA, "id,descuento,activa,localidadId", offerRows);

       
        List<String[]> tiqRows = Main.inventario.stream()
                .map(Tiquete::toCsv).collect(Collectors.toList());
        Csv.writeAll(F_TIQ, "id,tipo,estado,transferible,precio,asiento,propietarioId,localidadId", tiqRows);


        List<String[]> pagosRows = new ArrayList<>();
        List<String[]> pagoItemsRows = new ArrayList<>();
        for (Pago p : Main.pagos) {
            pagosRows.add(p.toCsv());
            for (Tiquete t : p.getItems()) {
                pagoItemsRows.add(new String[]{ String.valueOf(p.getIdPago()), t.getId() });
            }
        }
        Csv.writeAll(F_PAGO, "idPago,fechaISO,total,metodo,estado", pagosRows);
        Csv.writeAll(F_PAGO_ITEMS, "idPago,tiqueteId", pagoItemsRows);


        List<String[]> paq = new ArrayList<>();
        List<String[]> paqItems = new ArrayList<>();
        Csv.writeAll(F_PAQ, "id,precioTotal,beneficios,tipo,transferible", paq);
        Csv.writeAll(F_PAQ_ITEMS, "paqueteId,tiqueteId", paqItems);
    }

    // helpers
    private Cliente findClienteById(String id){
        for (Usuario u: Main.usuarios)
            if (u instanceof Cliente && u.getId().equals(id)) return (Cliente)u;
        return null;
    }
    private static String s(String[] a,int i){ return i<a.length? a[i]:""; }
    private static int parseI(String x,int d){ try{ return Integer.parseInt(x);}catch(Exception e){return d;} }
}