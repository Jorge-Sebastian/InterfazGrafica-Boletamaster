package InterfazGrafica;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JOptionPane;

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
        setBounds(100, 100, 449, 289);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
        contentPane.setBackground(new Color(245, 245, 245));
        contentPane.setLayout(new BorderLayout(10, 10));
        setContentPane(contentPane);

        // ========== HEADER ==========
        JLabel lblTitulo = new JLabel("Iniciar sesión como " + tipoUsuario.toLowerCase());
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        contentPane.add(lblTitulo, BorderLayout.NORTH);

        // ========== CENTRO: FORMULARIO ==========
        JPanel panelCentro = new JPanel();
        panelCentro.setOpaque(false);
        panelCentro.setLayout(null);
        contentPane.add(panelCentro, BorderLayout.CENTER);

        JLabel lblLogin = new JLabel("Usuario:");
        lblLogin.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblLogin.setBounds(30, 10, 80, 20);
        panelCentro.add(lblLogin);

        txtLogin = new JTextField();
        txtLogin.setBounds(30, 30, 320, 26);
        panelCentro.add(txtLogin);

        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblPassword.setBounds(30, 70, 100, 20);
        panelCentro.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(30, 90, 320, 26);
        panelCentro.add(txtPassword);

        // ========== BOTONES ==========
        JPanel panelBotones = new JPanel();
        panelBotones.setOpaque(false);
        contentPane.add(panelBotones, BorderLayout.SOUTH);

        JButton btnCancelar = new JButton("Cancelar");
        JButton btnIngresar = new JButton("Ingresar");

        btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnIngresar.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        panelBotones.add(btnCancelar);
        panelBotones.add(btnIngresar);

        // ========== ACCIONES ==========

        btnCancelar.addActionListener(e -> {
            dispose();
        });

        btnIngresar.addActionListener(e -> {
            manejarLogin();
        });
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
                	IServicioEventos servicioEventos = new ServicioEventosCSV(); // Todos los eventos
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
