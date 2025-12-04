package InterfazGrafica;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

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
import Boletamaster.IServicioEventos;
import Boletamaster.Localidad;
import Boletamaster.Main;
import Boletamaster.Organizador;
import Boletamaster.Tiquete;
import Boletamaster.TiqueteNumerado;
import Boletamaster.TiqueteSimple;
import Boletamaster.Usuario;
import Boletamaster.Venue;

public class VentanaOrganizador extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    private JTable tablaEventos;
    private JTable tablaLocalidades;
    private JTable tablaTiquetes;

    private DefaultTableModel modeloEventos;
    private DefaultTableModel modeloLocalidades;
    private DefaultTableModel modeloTiquetes;

    private Organizador organizador;
    private IServicioEventos servicioEventos;

    public VentanaOrganizador(Organizador organizador, IServicioEventos servicioEventos) {
        this.organizador = organizador;
        this.servicioEventos = servicioEventos;

        setTitle("Boletamaster - Organizador (" + organizador.getLogin() + ")");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setBounds(100, 100, 900, 550);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setBackground(new Color(245, 245, 245));
        contentPane.setLayout(new BorderLayout(10, 10));
        setContentPane(contentPane);

        // ========== HEADER ==========
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(Color.WHITE);
        panelHeader.setBorder(new EmptyBorder(10, 20, 10, 20));
        contentPane.add(panelHeader, BorderLayout.NORTH);

        JLabel lblTitulo = new JLabel("Panel de organizador");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        panelHeader.add(lblTitulo, BorderLayout.WEST);

        JLabel lblSubtitulo = new JLabel("Creación y administración de eventos, localidades, ofertas y tiquetes");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitulo.setForeground(new Color(100, 100, 100));
        panelHeader.add(lblSubtitulo, BorderLayout.SOUTH);

        JButton btnVerGanancias = new JButton("Ver mis ganancias");
        btnVerGanancias.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panelHeader.add(btnVerGanancias, BorderLayout.EAST);

        // ========== CENTRO: TABS ==========
        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setOpaque(false);
        contentPane.add(panelCentro, BorderLayout.CENTER);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        panelCentro.add(tabbedPane, BorderLayout.CENTER);

        Font fontTituloTab = new Font("Segoe UI", Font.BOLD, 16);
        Font fontDescTab = new Font("Segoe UI", Font.PLAIN, 12);
        Font fontBoton = new Font("Segoe UI", Font.PLAIN, 13);

        // ================== TAB EVENTOS ==================
        JPanel panelEventos = new JPanel(new BorderLayout(10, 10));
        panelEventos.setBorder(new EmptyBorder(10, 10, 10, 10));
        tabbedPane.addTab("Eventos", null, panelEventos, "Gestión de eventos");

        JPanel panelEventosHeader = new JPanel(new BorderLayout());
        panelEventosHeader.setOpaque(false);
        JLabel lblEventosTitulo = new JLabel("Eventos");
        lblEventosTitulo.setFont(fontTituloTab);
        JLabel lblEventosDesc = new JLabel("Administra los eventos disponibles para la venta de tiquetes.");
        lblEventosDesc.setFont(fontDescTab);
        lblEventosDesc.setForeground(new Color(100, 100, 100));
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
        JButton btnEditarEvento = new JButton("Editar (no implementado)");
        JButton btnEliminarEvento = new JButton("Cancelar (Admin)");
        JButton btnRefrescarEventos = new JButton("Refrescar");

        btnNuevoEvento.setFont(fontBoton);
        btnEditarEvento.setFont(fontBoton);
        btnEliminarEvento.setFont(fontBoton);
        btnRefrescarEventos.setFont(fontBoton);

        panelEventosBotones.add(btnNuevoEvento);
        panelEventosBotones.add(btnEditarEvento);
        panelEventosBotones.add(btnEliminarEvento);
        panelEventosBotones.add(btnRefrescarEventos);

        // ================== TAB LOCALIDADES ==================
        JPanel panelLocalidades = new JPanel(new BorderLayout(10, 10));
        panelLocalidades.setBorder(new EmptyBorder(10, 10, 10, 10));
        tabbedPane.addTab("Localidades", null, panelLocalidades, "Localidades por evento");

        JPanel panelLocHeader = new JPanel(new BorderLayout());
        panelLocHeader.setOpaque(false);
        JLabel lblLocTitulo = new JLabel("Localidades");
        lblLocTitulo.setFont(fontTituloTab);
        JLabel lblLocDesc = new JLabel("Configura las localidades, precios y capacidad para cada evento.");
        lblLocDesc.setFont(fontDescTab);
        lblLocDesc.setForeground(new Color(100, 100, 100));
        panelLocHeader.add(lblLocTitulo, BorderLayout.NORTH);
        panelLocHeader.add(lblLocDesc, BorderLayout.SOUTH);
        panelLocalidades.add(panelLocHeader, BorderLayout.NORTH);

        String[] columnasLocalidades = { "ID", "Evento", "Nombre", "Capacidad", "Precio vigente" };
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
        JButton btnEditarLoc = new JButton("Editar (no implementado)");
        JButton btnCrearOferta = new JButton("Crear oferta");
        JButton btnRefrescarLoc = new JButton("Refrescar");

        btnNuevaLoc.setFont(fontBoton);
        btnEditarLoc.setFont(fontBoton);
        btnCrearOferta.setFont(fontBoton);
        btnRefrescarLoc.setFont(fontBoton);

        panelLocBotones.add(btnNuevaLoc);
        panelLocBotones.add(btnEditarLoc);
        panelLocBotones.add(btnCrearOferta);
        panelLocBotones.add(btnRefrescarLoc);

        // ================== TAB TIQUETES ==================
        JPanel panelTiquetes = new JPanel(new BorderLayout(10, 10));
        panelTiquetes.setBorder(new EmptyBorder(10, 10, 10, 10));
        tabbedPane.addTab("Tiquetes", null, panelTiquetes, "Control de tiquetes generados");

        JPanel panelTiqHeader = new JPanel(new BorderLayout());
        panelTiqHeader.setOpaque(false);
        JLabel lblTiqTitulo = new JLabel("Tiquetes");
        lblTiqTitulo.setFont(fontTituloTab);
        JLabel lblTiqDesc = new JLabel("Consulta los tiquetes generados y su estado.");
        lblTiqDesc.setFont(fontDescTab);
        lblTiqDesc.setForeground(new Color(100, 100, 100));
        panelTiqHeader.add(lblTiqTitulo, BorderLayout.NORTH);
        panelTiqHeader.add(lblTiqDesc, BorderLayout.SOUTH);
        panelTiquetes.add(panelTiqHeader, BorderLayout.NORTH);

        String[] columnasTiq = { "ID", "Evento", "Cliente", "Localidad", "Asiento", "Estado" };
        modeloTiquetes = new DefaultTableModel(columnasTiq, 0);
        tablaTiquetes = new JTable(modeloTiquetes);
        tablaTiquetes.setFillsViewportHeight(true);
        JScrollPane scrollTiquetes = new JScrollPane(tablaTiquetes);
        scrollTiquetes.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        panelTiquetes.add(scrollTiquetes, BorderLayout.CENTER);

        JPanel panelTiqBotones = new JPanel();
        panelTiqBotones.setBackground(Color.WHITE);
        panelTiqBotones.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelTiquetes.add(panelTiqBotones, BorderLayout.SOUTH);

        JButton btnNuevoTiq = new JButton("Nuevo tiquete");
        JButton btnVerDetalleTiq = new JButton("Ver detalle");
        JButton btnRefrescarTiq = new JButton("Refrescar");

        btnNuevoTiq.setFont(fontBoton);
        btnVerDetalleTiq.setFont(fontBoton);
        btnRefrescarTiq.setFont(fontBoton);

        panelTiqBotones.add(btnNuevoTiq);
        panelTiqBotones.add(btnVerDetalleTiq);
        panelTiqBotones.add(btnRefrescarTiq);

        // ========== CARGA INICIAL ==========
        cargarEventosEnTabla();
        cargarLocalidadesEnTabla();
        cargarTiquetesEnTabla();

        // ========== ACCIONES ==========

        btnRefrescarEventos.addActionListener(e -> cargarEventosEnTabla());
        btnRefrescarLoc.addActionListener(e -> cargarLocalidadesEnTabla());
        btnRefrescarTiq.addActionListener(e -> cargarTiquetesEnTabla());

        btnNuevoEvento.addActionListener(e -> crearEventoGUI());
        btnNuevaLoc.addActionListener(e -> crearLocalidadGUI());
        btnCrearOferta.addActionListener(e -> crearOfertaGUI());
        btnNuevoTiq.addActionListener(e -> crearTiqueteGUI());

        btnEditarEvento.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "La edición de eventos aún no está implementada.",
                        "Info",
                        JOptionPane.INFORMATION_MESSAGE));

        btnEliminarEvento.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "La cancelación de eventos con reembolso la hace el ADMIN.",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE));

        btnEditarLoc.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "La edición de localidades aún no está implementada.",
                        "Info",
                        JOptionPane.INFORMATION_MESSAGE));

        btnVerDetalleTiq.addActionListener(e -> {
            int fila = tablaTiquetes.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this,
                        "Selecciona un tiquete para ver el detalle.",
                        "Sin selección",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                String id = (String) tablaTiquetes.getValueAt(fila, 0);
                JOptionPane.showMessageDialog(this,
                        "Aquí podríamos mostrar un detalle más completo del tiquete ID: " + id,
                        "Detalle de tiquete",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnVerGanancias.addActionListener(e -> {
            Object g = organizador.verGanancias(); // puede ser double o String
            JOptionPane.showMessageDialog(this,
                    "Tus ganancias: " + g,
                    "Ganancias",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }

    // ================== CARGA DE TABLAS ==================

    private void cargarEventosEnTabla() {
        modeloEventos.setRowCount(0);

        if (servicioEventos == null) return;
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
            String evNombre = "-";
            if (l.getEvento() != null) {
                evNombre = l.getEvento().getNombre();
            }
            Object[] fila = {
                    l.getId(),
                    evNombre,
                    l.getNombre(),
                    l.getAforo(),
                    (int) l.getPrecioVigente()
            };
            modeloLocalidades.addRow(fila);
        }
    }

    private void cargarTiquetesEnTabla() {
        modeloTiquetes.setRowCount(0);

        for (Tiquete t : Main.inventario) {
            String evNombre = "-";
            String locNombre = "-";
            String asiento = "-";
            String clienteLogin = "-";

            if (t.getLocalidad() != null) {
                Localidad l = t.getLocalidad();
                locNombre = l.getNombre();
                if (l.getEvento() != null) {
                    evNombre = l.getEvento().getNombre();
                }
            }
            if (t instanceof TiqueteNumerado) {
                asiento = ((TiqueteNumerado) t).getAsiento();
            }
            if (t.getPropietario() != null) {
                Usuario u = t.getPropietario();
                clienteLogin = u.getLogin();
            }

            Object[] fila = {
                    t.getId(),
                    evNombre,
                    clienteLogin,
                    locNombre,
                    asiento,
                    t.getEstado()
            };
            modeloTiquetes.addRow(fila);
        }
    }

    // ================== ACCIONES (equivalentes al menú de consola) ==================

    private void crearEventoGUI() {
        String nombre = JOptionPane.showInputDialog(this,
                "Nombre del evento:",
                "Nuevo evento",
                JOptionPane.PLAIN_MESSAGE);
        if (nombre == null || nombre.trim().isEmpty()) return;

        String tipo = JOptionPane.showInputDialog(this,
                "Tipo (MUSICAL/DEPORTIVO/...):",
                "Nuevo evento",
                JOptionPane.PLAIN_MESSAGE);
        if (tipo == null || tipo.trim().isEmpty()) return;

        String fechaStr = JOptionPane.showInputDialog(this,
                "Fecha (YYYY-MM-DD):",
                "Nuevo evento",
                JOptionPane.PLAIN_MESSAGE);
        if (fechaStr == null || fechaStr.trim().isEmpty()) return;

        String horaStr = JOptionPane.showInputDialog(this,
                "Hora (HH:MM):",
                "Nuevo evento",
                JOptionPane.PLAIN_MESSAGE);
        if (horaStr == null || horaStr.trim().isEmpty()) return;

        LocalDate fecha;
        LocalTime hora;
        try {
            fecha = LocalDate.parse(fechaStr.trim());
            hora = LocalTime.parse(horaStr.trim());
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Formato de fecha u hora inválido.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Venue v = new Venue("V" + System.currentTimeMillis(), "Venue-" + nombre, "Ciudad", 5000);
        String idEvento = "E" + (Main.eventos.size() + 1);

        Evento e = organizador.crearEvento(idEvento, nombre.trim(), v, fecha, hora, tipo.trim());
        if (e != null) {
            Main.eventos.add(e);
            guardarCambios();
            cargarEventosEnTabla();
            JOptionPane.showMessageDialog(this,
                    "Evento creado correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo crear el evento (reglas del modelo).",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crearLocalidadGUI() {
        if (Main.eventos.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay eventos. Crea primero un evento.",
                    "Sin eventos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int fila = tablaEventos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Debes seleccionar un evento en la pestaña 'Eventos'.",
                    "Sin selección",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idEvento = (String) tablaEventos.getValueAt(fila, 0);
        Evento e = buscarEventoPorId(idEvento);
        if (e == null) {
            JOptionPane.showMessageDialog(this,
                    "No se encontró el evento seleccionado.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nombreLoc = JOptionPane.showInputDialog(this,
                "Nombre de la localidad:",
                "Nueva localidad",
                JOptionPane.PLAIN_MESSAGE);
        if (nombreLoc == null || nombreLoc.trim().isEmpty()) return;

        String precioStr = JOptionPane.showInputDialog(this,
                "Precio base:",
                "Nueva localidad",
                JOptionPane.PLAIN_MESSAGE);
        if (precioStr == null || precioStr.trim().isEmpty()) return;

        String aforoStr = JOptionPane.showInputDialog(this,
                "Aforo (capacidad):",
                "Nueva localidad",
                JOptionPane.PLAIN_MESSAGE);
        if (aforoStr == null || aforoStr.trim().isEmpty()) return;

        int opcionNum = JOptionPane.showConfirmDialog(this,
                "¿Es numerada? (asientos individuales)",
                "Nueva localidad",
                JOptionPane.YES_NO_OPTION);
        boolean numerada = (opcionNum == JOptionPane.YES_OPTION);

        double precio;
        int aforo;
        try {
            precio = Double.parseDouble(precioStr.trim());
            aforo = Integer.parseInt(aforoStr.trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Precio o aforo inválidos.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String idLoc = "L" + (Main.localidades.size() + 1);
        Localidad l = organizador.crearLocalidad(e, idLoc, nombreLoc.trim(), precio, numerada, aforo);
        if (l != null) {
            Main.localidades.add(l);
            guardarCambios();
            cargarLocalidadesEnTabla();
            JOptionPane.showMessageDialog(this,
                    "Localidad creada correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo crear la localidad (reglas del modelo).",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crearOfertaGUI() {
        if (Main.localidades.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay localidades. Crea primero una localidad.",
                    "Sin localidades",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int fila = tablaLocalidades.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Debes seleccionar una localidad en la pestaña 'Localidades'.",
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
                "Descuento (0.0 a 1.0):",
                "Crear oferta",
                JOptionPane.PLAIN_MESSAGE);
        if (descStr == null || descStr.trim().isEmpty()) return;

        double d;
        try {
            d = Double.parseDouble(descStr.trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Valor de descuento inválido.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        organizador.crearOferta(l, "OF" + System.currentTimeMillis(), d);
        guardarCambios();
        JOptionPane.showMessageDialog(this,
                "Oferta creada y asociada a la localidad.",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void crearTiqueteGUI() {
        if (Main.localidades.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay localidades. Crea primero una localidad.",
                    "Sin localidades",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int fila = tablaLocalidades.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Debes seleccionar una localidad en la pestaña 'Localidades'.",
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

        double precio = l.getPrecioVigente();
        String idTiq = "T" + (Main.inventario.size() + 1);

        boolean numerada = l.isNumerada(); // asumiendo que tienes este método

        Tiquete nuevo;
        if (numerada) {
            String asiento = JOptionPane.showInputDialog(this,
                    "Asiento (ejemplo A1):",
                    "Nuevo tiquete numerado",
                    JOptionPane.PLAIN_MESSAGE);
            if (asiento == null || asiento.trim().isEmpty()) return;
            nuevo = new TiqueteNumerado(idTiq, precio, l, asiento.trim());
        } else {
            nuevo = new TiqueteSimple(idTiq, precio, l);
        }

        Main.inventario.add(nuevo);
        guardarCambios();
        cargarTiquetesEnTabla();

        JOptionPane.showMessageDialog(this,
                "Tiquete creado: " + idTiq,
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // ================== HELPERS BACKEND ==================

    private void guardarCambios() {
        DataStore ds = new DataStore();
        ds.saveAll();
    }

    private Evento buscarEventoPorId(String id) {
        for (Evento e : Main.eventos) {
            if (e.getId().equalsIgnoreCase(id)) return e;
        }
        return null;
    }

    private Localidad buscarLocalidadPorId(String id) {
        for (Localidad l : Main.localidades) {
            if (l.getId().equalsIgnoreCase(id)) return l;
        }
        return null;
    }
}
