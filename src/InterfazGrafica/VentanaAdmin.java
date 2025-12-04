package InterfazGrafica;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Boletamaster.Administrador;
import Boletamaster.Cliente;
import Boletamaster.DataStore;
import Boletamaster.Evento;
import Boletamaster.Main;
import Boletamaster.Usuario;

public class VentanaAdmin extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    private JTable tablaEventos;
    private DefaultTableModel modeloEventos;

    private Administrador admin;
    private JLabel lblTarifas;

    // Para compatibilidad con código que hace new VentanaAdmin()
    public VentanaAdmin() {
        this(Main.admin);
    }

    public VentanaAdmin(Administrador admin) {
        this.admin = admin != null ? admin : Main.admin;

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
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(Color.WHITE);
        panelHeader.setBorder(new EmptyBorder(10, 20, 10, 20));
        contentPane.add(panelHeader, BorderLayout.NORTH);

        JLabel lblTitulo = new JLabel("Panel de administrador");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        panelHeader.add(lblTitulo, BorderLayout.WEST);

        lblTarifas = new JLabel();
        lblTarifas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTarifas.setForeground(new Color(100, 100, 100));
        panelHeader.add(lblTarifas, BorderLayout.SOUTH);

        JButton btnConfigTarifas = new JButton("Configurar tarifas");
        btnConfigTarifas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panelHeader.add(btnConfigTarifas, BorderLayout.EAST);

        // ========== CENTRO: TABLA EVENTOS ==========
        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setOpaque(false);
        contentPane.add(panelCentro, BorderLayout.CENTER);

        JLabel lblEventosTitulo = new JLabel("Eventos del sistema");
        lblEventosTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel lblEventosDesc = new JLabel("Aquí puedes ver los eventos y cancelar uno con reembolso a clientes.");
        lblEventosDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblEventosDesc.setForeground(new Color(100, 100, 100));

        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.setOpaque(false);
        panelTop.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelTop.add(lblEventosTitulo, BorderLayout.NORTH);
        panelTop.add(lblEventosDesc, BorderLayout.SOUTH);
        panelCentro.add(panelTop, BorderLayout.NORTH);

        String[] columnas = { "ID", "Nombre", "Fecha", "Venue", "Estado" };
        modeloEventos = new DefaultTableModel(columnas, 0);
        tablaEventos = new JTable(modeloEventos);
        tablaEventos.setFillsViewportHeight(true);
        JScrollPane scrollEventos = new JScrollPane(tablaEventos);
        scrollEventos.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        panelCentro.add(scrollEventos, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(Color.WHITE);
        panelBotones.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelCentro.add(panelBotones, BorderLayout.SOUTH);

        JButton btnCancelarEvento = new JButton("Cancelar evento + reembolsar");
        JButton btnRefrescar = new JButton("Refrescar lista");

        btnCancelarEvento.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnRefrescar.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        panelBotones.add(btnCancelarEvento);
        panelBotones.add(btnRefrescar);

        // ========== CARGA INICIAL ==========
        actualizarLabelTarifas();
        cargarEventos();

        // ========== ACCIONES ==========

        btnRefrescar.addActionListener(e -> cargarEventos());

        btnConfigTarifas.addActionListener(e -> configurarTarifasGUI());

        btnCancelarEvento.addActionListener(e -> cancelarEventoGUI());
    }

    // ================== LÓGICA ==================

    private void cargarEventos() {
        modeloEventos.setRowCount(0);

        if (Main.eventos.isEmpty()) {
            return;
        }

        for (Evento ev : Main.eventos) {
            String venue = ev.getVenue() != null ? ev.getVenue().getNombre() : "-";
            Object[] fila = {
                    ev.getId(),
                    ev.getNombre(),
                    ev.getFecha(),
                    venue,
                    ev.getEstado()
            };
            modeloEventos.addRow(fila);
        }
    }

    private void actualizarLabelTarifas() {
        if (admin == null) {
            lblTarifas.setText("Tarifas no configuradas.");
            return;
        }
        // Asumo que tu clase Administrador tiene estos getters
        double p = admin.getPorcentajeServicio();
        double c = admin.getCuotaEmision();
        lblTarifas.setText(String.format("Tarifas actuales → Servicio: %.2f  |  Cuota de emisión: $%.0f", p, c));
    }

    private void configurarTarifasGUI() {
        String porcStr = JOptionPane.showInputDialog(this,
                "Porcentaje servicio (0.10 = 10%):",
                "Configurar tarifas",
                JOptionPane.PLAIN_MESSAGE);
        if (porcStr == null) return;

        String cuotaStr = JOptionPane.showInputDialog(this,
                "Cuota de emisión:",
                "Configurar tarifas",
                JOptionPane.PLAIN_MESSAGE);
        if (cuotaStr == null) return;

        double p, c;
        try {
            p = Double.parseDouble(porcStr.trim());
            c = Double.parseDouble(cuotaStr.trim());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Valores inválidos.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        admin.configurarTarifas(p, c);
        guardarCambios();
        actualizarLabelTarifas();

        JOptionPane.showMessageDialog(this,
                "Tarifas actualizadas correctamente.",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void cancelarEventoGUI() {
        int fila = tablaEventos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Debes seleccionar un evento para cancelarlo.",
                    "Sin selección",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idEvento = (String) tablaEventos.getValueAt(fila, 0);

        Evento seleccionado = null;
        for (Evento e : Main.eventos) {
            if (e.getId().equalsIgnoreCase(idEvento)) {
                seleccionado = e;
                break;
            }
        }

        if (seleccionado == null) {
            JOptionPane.showMessageDialog(this,
                    "No se encontró el evento seleccionado.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas cancelar el evento '" + seleccionado.getNombre() +
                        "' y procesar reembolsos?",
                "Confirmar cancelación",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        // ==== Lógica igual a cancelarYReembolsar(sc) ====
        admin.cancelarEvento(seleccionado);

        int cont = 0;
        for (Usuario u : Main.usuarios) {
            if (u instanceof Cliente) {
                admin.procesarReembolso((Cliente) u, seleccionado);
                cont++;
            }
        }

        guardarCambios();
        cargarEventos();

        JOptionPane.showMessageDialog(this,
                "Evento cancelado.\nReembolsos procesados para " + cont + " clientes.",
                "Operación completada",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void guardarCambios() {
        DataStore ds = new DataStore();
        ds.saveAll();
    }
}
