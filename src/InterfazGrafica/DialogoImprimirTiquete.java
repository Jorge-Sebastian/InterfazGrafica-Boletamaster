package InterfazGrafica;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import Boletamaster.DataStore;
import Boletamaster.Evento;
import Boletamaster.Localidad;
import Boletamaster.Tiquete;

/**
 * Muestra una vista previa del tiquete con QR y marca la impresión en el modelo.
 */
public class DialogoImprimirTiquete extends JDialog {

    private static final long serialVersionUID = 1L;
    private final Tiquete tiquete;
    private JLabel lblEstadoImpresion;
    private JButton btnImprimir;

    public DialogoImprimirTiquete(java.awt.Frame owner, Tiquete tiquete) {
        super(owner, "Imprimir tiquete", true);
        this.tiquete = tiquete;
        setSize(760, 440);
        setLocationRelativeTo(owner);

        getContentPane().setBackground(UIUtils.COLOR_BG);
        getContentPane().setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel card = UIUtils.createCardPanel();
        card.setLayout(new BorderLayout(12, 12));
        getContentPane().add(card, BorderLayout.CENTER);

        Evento evento = extraerEvento();
        Localidad loc = tiquete.getLocalidad();

        String titulo = evento != null ? evento.getNombre() : "Evento";
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        card.add(lblTitulo, BorderLayout.NORTH);

        JPanel cuerpo = new PanelTicket();
        cuerpo.setLayout(new BorderLayout(12, 12));
        cuerpo.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        card.add(cuerpo, BorderLayout.CENTER);

        // Panel con datos
        JPanel datos = new JPanel(new BorderLayout(8, 8));
        datos.setOpaque(false);
        datos.setBorder(new EmptyBorder(12, 12, 12, 12));
        cuerpo.add(datos, BorderLayout.CENTER);

        JTextArea detalle = new JTextArea();
        detalle.setEditable(false);
        detalle.setOpaque(false);
        detalle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        detalle.setForeground(Color.DARK_GRAY);
        String fechaEvento = (evento != null && evento.getFecha() != null) ? evento.getFecha().toString() : "-";
        detalle.setText(
                "ID: " + tiquete.getId() + "\n" +
                        "Evento: " + titulo + "\n" +
                        "Fecha evento: " + fechaEvento + "\n" +
                        "Localidad: " + (loc != null ? loc.getNombre() : "-") + "\n" +
                        "Estado: " + tiquete.getEstado());
        datos.add(new JScrollPane(detalle), BorderLayout.CENTER);

        lblEstadoImpresion = new JLabel();
        lblEstadoImpresion.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblEstadoImpresion.setForeground(UIUtils.COLOR_MUTED);
        lblEstadoImpresion.setBorder(new EmptyBorder(6, 0, 0, 0));
        datos.add(lblEstadoImpresion, BorderLayout.SOUTH);

        // QR
        JPanel panelQr = new JPanel(new BorderLayout());
        panelQr.setOpaque(false);
        panelQr.setPreferredSize(new Dimension(240, 240));
        BufferedImage qrImg = QrUtil.generateQrImage(buildPayload(evento), 240);
        JLabel lblQr = new JLabel(new ImageIcon(qrImg));
        lblQr.setHorizontalAlignment(SwingConstants.CENTER);
        lblQr.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        panelQr.add(lblQr, BorderLayout.CENTER);
        cuerpo.add(panelQr, BorderLayout.EAST);

        // Botones
        JPanel acciones = new JPanel();
        acciones.setOpaque(false);
        card.add(acciones, BorderLayout.SOUTH);

        btnImprimir = new JButton(tiquete.estaImpreso() ? "Ya impreso" : "Marcar como impreso");
        UIUtils.stylePrimaryButton(btnImprimir);
        acciones.add(btnImprimir);
        btnImprimir.setEnabled(!tiquete.estaImpreso());

        JButton btnCerrar = new JButton("Cerrar");
        UIUtils.styleSecondaryButton(btnCerrar);
        acciones.add(btnCerrar);

        actualizarEstadoLabel();

        btnCerrar.addActionListener(e -> dispose());
        btnImprimir.addActionListener(e -> marcarImpreso());
    }

    private void marcarImpreso() {
        if (tiquete.estaImpreso()) {
            return;
        }
        if (tiquete.marcarImpreso()) {
            new DataStore().saveAll();
            actualizarEstadoLabel();
            btnImprimir.setEnabled(false);
            btnImprimir.setText("Ya impreso");
        }
    }

    private String buildPayload(Evento evento) {
        String nombre = evento != null ? evento.getNombre() : "";
        String fecha = (evento != null && evento.getFecha() != null) ? evento.getFecha().toString() : "";
        String fechaExpedicion = LocalDate.now().toString();
        return "{"
                + "\"id\":\"" + tiquete.getId() + "\","
                + "\"evento\":\"" + nombre + "\","
                + "\"fechaEvento\":\"" + fecha + "\","
                + "\"fechaImpresion\":\"" + fechaExpedicion + "\""
                + "}";
    }

    private Evento extraerEvento() {
        if (tiquete.getLocalidad() != null) {
            return tiquete.getLocalidad().getEvento();
        }
        return null;
    }

    private void actualizarEstadoLabel() {
        if (tiquete.estaImpreso()) {
            lblEstadoImpresion.setText("Impreso el " + LocalDate.now() + ". Transferencia y reimpresión bloqueadas.");
        } else {
            lblEstadoImpresion.setText("Este código QR es único para el tiquete. Marca como impreso para bloquearlo.");
        }
    }

    /**
     * Panel con fondo degrade simple para simular el arte del evento.
     */
    private static class PanelTicket extends JPanel {
        private static final long serialVersionUID = 1L;

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            UIUtils.enableAntialias(g);
            int w = getWidth();
            int h = getHeight();
            g.setColor(new Color(31, 59, 115));
            g.fillRect(0, 0, w, h);
            g.setColor(new Color(36, 63, 122));
            g.fillRect(0, 0, w, h / 2);
        }
    }
}
