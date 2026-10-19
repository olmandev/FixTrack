package Vista;

import soportetecnico_g7.CambiarEscena;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * @author GRUPO #1
 * Menú principal: sidebar con iconos + tarjetas KPI + tickets que requieren
 * atención (solo Técnico/Administrador). RF07. Paleta corporativa ServiPC.
 */
public class MenuPrincipal extends JPanel {

    // ---- Paleta corporativa ----
    private static final Color AZUL_MARINO = Color.decode("#0F2D5B");
    private static final Color AZUL_ELECTRICO = Color.decode("#007BFF");
    private static final Color GRIS_MEDIO = Color.decode("#6B7280");
    private static final Color GRIS_CLARO = Color.decode("#F3F4F6");
    private static final Color GRIS_OSCURO = Color.decode("#1F2937");

    private CambiarEscena cambiarEscena;

    public MenuPrincipal(CambiarEscena cambiarEscena) {
        this.cambiarEscena = cambiarEscena;
        initComponents();
    }

    private void initComponents() {
    setLayout(new BorderLayout());
    JScrollPane scroll = new JScrollPane(construirContenido());
    scroll.setBorder(null);
    scroll.getVerticalScrollBar().setUnitIncrement(16);
    scroll.getViewport().setBackground(GRIS_CLARO);
    add(scroll, BorderLayout.CENTER);
}

