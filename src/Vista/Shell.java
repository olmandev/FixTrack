package Vista;

import soportetecnico_g7.CambiarEscena;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.util.function.Supplier;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * Contenedor principal: sidebar fijo + área de contenido intercambiable.
 */
public class Shell extends JPanel {

    private static final Color AZUL_MARINO = Color.decode("#0F2D5B");
    private static final Color AZUL_ELECTRICO = Color.decode("#007BFF");
    private static final Color GRIS_CLARO = Color.decode("#F3F4F6");
    private static final Color SIDEBAR_HOVER = new Color(24, 61, 117);
    private static final Color SIDEBAR_ACTIVO = new Color(30, 75, 140);
    private static final Color TEXTO_SIDEBAR = new Color(214, 222, 236);
    private static final Color ROJO_SUAVE = new Color(255, 138, 138);
    private static final int ANCHO_SIDEBAR = 220;
    private final CambiarEscena cambiarEscena;
    private final JPanel areaContenido = new JPanel(new BorderLayout());
    private BotonNav botonActivo;

    public Shell(CambiarEscena cambiarEscena) {
        this.cambiarEscena = cambiarEscena;
        setLayout(new BorderLayout());
        areaContenido.setBackground(GRIS_CLARO);
        add(construirSidebar(), BorderLayout.WEST);
        add(areaContenido, BorderLayout.CENTER);
    }

    private JPanel construirSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(AZUL_MARINO);
        sidebar.setPreferredSize(new Dimension(ANCHO_SIDEBAR, 0));
        sidebar.setBorder(new EmptyBorder(15, 0, 15, 0));

        PanelLogo logo = new PanelLogo("/imagenes/logoClaro.png");
        logo.setAlignmentX(CENTER_ALIGNMENT);
        sidebar.add(logo);

        BotonNav inicio = agregar(sidebar, "Inicio", Icono.Tipo.INICIO,
                () -> new MenuPrincipal(cambiarEscena));

        if (Sesion.esRecepcionista() || Sesion.esAdministrador()) {
            agregar(sidebar, "Clientes", Icono.Tipo.CLIENTES, () -> new Clientes_CRUD(cambiarEscena));
            agregar(sidebar, "Equipos", Icono.Tipo.EQUIPOS, () -> new Equipos_CRUD(cambiarEscena));
        }

        agregar(sidebar, "Tickets de Soporte", Icono.Tipo.TICKETS, () -> new Tickets_CRUD(cambiarEscena));

        if (Sesion.esAdministrador()) {
            agregar(sidebar, "Reportes (MIS)", Icono.Tipo.REPORTES, () -> new Reportes_MIS(cambiarEscena));
        }

        sidebar.add(Box.createVerticalGlue());

        TarjetaPerfil perfil = new TarjetaPerfil(Sesion.getUsuario(), Sesion.getRol());
        perfil.addActionListener(e -> navegar(null, () -> new Perfil()));
        sidebar.add(perfil);
        sidebar.add(Box.createVerticalStrut(6));

        BotonNav salir = new BotonNav("Cerrar Sesión", Icono.Tipo.SALIR);
        salir.setColorTexto(ROJO_SUAVE);
        salir.addActionListener(e -> {
            Sesion.cerrar();
            cambiarEscena.cambiarEscena(new Login(cambiarEscena));
        });
        sidebar.add(salir);

