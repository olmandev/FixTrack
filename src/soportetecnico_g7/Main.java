package soportetecnico_g7;

import Vista.Login;
import javax.swing.JFrame;
import java.awt.EventQueue;

/**
 * @author GRUPO #1: - OLMAN GARCIA - DAVID FUENTES - DARWIN RIVAS 
 * Punto de entrada del Sistema de Soporte Técnico.
 */
public class Main {

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame("FixTrack - Grupo #1");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setMinimumSize(new java.awt.Dimension(1000, 600));
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setResizable(true);

            CambiarEscena cambiarEscena = new CambiarEscena(frame);

            Login login = new Login(cambiarEscena);
            frame.setContentPane(login);
            frame.setVisible(true);
        });
    }
}