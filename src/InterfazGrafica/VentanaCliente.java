package InterfazGrafica;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
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

import Boletamaster.Cliente;
import Boletamaster.DataStore;
import Boletamaster.Evento;
import Boletamaster.IServicioEventos;
import Boletamaster.Localidad;
import Boletamaster.Main;
import Boletamaster.Pago;
import Boletamaster.Tiquete;
import Boletamaster.TiqueteNumerado;
import Boletamaster.Usuario;

public class VentanaCliente extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;

    private JTable tablaEventosDisponibles;
    private JTable tablaInventario;
    private JTable tablaMisTiquetes;

    private DefaultTableModel modeloEventos;
    private DefaultTableModel modeloInventario;
    private DefaultTableModel modeloTiquetes;

    private Cliente cliente;
    private IServicioEventos servicioEventos;

    private JLabel lblSaldoHeader;

    public VentanaCliente(Cliente cliente, IServicioEventos servicioEventos) {
        this.cliente = cliente;
        this.servicioEventos = servicioEventos;

        setTitle("Boletamaster - Cliente");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setBounds(100, 100, 900, 550);

        // ---------- CONTENEDOR PRINCIPAL ----------
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setBackground(UIUtils.COLOR_BG);
        contentPane.setLayout(new BorderLayout(10, 10));
        setContentPane(contentPane);

        // ================== HEADER ==================
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(UIUtils.COLOR_CARD);
        panelHeader.setBorder(new EmptyBorder(10, 20, 10, 20));
        contentPane.add(panelHeader, BorderLayout.NORTH);

        JLabel lblTitulo = new JLabel("Portal de cliente (" + cliente.getLogin() + ")");
        lblTitulo.setFont(UIUtils.FONT_TITLE);
        panelHeader.add(lblTitulo, BorderLayout.WEST);

        lblSaldoHeader = new JLabel();
        lblSaldoHeader.setFont(UIUtils.FONT_SUBTITLE);
        lblSaldoHeader.setForeground(UIUtils.COLOR_MUTED);
        panelHeader.add(lblSaldoHeader, BorderLayout.SOUTH);

        JButton btnAbonarSaldo = new JButton("Abonar saldo");
        UIUtils.stylePrimaryButton(btnAbonarSaldo);
        panelHeader.add(btnAbonarSaldo, BorderLayout.EAST);

        // ================== CENTRO: TABS ==================
        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setOpaque(false);
        contentPane.add(panelCentro, BorderLayout.CENTER);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        panelCentro.add(tabbedPane, BorderLayout.CENTER);

        // ---------- TAB 1: EVENTOS DISPONIBLES ----------
        JPanel panelEventos = new JPanel(new BorderLayout(10, 10));
        panelEventos.setBackground(UIUtils.COLOR_CARD);
        panelEventos.setBorder(UIUtils.softCardBorder());
        tabbedPane.addTab("Eventos disponibles", null, panelEventos,
                "Eventos a los que puedes comprar tiquetes");

        JPanel headerEventos = UIUtils.createHeader(
                "Eventos disponibles",
                "Selecciona un evento para ver detalles o comprar tiquetes por ID.");
        panelEventos.add(headerEventos, BorderLayout.NORTH);

        String[] columnasEventos = { "ID", "Nombre", "Fecha", "Ciudad", "Estado" };
        modeloEventos = new DefaultTableModel(columnasEventos, 0);
        tablaEventosDisponibles = new JTable(modeloEventos);
        tablaEventosDisponibles.setFillsViewportHeight(true);
        UIUtils.stylizeTable(tablaEventosDisponibles);

        JScrollPane scrollEventos = new JScrollPane(tablaEventosDisponibles);
        scrollEventos.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        panelEventos.add(scrollEventos, BorderLayout.CENTER);

        JPanel panelEventosBotones = new JPanel();
        panelEventosBotones.setOpaque(false);
        panelEventosBotones.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelEventos.add(panelEventosBotones, BorderLayout.SOUTH);

        JButton btnVerDetalleEvento = new JButton("Ver detalle");
        JButton btnComprar = new JButton("Comprar tiquetes (por ID)");
        JButton btnRefrescarEventos = new JButton("Refrescar");

        UIUtils.styleSecondaryButton(btnVerDetalleEvento);
        UIUtils.stylePrimaryButton(btnComprar);
        UIUtils.styleSecondaryButton(btnRefrescarEventos);

        panelEventosBotones.add(btnVerDetalleEvento);
        panelEventosBotones.add(btnComprar);
        panelEventosBotones.add(btnRefrescarEventos);

        // ---------- TAB 2: INVENTARIO ----------
        JPanel panelInventario = new JPanel(new BorderLayout(10, 10));
        panelInventario.setBackground(UIUtils.COLOR_CARD);
        panelInventario.setBorder(UIUtils.softCardBorder());
        tabbedPane.addTab("Inventario", null, panelInventario,
                "Tiquetes disponibles en el sistema");

        JPanel headerInv = UIUtils.createHeader(
                "Inventario de tiquetes",
                "Aquí se listan los tiquetes disponibles (como en 'verInventario()').");
        panelInventario.add(headerInv, BorderLayout.NORTH);

        String[] columnasInv = { "ID", "Estado", "Evento", "Asiento", "Precio base", "Total con tarifas" };
        modeloInventario = new DefaultTableModel(columnasInv, 0);
        tablaInventario = new JTable(modeloInventario);
        tablaInventario.setFillsViewportHeight(true);
        UIUtils.stylizeTable(tablaInventario);

        JScrollPane scrollInv = new JScrollPane(tablaInventario);
        scrollInv.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        panelInventario.add(scrollInv, BorderLayout.CENTER);

        JPanel panelInvBotones = new JPanel();
        panelInvBotones.setOpaque(false);
        panelInvBotones.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelInventario.add(panelInvBotones, BorderLayout.SOUTH);

        JButton btnRefrescarInv = new JButton("Refrescar inventario");
        UIUtils.styleSecondaryButton(btnRefrescarInv);
        panelInvBotones.add(btnRefrescarInv);

        // ---------- TAB 3: MIS TIQUETES ----------
        JPanel panelMisTiquetes = new JPanel(new BorderLayout(10, 10));
        panelMisTiquetes.setBackground(UIUtils.COLOR_CARD);
        panelMisTiquetes.setBorder(UIUtils.softCardBorder());
        tabbedPane.addTab("Mis tiquetes", null, panelMisTiquetes,
                "Tiquetes comprados");

        JPanel headerMisTiq = UIUtils.createHeader(
                "Mis tiquetes",
                "Consulta y gestiona tus tiquetes. Puedes transferirlos a otro cliente.");
        panelMisTiquetes.add(headerMisTiq, BorderLayout.NORTH);

        String[] columnasTiquetes = { "ID", "Evento", "Fecha", "Localidad", "Estado" };
        modeloTiquetes = new DefaultTableModel(columnasTiquetes, 0);
        tablaMisTiquetes = new JTable(modeloTiquetes);
        tablaMisTiquetes.setFillsViewportHeight(true);
        UIUtils.stylizeTable(tablaMisTiquetes);

        JScrollPane scrollTiq = new JScrollPane(tablaMisTiquetes);
        scrollTiq.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        panelMisTiquetes.add(scrollTiq, BorderLayout.CENTER);

        JPanel panelMisTiqBotones = new JPanel();
        panelMisTiqBotones.setOpaque(false);
        panelMisTiqBotones.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelMisTiquetes.add(panelMisTiqBotones, BorderLayout.SOUTH);

        JButton btnVerTiq = new JButton("Ver / Imprimir");
        JButton btnTransferirTiq = new JButton("Transferir tiquete");
        JButton btnRefrescarTiq = new JButton("Refrescar");

        UIUtils.stylePrimaryButton(btnVerTiq);
        UIUtils.styleSecondaryButton(btnTransferirTiq);
        UIUtils.styleSecondaryButton(btnRefrescarTiq);

        panelMisTiqBotones.add(btnVerTiq);
        panelMisTiqBotones.add(btnTransferirTiq);
        panelMisTiqBotones.add(btnRefrescarTiq);

        // ---------- TAB 4: PERFIL (PLACEHOLDER) ----------
        
/*
        JPanel panelPerfil = new JPanel(new BorderLayout(10, 10));
        panelPerfil.setBackground(UIUtils.COLOR_CARD);
        panelPerfil.setBorder(UIUtils.softCardBorder());
        tabbedPane.addTab("Mi perfil", null, panelPerfil, "Datos básicos del cliente");

        JPanel headerPerfil = UIUtils.createHeader(
                "Mi perfil",
                "Aquí irán los datos del cliente y opciones para actualizar información (pendiente).");
        panelPerfil.add(headerPerfil, BorderLayout.NORTH);

        JPanel centroPerfil = new JPanel();
        centroPerfil.setOpaque(false);
        centroPerfil.setBorder(new EmptyBorder(30, 40, 30, 40));
        JLabel lblPerfil = new JLabel(
                "Aquí irán los datos del cliente y opciones para actualizar información.");
        lblPerfil.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblPerfil.setForeground(new Color(80, 80, 80));
        centroPerfil.add(lblPerfil);
        panelPerfil.add(centroPerfil, BorderLayout.CENTER);
*/
        

        // ================== CARGA INICIAL ==================
        actualizarSaldoHeader();
        cargarEventosDisponibles();
        cargarInventario();
        cargarMisTiquetes();

        // ================== ACCIONES ==================
        btnAbonarSaldo.addActionListener(e -> abonarSaldo());

        btnRefrescarEventos.addActionListener(e -> cargarEventosDisponibles());
        btnRefrescarInv.addActionListener(e -> cargarInventario());
        btnRefrescarTiq.addActionListener(e -> cargarMisTiquetes());

        btnVerDetalleEvento.addActionListener(e -> verDetalleEvento());
        btnComprar.addActionListener(e -> comprarTiquetesPorId());
        btnVerTiq.addActionListener(e -> verOImprimirTiquete());
        btnTransferirTiq.addActionListener(e -> transferirTiqueteSeleccionado());
    }

    // ================== CARGA DE DATOS ==================

    private void actualizarSaldoHeader() {
        lblSaldoHeader.setText("Saldo actual: $" + (int) cliente.getSaldo());
    }

    private void cargarEventosDisponibles() {
        modeloEventos.setRowCount(0);

        List<Evento> eventos;
        if (servicioEventos != null) {
            eventos = servicioEventos.listarEventos();
        } else {
            eventos = Main.eventos;
        }

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

    private void cargarInventario() {
        modeloInventario.setRowCount(0);

        for (Tiquete t : Main.inventario) {
            String evNombre = "-";
            String asiento = "-";

            if (t.getLocalidad() != null && t.getLocalidad().getEvento() != null) {
                evNombre = t.getLocalidad().getEvento().getNombre();
            }
            if (t instanceof TiqueteNumerado) {
                asiento = ((TiqueteNumerado) t).getAsiento();
            }

            double base = t.getPrecio();
            double total = t.calcularPrecioTotal(Main.admin);

            Object[] fila = {
                    t.getId(),
                    t.getEstado(),
                    evNombre,
                    asiento,
                    (int) base,
                    (int) total
            };
            modeloInventario.addRow(fila);
        }
    }

    private void cargarMisTiquetes() {
        modeloTiquetes.setRowCount(0);

        for (Tiquete t : cliente.getTiquetes()) {
            String eventoNombre = "-";
            String fecha = "-";
            String nomLoc = "-";

            if (t.getLocalidad() != null) {
                Localidad l = t.getLocalidad();
                nomLoc = l.getNombre();
                if (l.getEvento() != null) {
                    Evento ev = l.getEvento();
                    eventoNombre = ev.getNombre();
                    if (ev.getFecha() != null) {
                        fecha = ev.getFecha().toString();
                    }
                }
            }

            Object[] fila = {
                    t.getId(),
                    eventoNombre,
                    fecha,
                    nomLoc,
                    t.getEstado()
            };
            modeloTiquetes.addRow(fila);
        }
    }

    // ================== ACCIONES ==================

    private void verDetalleEvento() {
        int fila = tablaEventosDisponibles.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Debes seleccionar un evento para ver el detalle.",
                    "Sin selección",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = (String) tablaEventosDisponibles.getValueAt(fila, 0);
        JOptionPane.showMessageDialog(this,
                "Aquí podrías mostrar el detalle del evento con ID: " + id,
                "Detalle de evento",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void abonarSaldo() {
        String input = JOptionPane.showInputDialog(this,
                "Valor a abonar:",
                "Abonar saldo",
                JOptionPane.PLAIN_MESSAGE);
        if (input == null) return;

        double v;
        try {
            v = Double.parseDouble(input.trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Valor inválido.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (v <= 0) {
            JOptionPane.showMessageDialog(this,
                    "El valor debe ser mayor que cero.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        cliente.abonarSaldo(v);
        guardarCambios();
        actualizarSaldoHeader();

        JOptionPane.showMessageDialog(this,
                "Nuevo saldo: $" + (int) cliente.getSaldo(),
                "Saldo actualizado",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void comprarTiquetesPorId() {
        String input = JOptionPane.showInputDialog(this,
                "IDs de tiquetes separados por coma (ej: T1,T2,T3):",
                "Comprar tiquetes",
                JOptionPane.PLAIN_MESSAGE);
        if (input == null || input.trim().isEmpty()) return;

        String[] partes = input.split(",");
        ArrayList<Tiquete> items = new ArrayList<>();

        for (String s : partes) {
            String id = s.trim();
            if (id.isEmpty()) continue;
            Tiquete t = buscarTiquetePorId(id);
            if (t != null) {
                items.add(t);
            }
        }

        if (items.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No se encontró ningún tiquete con esos IDs.",
                    "Sin tiquetes",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Pago p = cliente.comprarTiquetes(items, Main.admin);
        if (p == null) {
            JOptionPane.showMessageDialog(this,
                    "Compra rechazada (saldo insuficiente o problemas de disponibilidad).",
                    "Compra rechazada",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            Main.pagos.add(p);
            guardarCambios();
            actualizarSaldoHeader();
            cargarInventario();
            cargarMisTiquetes();

            JOptionPane.showMessageDialog(this,
                    "Compra aprobada. Total: $" + (int) p.getTotal(),
                    "Compra exitosa",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void verOImprimirTiquete() {
        int fila = tablaMisTiquetes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Debes seleccionar un tiquete para ver/imprimir.",
                    "Sin selección",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idTiq = (String) tablaMisTiquetes.getValueAt(fila, 0);
        Tiquete tiq = buscarTiqueteDelCliente(idTiq);
        if (tiq == null) {
            JOptionPane.showMessageDialog(this,
                    "No se encontró el tiquete seleccionado.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        DialogoImprimirTiquete dlg = new DialogoImprimirTiquete(this, tiq);
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);

        // Después de cerrar el diálogo recargamos info (por si se marcó como impreso)
        cargarMisTiquetes();
        cargarInventario();
    }

    private void transferirTiqueteSeleccionado() {
        int fila = tablaMisTiquetes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Debes seleccionar un tiquete para transferir.",
                    "Sin selección",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idTiq = (String) tablaMisTiquetes.getValueAt(fila, 0);
        Tiquete elegido = buscarTiqueteDelCliente(idTiq);
        if (elegido == null) {
            JOptionPane.showMessageDialog(this,
                    "No se encontró el tiquete seleccionado en tu lista.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String loginDestino = JOptionPane.showInputDialog(this,
                "Login del cliente destino:",
                "Transferir tiquete",
                JOptionPane.PLAIN_MESSAGE);
        if (loginDestino == null || loginDestino.trim().isEmpty()) return;

        Cliente destino = buscarClientePorLogin(loginDestino.trim());
        if (destino == null) {
            JOptionPane.showMessageDialog(this,
                    "No existe un cliente con ese login.",
                    "Destino inválido",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String pass = JOptionPane.showInputDialog(this,
                "Confirma tu contraseña:",
                "Confirmar transferencia",
                JOptionPane.PLAIN_MESSAGE);
        if (pass == null) return;

        boolean ok = cliente.transferirTiquete(elegido, destino, pass);
        if (ok) {
            guardarCambios();
            cargarMisTiquetes();
            cargarInventario();
            JOptionPane.showMessageDialog(this,
                    "Transferencia exitosa.",
                    "Transferencia",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Falló la transferencia (contraseña incorrecta o reglas del modelo).",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // ================== HELPERS BACKEND ==================

    private void guardarCambios() {
        DataStore ds = new DataStore();
        ds.saveAll();
    }

    private Tiquete buscarTiquetePorId(String id) {
        for (Tiquete t : Main.inventario) {
            if (t.getId().equalsIgnoreCase(id)) return t;
        }
        return null;
    }

    private Tiquete buscarTiqueteDelCliente(String id) {
        for (Tiquete t : cliente.getTiquetes()) {
            if (t.getId().equalsIgnoreCase(id)) {
                return t;
            }
        }
        return null;
    }

    private Cliente buscarClientePorLogin(String login) {
        for (Usuario u : Main.usuarios) {
            if (u instanceof Cliente && u.getLogin().equals(login)) {
                return (Cliente) u;
            }
        }
        return null;
    }
}
