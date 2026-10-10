package soportetecnico_g7;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author GRUPO #1
 * Permite cambiar de pantalla (JPanel) dentro de una misma ventana (JFrame),
 * sin abrir ventanas nuevas.
 */
public class CambiarEscena {

    private JFrame ventana;

    public CambiarEscena(JFrame ventana) {
        this.ventana = ventana;
    }

    public JFrame getVentana() {
        return ventana;
    }

    public void cambiarEscena(JPanel nuevaEscena) {
        ventana.setContentPane(nuevaEscena);
        ventana.revalidate();
        ventana.repaint();
        nuevaEscena.requestFocusInWindow();
    }
}