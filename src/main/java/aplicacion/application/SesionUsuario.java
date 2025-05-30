package aplicacion.application;

import aplicacion.VO.UsuarioVO;

/**
 * Clase para gestionar la sesión del usuario actual
 * Implementa el patrón Singleton para asegurar una única instancia
 */
public class SesionUsuario {
    
    private static SesionUsuario instancia;
    private UsuarioVO usuarioActual;
    
    // Constructor privado para evitar instanciación directa
    private SesionUsuario() {
        this.usuarioActual = null;
    }
    
    /**
     * Obtener la instancia única de la sesión
     * @return La instancia de SesionUsuario
     */
    public static synchronized SesionUsuario getInstancia() {
        if (instancia == null) {
            instancia = new SesionUsuario();
        }
        return instancia;
    }
    
    /**
     * Inicia sesión con un usuario
     * @param usuario El usuario que inicia sesión
     */
    public void iniciarSesion(UsuarioVO usuario) {
        this.usuarioActual = usuario;
    }
    
    /**
     * Cierra la sesión actual
     */
    public void cerrarSesion() {
        this.usuarioActual = null;
    }
    
    /**
     * Verifica si hay un usuario con sesión activa
     * @return true si hay un usuario logueado, false en caso contrario
     */
    public boolean haySesionActiva() {
        return usuarioActual != null;
    }
    
    /**
     * Obtiene el usuario con sesión activa
     * @return El usuario logueado o null si no hay sesión
     */
    public UsuarioVO getUsuarioActual() {
        return usuarioActual;
    }
    
    /**
     * Verifica si el usuario actual es administrador
     * @return true si el usuario es administrador, false si no lo es o no hay sesión
     */
    public boolean esUsuarioAdministrador() {
        return usuarioActual != null && usuarioActual.isAdmin();
    }
}
