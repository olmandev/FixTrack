package Vista;

import soportetecnico_g7.CambiarEscena;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * @author GRUPO #1
 * RF07: Pantalla de inicio de sesión (JPanel dentro de una sola ventana).
 * Diseño split screen responsive con paleta corporativa.
 */
public class Login extends JPanel {

    // ---- Paleta corporativa ----
    private static final Color AZUL_MARINO = Color.decode("#0F2D5B");
    private static final Color AZUL_ELECTRICO = Color.decode("#007BFF");
    private static final Color GRIS_MEDIO = Color.decode("#6B7280");
    private static final Color GRIS_CLARO = Color.decode("#F3F4F6");
    private static final Color GRIS_OSCURO = Color.decode("#1F2937");

    private JTextField txtUsuario;
    private JPasswordField txtClave;
    private JLabel lblError;
    private CambiarEscena cambiarEscena;

    public Login(CambiarEscena cambiarEscena) {
        this.cambiarEscena = cambiarEscena;
        initComponents();
    }

    private void initComponents() {
        // Dos columnas proporcionales: 42% branding / 58% formulario
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;

        gbc.gridx = 0;
        gbc.weightx = 0.40;
        add(construirPanelLateral(), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.60;
        add(construirPanelFormulario(), gbc);
    }

    // ---------- Lado izquierdo (logo / branding) ----------

    private JPanel construirPanelLateral() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(GRIS_CLARO);
        panel.setBorder(new EmptyBorder(0, 40, 0, 40));
        panel.setPreferredSize(new Dimension(150, 110));
        panel.setMinimumSize(new Dimension(150, 110));

        panel.add(Box.createVerticalGlue());

        PanelLogo logo = new PanelLogo("/imagenes/logo.png");
        logo.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(logo);

        panel.add(Box.createVerticalStrut(10));

        /*JLabel lblDescripcion = new JLabel(
                "<html><div style='text-align:center; width:240px'>Plataforma interna para la gestión "
                + "de servicios y mantenimiento de equipos.</div></html>");
        lblDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblDescripcion.setForeground(GRIS_OSCURO);
        lblDescripcion.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(lblDescripcion);*/

        panel.add(Box.createVerticalGlue());
        return panel;
    }

    // ---------- Lado derecho (formulario) ----------

