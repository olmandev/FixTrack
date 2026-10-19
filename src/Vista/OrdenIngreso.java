package Vista;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * Vista previa de la orden de ingreso (comprobante que se entrega al cliente
 * al recibir su equipo). Solo visual: usa datos de ejemplo fijos.
 */
public class OrdenIngreso extends JDialog {

    private static final Color AZUL_MARINO = Color.decode("#0F2D5B");
    private static final Color AZUL_ELECTRICO = Color.decode("#007BFF");
    private static final Color GRIS_MEDIO = Color.decode("#6B7280");
    private static final Color GRIS_CLARO = Color.decode("#F3F4F6");
    private static final Color GRIS_OSCURO = Color.decode("#1F2937");
    private static final Color BORDE = new Color(209, 213, 219);

    private final JPanel hoja;

    public OrdenIngreso(Window owner) {
        super(owner, "Orden de ingreso", ModalityType.APPLICATION_MODAL);
        hoja = construirHoja();

        // "Mesa" gris con la hoja blanca centrada, con scroll
        JPanel mesa = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 24));
        mesa.setBackground(new Color(209, 213, 219));
        mesa.add(hoja);
        JScrollPane scroll = new JScrollPane(mesa);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        // Barra inferior con acciones
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        barra.setBackground(Color.WHITE);
        JButton btnCerrar = boton("Cerrar", false);
        JButton btnImprimir = boton("Imprimir", true);
        btnCerrar.addActionListener(e -> dispose());
        btnImprimir.addActionListener(e -> imprimir());
        barra.add(btnCerrar);
        barra.add(btnImprimir);

        setLayout(new BorderLayout());
        add(scroll, BorderLayout.CENTER);
        add(barra, BorderLayout.SOUTH);

        int altoPantalla = Toolkit.getDefaultToolkit().getScreenSize().height;
        setSize(740, Math.min(900, altoPantalla - 80));
        setLocationRelativeTo(owner);
    }

    // ---------- La hoja ----------

    private JPanel construirHoja() {
        JPanel h = new JPanel();
        h.setLayout(new BoxLayout(h, BoxLayout.Y_AXIS));
        h.setBackground(Color.WHITE);
        h.setBorder(new EmptyBorder(34, 44, 34, 44));
        h.setPreferredSize(new Dimension(680, 900));

        h.add(encabezado());
        h.add(Box.createVerticalStrut(18));

        h.add(seccion("DATOS DEL CLIENTE"));
        h.add(Box.createVerticalStrut(8));
        h.add(dosColumnas(campo("Nombre", "María Eugenia López"), campo("Teléfono", "(504) 9865-6566")));
        h.add(Box.createVerticalStrut(6));
        h.add(dosColumnas(campo("Correo", "mlopez@correo.com"), campo("Fecha de ingreso", "20/07/2026")));
        h.add(Box.createVerticalStrut(16));

        h.add(seccion("EQUIPO RECIBIDO"));
        h.add(Box.createVerticalStrut(8));
        h.add(dosColumnas(campo("Tipo", "Laptop"), campo("Marca / Modelo", "HP Pavilion 15")));
        h.add(Box.createVerticalStrut(6));
        h.add(dosColumnas(campo("Número de serie", "SN-HP-001"), campo("Técnico asignado", "Por asignar")));
        h.add(Box.createVerticalStrut(16));

        h.add(seccion("FALLA REPORTADA"));
        h.add(Box.createVerticalStrut(8));
        JLabel falla = new JLabel("<html><div style='width:520px'>El equipo no enciende. El cliente indica que dejó de "
                + "funcionar después de una descarga eléctrica. Se recibe con cargador y sin maletín.</div></html>");
        falla.setFont(new Font("SansSerif", Font.PLAIN, 13));
        falla.setForeground(GRIS_OSCURO);
        falla.setAlignmentX(LEFT_ALIGNMENT);
        h.add(falla);
        h.add(Box.createVerticalStrut(16));

        h.add(seccion("COSTO ESTIMADO"));
        h.add(Box.createVerticalStrut(8));
        h.add(filaCosto("Revisión y diagnóstico", "L. 150.00", false));
        h.add(filaCosto("Mano de obra", "L. 400.00", false));
        h.add(filaCosto("Repuestos", "L. 300.00", false));
        h.add(filaCosto("TOTAL", "L. 850.00", true));
        h.add(Box.createVerticalStrut(14));

        JLabel cond = new JLabel("<html><div style='width:540px'><b>Condiciones:</b> el costo es un estimado y puede variar "
                + "tras el diagnóstico. Presente este comprobante para retirar su equipo. Equipos no retirados "
                + "después de 30 días no generan responsabilidad para la empresa.</div></html>");
        cond.setFont(new Font("SansSerif", Font.PLAIN, 11));
        cond.setForeground(GRIS_MEDIO);
        cond.setAlignmentX(LEFT_ALIGNMENT);
        h.add(cond);

        h.add(Box.createVerticalGlue());
        h.add(Box.createVerticalStrut(30));
        h.add(firmas());
        return h;
    }

    private JPanel encabezado() {
        JPanel p = new JPanel(new BorderLayout(16, 0));
        p.setOpaque(false);
        p.setAlignmentX(LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        // Logo (si no se encuentra, simplemente no se muestra)
        java.net.URL url = getClass().getResource("/imagenes/logo.png");
        if (url != null) {
            Image img = new ImageIcon(url).getImage().getScaledInstance(-1, 70, Image.SCALE_SMOOTH);
            p.add(new JLabel(new ImageIcon(img)), BorderLayout.WEST);
        }

        JPanel datos = new JPanel();
        datos.setLayout(new BoxLayout(datos, BoxLayout.Y_AXIS));
        datos.setOpaque(false);
        datos.add(texto("ServiPC", 20, true, AZUL_MARINO));
        datos.add(texto("Soluciones Informáticas", 12, false, GRIS_MEDIO));
        datos.add(texto("San pedro sula, Cortes, Honduras", 11, false, GRIS_MEDIO));
        datos.add(texto("Tel: (504) 2200-0000  ·  DarwinR@servipc.com", 11, false, GRIS_MEDIO));
        p.add(datos, BorderLayout.CENTER);

        // Recuadro con el número de orden
        JPanel caja = new JPanel();
        caja.setLayout(new BoxLayout(caja, BoxLayout.Y_AXIS));
        caja.setBackground(GRIS_CLARO);
        caja.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE), new EmptyBorder(8, 14, 8, 14)));
        caja.add(texto("ORDEN DE INGRESO", 11, true, GRIS_MEDIO));
        caja.add(texto("N.º 0001", 20, true, AZUL_ELECTRICO));
        p.add(caja, BorderLayout.EAST);
        return p;
    }

    private JPanel seccion(String titulo) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(AZUL_MARINO);
        p.setBorder(new EmptyBorder(5, 10, 5, 10));
        p.setAlignmentX(LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        JLabel l = new JLabel(titulo);
        l.setFont(new Font("SansSerif", Font.BOLD, 11));
        l.setForeground(Color.WHITE);
        p.add(l, BorderLayout.WEST);
        return p;
    }

    private JPanel campo(String etiqueta, String valor) {
        JPanel p = new JPanel(new BorderLayout(0, 1));
        p.setOpaque(false);
        JLabel e = new JLabel(etiqueta);
        e.setFont(new Font("SansSerif", Font.BOLD, 10));
        e.setForeground(GRIS_MEDIO);
        JLabel v = new JLabel(valor);
        v.setFont(new Font("SansSerif", Font.PLAIN, 13));
        v.setForeground(GRIS_OSCURO);
        p.add(e, BorderLayout.NORTH);
        p.add(v, BorderLayout.CENTER);
        return p;
    }

    private JPanel dosColumnas(JPanel a, JPanel b) {
        JPanel p = new JPanel(new GridLayout(1, 2, 16, 0));
        p.setOpaque(false);
        p.setAlignmentX(LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        p.add(a);
        p.add(b);
        return p;
    }

    private JPanel filaCosto(String concepto, String monto, boolean total) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(!total ? false : true);
        p.setBackground(GRIS_CLARO);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDE), new EmptyBorder(6, 10, 6, 10)));
        p.setAlignmentX(LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        JLabel a = texto(concepto, 13, total, GRIS_OSCURO);
        JLabel b = texto(monto, 13, total, total ? AZUL_MARINO : GRIS_OSCURO);
        b.setHorizontalAlignment(SwingConstants.RIGHT);
        p.add(a, BorderLayout.WEST);
        p.add(b, BorderLayout.EAST);
        return p;
    }

    private JPanel firmas() {
        JPanel p = new JPanel(new GridLayout(1, 2, 70, 0));
        p.setOpaque(false);
        p.setAlignmentX(LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        p.add(bloqueFirma("Firma del cliente", "Nombre y DNI"));
        p.add(bloqueFirma("Firma y sello de ServiPC", "Recibido por"));
        return p;
    }

    private JPanel bloqueFirma(String titulo, String subtitulo) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, GRIS_OSCURO), new EmptyBorder(6, 0, 0, 0)));
        JLabel t = texto(titulo, 12, true, GRIS_OSCURO);
        t.setAlignmentX(CENTER_ALIGNMENT);
        JLabel s = texto(subtitulo, 10, false, GRIS_MEDIO);
        s.setAlignmentX(CENTER_ALIGNMENT);
        p.add(t);
        p.add(s);
        return p;
    }

    private JLabel texto(String t, int size, boolean negrita, Color color) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("SansSerif", negrita ? Font.BOLD : Font.PLAIN, size));
        l.setForeground(color);
        return l;
    }

    private JButton boton(String texto, boolean principal) {
        JButton b = new JButton(texto);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setOpaque(true);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setBackground(principal ? AZUL_ELECTRICO : GRIS_CLARO);
        b.setForeground(principal ? Color.WHITE : GRIS_OSCURO);
        b.setBorder(new EmptyBorder(10, 18, 10, 18));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    // ---------- Impresión ----------

    /** Abre el diálogo de impresión; ahí también se puede elegir "Guardar como PDF". */
    private void imprimir() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Orden de ingreso");
        job.setPrintable((g, pf, pagina) -> {
            if (pagina > 0) {
                return java.awt.print.Printable.NO_SUCH_PAGE;
            }
            Graphics2D g2 = (Graphics2D) g;
            g2.translate(pf.getImageableX(), pf.getImageableY());
            double escala = Math.min(pf.getImageableWidth() / hoja.getWidth(),
                    pf.getImageableHeight() / hoja.getHeight());
            g2.scale(escala, escala);
            hoja.printAll(g2);
            return java.awt.print.Printable.PAGE_EXISTS;
        });
        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException ex) {
                javax.swing.JOptionPane.showMessageDialog(this, "No se pudo imprimir.");
            }
        }
    }
}