    private JPanel construirContenido() {
        JPanel contenedor = new JPanel();
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));
        contenedor.setBackground(GRIS_CLARO);
        contenedor.setBorder(new EmptyBorder(40, 50, 40, 50));

        String usuario = Sesion.getUsuario();
        String rol = Sesion.getRol();

        // ---- Encabezado con avatar circular ----
        JPanel encabezado = new JPanel();
        encabezado.setLayout(new BoxLayout(encabezado, BoxLayout.X_AXIS));
        encabezado.setOpaque(false);
        encabezado.setAlignmentX(LEFT_ALIGNMENT);
        encabezado.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        encabezado.add(avatarCircular(usuario));
        encabezado.add(Box.createHorizontalStrut(15));

        JPanel textos = new JPanel();
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.setOpaque(false);

        JLabel lblBienvenida = new JLabel("Bienvenido(a), " + usuario);
        lblBienvenida.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblBienvenida.setForeground(GRIS_OSCURO);
        lblBienvenida.setAlignmentX(LEFT_ALIGNMENT);
        textos.add(lblBienvenida);

        JLabel lblRol = new JLabel("Rol: " + rol);
        lblRol.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblRol.setForeground(GRIS_MEDIO);
        lblRol.setAlignmentX(LEFT_ALIGNMENT);
        textos.add(lblRol);

        encabezado.add(textos);
        encabezado.add(Box.createHorizontalGlue());
        contenedor.add(encabezado);
        contenedor.add(Box.createVerticalStrut(28));

        // ---- Tarjetas KPI ----
        JPanel filaKpis = new JPanel(new GridLayout(1, 3, 20, 0));
        filaKpis.setOpaque(false);
        filaKpis.setAlignmentX(LEFT_ALIGNMENT);
        filaKpis.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        filaKpis.setPreferredSize(new Dimension(300, 110));

        // DEMO: datos de ejemplo fijos
        filaKpis.add(tarjetaKpi("Clientes Activos", "24", AZUL_ELECTRICO, Icono.Tipo.CLIENTES));
        filaKpis.add(tarjetaKpi("Equipos Registrados", "37", AZUL_MARINO, Icono.Tipo.EQUIPOS));
        filaKpis.add(tarjetaKpi("Tickets Abiertos", "8", GRIS_OSCURO, Icono.Tipo.TICKETS));

        contenedor.add(filaKpis);
        contenedor.add(Box.createVerticalStrut(32));

        // Tickets que requieren atención: solo Técnico o Administrador
        if (Sesion.esTecnico() || Sesion.esAdministrador()) {
            JLabel lblSeccion = new JLabel("Tickets que requieren atención");
            lblSeccion.setFont(new Font("SansSerif", Font.BOLD, 16));
            lblSeccion.setForeground(GRIS_OSCURO);
            lblSeccion.setAlignmentX(LEFT_ALIGNMENT);
            contenedor.add(lblSeccion);
            contenedor.add(Box.createVerticalStrut(12));

            String[][] demo = {
                {"1", "María Eugenia López", "20/07/2026", "Carlos Rodríguez", "En Diagnostico"},
                {"2", "Carlos Fernández",    "25/07/2026", "sin técnico asignado", "Recibido"},
                {"3", "Ana Martínez",        "28/07/2026", "Olman Alemán", "En Reparacion"}
            };
            for (String[] t : demo) {
                contenedor.add(notificacionCompacta(t[0], t[1], t[2], t[3], t[4]));
                contenedor.add(Box.createVerticalStrut(10));
            }
        }

        contenedor.add(Box.createVerticalGlue());
        return contenedor;
    }

    /** Círculo con la inicial del usuario. */
    private JPanel avatarCircular(String nombreUsuario) {
        String inicial = nombreUsuario.isEmpty() ? "?" : nombreUsuario.substring(0, 1).toUpperCase();
        JPanel circulo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AZUL_ELECTRICO);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 20));
                java.awt.FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(inicial)) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(inicial, x, y);
                g2.dispose();
            }
        };
        circulo.setPreferredSize(new Dimension(50, 50));
        circulo.setMaximumSize(new Dimension(50, 50));
        circulo.setOpaque(false);
        return circulo;
    }

    /** Tarjeta redondeada con número grande, etiqueta e icono en un recuadro de color. */
    private JPanel tarjetaKpi(String titulo, String valor, Color acento, Icono.Tipo tipo) {
        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setLayout(new BorderLayout(10, 0));
        tarjeta.setBorder(new EmptyBorder(18, 22, 18, 22));

        JPanel izq = new JPanel();
        izq.setLayout(new BoxLayout(izq, BoxLayout.Y_AXIS));
        izq.setOpaque(false);

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("SansSerif", Font.BOLD, 32));
        lblValor.setForeground(acento);
        lblValor.setAlignmentX(LEFT_ALIGNMENT);
        izq.add(lblValor);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblTitulo.setForeground(GRIS_MEDIO);
        lblTitulo.setAlignmentX(LEFT_ALIGNMENT);
        izq.add(lblTitulo);

        tarjeta.add(izq, BorderLayout.CENTER);

        JPanel recuadro = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(acento.getRed(), acento.getGreen(), acento.getBlue(), 28));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
            }
        };
        recuadro.setOpaque(false);
        recuadro.setPreferredSize(new Dimension(48, 48));
        JLabel icono = new JLabel(new Icono(tipo, 26, acento), SwingConstants.CENTER);
        recuadro.add(icono, BorderLayout.CENTER);

        JPanel derecha = new JPanel(new BorderLayout());
        derecha.setOpaque(false);
        derecha.add(recuadro, BorderLayout.NORTH);
        tarjeta.add(derecha, BorderLayout.EAST);

        return tarjeta;
    }

    /** Notificación: punto de color + info + estado. */
    private JPanel notificacionCompacta(String id, String cliente, String fecha, String tecnico, String estado) {
        Tarjeta fila = new Tarjeta();
        fila.setLayout(new BorderLayout(12, 0));
        fila.setBorder(new EmptyBorder(12, 18, 12, 18));
        fila.setAlignmentX(LEFT_ALIGNMENT);
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 54));

        JLabel punto = new JLabel("●");
        punto.setFont(new Font("SansSerif", Font.PLAIN, 16));
        punto.setForeground(colorPorEstado(estado));
        fila.add(punto, BorderLayout.WEST);

        //String textoTecnico = (tecnico == null || tecnico.isEmpty()) ? "sin técnico asignado" : tecnico;

        JLabel lblInfo = new JLabel("Ticket #" + id + " — " + cliente
            + "  ·  ingresó " + fecha + "  ·  " + tecnico);
        lblInfo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblInfo.setForeground(GRIS_OSCURO);
        fila.add(lblInfo, BorderLayout.CENTER);

        JLabel lblEstado = new JLabel(estado);
        lblEstado.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblEstado.setForeground(colorPorEstado(estado));
        fila.add(lblEstado, BorderLayout.EAST);

        return fila;
    }

    /** Colores de estado (semáforo): se mantienen aparte de la paleta para que se distingan. */
    private Color colorPorEstado(String estado) {
        if (estado == null) {
            return GRIS_MEDIO;
        }
        switch (estado) {
            case "Recibido":
                return new Color(217, 119, 6);
            case "En Diagnostico":
                return new Color(234, 88, 12);
            case "En Reparacion":
                return AZUL_ELECTRICO;
            case "Reparado":
                return new Color(22, 163, 74);
            case "Entregado":
                return GRIS_MEDIO;
            default:
                return GRIS_MEDIO;
        }
    }

    // ================= Componentes visuales =================

    /** Panel blanco con esquinas redondeadas y borde sutil. */
    private static class Tarjeta extends JPanel {
        Tarjeta() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
            g2.setColor(GRIS_CLARO);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}