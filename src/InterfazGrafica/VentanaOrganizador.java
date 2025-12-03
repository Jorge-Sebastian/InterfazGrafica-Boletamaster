package InterfazGrafica;

import java.util.*;

import Boletamaster.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;

public class VentanaOrganizador extends JFrame {
	
	private Organizador organizador;

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    // Tablas para cada pestaña (luego las usamos para cargar datos reales)
    private JTable tablaEventos;
    private JTable tablaLocalidades;
    private JTable tablaTiquetes;
    
    private DefaultTableModel modeloEventos;
    private IServicioEventos servicioEventos;
    
    //---------CARGAR EVENTOS-----------------
    private void cargarEventosEnTabla() {
        modeloEventos.setRowCount(0); // limpiar

        if (servicioEventos == null) {
            return;
        }

        List<Evento> eventos = servicioEventos.listarEventos();

        for (Evento ev : eventos) {
            String ciudad = "-";
            if (ev.getVenue() != null) {
                // En Venue tienes nombre, ubicación, etc.
                // Si quieres la ciudad/ubicación:
                ciudad = ev.getVenue().getUbicacion();  // o getNombre(), según prefieras
            }

            Object[] fila = {
                ev.getId(),
                ev.getNombre(),
                ev.getFecha(),   // LocalDate: la tabla muestra toString()
                ciudad,
                ev.getEstado()
            };
            modeloEventos.addRow(fila);
        }
    }


    public VentanaOrganizador(Organizador organizador, IServicioEventos servicioEventos) {
    	this.organizador = organizador;
    	this.servicioEventos = servicioEventos;
        setTitle("Boletamaster - Organizador");
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

        JLabel lblTitulo = new JLabel("Panel de organizador (" + organizador.getLogin() + ")");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        panelHeader.add(lblTitulo, BorderLayout.WEST);

        JLabel lblSubtitulo = new JLabel("Creación y administración de eventos, localidades y tiquetes");
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

     // ================== PESTAÑA: EVENTOS ==================
        JPanel panelEventos = new JPanel();
        panelEventos.setLayout(new BorderLayout(10, 10));
        panelEventos.setBorder(new EmptyBorder(10, 10, 10, 10));
        tabbedPane.addTab("Eventos", null, panelEventos, "Gestión de eventos");

        // Encabezado de la pestaña
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

        // ✅ OJO AQUÍ: usamos el atributo, no una variable local
        String[] columnasEventos = { "ID", "Nombre", "Fecha", "Ciudad", "Estado" };
        modeloEventos = new DefaultTableModel(columnasEventos, 0);   // <--- SIN "DefaultTableModel" adelante
        tablaEventos = new JTable(modeloEventos);
        tablaEventos.setFillsViewportHeight(true);
        JScrollPane scrollEventos = new JScrollPane(tablaEventos);
        scrollEventos.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        panelEventos.add(scrollEventos, BorderLayout.CENTER);

        // Botones de acciones para eventos
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


        // ================== PESTAÑA: LOCALIDADES ==================
        JPanel panelLocalidades = new JPanel();
        panelLocalidades.setLayout(new BorderLayout(10, 10));
        panelLocalidades.setBorder(new EmptyBorder(10, 10, 10, 10));
        tabbedPane.addTab("Localidades", null, panelLocalidades, "Localidades por evento");

        JLabel lblLocTitulo = new JLabel("Localidades");
        lblLocTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JLabel lblLocDesc = new JLabel("Configura las localidades, precios y capacidad para cada evento.");
        lblLocDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblLocDesc.setForeground(new Color(100, 100, 100));

        JPanel panelLocHeader = new JPanel(new BorderLayout());
        panelLocHeader.setOpaque(false);
        panelLocHeader.add(lblLocTitulo, BorderLayout.NORTH);
        panelLocHeader.add(lblLocDesc, BorderLayout.SOUTH);
        panelLocalidades.add(panelLocHeader, BorderLayout.NORTH);

        String[] columnasLocalidades = { "ID", "Evento", "Nombre localidad", "Capacidad", "Precio" };
        DefaultTableModel modeloLocalidades = new DefaultTableModel(columnasLocalidades, 0);
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
        JButton btnRefrescarLoc = new JButton("Refrescar");

        btnNuevaLoc.setFont(fontBoton);
        btnEditarLoc.setFont(fontBoton);
        btnEliminarLoc.setFont(fontBoton);
        btnRefrescarLoc.setFont(fontBoton);

        panelLocBotones.add(btnNuevaLoc);
        panelLocBotones.add(btnEditarLoc);
        panelLocBotones.add(btnEliminarLoc);
        panelLocBotones.add(btnRefrescarLoc);

        // ================== PESTAÑA: TIQUETES ==================
        JPanel panelTiquetes = new JPanel();
        panelTiquetes.setLayout(new BorderLayout(10, 10));
        panelTiquetes.setBorder(new EmptyBorder(10, 10, 10, 10));
        tabbedPane.addTab("Tiquetes", null, panelTiquetes, "Control de tiquetes generados");

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
        DefaultTableModel modeloTiquetes = new DefaultTableModel(columnasTiquetes, 0);
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

        // ================== ACCIONES BÁSICAS (por ahora solo ejemplos) ==================

        btnNuevoEvento.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Aquí abriremos el formulario para crear un nuevo evento.",
                    "Nuevo evento",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        btnEditarEvento.addActionListener(e -> {
            int fila = tablaEventos.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this,
                        "Debes seleccionar un evento en la tabla.",
                        "Sin selección",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Aquí editaríamos el evento seleccionado (ID: " + tablaEventos.getValueAt(fila, 0) + ").",
                        "Editar evento",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnEliminarEvento.addActionListener(e -> {
            int fila = tablaEventos.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this,
                        "Debes seleccionar un evento para eliminar.",
                        "Sin selección",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                int opcion = JOptionPane.showConfirmDialog(this,
                        "¿Seguro que deseas eliminar el evento seleccionado?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION);
                if (opcion == JOptionPane.YES_OPTION) {
                    modeloEventos.removeRow(fila);
                }
            }
        });
        
     // Cargar eventos al abrir la ventana
        cargarEventosEnTabla();

        // Acción del botón Refrescar
        btnRefrescarEventos.addActionListener(e -> {
            cargarEventosEnTabla();
        });

    }
}
