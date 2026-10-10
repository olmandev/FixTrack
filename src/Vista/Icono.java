package Vista;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.Icon;

public class Icono implements Icon {
    public enum Tipo { INICIO, CLIENTES, EQUIPOS, TICKETS, REPORTES, SALIR, NUEVO, GUARDAR, EDITAR, ELIMINAR, BUSCAR }

    private final Tipo tipo;
    private final int size;
    private final Color color;

    public Icono(Tipo tipo, int size, Color color) {
        this.tipo = tipo;
        this.size = size;
        this.color = color;
    }

    @Override
    public int getIconWidth() {
        return size;
    }

    @Override
    public int getIconHeight() {
        return size;
    }

    
    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.translate(x, y);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2.setColor(color);
        g2.setStroke(new BasicStroke(size / 12f + 0.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        int s = size;

        switch (tipo) {
            case INICIO:
                g2.drawPolyline(new int[] { s * 12 / 100, s / 2, s * 88 / 100 },
                        new int[] { s * 50 / 100, s * 12 / 100, s * 50 / 100 }, 3);
                g2.drawPolyline(new int[] { s * 22 / 100, s * 22 / 100, s * 78 / 100, s * 78 / 100 },
                        new int[] { s * 44 / 100, s * 86 / 100, s * 86 / 100, s * 44 / 100 }, 4);
                g2.drawRect(s * 42 / 100, s * 60 / 100, s * 16 / 100, s * 26 / 100);
                break;
            case CLIENTES:
                g2.drawOval(s * 31 / 100, s * 10 / 100, s * 38 / 100, s * 38 / 100);
                g2.drawArc(s * 12 / 100, s * 55 / 100, s * 76 / 100, s * 70 / 100, 0, 180);
                break;
            case EQUIPOS:
                g2.drawRoundRect(s * 10 / 100, s * 12 / 100, s * 80 / 100, s * 56 / 100, 4, 4);
                g2.drawLine(s / 2, s * 68 / 100, s / 2, s * 86 / 100);
                g2.drawLine(s * 30 / 100, s * 86 / 100, s * 70 / 100, s * 86 / 100);
                break;
            case TICKETS:
                g2.drawRoundRect(s * 16 / 100, s * 10 / 100, s * 68 / 100, s * 80 / 100, 5, 5);
                g2.drawLine(s * 32 / 100, s * 34 / 100, s * 68 / 100, s * 34 / 100);
                g2.drawLine(s * 32 / 100, s * 50 / 100, s * 68 / 100, s * 50 / 100);
                g2.drawLine(s * 32 / 100, s * 66 / 100, s * 54 / 100, s * 66 / 100);
                break;
            case REPORTES:
                g2.drawRect(s * 14 / 100, s * 52 / 100, s * 18 / 100, s * 36 / 100);
                g2.drawRect(s * 41 / 100, s * 30 / 100, s * 18 / 100, s * 58 / 100);
                g2.drawRect(s * 68 / 100, s * 12 / 100, s * 18 / 100, s * 76 / 100);
                break;
            case SALIR:
                g2.drawPolyline(new int[] { s * 46 / 100, s * 14 / 100, s * 14 / 100, s * 46 / 100 },
                        new int[] { s * 12 / 100, s * 12 / 100, s * 88 / 100, s * 88 / 100 }, 4);
                g2.drawLine(s * 36 / 100, s / 2, s * 88 / 100, s / 2);
                g2.drawLine(s * 88 / 100, s / 2, s * 72 / 100, s * 35 / 100);
                g2.drawLine(s * 88 / 100, s / 2, s * 72 / 100, s * 65 / 100);
                break;
            case NUEVO:
                g2.drawLine(s / 2, s * 18 / 100, s / 2, s * 82 / 100);
                g2.drawLine(s * 18 / 100, s / 2, s * 82 / 100, s / 2);
                break;
            case GUARDAR:
                g2.drawPolyline(new int[]{s * 18 / 100, s * 42 / 100, s * 84 / 100},
                        new int[]{s * 54 / 100, s * 76 / 100, s * 26 / 100}, 3);
                break;
            case EDITAR:
                g2.drawLine(s * 20 / 100, s * 80 / 100, s * 74 / 100, s * 26 / 100);
                g2.drawLine(s * 20 / 100, s * 80 / 100, s * 22 / 100, s * 62 / 100);
                g2.drawLine(s * 20 / 100, s * 80 / 100, s * 38 / 100, s * 78 / 100);
                g2.drawLine(s * 66 / 100, s * 18 / 100, s * 82 / 100, s * 34 / 100);
                break;
            case ELIMINAR:
                g2.drawLine(s * 16 / 100, s * 26 / 100, s * 84 / 100, s * 26 / 100);
                g2.drawRect(s * 28 / 100, s * 32 / 100, s * 44 / 100, s * 54 / 100);
                g2.drawLine(s * 40 / 100, s * 14 / 100, s * 60 / 100, s * 14 / 100);
                g2.drawLine(s * 42 / 100, s * 44 / 100, s * 42 / 100, s * 74 / 100);
                g2.drawLine(s * 58 / 100, s * 44 / 100, s * 58 / 100, s * 74 / 100);
                break;
            case BUSCAR:
                g2.drawOval(s * 12 / 100, s * 12 / 100, s * 50 / 100, s * 50 / 100);
                g2.drawLine(s * 56 / 100, s * 56 / 100, s * 86 / 100, s * 86 / 100);
                break;
            default:
                break;
        }
        g2.dispose();
    }
}
