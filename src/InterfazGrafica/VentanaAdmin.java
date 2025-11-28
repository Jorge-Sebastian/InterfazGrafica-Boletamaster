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

public class VentanaAdmin extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    private JTable tablaUsuarios;
    private JTable tablaReportes;

    public VentanaAdmin() {
        setTitle("Boletamaster - Administrador");
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

        JLabel lblTitulo = new JLabel("Panel de administrador");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        panelHeader.add(lblTitulo, BorderLayout.WEST);

        JLabel lblSubtitulo = new JLabel("Gestión global del sistema, usuarios y reportes");
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

        // ================== PESTAÑA: USUARIOS ==================
        JPanel panelUsuarios = new JPanel(new BorderLayout(10, 10));
        panelUsuarios.setBorder(new EmptyBorder(10, 10, 10, 10));
        tabbedPane.addTab("Usuarios", null, panelUsuarios, "Gestión de usuarios del sistema");

        JPanel panelUsuariosHeader = new JPanel(new BorderLayout());
        panelUsuariosHeader.setOpaque(false);
        JLabel lblUsuariosTitulo = new JLabel("Usuarios");
        lblUsuariosTitulo.setFont(fontTituloTab);
        JLabel lblUsuariosDesc = new JLabel("Administra los usuarios del sistema y sus roles.");
        lblUsuariosDesc.setFont(fontDescTab);
        lblUsuariosDesc.setForeground(new Color(100, 100, 100));
        panelUsuariosHeader.add(lblUsuariosTitulo, BorderLayout.NORTH);
        panelUsuariosHeader.add(lblUsuariosDesc, BorderLayout.SOUTH);
        panelUsuarios.add(panelUsuariosHeader, BorderLayout.NORTH);

        String[] columnasUsuarios = { "ID", "Nombre", "Rol", "Estado" };
        DefaultTableModel modeloUsuarios = new DefaultTableModel(columnasUsuarios, 0);
        tablaUsuarios = new JTable(modeloUsuarios);
        tablaUsuarios.setFillsViewportHeight(true);
        JScrollPane scrollUsuarios = new JScrollPane(tablaUsuarios);
        scrollUsuarios.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        panelUsuarios.add(scrollUsuarios, BorderLayout.CENTER);

        JPanel panelUsuariosBotones = new JPanel();
        panelUsuariosBotones.setBackground(Color.WHITE);
        panelUsuariosBotones.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelUsuarios.add(panelUsuariosBotones, BorderLayout.SOUTH);

        JButton btnNuevoUsuario = new JButton("Nuevo usuario");
        JButton btnEditarUsuario = new JButton("Editar");
        JButton btnDesactivarUsuario = new JButton("Activar / Desactivar");
        JButton btnRefrescarUsuarios = new JButton("Refrescar");

        btnNuevoUsuario.setFont(fontBoton);
        btnEditarUsuario.setFont(fontBoton);
        btnDesactivarUsuario.setFont(fontBoton);
        btnRefrescarUsuarios.setFont(fontBoton);

        panelUsuariosBotones.add(btnNuevoUsuario);
        panelUsuariosBotones.add(btnEditarUsuario);
        panelUsuariosBotones.add(btnDesactivarUsuario);
        panelUsuariosBotones.add(btnRefrescarUsuarios);

        // ================== PESTAÑA: REPORTES ==================
        JPanel panelReportes = new JPanel(new BorderLayout(10, 10));
        panelReportes.setBorder(new EmptyBorder(10, 10, 10, 10));
        tabbedPane.addTab("Reportes", null, panelReportes, "Reportes y métricas del sistema");

        JPanel panelReportesHeader = new JPanel(new BorderLayout());
        panelReportesHeader.setOpaque(false);
        JLabel lblReportesTitulo = new JLabel("Reportes");
        lblReportesTitulo.setFont(fontTituloTab);
        JLabel lblReportesDesc = new JLabel("Visualiza reportes generales de ventas, asistencia y rendimiento.");
        lblReportesDesc.setFont(fontDescTab);
        lblReportesDesc.setForeground(new Color(100, 100, 100));
        panelReportesHeader.add(lblReportesTitulo, BorderLayout.NORTH);
        panelReportesHeader.add(lblReportesDesc, BorderLayout.SOUTH);
        panelReportes.add(panelReportesHeader, BorderLayout.NORTH);

        String[] columnasReportes = { "ID", "Tipo", "Rango de fechas", "Descripción" };
        DefaultTableModel modeloReportes = new DefaultTableModel(columnasReportes, 0);
        tablaReportes = new JTable(modeloReportes);
        tablaReportes.setFillsViewportHeight(true);
        JScrollPane scrollReportes = new JScrollPane(tablaReportes);
        scrollReportes.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        panelReportes.add(scrollReportes, BorderLayout.CENTER);

        JPanel panelReportesBotones = new JPanel();
        panelReportesBotones.setBackground(Color.WHITE);
        panelReportesBotones.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelReportes.add(panelReportesBotones, BorderLayout.SOUTH);

        JButton btnGenerarReporte = new JButton("Generar reporte");
        JButton btnVerReporte = new JButton("Ver detalle");
        JButton btnExportarReporte = new JButton("Exportar");
        JButton btnRefrescarReportes = new JButton("Refrescar");

        btnGenerarReporte.setFont(fontBoton);
        btnVerReporte.setFont(fontBoton);
        btnExportarReporte.setFont(fontBoton);
        btnRefrescarReportes.setFont(fontBoton);

        panelReportesBotones.add(btnGenerarReporte);
        panelReportesBotones.add(btnVerReporte);
        panelReportesBotones.add(btnExportarReporte);
        panelReportesBotones.add(btnRefrescarReportes);

        // ================== PESTAÑA: CONFIGURACIÓN ==================
        JPanel panelConfig = new JPanel(new BorderLayout(10, 10));
        panelConfig.setBorder(new EmptyBorder(10, 10, 10, 10));
        tabbedPane.addTab("Configuración", null, panelConfig, "Parámetros generales del sistema");

        JPanel panelConfigHeader = new JPanel(new BorderLayout());
        panelConfigHeader.setOpaque(false);
        JLabel lblConfigTitulo = new JLabel("Configuración");
        lblConfigTitulo.setFont(fontTituloTab);
        JLabel lblConfigDesc = new JLabel("Ajusta parámetros globales del sistema (tarifas, rutas de archivos, etc.).");
        lblConfigDesc.setFont(fontDescTab);
        lblConfigDesc.setForeground(new Color(100, 100, 100));
        panelConfigHeader.add(lblConfigTitulo, BorderLayout.NORTH);
        panelConfigHeader.add(lblConfigDesc, BorderLayout.SOUTH);
        panelConfig.add(panelConfigHeader, BorderLayout.NORTH);

        JPanel panelConfigCentro = new JPanel();
        panelConfigCentro.setBackground(Color.WHITE);
        panelConfigCentro.setBorder(new EmptyBorder(30, 40, 30, 40));
        panelConfig.add(panelConfigCentro, BorderLayout.CENTER);

        JLabel lblConfigPlaceholder = new JLabel("Aquí irán los controles de configuración general (pendiente por implementar)");
        lblConfigPlaceholder.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblConfigPlaceholder.setForeground(new Color(80, 80, 80));
        panelConfigCentro.add(lblConfigPlaceholder);

        // ================== ACCIONES BÁSICAS (Placeholders) ==================

        btnNuevoUsuario.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Aquí se abriría el formulario para crear un nuevo usuario.",
                    "Nuevo usuario",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        btnEditarUsuario.addActionListener(e -> {
            int fila = tablaUsuarios.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this,
                        "Debes seleccionar un usuario para editar.",
                        "Sin selección",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Aquí editaríamos el usuario con ID: " + tablaUsuarios.getValueAt(fila, 0),
                        "Editar usuario",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnDesactivarUsuario.addActionListener(e -> {
            int fila = tablaUsuarios.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this,
                        "Debes seleccionar un usuario para activar/desactivar.",
                        "Sin selección",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Aquí cambiaríamos el estado del usuario con ID: " + tablaUsuarios.getValueAt(fila, 0),
                        "Cambiar estado",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnGenerarReporte.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Aquí se mostraría un diálogo para configurar y generar un reporte.",
                    "Generar reporte",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        btnVerReporte.addActionListener(e -> {
            int fila = tablaReportes.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this,
                        "Debes seleccionar un reporte para ver el detalle.",
                        "Sin selección",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Aquí se vería el detalle del reporte con ID: " + tablaReportes.getValueAt(fila, 0),
                        "Ver reporte",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }
}
