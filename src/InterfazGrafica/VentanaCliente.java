package InterfazGrafica;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

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

public class VentanaCliente extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    private JTable tablaEventosDisponibles;
    private JTable tablaMisTiquetes;

    public VentanaCliente() {
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

        JLabel lblTitulo = new JLabel("Portal de cliente");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        panelHeader.add(lblTitulo, BorderLayout.WEST);

        JLabel lblSubtitulo = new JLabel("Consulta eventos y gestiona tus tiquetes");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitulo.setForeground(new Color(100, 100, 100));
        panelHeader.add(lblSubtitulo, BorderLayout.SOUTH);

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
        JLabel lblEventosDesc = new JLabel("Selecciona un evento para ver detalles y comprar tiquetes.");
        lblEventosDesc.setFont(fontDescTab);
        lblEventosDesc.setForeground(new Color(100, 100, 100));
        panelEventosHeader.add(lblEventosTitulo, BorderLayout.NORTH);
        panelEventosHeader.add(lblEventosDesc, BorderLayout.SOUTH);
        panelEventos.add(panelEventosHeader, BorderLayout.NORTH);

        String[] columnasEventos = { "ID", "Nombre", "Fecha", "Ciudad", "Estado" };
        DefaultTableModel modeloEventos = new DefaultTableModel(columnasEventos, 0);
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
        JButton btnComprar = new JButton("Comprar tiquete");
        JButton btnRefrescarEventos = new JButton("Refrescar");

        btnVerDetalleEvento.setFont(fontBoton);
        btnComprar.setFont(fontBoton);
        btnRefrescarEventos.setFont(fontBoton);

        panelEventosBotones.add(btnVerDetalleEvento);
        panelEventosBotones.add(btnComprar);
        panelEventosBotones.add(btnRefrescarEventos);

        // ================== PESTAÑA: MIS TIQUETES ==================
        JPanel panelMisTiquetes = new JPanel(new BorderLayout(10, 10));
        panelMisTiquetes.setBorder(new EmptyBorder(10, 10, 10, 10));
        tabbedPane.addTab("Mis tiquetes", null, panelMisTiquetes, "Tiquetes comprados");

        JPanel panelMisTiqHeader = new JPanel(new BorderLayout());
        panelMisTiqHeader.setOpaque(false);
        JLabel lblMisTiqTitulo = new JLabel("Mis tiquetes");
        lblMisTiqTitulo.setFont(fontTituloTab);
        JLabel lblMisTiqDesc = new JLabel("Consulta el historial de tus compras y el estado de tus tiquetes.");
        lblMisTiqDesc.setFont(fontDescTab);
        lblMisTiqDesc.setForeground(new Color(100, 100, 100));
        panelMisTiqHeader.add(lblMisTiqTitulo, BorderLayout.NORTH);
        panelMisTiqHeader.add(lblMisTiqDesc, BorderLayout.SOUTH);
        panelMisTiquetes.add(panelMisTiqHeader, BorderLayout.NORTH);

        String[] columnasTiquetes = { "ID", "Evento", "Fecha", "Localidad", "Estado" };
        DefaultTableModel modeloTiquetes = new DefaultTableModel(columnasTiquetes, 0);
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
        JButton btnCancelarTiq = new JButton("Cancelar (si aplica)");
        JButton btnRefrescarTiq = new JButton("Refrescar");

        btnVerTiq.setFont(fontBoton);
        btnCancelarTiq.setFont(fontBoton);
        btnRefrescarTiq.setFont(fontBoton);

        panelMisTiqBotones.add(btnVerTiq);
        panelMisTiqBotones.add(btnCancelarTiq);
        panelMisTiqBotones.add(btnRefrescarTiq);

        // ================== PESTAÑA: PERFIL (OPCIONAL / BASE) ==================
        JPanel panelPerfil = new JPanel(new BorderLayout(10, 10));
        panelPerfil.setBorder(new EmptyBorder(10, 10, 10, 10));
        tabbedPane.addTab("Mi perfil", null, panelPerfil, "Datos básicos del cliente");

        JPanel panelPerfilHeader = new JPanel(new BorderLayout());
        panelPerfilHeader.setOpaque(false);
        JLabel lblPerfilTitulo = new JLabel("Mi perfil");
        lblPerfilTitulo.setFont(fontTituloTab);
        JLabel lblPerfilDesc = new JLabel("Aquí podrás ver y editar tus datos básicos (pendiente por implementar).");
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

        // ================== ACCIONES BÁSICAS (Placeholders) ==================

        btnVerDetalleEvento.addActionListener(e -> {
            int fila = tablaEventosDisponibles.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this,
                        "Debes seleccionar un evento para ver el detalle.",
                        "Sin selección",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Aquí mostraríamos el detalle del evento con ID: " + tablaEventosDisponibles.getValueAt(fila, 0),
                        "Detalle de evento",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnComprar.addActionListener(e -> {
            int fila = tablaEventosDisponibles.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this,
                        "Debes seleccionar un evento para comprar tiquetes.",
                        "Sin selección",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Aquí iniciaría el flujo de compra para el evento con ID: " + tablaEventosDisponibles.getValueAt(fila, 0),
                        "Comprar tiquete",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnVerTiq.addActionListener(e -> {
            int fila = tablaMisTiquetes.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this,
                        "Debes seleccionar un tiquete para ver el detalle.",
                        "Sin selección",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Aquí mostraríamos el detalle del tiquete con ID: " + tablaMisTiquetes.getValueAt(fila, 0),
                        "Detalle de tiquete",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnCancelarTiq.addActionListener(e -> {
            int fila = tablaMisTiquetes.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this,
                        "Debes seleccionar un tiquete para intentar cancelarlo.",
                        "Sin selección",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Aquí validaríamos si el tiquete se puede cancelar y actualizaríamos su estado.",
                        "Cancelar tiquete",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }
}
