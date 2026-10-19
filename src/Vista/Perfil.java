package Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/** Pantalla de perfil: datos de la sesión y permisos según el rol. Solo visual. */
public class Perfil extends JPanel {

    private static final Color AZUL_MARINO = Color.decode("#0F2D5B");
    private static final Color AZUL_ELECTRICO = Color.decode("#007BFF");
    private static final Color GRIS_MEDIO = Color.decode("#6B7280");
    private static final Color GRIS_CLARO = Color.decode("#F3F4F6");
    private static final Color GRIS_OSCURO = Color.decode("#1F2937");
    private static final Color BORDE = new Color(224, 227, 232);

    public Perfil() {
        setLayout(new BorderLayout());
        setBackground(GRIS_CLARO);
        setBorder(new EmptyBorder(30, 36, 30, 36));

        JLabel titulo = new JLabel("Mi perfil");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        titulo.setForeground(GRIS_OSCURO);
        titulo.setBorder(new EmptyBorder(0, 0, 22, 0));
        add(titulo, BorderLayout.NORTH);

        JPanel tarjeta = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
                g2.setColor(BORDE);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
                g2.dispose();
            }
        };
        tarjeta.setOpaque(false);
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBorder(new EmptyBorder(28, 32, 28, 32));

        String usuario = Sesion.getUsuario();
        String rol = Sesion.getRol();

        tarjeta.add(avatar(usuario));
        tarjeta.add(Box.createVerticalStrut(14));
        tarjeta.add(etiqueta(usuario, 22, true, GRIS_OSCURO));
        tarjeta.add(etiqueta(rol, 13, false, AZUL_ELECTRICO));
        tarjeta.add(Box.createVerticalStrut(24));
        tarjeta.add(etiqueta("Permisos de este rol", 14, true, GRIS_OSCURO));
        tarjeta.add(Box.createVerticalStrut(8));
        for (String p : permisos(rol)) {
            tarjeta.add(etiqueta("✓  " + p, 13, false, GRIS_MEDIO));
            tarjeta.add(Box.createVerticalStrut(5));
        }

        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setOpaque(false);
        tarjeta.setPreferredSize(new Dimension(420, 380));
        JPanel izq = new JPanel(new BorderLayout());
        izq.setOpaque(false);
        izq.add(tarjeta, BorderLayout.NORTH);
        contenedor.add(izq, BorderLayout.WEST);
        add(contenedor, BorderLayout.CENTER);
    }

    private String[] permisos(String rol) {
        switch (rol) {
            case "ADMINISTRADOR":
                return new String[]{"Gestionar clientes y equipos", "Crear y atender tickets", "Ver reportes (MIS)"};
            case "TECNICO":
                return new String[]{"Ver tickets", "Registrar diagnóstico y reparación", "Actualizar estado y costo"};
            default:
                return new String[]{"Registrar clientes y equipos", "Crear tickets de ingreso"};
        }
    }

    private JLabel etiqueta(String texto, int size, boolean negrita, Color color) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("SansSerif", negrita ? Font.BOLD : Font.PLAIN, size));
        l.setForeground(color);
        l.setAlignmentX(LEFT_ALIGNMENT);
        return l;
    }

    private JPanel avatar(String usuario) {
        String inicial = usuario.isEmpty() ? "?" : usuario.substring(0, 1).toUpperCase();
        JPanel c = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AZUL_ELECTRICO);
                g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 34));
                java.awt.FontMetrics fm = g2.getFontMetrics();
                g2.drawString(inicial, (getWidth() - fm.stringWidth(inicial)) / 2,
                        (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
                g2.dispose();
            }
        };
        c.setOpaque(false);
        c.setPreferredSize(new Dimension(84, 84));
        c.setMaximumSize(new Dimension(84, 84));
        c.setAlignmentX(LEFT_ALIGNMENT);
        return c;
    }
}