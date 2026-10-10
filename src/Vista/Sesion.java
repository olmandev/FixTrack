package Vista;

/**
 * Sesión de demostración (solo interfaz, sin base de datos).
 */
public class Sesion {

    private static String usuario = "";
    private static String rol = "";

    /** Credenciales fijas de la demo. Devuelve true si son válidas y guarda la sesión. */
    public static boolean iniciar(String user, String clave) {
        if (user == null || clave == null) return false;
        user = user.trim().toLowerCase();

        if (user.equals("admin") && clave.equals("admin"))          { usuario = "admin";     rol = "ADMINISTRADOR"; return true; }
        if (user.equals("tecnico") && clave.equals("tecnico"))      { usuario = "tecnico";   rol = "TECNICO";       return true; }
        if (user.equals("recepcion") && clave.equals("recepcion"))  { usuario = "recepcion"; rol = "RECEPCIONISTA"; return true; }
        return false;
    }

    public static void cerrar() { usuario = ""; rol = ""; }

    public static String getUsuario() { return usuario; }
    public static String getRol() { return rol; }

    public static boolean esAdministrador() { return rol.equals("ADMINISTRADOR"); }
    public static boolean esTecnico() { return rol.equals("TECNICO"); }
    public static boolean esRecepcionista() { return rol.equals("RECEPCIONISTA"); }
}