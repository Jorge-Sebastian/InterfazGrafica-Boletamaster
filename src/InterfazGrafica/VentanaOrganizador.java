package InterfazGrafica;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Boletamaster.Administrador;
import Boletamaster.DataStore;
import Boletamaster.Evento;
import Boletamaster.Localidad;
import Boletamaster.Main;
import Boletamaster.Organizador;
import Boletamaster.ServicioEventosCSV;
import Boletamaster.Tiquete;
import Boletamaster.TiqueteNumerado;
import Boletamaster.TiqueteSimple;
import Boletamaster.IServicioEventos;
import Boletamaster.Cliente;
import Boletamaster.Usuario;

import java.util.List;

public class VentanaOrganizador extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    private JTable tablaEventos;
    private JTable tablaLocalidades;
    private JTable tablaTiquetes;

    private DefaultTableModel modeloEventos;
    private DefaultTableModel modeloLocalidades;
    private DefaultTableModel modeloTiquetes;

    private IServicioEventos servicioEventos;
    private Organizador organizador;

    // ================== CONSTRUCTORES ==================

    /** Constructor usado cuando ya tenemos el organizador logueado */
    public VentanaOrganizador(Organizador organizador) {
        this.organizador = organizador;
        this.servicioEventos = new ServicioEventosCSV(organizador);
        initUI();
        cargarEventosEnTabla();
        cargarLocalidadesEnTabla();
        cargarTiquetesEnTabla();
    }

    /** Constructor de compatibilidad (si lo llamas sin login solo podrás ver datos) */
    public VentanaOrganizador(IServicioEventos servicioEventos) {
        this.servicioEventos = servicioEventos;
        this.organizador = null; // sin organizador, solo lectura
        initUI();
        cargarEventosEnTabla();
        cargarLocalidadesEnTabla();
        cargarTiquetesEnTabla();
    }

    // ================== UI ==================

    private void initUI() {
        setTitle("Boletamaster - Organizador");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setBounds(100, 100, 900, 550);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setBackground(new Color(245, 245, 245));
        contentPane.setLayout(new BorderLayout(10, 10));
        setContentPane(contentPane);

        // ---------- HEADER ----------
        JPanel panelHeader = new JPanel();
        panelHeader.setBackground(Color.WHITE);
        panelHeader.setBorder(new EmptyBorder(10, 20, 10, 20));
        panelHeader.setLayout(new BorderLayout());
        contentPane.add(panelHeader, BorderLayout.NORTH);

        JLabel lblTitulo = new JLabel("Panel de organizador");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        panelHeader.add(lblTitulo, BorderLayout.WEST);

        JLabel lblSubtitulo = new JLabel("Creación y administración de eventos, localidades, ofertas y tiquetes");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitulo.setForeground(new Color(100, 100, 100));
        panelHeader.add(lblSubtitulo, BorderLayout.SOUTH);

        JButton btnVerGanancias = new JButton("Ver ganancias");
        btnVerGanancias.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panelHeader.add(btnVerGanancias, BorderLayout.EAST);

        // ---------- CENTRO ----------
        JPanel panelCentro = new JPanel();
        panelCentro.setOpaque(false);
        panelCentro.setLayout(new BorderLayout());
        contentPane.add(panelCentro, BorderLayout.CENTER);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        panelCentro.add(tabbedPane, BorderLayout.CENTER);

        // ================== PESTAÑA EVENTOS ==================
        JPanel panelEventos = new JPanel(new BorderLayout(10, 10));
        panelEventos.setBorder(new EmptyBorder(10, 10, 10, 10));
        tabbedPane.addTab("Eventos", null, panelEventos, "Gestión de eventos");

        JLabel lblEventosTitulo = new JLabel("Eventos");
        lblEventosTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JLabel lblEventosDesc = new JLabel("Administra los eventos disponibles para la venta de tiquetes.");
        lblEventosDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblEventosDesc.setForeground(new Color(100, 100, 100));

        JPanel panelEventosHeader = new JPanel(new BorderLayout());
        panelEventosHeader.setOpaque(false);
        panelEventosHeader.add(lblEventosTitulo, BorderLayout.NORTH);
        panelEventosHeader.add(lblEventosDesc, BorderLayout.SOUTH);
        panelEventos.add(panelEventosHeader, BorderLayout.NORTH);

        String[] columnasEventos = { "ID", "Nombre", "Fecha", "Ciudad", "Estado" };
        modeloEventos = new DefaultTableModel(columnasEventos, 0);
        tablaEventos = new JTable(modeloEventos);
        tablaEventos.setFillsViewportHeight(true);
        JScrollPane scrollEventos = new JScrollPane(tablaEventos);
        scrollEventos.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        panelEventos.add(scrollEventos, BorderLayout.CENTER);

        JPanel panelEventosBotones = new JPanel();
        panelEventosBotones.setBackground(Color.WHITE);
        panelEventosBotones.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelEventos.add(panelEventosBotones, BorderLayout.SOUTH);

        JButton btnNuevoEvento = new JButton("Nuevo evento");
        JButton btnEditarEvento = new JButton("Editar");
        JButton btnEliminarEvento = new JButton("Eliminar");
        JButton btnRefrescarEventos = new JButton("Refrescar");

        Font fontBoton = new Font("Segoe UI", Font.PLAIN, 13);
        btnNuevoEvento.setFont(fontBoton);
        btnEditarEvento.setFont(fontBoton);
        btnEliminarEvento.setFont(fontBoton);
        btnRefrescarEventos.setFont(fontBoton);

        panelEventosBotones.add(btnNuevoEvento);
        panelEventosBotones.add(btnEditarEvento);
        panelEventosBotones.add(btnEliminarEvento);
        panelEventosBotones.add(btnRefrescarEventos);

        // ================== PESTAÑA LOCALIDADES ==================
        JPanel panelLocalidades = new JPanel(new BorderLayout(10, 10));
        panelLocalidades.setBorder(new EmptyBorder(10, 10, 10, 10));
        tabbedPane.addTab("Localidades", null, panelLocalidades, "Localidades por evento");

        JLabel lblLocTitulo = new JLabel("Localidades");
        lblLocTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JLabel lblLocDesc = new JLabel("Configura localidades, precios, capacidad y ofertas para cada evento.");
        lblLocDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblLocDesc.setForeground(new Color(100, 100, 100));

        JPanel panelLocHeader = new JPanel(new BorderLayout());
        panelLocHeader.setOpaque(false);
        panelLocHeader.add(lblLocTitulo, BorderLayout.NORTH);
        panelLocHeader.add(lblLocDesc, BorderLayout.SOUTH);
        panelLocalidades.add(panelLocHeader, BorderLayout.NORTH);

        String[] columnasLocalidades = { "ID", "Evento", "Nombre localidad", "Capacidad", "Vendidos", "Precio vigente" };
        modeloLocalidades = new DefaultTableModel(columnasLocalidades, 0);
        tablaLocalidades = new JTable(modeloLocalidades);
        tablaLocalidades.setFillsViewportHeight(true);
        JScrollPane scrollLocalidades = new JScrollPane(tablaLocalidades);
        scrollLocalidades.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        panelLocalidades.add(scrollLocalidades, BorderLayout.CENTER);

        JPanel panelLocBotones = new JPanel();
        panelLocBotones.setBackground(Color.WHITE);
        panelLocBotones.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelLocalidades.add(panelLocBotones, BorderLayout.SOUTH);

        JButton btnNuevaLoc = new JButton("Nueva localidad");
        JButton btnEditarLoc = new JButton("Editar");
        JButton btnEliminarLoc = new JButton("Eliminar");
        JButton btnCrearOferta = new JButton("Crear oferta");
        JButton btnCrearTiq = new JButton("Crear tiquete");
        JButton btnRefrescarLoc = new JButton("Refrescar");

        btnNuevaLoc.setFont(fontBoton);
        btnEditarLoc.setFont(fontBoton);
        btnEliminarLoc.setFont(fontBoton);
        btnCrearOferta.setFont(fontBoton);
        btnCrearTiq.setFont(fontBoton);
        btnRefrescarLoc.setFont(fontBoton);

        panelLocBotones.add(btnNuevaLoc);
        panelLocBotones.add(btnEditarLoc);
        panelLocBotones.add(btnEliminarLoc);
        panelLocBotones.add(btnCrearOferta);
        panelLocBotones.add(btnCrearTiq);
        panelLocBotones.add(btnRefrescarLoc);

        // ================== PESTAÑA TIQUETES ==================
        JPanel panelTiquetes = new JPanel(new BorderLayout(10, 10));
        panelTiquetes.setBorder(new EmptyBorder(10, 10, 10, 10));
        tabbedPane.addTab("Tiquetes", null, panelTiquetes, "Control de tiquetes");

        JLabel lblTiqTitulo = new JLabel("Tiquetes");
        lblTiqTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JLabel lblTiqDesc = new JLabel("Consulta los tiquetes generados y su estado.");
        lblTiqDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTiqDesc.setForeground(new Color(100, 100, 100));

        JPanel panelTiqHeader = new JPanel(new BorderLayout());
        panelTiqHeader.setOpaque(false);
        panelTiqHeader.add(lblTiqTitulo, BorderLayout.NORTH);
        panelTiqHeader.add(lblTiqDesc, BorderLayout.SOUTH);
        panelTiquetes.add(panelTiqHeader, BorderLayout.NORTH);

        String[] columnasTiquetes = { "ID", "Evento", "Cliente", "Localidad", "Estado" };
        modeloTiquetes = new DefaultTableModel(columnasTiquetes, 0);
        tablaTiquetes = new JTable(modeloTiquetes);
        tablaTiquetes.setFillsViewportHeight(true);
        JScrollPane scrollTiquetes = new JScrollPane(tablaTiquetes);
        scrollTiquetes.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        panelTiquetes.add(scrollTiquetes, BorderLayout.CENTER);

        JPanel panelTiqBotones = new JPanel();
        panelTiqBotones.setBackground(Color.WHITE);
        panelTiqBotones.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelTiquetes.add(panelTiqBotones, BorderLayout.SOUTH);

        JButton btnVerDetalleTiq = new JButton("Ver detalle");
        JButton btnRefrescarTiq = new JButton("Refrescar");
        btnVerDetalleTiq.setFont(fontBoton);
        btnRefrescarTiq.setFont(fontBoton);

        panelTiqBotones.add(btnVerDetalleTiq);
        panelTiqBotones.add(btnRefrescarTiq);

        // ================== ACCIONES ==================

        // --- Eventos ---
        btnRefrescarEventos.addActionListener(e -> cargarEventosEnTabla());
        btnNuevoEvento.addActionListener(e -> crearEventoGUI());
        btnEditarEvento.addActionListener(e -> editarEventoGUI());
        btnEliminarEvento.addActionListener(e -> eliminarEventoGUI());

        // --- Localidades ---
        btnRefrescarLoc.addActionListener(e -> cargarLocalidadesEnTabla());
        btnNuevaLoc.addActionListener(e -> crearLocalidadGUI());
        btnCrearOferta.addActionListener(e -> crearOfertaGUI());
        btnCrearTiq.addActionListener(e -> crearTiqueteGUI());
        // (Editar / Eliminar localidad: opcionales para el proyecto, se pueden dejar para después)

        // --- Tiquetes ---
        btnRefrescarTiq.addActionListener(e -> cargarTiquetesEnTabla());
        btnVerDetalleTiq.addActionListener(e -> verDetalleTiqueteGUI());

        // --- Ganancias ---
        btnVerGanancias.addActionListener(e -> verGananciasGUI());
    }

    // ================== CARGA DE TABLAS ==================

    private void cargarEventosEnTabla() {
        modeloEventos.setRowCount(0);
        if (servicioEventos == null) {
            servicioEventos = new ServicioEventosCSV(organizador);
        }

        List<Evento> eventos = servicioEventos.listarEventos();
        for (Evento ev : eventos) {
            String ciudad = "-";
            if (ev.getVenue() != null) {
                ciudad = ev.getVenue().getUbicacion();
            }
            Object[] fila = {
                    ev.getId(),
                    ev.getNombre(),
                    ev.getFecha(),
                    ciudad,
                    ev.getEstado()
            };
            modeloEventos.addRow(fila);
        }
    }

    private void cargarLocalidadesEnTabla() {
        modeloLocalidades.setRowCount(0);
        for (Localidad l : Main.localidades) {
            Evento e = l.getEvento();
            String nomEv = (e != null) ? e.getNombre() : "-";
            Object[] fila = {
                    l.getId(),
                    nomEv,
                    l.getNombre(),
                    l.getAforo(),
                    l.getVendidos(),
                    l.getPrecioVigente()
            };
            modeloLocalidades.addRow(fila);
        }
    }

    private void cargarTiquetesEnTabla() {
        modeloTiquetes.setRowCount(0);
        for (Tiquete t : Main.inventario) {
            String ev = "-";
            String loc = "-";
            if (t.getLocalidad() != null) {
                loc = t.getLocalidad().getNombre();
                if (t.getLocalidad().getEvento() != null) {
                    ev = t.getLocalidad().getEvento().getNombre();
                }
            }
            String cliente = (t.getPropietario() != null) ? t.getPropietario().getLogin() : "-";
            Object[] fila = {
                    t.getId(),
                    ev,
                    cliente,
                    loc,
                    t.getEstado()
            };
            modeloTiquetes.addRow(fila);
        }
    }

    // ================== OPERACIONES (EVENTOS) ==================

    private boolean asegurarOrganizador() {
        if (organizador == null) {
            JOptionPane.showMessageDialog(this,
                    "Para esta operación debes estar autenticado como organizador.",
                    "Operación no disponible",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void crearEventoGUI() {
        if (!asegurarOrganizador()) return;

        String nombre = JOptionPane.showInputDialog(this, "Nombre del evento:");
        if (nombre == null || nombre.isBlank()) return;

        String tipo = JOptionPane.showInputDialog(this, "Tipo (MUSICAL/DEPORTIVO/...):");
        if (tipo == null || tipo.isBlank()) return;

        String fechaStr = JOptionPane.showInputDialog(this, "Fecha (YYYY-MM-DD):");
        String horaStr = JOptionPane.showInputDialog(this, "Hora (HH:MM):");
        String ciudad = JOptionPane.showInputDialog(this, "Ciudad del venue:");
        String nombreVenue = JOptionPane.showInputDialog(this, "Nombre del venue:");
        String capacidadStr = JOptionPane.showInputDialog(this, "Capacidad del venue:");

        try {
            LocalDate fecha = LocalDate.parse(fechaStr.trim());
            LocalTime hora = LocalTime.parse(horaStr.trim());
            int capacidad = Integer.parseInt(capacidadStr.trim());

            String idVenue = "V" + System.currentTimeMillis();
            Boletamaster.Venue v = new Boletamaster.Venue(idVenue, nombreVenue, ciudad, capacidad);

            String idEvento = "E" + (Main.eventos.size() + 1);
            Evento e = organizador.crearEvento(idEvento, nombre, v, fecha, hora, tipo);
            if (e == null) {
                JOptionPane.showMessageDialog(this,
                        "No se pudo crear el evento. Puede que el venue ya esté reservado ese día.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            Main.eventos.add(e);
            guardarCambios();
            cargarEventosEnTabla();
            JOptionPane.showMessageDialog(this, "Evento creado correctamente.", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Datos inválidos: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarEventoGUI() {
        // Para el proyecto no es estrictamente necesario.
        JOptionPane.showMessageDialog(this,
                "La edición de eventos se puede implementar luego.\nPor ahora solo creamos y listamos.",
                "Info",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void eliminarEventoGUI() {
        int fila = tablaEventos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Debes seleccionar un evento para eliminarlo.",
                    "Sin selección",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        String id = (String) tablaEventos.getValueAt(fila, 0);

        Evento encontrado = null;
        for (Evento e : Main.eventos) {
            if (e.getId().equals(id)) {
                encontrado = e;
                break;
            }
        }
        if (encontrado == null) return;

        int conf = JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas eliminar el evento '" + encontrado.getNombre() + "'?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if (conf != JOptionPane.YES_OPTION) return;

        Main.eventos.remove(encontrado);
        guardarCambios();
        cargarEventosEnTabla();
    }

    // ================== OPERACIONES (LOCALIDADES / OFERTAS / TIQUETES) ==================

    private Evento buscarEventoPorId(String id) {
        for (Evento e : Main.eventos) {
            if (e.getId().equals(id)) return e;
        }
        return null;
    }

    private Localidad buscarLocalidadPorId(String id) {
        for (Localidad l : Main.localidades) {
            if (l.getId().equals(id)) return l;
        }
        return null;
    }

    private void crearLocalidadGUI() {
        if (!asegurarOrganizador()) return;
        if (Main.eventos.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay eventos. Crea primero un evento.",
                    "Aviso",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int filaEv = tablaEventos.getSelectedRow();
        if (filaEv == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona primero un evento en la pestaña de Eventos.",
                    "Sin selección",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idEvento = (String) tablaEventos.getValueAt(filaEv, 0);
        Evento e = buscarEventoPorId(idEvento);
        if (e == null) {
            JOptionPane.showMessageDialog(this,
                    "No se encontró el evento seleccionado.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nombreLoc = JOptionPane.showInputDialog(this, "Nombre de la localidad:");
        if (nombreLoc == null || nombreLoc.isBlank()) return;

        String precioStr = JOptionPane.showInputDialog(this, "Precio base:");
        String numeradaStr = JOptionPane.showInputDialog(this, "¿Numerada? (s/n):");
        String aforoStr = JOptionPane.showInputDialog(this, "Aforo:");

        try {
            double precio = Double.parseDouble(precioStr.trim());
            boolean numerada = numeradaStr.trim().equalsIgnoreCase("s");
            int aforo = Integer.parseInt(aforoStr.trim());

            String idLoc = "L" + (Main.localidades.size() + 1);
            Localidad l = organizador.crearLocalidad(e, idLoc, nombreLoc, precio, numerada, aforo);
            Main.localidades.add(l);
            guardarCambios();
            cargarLocalidadesEnTabla();
            JOptionPane.showMessageDialog(this,
                    "Localidad creada correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Datos inválidos.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crearOfertaGUI() {
        if (!asegurarOrganizador()) return;

        int fila = tablaLocalidades.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Debes seleccionar una localidad para crear una oferta.",
                    "Sin selección",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idLoc = (String) tablaLocalidades.getValueAt(fila, 0);
        Localidad l = buscarLocalidadPorId(idLoc);
        if (l == null) {
            JOptionPane.showMessageDialog(this,
                    "No se encontró la localidad seleccionada.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String descStr = JOptionPane.showInputDialog(this,
                "Descuento (0.0 a 1.0, por ejemplo 0.10 para 10%):");
        if (descStr == null) return;

        try {
            double d = Double.parseDouble(descStr.trim());
            if (d < 0 || d > 1) throw new IllegalArgumentException();

            String idOferta = "OF" + System.currentTimeMillis();
            organizador.crearOferta(l, idOferta, d);
            guardarCambios();
            cargarLocalidadesEnTabla();

            JOptionPane.showMessageDialog(this,
                    "Oferta creada y aplicada a la localidad.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Descuento inválido. Debe estar entre 0.0 y 1.0.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crearTiqueteGUI() {
        if (!asegurarOrganizador()) return;

        int fila = tablaLocalidades.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona primero una localidad en la pestaña Localidades.",
                    "Sin selección",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idLoc = (String) tablaLocalidades.getValueAt(fila, 0);
        Localidad l = buscarLocalidadPorId(idLoc);
        if (l == null) {
            JOptionPane.showMessageDialog(this,
                    "No se encontró la localidad seleccionada.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String numeradaStr = JOptionPane.showInputDialog(this, "¿Tiquete numerado? (s/n):");
        if (numeradaStr == null) return;
        boolean num = numeradaStr.trim().equalsIgnoreCase("s");

        String idTiq = "T" + (Main.inventario.size() + 1);
        double precio = l.getPrecioVigente();

        Tiquete t;
        if (num) {
            String asiento = JOptionPane.showInputDialog(this, "Asiento (ej: A1):");
            if (asiento == null || asiento.isBlank()) return;
            t = new TiqueteNumerado(idTiq, precio, l, asiento.trim());
        } else {
            t = new TiqueteSimple(idTiq, precio, l);
        }

        Main.inventario.add(t);
        guardarCambios();
        cargarTiquetesEnTabla();

        JOptionPane.showMessageDialog(this,
                "Tiquete creado: " + idTiq,
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void verDetalleTiqueteGUI() {
        int fila = tablaTiquetes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona un tiquete.",
                    "Sin selección",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = (String) tablaTiquetes.getValueAt(fila, 0);
        Tiquete encontrado = null;
        for (Tiquete t : Main.inventario) {
            if (t.getId().equals(id)) {
                encontrado = t;
                break;
            }
        }
        if (encontrado == null) return;

        String ev = "-";
        String loc = "-";
        if (encontrado.getLocalidad() != null) {
            loc = encontrado.getLocalidad().getNombre();
            if (encontrado.getLocalidad().getEvento() != null) {
                ev = encontrado.getLocalidad().getEvento().getNombre();
            }
        }
        String cliente = (encontrado.getPropietario() != null)
                ? encontrado.getPropietario().getLogin()
                : "(sin comprador)";

        String msg = String.format(
                "ID: %s%nEvento: %s%nLocalidad: %s%nCliente: %s%nEstado: %s%nPrecio base: %.0f",
                encontrado.getId(),
                ev,
                loc,
                cliente,
                encontrado.getEstado(),
                encontrado.getPrecio()
        );
        JOptionPane.showMessageDialog(this, msg, "Detalle del tiquete",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // ================== GANANCIAS ==================

    private void verGananciasGUI() {
        if (!asegurarOrganizador()) return;
        String texto = organizador.verGanancias();
        JOptionPane.showMessageDialog(this,
                texto,
                "Ganancias del organizador",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // ================== PERSISTENCIA ==================

    private void guardarCambios() {
        DataStore ds = new DataStore();
        ds.saveAll();
    }
}
