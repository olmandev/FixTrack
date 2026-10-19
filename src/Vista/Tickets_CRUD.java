package Vista;

import soportetecnico_g7.CambiarEscena;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

/**
 * @author GRUPO #1
 * RF03-RF06: Gestión de tickets de soporte.
 * VERSIÓN SOLO VISUAL (prototipo): los botones y formularios son apariencia,
 * la tabla muestra datos de ejemplo fijos. Solo el buscador y las pestañas
 * (cambio de formulario) funcionan.
 */
public class Tickets_CRUD extends JPanel {

    // ---- Paleta corporativa ----
    private static final Color AZUL_MARINO = Color.decode("#0F2D5B");
    private static final Color AZUL_ELECTRICO = Color.decode("#007BFF");
    private static final Color GRIS_MEDIO = Color.decode("#6B7280");
    private static final Color GRIS_CLARO = Color.decode("#F3F4F6");
    private static final Color GRIS_OSCURO = Color.decode("#1F2937");
    private static final Color BORDE = new Color(224, 227, 232);
    private static final Color ROJO = new Color(220, 38, 38);

    private DefaultTableModel modeloTabla;
    private JTable tabla;
    private TableRowSorter<DefaultTableModel> filtro;
    private CambiarEscena cambiarEscena;
    private JTextField txtBuscar;

    // Pestañas del formulario
    private JPanel tarjetasForm;
    private Pestana pestNuevo;
    private Pestana pestDiag;

    public Tickets_CRUD(CambiarEscena cambiarEscena) {
        this.cambiarEscena = cambiarEscena;
        initComponents();
        cargarDatosEjemplo();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(GRIS_CLARO);

        JPanel pagina = new JPanel();
        pagina.setLayout(new BoxLayout(pagina, BoxLayout.Y_AXIS));
        pagina.setBackground(GRIS_CLARO);
        pagina.setBorder(new EmptyBorder(30, 36, 30, 36));

        JPanel encabezado = construirEncabezado();
        JPanel formulario = construirFormulario();
        JPanel listado = construirListado();

        encabezado.setAlignmentX(LEFT_ALIGNMENT);
        formulario.setAlignmentX(LEFT_ALIGNMENT);
        listado.setAlignmentX(LEFT_ALIGNMENT);

        listado.setPreferredSize(new Dimension(100, 420));
        listado.setMinimumSize(new Dimension(100, 420));
        listado.setMaximumSize(new Dimension(Integer.MAX_VALUE, 420));

        pagina.add(encabezado);
        pagina.add(Box.createVerticalStrut(22));
        pagina.add(formulario);
        pagina.add(Box.createVerticalStrut(22));
        pagina.add(listado);

        JScrollPane scroll = new JScrollPane(pagina);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getViewport().setBackground(GRIS_CLARO);
        add(scroll, BorderLayout.CENTER);
    }

    // ---------- Encabezado ----------

