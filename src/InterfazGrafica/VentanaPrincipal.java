package InterfazGrafica;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

public class VentanaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public static void main(String[] args) {
		// Look & Feel del sistema
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaPrincipal frame = new VentanaPrincipal();
					frame.setLocationRelativeTo(null); // centrar
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public VentanaPrincipal() {
		setResizable(false);
		setTitle("Boletamaster");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

		JLabel lblTitulo = new JLabel("Boletamaster");
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
		panelHeader.add(lblTitulo, BorderLayout.WEST);

		JLabel lblSubtitulo = new JLabel("Gestión de eventos y tiquetes");
		lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblSubtitulo.setForeground(new Color(100, 100, 100));
		panelHeader.add(lblSubtitulo, BorderLayout.SOUTH);

		// ========== PANEL CENTRAL ==========
		JPanel panelCentro = new JPanel();
		panelCentro.setOpaque(false);
		panelCentro.setLayout(new BorderLayout());
		contentPane.add(panelCentro, BorderLayout.CENTER);

		JPanel panelCard = new JPanel();
		panelCard.setBackground(Color.WHITE);
		panelCard.setBorder(new EmptyBorder(30, 40, 30, 40));
		panelCard.setLayout(null);
		panelCentro.add(panelCard, BorderLayout.CENTER);

		JLabel lblElige = new JLabel("Elige cómo quieres ingresar");
		lblElige.setFont(new Font("Segoe UI", Font.BOLD, 20));
		lblElige.setBounds(40, 20, 400, 30);
		panelCard.add(lblElige);

		JLabel lblAyuda = new JLabel("Selecciona el tipo de usuario para continuar");
		lblAyuda.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblAyuda.setForeground(new Color(120, 120, 120));
		lblAyuda.setBounds(40, 55, 350, 20);
		panelCard.add(lblAyuda);

		// Botón Administrador
		JButton btnAdmin = new JButton("Administrador");
		btnAdmin.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnAdmin.setBackground(new Color(0, 255, 128));
		btnAdmin.setForeground(new Color(0, 0, 0));
		btnAdmin.setFocusPainted(false);
		btnAdmin.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
		btnAdmin.setBounds(40, 100, 220, 40);
		panelCard.add(btnAdmin);

		// Botón Organizador
		JButton btnOrganizador = new JButton("Organizador");
		btnOrganizador.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnOrganizador.setBackground(new Color(3, 169, 244));
		btnOrganizador.setForeground(new Color(0, 0, 0));
		btnOrganizador.setFocusPainted(false);
		btnOrganizador.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
		btnOrganizador.setBounds(40, 155, 220, 40);
		panelCard.add(btnOrganizador);

		// Botón Cliente
		JButton btnCliente = new JButton("Cliente");
		btnCliente.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnCliente.setBackground(new Color(0, 255, 255));
		btnCliente.setForeground(new Color(33, 33, 33));
		btnCliente.setFocusPainted(false);
		btnCliente.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
		btnCliente.setBounds(40, 210, 220, 40);
		panelCard.add(btnCliente);

		// ========== FOOTER ==========
		JPanel panelFooter = new JPanel();
		panelFooter.setBackground(new Color(245, 245, 245));
		panelFooter.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.add(panelFooter, BorderLayout.SOUTH);

		JLabel lblFooter = new JLabel("© 2025 Boletamaster · Proyecto académico");
		lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		lblFooter.setForeground(new Color(150, 150, 150));
		panelFooter.add(lblFooter);

		// ========== CONEXIÓN DE BOTONES CON VENTANAS ==========

		btnAdmin.addActionListener(e -> {
			VentanaAdmin v = new VentanaAdmin();
			v.setLocationRelativeTo(this);
			v.setVisible(true);
		});

		btnOrganizador.addActionListener(e -> {
			VentanaOrganizador v = new VentanaOrganizador();
			v.setLocationRelativeTo(this);
			v.setVisible(true);
		});

		btnCliente.addActionListener(e -> {
			VentanaCliente v = new VentanaCliente();
			v.setLocationRelativeTo(this);
			v.setVisible(true);
		});
	}
}
