package InterfazGrafica;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import Boletamaster.AuthUtil;
import Boletamaster.Administrador;
import Boletamaster.Cliente;
import Boletamaster.IServicioEventos;
import Boletamaster.ServicioEventosCSV;
import Boletamaster.Organizador;

public class VentanaLogin extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private JTextField txtLogin;
    private JPasswordField txtPassword;

    // "ADMIN", "ORGANIZADOR" o "CLIENTE"
    private String tipoUsuario;

    public VentanaLogin(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;

        setTitle("Iniciar sesión - " + tipoUsuario);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setBounds(100, 100, 480, 320);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
        contentPane.setBackground(UIUtils.COLOR_BG);
        contentPane.setLayout(new BorderLayout(10, 10));
        setContentPane(contentPane);

        // ========== HEADER ==========
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(UIUtils.COLOR_CARD);
        panelHeader.setBorder(new EmptyBorder(10, 20, 10, 20));
        contentPane.add(panelHeader, BorderLayout.NORTH);

        JLabel lblTitulo = new JLabel("Iniciar sesión como " + tipoUsuario.toLowerCase());
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(UIUtils.FONT_TITLE);
        panelHeader.add(lblTitulo, BorderLayout.CENTER);

        JLabel lblSub = new JLabel("Ingresa tu usuario y contraseña para continuar");
        lblSub.setHorizontalAlignment(SwingConstants.CENTER);
        lblSub.setFont(UIUtils.FONT_SUBTITLE);
        lblSub.setForeground(UIUtils.COLOR_MUTED);
        panelHeader.add(lblSub, BorderLayout.SOUTH);

        // ========== CENTRO: CARD CON FORM ==========
        JPanel card = UIUtils.createCardPanel();
        card.setLayout(new GridBagLayout());
        contentPane.add(card, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;

        JLabel lblLogin = new JLabel("Usuario:");
        lblLogin.setFont(UIUtils.FONT_SUBTITLE);
        card.add(lblLogin, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtLogin = new JTextField();
        txtLogin.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        card.add(txtLogin, gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(UIUtils.FONT_SUBTITLE);
        card.add(lblPassword, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        card.add(txtPassword, gbc);

        // Espacio vertical
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        JPanel spacer = new JPanel();
        spacer.setOpaque(false);
        card.add(spacer, gbc);

        // ========== BOTONES ==========
        JPanel panelBotones = new JPanel();
        panelBotones.setOpaque(false);
        panelBotones.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.add(panelBotones, BorderLayout.SOUTH);

        JButton btnCancelar = new JButton("Cancelar");
        JButton btnIngresar = new JButton("Ingresar");

        UIUtils.styleSecondaryButton(btnCancelar);
        UIUtils.stylePrimaryButton(btnIngresar);

        panelBotones.add(btnCancelar);
        panelBotones.add(btnIngresar);

        // ========== ACCIONES ==========
        btnCancelar.addActionListener(e -> dispose());
        btnIngresar.addActionListener(e -> manejarLogin());
    }

    private void manejarLogin() {
        String login = txtLogin.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (login.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debes ingresar usuario y contraseña.",
                    "Campos vacíos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        switch (tipoUsuario) {
            case "ADMIN":
                Administrador a = AuthUtil.loginAdmin(login, password);
                if (a == null) {
                    mostrarErrorCredenciales();
                } else {
                    VentanaAdmin va = new VentanaAdmin();
                    va.setLocationRelativeTo(this);
                    va.setVisible(true);
                    dispose();
                }
                break;

            case "ORGANIZADOR":
                Organizador o = AuthUtil.loginOrganizador(login, password);
                if (o == null) {
                    mostrarErrorCredenciales();
                } else {
                    VentanaOrganizador vo = new VentanaOrganizador(o);
                    vo.setLocationRelativeTo(this);
                    vo.setVisible(true);
                    dispose();
                }
                break;

            case "CLIENTE":
                Cliente c = AuthUtil.loginCliente(login, password);
                if (c == null) {
                    mostrarErrorCredenciales();
                } else {
                    IServicioEventos servicioEventos = new ServicioEventosCSV(); // todos los eventos
                    VentanaCliente vc = new VentanaCliente(c, servicioEventos);
                    vc.setLocationRelativeTo(this);
                    vc.setVisible(true);
                    dispose();
                }
                break;

            default:
                JOptionPane.showMessageDialog(this,
                        "Tipo de usuario no soportado.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarErrorCredenciales() {
        JOptionPane.showMessageDialog(this,
                "Usuario o contraseña incorrectos para " + tipoUsuario.toLowerCase() + ".",
                "Credenciales inválidas",
                JOptionPane.ERROR_MESSAGE);
    }
}