    private JPanel construirEncabezado() {
        JPanel p = new JPanel(new BorderLayout(16, 0));
        p.setOpaque(false);

        JPanel recuadro = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AZUL_MARINO);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
            }
        };
        recuadro.setOpaque(false);
        recuadro.setPreferredSize(new Dimension(52, 52));
        recuadro.add(new JLabel(new Icono(Icono.Tipo.TICKETS, 28, Color.WHITE), SwingConstants.CENTER));
        p.add(recuadro, BorderLayout.WEST);

        JPanel textos = new JPanel();
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.setOpaque(false);
        JLabel titulo = new JLabel("Tickets de Soporte");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        titulo.setForeground(GRIS_OSCURO);
        JLabel sub = new JLabel("Registra fallas, diagnósticos y da seguimiento al estado de cada servicio");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        sub.setForeground(GRIS_MEDIO);
        textos.add(titulo);
        textos.add(sub);
        p.add(textos, BorderLayout.CENTER);
        return p;
    }

    // ---------- Formulario con pestañas (solo visual) ----------

    private JPanel construirFormulario() {
        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setLayout(new BorderLayout(0, 18));
        tarjeta.setBorder(new EmptyBorder(22, 26, 22, 26));

        // Fila de pestañas
        JPanel pestanas = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pestanas.setOpaque(false);
        pestNuevo = new Pestana("Nuevo ticket", Icono.Tipo.NUEVO);
        pestDiag = new Pestana("Diagnóstico, estado y costo", Icono.Tipo.EDITAR);
        pestanas.add(pestNuevo);
        pestanas.add(pestDiag);
        tarjeta.add(pestanas, BorderLayout.NORTH);

        // Contenido de cada pestaña (CardLayout: solo se ve una a la vez)
        tarjetasForm = new JPanel(new CardLayout());
        tarjetasForm.setOpaque(false);
        tarjetasForm.add(construirPanelNuevo(), "nuevo");
        tarjetasForm.add(construirPanelDiagnostico(), "diag");
        tarjeta.add(tarjetasForm, BorderLayout.CENTER);

        pestNuevo.addActionListener(e -> mostrarPestana("nuevo"));
        pestDiag.addActionListener(e -> mostrarPestana("diag"));
        mostrarPestana("nuevo");

        return tarjeta;
    }

    private void mostrarPestana(String nombre) {
        ((CardLayout) tarjetasForm.getLayout()).show(tarjetasForm, nombre);
        pestNuevo.setActiva(nombre.equals("nuevo"));
        pestDiag.setActiva(nombre.equals("diag"));
    }

    private JPanel construirPanelNuevo() {
        JPanel panel = new JPanel(new BorderLayout(0, 18));
        panel.setOpaque(false);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints g = nuevasReglas();

        agregarCampo(form, g, 0, 0, 1, "Cliente", combo(new String[]{
            "1 - María Eugenia López", "2 - Carlos Fernández", "4 - Jorge Ramírez"}));
        agregarCampo(form, g, 1, 0, 1, "Equipo recibido", combo(new String[]{
            "1 - HP Pavilion 15 (SN-HP-001)", "4 - Lenovo IdeaPad 3 (SN-LN-004)"}));
        agregarCampo(form, g, 0, 1, 2, "Falla reportada", areaTexto(3));
        panel.add(form, BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        botones.setOpaque(false);
        botones.add(new BotonRedondeado("Crear ticket", Icono.Tipo.GUARDAR, 0));
        botones.add(new BotonRedondeado("Limpiar", Icono.Tipo.NUEVO, 1));
        BotonRedondeado btnOrden = new BotonRedondeado("Ver orden de ingreso", Icono.Tipo.TICKETS, 1);
        btnOrden.addActionListener(e -> new OrdenIngreso(SwingUtilities.getWindowAncestor(this)).setVisible(true));
        botones.add(btnOrden);
        panel.add(botones, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel construirPanelDiagnostico() {
        JPanel panel = new JPanel(new BorderLayout(0, 18));
        panel.setOpaque(false);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints g = nuevasReglas();

        JTextField txtTicket = campo();
        txtTicket.setEditable(false);
        txtTicket.setText("Ninguno seleccionado");

        agregarCampo(form, g, 0, 0, 1, "Ticket seleccionado", txtTicket);
        agregarCampo(form, g, 1, 0, 1, "Técnico asignado", combo(new String[]{
            "Carlos Rodríguez", "Olman Alemán", "Luis Alvarado"}));
        agregarCampo(form, g, 0, 1, 1, "Estado del servicio", combo(new String[]{
            "Recibido", "En Diagnostico", "En Reparacion", "Reparado", "Entregado"}));
        agregarCampo(form, g, 1, 1, 1, "Costo estimado (L.)", campo());
        agregarCampo(form, g, 0, 2, 1, "Fecha de ingreso (dd/mm/aaaa)", campo());
        agregarCampo(form, g, 0, 3, 1, "Diagnóstico técnico", areaTexto(3));
        agregarCampo(form, g, 1, 3, 1, "Reparación realizada", areaTexto(3));
        panel.add(form, BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        botones.setOpaque(false);
        botones.add(new BotonRedondeado("Guardar cambios", Icono.Tipo.GUARDAR, 0));
        botones.add(new BotonRedondeado("Cargar seleccionado", Icono.Tipo.EDITAR, 1));
        panel.add(botones, BorderLayout.SOUTH);
        return panel;
    }

    private GridBagConstraints nuevasReglas() {
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 0.5;
        g.insets = new Insets(0, 0, 14, 18);
        g.anchor = GridBagConstraints.NORTHWEST;
        return g;
    }

    /** ancho = cuántas columnas ocupa el campo (1 o 2). */
    private void agregarCampo(JPanel form, GridBagConstraints g, int col, int fila, int ancho, String texto, Component c) {
        JPanel celda = new JPanel(new BorderLayout(0, 6));
        celda.setOpaque(false);
        JLabel l = new JLabel(texto);
        l.setFont(new Font("SansSerif", Font.BOLD, 12));
        l.setForeground(GRIS_MEDIO);
        celda.add(l, BorderLayout.NORTH);
        celda.add(c, BorderLayout.CENTER);
        g.gridx = col;
        g.gridy = fila;
        g.gridwidth = ancho;
        form.add(celda, g);
        g.gridwidth = 1;
    }

    private JComboBox<String> combo(String[] opciones) {
        JComboBox<String> c = new JComboBox<>(opciones);
        c.setFont(new Font("SansSerif", Font.PLAIN, 14));
        c.setPreferredSize(new Dimension(100, 42));
        return c;
    }

    private JScrollPane areaTexto(int filas) {
        JTextArea a = new JTextArea(filas, 20);
        a.setFont(new Font("SansSerif", Font.PLAIN, 14));
        a.setForeground(GRIS_OSCURO);
        a.setLineWrap(true);
        a.setWrapStyleWord(true);
        a.setBorder(new EmptyBorder(8, 10, 8, 10));
        JScrollPane sp = new JScrollPane(a);
        sp.setBorder(new BordeRedondeado(BORDE));
        sp.setPreferredSize(new Dimension(100, 86));
        return sp;
    }

    private JTextField campo() {
        JTextField c = new JTextField();
        c.setFont(new Font("SansSerif", Font.PLAIN, 14));
        c.setForeground(GRIS_OSCURO);
        c.setCaretColor(AZUL_ELECTRICO);
        c.setBackground(Color.WHITE);
        c.setPreferredSize(new Dimension(100, 42));
        c.setBorder(BorderFactory.createCompoundBorder(new BordeRedondeado(BORDE), new EmptyBorder(6, 12, 6, 12)));
        c.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                c.setBorder(BorderFactory.createCompoundBorder(new BordeRedondeado(AZUL_ELECTRICO), new EmptyBorder(6, 12, 6, 12)));
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                c.setBorder(BorderFactory.createCompoundBorder(new BordeRedondeado(BORDE), new EmptyBorder(6, 12, 6, 12)));
            }
        });
        return c;
    }

    // ---------- Listado ----------

    private JPanel construirListado() {
        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setLayout(new BorderLayout(0, 14));
        tarjeta.setBorder(new EmptyBorder(20, 26, 20, 26));

        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setOpaque(false);
        JLabel lbl = new JLabel("Listado de tickets");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 16));
        lbl.setForeground(GRIS_OSCURO);
        cabecera.add(lbl, BorderLayout.WEST);

        JPanel buscador = new JPanel(new BorderLayout(8, 0));
        buscador.setOpaque(false);
        buscador.add(new JLabel(new Icono(Icono.Tipo.BUSCAR, 18, GRIS_MEDIO)), BorderLayout.WEST);
        txtBuscar = campo();
        txtBuscar.setPreferredSize(new Dimension(260, 38));
        buscador.add(txtBuscar, BorderLayout.CENTER);
        cabecera.add(buscador, BorderLayout.EAST);
        tarjeta.add(cabecera, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(new Object[]{
            "Ticket", "Cliente", "Equipo", "Falla", "Estado", "Técnico", "Ingreso", "Costo (L.)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        estilizarTabla(tabla);

        filtro = new TableRowSorter<>(modeloTabla);
        tabla.setRowSorter(filtro);
        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            private void aplicar() {
                String t = txtBuscar.getText().trim();
                filtro.setRowFilter(t.isEmpty() ? null
                        : RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(t)));
            }
            @Override public void insertUpdate(DocumentEvent e) { aplicar(); }
            @Override public void removeUpdate(DocumentEvent e) { aplicar(); }
            @Override public void changedUpdate(DocumentEvent e) { aplicar(); }
        });

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(BORDE, 1));
        scroll.getViewport().setBackground(Color.WHITE);
        tarjeta.add(scroll, BorderLayout.CENTER);
        return tarjeta;
    }

    private void estilizarTabla(JTable t) {
        t.setFont(new Font("SansSerif", Font.PLAIN, 13));
        t.setForeground(GRIS_OSCURO);
        t.setRowHeight(38);
        t.setShowVerticalLines(false);
        t.setShowHorizontalLines(true);
        t.setGridColor(new Color(238, 240, 243));
        t.setIntercellSpacing(new Dimension(0, 1));
        t.setSelectionBackground(new Color(219, 234, 254));
        t.setSelectionForeground(GRIS_OSCURO);
        t.setFillsViewportHeight(true);

        JTableHeader h = t.getTableHeader();
        h.setPreferredSize(new Dimension(100, 40));
        h.setReorderingAllowed(false);
        h.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tb, Object v, boolean s, boolean f, int r, int c) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(tb, v, false, false, r, c);
                l.setBackground(AZUL_MARINO);
                l.setForeground(Color.WHITE);
                l.setFont(new Font("SansSerif", Font.BOLD, 13));
                l.setBorder(new EmptyBorder(0, 12, 0, 12));
                l.setOpaque(true);
                return l;
            }
        });

        // Celdas: filas alternadas y columna Estado (índice 4) con color de semáforo
        t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tb, Object v, boolean sel, boolean f, int r, int c) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(tb, v, sel, false, r, c);
                l.setBorder(new EmptyBorder(0, 12, 0, 12));
                if (!sel) {
                    l.setBackground(r % 2 == 0 ? Color.WHITE : new Color(249, 250, 251));
                }
                l.setForeground(GRIS_OSCURO);
                l.setFont(new Font("SansSerif", Font.PLAIN, 13));
                if (c == 4 && v != null) {
                    l.setText("●  " + v);
                    l.setForeground(colorPorEstado(v.toString()));
                    l.setFont(new Font("SansSerif", Font.BOLD, 12));
                }
                return l;
            }
        });

        // Columnas angostas para ID y costo
        t.getColumnModel().getColumn(0).setMaxWidth(80);
        t.getColumnModel().getColumn(7).setMaxWidth(110);
    }

    private static Color colorPorEstado(String estado) {
        switch (estado) {
            case "Recibido":       return new Color(217, 119, 6);
            case "En Diagnostico": return new Color(234, 88, 12);
            case "En Reparacion":  return AZUL_ELECTRICO;
            case "Reparado":       return new Color(22, 163, 74);
            default:               return GRIS_MEDIO;
        }
    }

    // ---------- Datos de ejemplo ----------

    private void cargarDatosEjemplo() {
        modeloTabla.setRowCount(0);
        modeloTabla.addRow(new Object[]{1, "María Eugenia López", "HP Pavilion 15", "El equipo no enciende", "En Diagnostico", "Carlos Rodríguez", "20/07/2026", "850.00"});
        modeloTabla.addRow(new Object[]{2, "Carlos Fernández", "Dell OptiPlex 3020", "Pantalla azul constante", "Recibido", "Sin asignar", "25/07/2026", "0.00"});
        modeloTabla.addRow(new Object[]{3, "Jorge Ramírez", "Epson L3150", "No imprime a color", "En Reparacion", "Olman Alemán", "28/07/2026", "420.00"});
        modeloTabla.addRow(new Object[]{4, "Lucía Mendoza", "Asus VivoPC", "Lentitud al iniciar", "Reparado", "Luis Alvarado", "02/08/2026", "300.00"});
        modeloTabla.addRow(new Object[]{5, "María Eugenia López", "Lenovo IdeaPad 3", "Batería no carga", "Entregado", "Carlos Rodríguez", "05/08/2026", "650.00"});
    }

    // ================= Componentes visuales =================

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
            g2.setColor(BORDE);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class BordeRedondeado extends AbstractBorder {
        private final Color color;

        BordeRedondeado(Color color) {
            this.color = color;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, w - 1, h - 1, 12, 12);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(1, 1, 1, 1);
        }
    }

    /** Botón redondeado con icono. estilo: 0 = principal, 1 = secundario, 2 = peligro. Sin acción. */
    private static class BotonRedondeado extends JButton {
        private final int estilo;
        private boolean encima = false;

        BotonRedondeado(String texto, Icono.Tipo tipo, int estilo) {
            super(texto);
            this.estilo = estilo;
            Color fg = estilo == 0 ? Color.WHITE : (estilo == 2 ? ROJO : GRIS_OSCURO);
            setForeground(fg);
            setIcon(new Icono(tipo, 17, fg));
            setIconTextGap(8);
            setFont(new Font("SansSerif", Font.BOLD, 13));
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setBorder(new EmptyBorder(11, 18, 11, 18));
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
            int w = getWidth() - 1, h = getHeight() - 1;
            if (estilo == 0) {
                g2.setColor(encima ? new Color(0, 98, 204) : AZUL_ELECTRICO);
                g2.fillRoundRect(0, 0, w, h, 12, 12);
            } else if (estilo == 1) {
                g2.setColor(encima ? GRIS_CLARO : Color.WHITE);
                g2.fillRoundRect(0, 0, w, h, 12, 12);
                g2.setColor(BORDE);
                g2.drawRoundRect(0, 0, w, h, 12, 12);
            } else {
                g2.setColor(encima ? new Color(254, 242, 242) : Color.WHITE);
                g2.fillRoundRect(0, 0, w, h, 12, 12);
                g2.setColor(ROJO);
                g2.drawRoundRect(0, 0, w, h, 12, 12);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** Botón tipo pestaña: azul marino cuando está activa, blanco con borde cuando no. */
    private static class Pestana extends JButton {
        private final Icono.Tipo tipo;
        private boolean activa = false;

        Pestana(String texto, Icono.Tipo tipo) {
            super(texto);
            this.tipo = tipo;
            setIconTextGap(8);
            setFont(new Font("SansSerif", Font.BOLD, 13));
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setBorder(new EmptyBorder(10, 18, 10, 18));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setActiva(false);
        }

        void setActiva(boolean a) {
            this.activa = a;
            Color fg = a ? Color.WHITE : GRIS_MEDIO;
            setForeground(fg);
            setIcon(new Icono(tipo, 16, fg));
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth() - 1, h = getHeight() - 1;
            if (activa) {
                g2.setColor(AZUL_MARINO);
                g2.fillRoundRect(0, 0, w, h, 12, 12);
            } else {
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, w, h, 12, 12);
                g2.setColor(BORDE);
                g2.drawRoundRect(0, 0, w, h, 12, 12);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }
}