        // Pantalla inicial
        navegar(inicio, () -> new MenuPrincipal(cambiarEscena));
        return sidebar;
    }

    private BotonNav agregar(JPanel sidebar, String texto, Icono.Tipo tipo, Supplier<JPanel> fabrica) {
        BotonNav b = new BotonNav(texto, tipo);
        b.addActionListener(e -> navegar(b, fabrica));
        sidebar.add(b);
        sidebar.add(Box.createVerticalStrut(6));
        return b;
    }

    /** Tarjeta con avatar, usuario y rol. Al hacer clic abre el perfil. */
    private static class TarjetaPerfil extends JButton {
        private final String usuario;
        private final String rol;
        private boolean encima = false;

        TarjetaPerfil(String usuario, String rol) {
            this.usuario = usuario;
            this.rol = rol;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setAlignmentX(CENTER_ALIGNMENT);
            setPreferredSize(new Dimension(ANCHO_SIDEBAR - 24, 64));
            setMaximumSize(new Dimension(ANCHO_SIDEBAR - 24, 64));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) { encima = true; repaint(); }
                @Override
                public void mouseExited(java.awt.event.MouseEvent e) { encima = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(java.awt.Graphics g) {
            java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
            g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            if (encima) {
                g2.setColor(SIDEBAR_HOVER);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
            }

            // Avatar circular con la inicial
            int d = 40, x = 12, y = (getHeight() - d) / 2;
            g2.setColor(AZUL_ELECTRICO);
            g2.fillOval(x, y, d, d);
            String inicial = usuario.isEmpty() ? "?" : usuario.substring(0, 1).toUpperCase();
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 17));
            java.awt.FontMetrics fm = g2.getFontMetrics();
            g2.drawString(inicial, x + (d - fm.stringWidth(inicial)) / 2, y + (d - fm.getHeight()) / 2 + fm.getAscent());

            // Usuario y rol
            g2.setFont(new Font("SansSerif", Font.BOLD, 13));
            g2.setColor(Color.WHITE);
            g2.drawString(usuario, x + d + 12, getHeight() / 2 - 2);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
            g2.setColor(new Color(150, 170, 200));
            g2.drawString(rol, x + d + 12, getHeight() / 2 + 14);
            g2.dispose();
        }
    }

    private void navegar(BotonNav boton, Supplier<JPanel> fabrica) {
        if (botonActivo != null) {
            botonActivo.setActivo(false);
        }
        botonActivo = boton;
        if (boton != null) {
            boton.setActivo(true);
        }
        areaContenido.removeAll();
        areaContenido.add(fabrica.get(), BorderLayout.CENTER);
        areaContenido.revalidate();
        areaContenido.repaint();
}

    /** Botón de navegación con estado activo/hover. */
    private static class BotonNav extends JButton {
        private final Icono.Tipo tipo;
        private boolean activo = false;
        private Color colorTexto = TEXTO_SIDEBAR;

        BotonNav(String texto, Icono.Tipo tipo) {
            super(texto);
            this.tipo = tipo;
            setOpaque(true);
            setContentAreaFilled(true);
            setBorderPainted(false);
            setFocusPainted(false);
            setHorizontalAlignment(SwingConstants.LEFT);
            setIconTextGap(14);
            setAlignmentX(CENTER_ALIGNMENT);
            setMaximumSize(new Dimension(ANCHO_SIDEBAR, 48));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    if (!BotonNav.this.activo) setBackground(SIDEBAR_HOVER);
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    if (!BotonNav.this.activo) setBackground(AZUL_MARINO);
                }
            });
            setActivo(false);
        }

        void setColorTexto(Color c) {
            this.colorTexto = c;
            setActivo(activo);
        }

        void setActivo(boolean a) {
            this.activo = a;
            Color fg = a ? Color.WHITE : colorTexto;
            setBackground(a ? SIDEBAR_ACTIVO : AZUL_MARINO);
            setForeground(fg);
            setFont(new Font("SansSerif", a ? Font.BOLD : Font.PLAIN, 14));
            setIcon(new Icono(tipo, 20, fg));
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 4, 0, 0, a ? AZUL_ELECTRICO : AZUL_MARINO),
                    new EmptyBorder(13, 20, 13, 15)));
        }
    }
    /** Dibuja el logo escalado y centrado, manteniendo proporción (responsive). */
    private static class PanelLogo extends JPanel {
        private final Image imagen;

        PanelLogo(String ruta) {
            setOpaque(false);
            java.net.URL url = getClass().getResource(ruta);
            imagen = (url != null) ? new ImageIcon(url).getImage() : null;
            setPreferredSize(new Dimension(120, 70));
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