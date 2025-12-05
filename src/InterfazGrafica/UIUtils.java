package InterfazGrafica;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

/**
 * Utilidades básicas para aplicar un look consistente en Swing sin repetir
 * estilos en todas las ventanas.
 */
public final class UIUtils {

    public static final Color COLOR_BG = new Color(245, 245, 245);
    public static final Color COLOR_CARD = Color.WHITE;
    public static final Color COLOR_PRIMARY = new Color(31, 59, 115);
    public static final Color COLOR_ACCENT = new Color(245, 166, 35);
    public static final Color COLOR_MUTED = new Color(110, 110, 110);

    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_TABLE_HEADER = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font FONT_TABLE = new Font("Segoe UI", Font.PLAIN, 12);

    private UIUtils() {
    }

    public static void stylePrimaryButton(JButton btn) {
        btn.setFont(FONT_BUTTON);
        btn.setBackground(COLOR_PRIMARY);
        btn.setForeground(COLOR_PRIMARY);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
    }

    public static void styleSecondaryButton(JButton btn) {
    	btn.setFont(FONT_BUTTON);
        btn.setBackground(COLOR_PRIMARY);
        btn.setForeground(COLOR_PRIMARY);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
    }

    public static JPanel createCardPanel() {
        JPanel p = new JPanel();
        p.setBackground(COLOR_CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 225, 225)),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)));
        return p;
    }

    public static JPanel createHeader(String titulo, String subtitulo) {
        JPanel header = new JPanel();
        header.setBackground(COLOR_CARD);
        header.setLayout(null);
        header.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(FONT_TITLE);
        lblTitulo.setBounds(10, 0, 600, 28);
        header.add(lblTitulo);

        if (subtitulo != null && !subtitulo.isBlank()) {
            JLabel lblSub = new JLabel(subtitulo);
            lblSub.setFont(FONT_SUBTITLE);
            lblSub.setForeground(COLOR_MUTED);
            lblSub.setBounds(10, 28, 700, 22);
            header.add(lblSub);
            header.setPreferredSize(header.getPreferredSize());
        }
        return header;
    }

    public static void stylizeTable(JTable table) {
        table.setFont(FONT_TABLE);
        table.setRowHeight(28);
        table.setShowGrid(true);
        table.setGridColor(new Color(235, 235, 235));
        table.setBackground(Color.WHITE);
        table.setSelectionBackground(new Color(31, 59, 115, 25));
        table.setSelectionForeground(Color.BLACK);

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_TABLE_HEADER);
        header.setOpaque(false);
        header.setBackground(COLOR_PRIMARY);
        header.setForeground(Color.WHITE);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_PRIMARY.darker()));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.setDefaultRenderer(Object.class, new ZebraRenderer(centerRenderer));
    }

    /**
     * Renderer con efecto zebra discreto.
     */
    private static class ZebraRenderer extends DefaultTableCellRenderer {
        private final DefaultTableCellRenderer delegate;
        private final Color even = new Color(250, 250, 250);
        private final Color odd = new Color(240, 245, 250);

        ZebraRenderer(DefaultTableCellRenderer delegate) {
            this.delegate = delegate;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            Component c = delegate.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (!isSelected) {
                c.setBackground((row % 2 == 0) ? even : odd);
            }
            return c;
        }
    }

    /**
     * Bordes suaves para paneles blancos sobre fondo gris claro.
     */
    public static Border softCardBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12));
    }

    /**
     * Forza antialias en componentes custom.
     */
    public static void enableAntialias(Graphics g) {
        if (g instanceof Graphics2D) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }
    }

    static {
        // Opcional: bordes menos marcados en controles por defecto.
        UIManager.put("Button.focusPainted", false);
    }
}