    private JPanel construirPanelFormulario() {
        JPanel contenedor = new JPanel();
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));
        contenedor.setBackground(AZUL_MARINO);

        JPanel formulario = new JPanel();
        formulario.setLayout(new BoxLayout(formulario, BoxLayout.Y_AXIS));
        formulario.setBackground(AZUL_MARINO);
        formulario.setAlignmentX(CENTER_ALIGNMENT);
        formulario.setPreferredSize(new Dimension(420, 380));
        formulario.setMaximumSize(new Dimension(420, 380));

        JLabel lblBienvenida = new JLabel("Bienvenido(a)");
        lblBienvenida.setFont(new Font("SansSerif", Font.BOLD, 30));
        lblBienvenida.setForeground(GRIS_CLARO);
        lblBienvenida.setAlignmentX(LEFT_ALIGNMENT);
        formulario.add(lblBienvenida);

        JLabel lblSubtitulo = new JLabel("Ingresa tus credenciales para continuar");
        lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblSubtitulo.setForeground(new Color(174, 184, 204));
        lblSubtitulo.setAlignmentX(LEFT_ALIGNMENT);
        lblSubtitulo.setBorder(new EmptyBorder(6, 0, 28, 0));
        formulario.add(lblSubtitulo);

        txtUsuario = new CampoTexto("Usuario");
        configurarCampo(txtUsuario);
        formulario.add(txtUsuario);

        formulario.add(Box.createVerticalStrut(16));

        txtClave = new CampoClave("Contraseña");
        configurarCampo(txtClave);
        formulario.add(txtClave);

        formulario.add(Box.createVerticalStrut(26));

        BotonRedondeado btnIngresar = new BotonRedondeado("Ingresar");
        btnIngresar.setAlignmentX(LEFT_ALIGNMENT);
        btnIngresar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btnIngresar.setPreferredSize(new Dimension(100, 50));
        btnIngresar.addActionListener(e -> iniciarSesion());
        formulario.add(btnIngresar);

        lblError = new JLabel(" ");
        lblError.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblError.setForeground(new Color(255, 138, 138));
        lblError.setAlignmentX(LEFT_ALIGNMENT);
        lblError.setBorder(new EmptyBorder(14, 0, 0, 0));
        formulario.add(lblError);

        txtClave.addActionListener(e -> iniciarSesion());

        contenedor.add(Box.createVerticalGlue());
        contenedor.add(formulario);
        contenedor.add(Box.createVerticalGlue());
        return contenedor;
    }

    private void configurarCampo(JTextField campo) {
        campo.setFont(new Font("SansSerif", Font.PLAIN, 16));
        campo.setForeground(GRIS_OSCURO);
        campo.setCaretColor(AZUL_ELECTRICO);
        campo.setOpaque(false);
        campo.setBorder(new EmptyBorder(8, 20, 8, 20));
        campo.setAlignmentX(LEFT_ALIGNMENT);
        campo.setPreferredSize(new Dimension(100, 50));
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) { campo.repaint(); }
            @Override
            public void focusLost(FocusEvent e) { campo.repaint(); }
        });
    }

    // ---------- Lógica (sin cambios) ----------

    // DEMO: valida contra credenciales fijas (clase Sesion), sin base de datos
    private void iniciarSesion() {
        String usuario = txtUsuario.getText();
        String clave = new String(txtClave.getPassword());

        if (!Sesion.iniciar(usuario, clave)) {
            lblError.setText("Usuario o contraseña incorrectos.");
            txtClave.setText("");
            return;
        }

        cambiarEscena.cambiarEscena(new Shell(cambiarEscena));
    }

    // ================= Componentes visuales personalizados =================

    private static final int ARCO = 22;

    /** Pinta fondo blanco redondeado (antes del texto). */
    private static void pintarFondo(Graphics g, JComponent c) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, c.getWidth() - 1, c.getHeight() - 1, ARCO, ARCO);
        g2.dispose();
    }

    /** Pinta placeholder (si está vacío) y borde azul eléctrico al enfocar. */
    private static void pintarDetalles(Graphics g, JTextField c, String placeholder, boolean vacio) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        if (vacio) {
            g2.setColor(GRIS_MEDIO);
            g2.setFont(c.getFont());
            FontMetrics fm = g2.getFontMetrics();
            int x = c.getInsets().left;
            int y = (c.getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(placeholder, x, y);
        }
        if (c.hasFocus()) {
            g2.setColor(AZUL_ELECTRICO);
            g2.setStroke(new java.awt.BasicStroke(2f));
            g2.drawRoundRect(1, 1, c.getWidth() - 3, c.getHeight() - 3, ARCO, ARCO);
        }
        g2.dispose();
    }

    private static class CampoTexto extends JTextField {
        private final String placeholder;
        CampoTexto(String placeholder) { this.placeholder = placeholder; }
        @Override
        protected void paintComponent(Graphics g) {
            pintarFondo(g, this);
            super.paintComponent(g);
            pintarDetalles(g, this, placeholder, getText().isEmpty());
        }
    }

    private static class CampoClave extends JPasswordField {
        private final String placeholder;
        CampoClave(String placeholder) { this.placeholder = placeholder; }
        @Override
        protected void paintComponent(Graphics g) {
            pintarFondo(g, this);
            super.paintComponent(g);
            pintarDetalles(g, this, placeholder, getPassword().length == 0);
        }
    }

    /** Botón azul eléctrico redondeado con efecto hover. */
    private static class BotonRedondeado extends JButton {
        private boolean encima = false;

        BotonRedondeado(String texto) {
            super(texto);
            setFont(new Font("SansSerif", Font.BOLD, 16));
            setForeground(Color.WHITE);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) { encima = true; repaint(); }
                @Override
                public void mouseExited(MouseEvent e) { encima = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(encima ? new Color(0, 98, 204) : AZUL_ELECTRICO);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), ARCO, ARCO);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** Dibuja el logo escalado y centrado, manteniendo proporción (responsive). */
    private static class PanelLogo extends JPanel {
        private final Image imagen;

        PanelLogo(String ruta) {
            setOpaque(false);
            java.net.URL url = getClass().getResource(ruta);
            imagen = (url != null) ? new ImageIcon(url).getImage() : null;
            setPreferredSize(new Dimension(200, 180));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (imagen == null) {
                return;
            }
            int iw = imagen.getWidth(null);
            int ih = imagen.getHeight(null);
            if (iw <= 0 || ih <= 0) {
                return;
            }
            double escala = Math.min((double) getWidth() / iw, (double) getHeight() / ih);
            int w = (int) (iw * escala);
            int h = (int) (ih * escala);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2.drawImage(imagen, (getWidth() - w) / 2, (getHeight() - h) / 2, w, h, null);
            g2.dispose();
        }
    }
}