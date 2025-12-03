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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;

import Boletamaster.*;

public class VentanaCliente extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    private JTable tablaEventosDisponibles;
    private JTable tablaMisTiquetes;
    private JTable tablaInventario;

    private DefaultTableModel modeloEventos;
    private DefaultTableModel modeloTiquetes;
    private DefaultTableModel modeloInventario;

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

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setBackground(new Color(245, 245, 245));
        contentPane.setLayout(new BorderLayout(10, 10));
        setContentPane(contentPane);

        // ========== HEADER ==========
        JPanel panelHeader = new JPanel();
        panelHeader.setBackground(Color.WHITE);
        panelHeader.setBorder(new EmptyBorder(10, 20, 10, 20));
        panelHeader.setLayout(new BorderLayout());
        contentPane.add(panelHeader, BorderLayout.NORTH);

        JLabel lblTitulo = new JLabel("Portal de cliente (" + cliente.getLogin() + ")");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        panelHeader.add(lblTitulo, BorderLayout.WEST);

        lblSaldoHeader = new JLabel();
        lblSaldoHeader.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSaldoHeader.setForeground(new Color(100, 100, 100));
        panelHeader.add(lblSaldoHeader, BorderLayout.SOUTH);

        JButton btnAbonarSaldo = new JButton("Abonar saldo");
        btnAbonarSaldo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panelHeader.add(btnAbonarSaldo, BorderLayout.EAST);

        // ========== CENTRO: JTabbedPane ==========
        JPanel panelCentro = new JPanel();
        panelCentro.setOpaque(false);
        panelCentro.setLayout(new BorderLayout());
        contentPane.add(panelCentro, BorderLayout.CENTER);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        panelCentro.add(tabbedPane, BorderLayout.CENTER);

        Font fontTituloTab = new Font("Segoe UI", Font.BOLD, 16);
        Font fontDescTab = new Font("Segoe UI", Font.PLAIN, 12);
        Font fontBoton = new Font("Segoe UI", Font.PLAIN, 13);

        // ================== PESTAÑA: EVENTOS DISPONIBLES ==================
        JPanel panelEventos = new JPanel(new BorderLayout(10, 10));
        panelEventos.setBorder(new EmptyBorder(10, 10, 10, 10));
        tabbedPane.addTab("Eventos disponibles", null, panelEventos, "Eventos a los que puedes comprar tiquetes");

        JPanel panelEventosHeader = new JPanel(new BorderLayout());
        panelEventosHeader.setOpaque(false);
        JLabel lblEventosTitulo = new JLabel("Eventos disponibles");
        lblEventosTitulo.setFont(fontTituloTab);
        JLabel lblEventosDesc = new JLabel("Selecciona un evento para ver detalles o comprar tiquetes por ID.");
        lblEventosDesc.setFont(fontDescTab);
        lblEventosDesc.setForeground(new Color(100, 100, 100));
        panelEventosHeader.add(lblEventosTitulo, BorderLayout.NORTH);
        panelEventosHeader.add(lblEventosDesc, BorderLayout.SOUTH);
        panelEventos.add(panelEventosHeader, BorderLayout.NORTH);

        String[] columnasEventos = { "ID", "Nombre", "Fecha", "Ciudad", "Estado" };
        modeloEventos = new DefaultTableModel(columnasEventos, 0);
        tablaEventosDisponibles = new JTable(modeloEventos);
        tablaEventosDisponibles.setFillsViewportHeight(true);
        JScrollPane scrollEventos = new JScrollPane(tablaEventosDisponibles);
        scrollEventos.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        panelEventos.add(scrollEventos, BorderLayout.CENTER);

        JPanel panelEventosBotones = new JPanel();
        panelEventosBotones.setBackground(Color.WHITE);
        panelEventosBotones.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelEventos.add(panelEventosBotones, BorderLayout.SOUTH);

        JButton btnVerDetalleEvento = new JButton("Ver detalle");
        JButton btnComprar = new JButton("Comprar tiquetes (por ID)");
        JButton btnRefrescarEventos = new JButton("Refrescar");

        btnVerDetalleEvento.setFont(fontBoton);
        btnComprar.setFont(fontBoton);
        btnRefrescarEventos.setFont(fontBoton);

        panelEventosBotones.add(btnVerDetalleEvento);
        panelEventosBotones.add(btnComprar);
        panelEventosBotones.add(btnRefrescarEventos);

        // ================== PESTAÑA: INVENTARIO ==================
        JPanel panelInventario = new JPanel(new BorderLayout(10, 10));
        panelInventario.setBorder(new EmptyBorder(10, 10, 10, 10));
        tabbedPane.addTab("Inventario", null, panelInventario, "Tiquetes disponibles en el sistema");

        JPanel panelInvHeader = new JPanel(new BorderLayout());
        panelInvHeader.setOpaque(false);
        JLabel lblInvTitulo = new JLabel("Inventario de tiquetes");
        lblInvTitulo.setFont(fontTituloTab);
        JLabel lblInvDesc = new JLabel("Aquí se listan los tiquetes disponibles (como en 'verInventario()').");
        lblInvDesc.setFont(fontDescTab);
        lblInvDesc.setForeground(new Color(100, 100, 100));
        panelInvHeader.add(lblInvTitulo, BorderLayout.NORTH);
        panelInvHeader.add(lblInvDesc, BorderLayout.SOUTH);
        panelInventario.add(panelInvHeader, BorderLayout.NORTH);

        String[] columnasInv = { "ID", "Estado", "Evento", "Asiento", "Precio base", "Total con tarifas" };
        modeloInventario = new DefaultTableModel(columnasInv, 0);
        tablaInventario = new JTable(modeloInventario);
        tablaInventario.setFillsViewportHeight(true);
        JScrollPane scrollInv = new JScrollPane(tablaInventario);
        scrollInv.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        panelInventario.add(scrollInv, BorderLayout.CENTER);

        JPanel panelInvBotones = new JPanel();
        panelInvBotones.setBackground(Color.WHITE);
        panelInvBotones.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelInventario.add(panelInvBotones, BorderLayout.SOUTH);

        JButton btnRefrescarInv = new JButton("Refrescar inventario");
        btnRefrescarInv.setFont(fontBoton);
        panelInvBotones.add(btnRefrescarInv);

        // ================== PESTAÑA: MIS TIQUETES ==================
        JPanel panelMisTiquetes = new JPanel(new BorderLayout(10, 10));
        panelMisTiquetes.setBorder(new EmptyBorder(10, 10, 10, 10));
        tabbedPane.addTab("Mis tiquetes", null, panelMisTiquetes, "Tiquetes comprados");

        JPanel panelMisTiqHeader = new JPanel(new BorderLayout());
        panelMisTiqHeader.setOpaque(false);
        JLabel lblMisTiqTitulo = new JLabel("Mis tiquetes");
        lblMisTiqTitulo.setFont(fontTituloTab);
        JLabel lblMisTiqDesc = new JLabel("Consulta y gestiona tus tiquetes. Puedes transferirlos a otro cliente.");
        lblMisTiqDesc.setFont(fontDescTab);
        lblMisTiqDesc.setForeground(new Color(100, 100, 100));
        panelMisTiqHeader.add(lblMisTiqTitulo, BorderLayout.NORTH);
        panelMisTiqHeader.add(lblMisTiqDesc, BorderLayout.SOUTH);
        panelMisTiquetes.add(panelMisTiqHeader, BorderLayout.NORTH);

        String[] columnasTiquetes = { "ID", "Evento", "Fecha", "Localidad", "Estado" };
        modeloTiquetes = new DefaultTableModel(columnasTiquetes, 0);
        tablaMisTiquetes = new JTable(modeloTiquetes);
        tablaMisTiquetes.setFillsViewportHeight(true);
        JScrollPane scrollTiquetes = new JScrollPane(tablaMisTiquetes);
        scrollTiquetes.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        panelMisTiquetes.add(scrollTiquetes, BorderLayout.CENTER);

        JPanel panelMisTiqBotones = new JPanel();
        panelMisTiqBotones.setBackground(Color.WHITE);
        panelMisTiqBotones.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelMisTiquetes.add(panelMisTiqBotones, BorderLayout.SOUTH);

        JButton btnVerTiq = new JButton("Ver detalle");
        JButton btnTransferirTiq = new JButton("Transferir tiquete");
        JButton btnRefrescarTiq = new JButton("Refrescar");

        btnVerTiq.setFont(fontBoton);
        btnTransferirTiq.setFont(fontBoton);
        btnRefrescarTiq.setFont(fontBoton);

        panelMisTiqBotones.add(btnVerTiq);
        panelMisTiqBotones.add(btnTransferirTiq);
        panelMisTiqBotones.add(btnRefrescarTiq);

        // ================== PESTAÑA: PERFIL (BASE) ==================
        JPanel panelPerfil = new JPanel(new BorderLayout(10, 10));
        panelPerfil.setBorder(new EmptyBorder(10, 10, 10, 10));
        tabbedPane.addTab("Mi perfil", null, panelPerfil, "Datos básicos del cliente");

        JPanel panelPerfilHeader = new JPanel(new BorderLayout());
        panelPerfilHeader.setOpaque(false);
        JLabel lblPerfilTitulo = new JLabel("Mi perfil");
        lblPerfilTitulo.setFont(fontTituloTab);
        JLabel lblPerfilDesc = new JLabel("Aquí irán los datos del cliente y opciones para actualizar información (pendiente).");
        lblPerfilDesc.setFont(fontDescTab);
        lblPerfilDesc.setForeground(new Color(100, 100, 100));
        panelPerfilHeader.add(lblPerfilTitulo, BorderLayout.NORTH);
        panelPerfilHeader.add(lblPerfilDesc, BorderLayout.SOUTH);
        panelPerfil.add(panelPerfilHeader, BorderLayout.NORTH);

        JPanel panelPerfilCentro = new JPanel();
        panelPerfilCentro.setBackground(Color.WHITE);
        panelPerfilCentro.setBorder(new EmptyBorder(30, 40, 30, 40));
        panelPerfil.add(panelPerfilCentro, BorderLayout.CENTER);

        JLabel lblPerfilPlaceholder = new JLabel("Aquí irán los datos del cliente y opciones para actualizar información.");
        lblPerfilPlaceholder.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblPerfilPlaceholder.setForeground(new Color(80, 80, 80));
        panelPerfilCentro.add(lblPerfilPlaceholder);

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

        btnVerDetalleEvento.addActionListener(e -> {
            int fila = tablaEventosDisponibles.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this,
                        "Debes seleccionar un evento para ver el detalle.",
                        "Sin selección",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Aquí mostraríamos el detalle del evento con ID: " +
                                tablaEventosDisponibles.getValueAt(fila, 0),
                        "Detalle de evento",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnComprar.addActionListener(e -> comprarTiquetesPorId());

        btnVerTiq.addActionListener(e -> {
            int fila = tablaMisTiquetes.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this,
                        "Debes seleccionar un tiquete para ver el detalle.",
                        "Sin selección",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Aquí mostraríamos el detalle del tiquete con ID: " +
                                tablaMisTiquetes.getValueAt(fila, 0),
                        "Detalle de tiquete",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnTransferirTiq.addActionListener(e -> transferirTiqueteSeleccionado());
    }

    // ======== MÉTODOS DE CARGA ========

    private void actualizarSaldoHeader() {
        lblSaldoHeader.setText("Saldo actual: $" + (int) cliente.getSaldo());
    }

    private void cargarEventosDisponibles() {
        modeloEventos.setRowCount(0);

        List<Evento> eventos;
        if (servicioEventos != null) {
            eventos = servicioEventos.listarEventos();
        } else {
            eventos = Boletamaster.Main.eventos; // fallback
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

        for (Tiquete t : Boletamaster.Main.inventario) {
            String evNombre = "-";
            String asiento = "-";

            if (t.getLocalidad() != null && t.getLocalidad().getEvento() != null) {
                evNombre = t.getLocalidad().getEvento().getNombre();
            }
            if (t instanceof TiqueteNumerado) {
                asiento = ((TiqueteNumerado) t).getAsiento();
            }

            double base = t.getPrecio();
            double total = t.calcularPrecioTotal(Boletamaster.Main.admin);

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

    // ======== LÓGICA DE ACCIONES (equivalente al Main de consola) ========

    private void abonarSaldo() {
        String input = JOptionPane.showInputDialog(this,
                "Valor a abonar:",
                "Abonar saldo",
                JOptionPane.PLAIN_MESSAGE);
        if (input == null) return; // canceló

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

        Pago p = cliente.comprarTiquetes(items, Boletamaster.Main.admin);
        if (p == null) {
            JOptionPane.showMessageDialog(this,
                    "Compra rechazada (saldo insuficiente o problemas de disponibilidad).",
                    "Compra rechazada",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            Boletamaster.Main.pagos.add(p);
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
        Tiquete elegido = null;
        for (Tiquete t : cliente.getTiquetes()) {
            if (t.getId().equalsIgnoreCase(idTiq)) {
                elegido = t;
                break;
            }
        }

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

    // ======== HELPERS BACKEND ========

    private void guardarCambios() {
        DataStore ds = new DataStore();
        ds.saveAll();
    }

    private Tiquete buscarTiquetePorId(String id) {
        for (Tiquete t : Boletamaster.Main.inventario) {
            if (t.getId().equalsIgnoreCase(id)) return t;
        }
        return null;
    }

    private Cliente buscarClientePorLogin(String login) {
        for (Usuario u : Boletamaster.Main.usuarios) {
            if (u instanceof Cliente && u.getLogin().equals(login)) {
                return (Cliente) u;
            }
        }
        return null;
    }
}
