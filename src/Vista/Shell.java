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
    private static final int ANCHO_SIDEBAR = 240;

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
        sidebar.setBorder(new EmptyBorder(28, 0, 20, 0));

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

    private void navegar(BotonNav boton, Supplier<JPanel> fabrica) {
        if (botonActivo != null) {
            botonActivo.setActivo(false);
        }
        botonActivo = boton;
        boton.setActivo(true);
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