package aplicacion.DAO;
import aplicacion.VO.UsuarioVO;

public interface UsuarioDAO {

    boolean AgregarUsuario(UsuarioVO usuario);
    UsuarioVO obtenerPorCorreo(String correo);
    boolean actualizarUsuario(UsuarioVO usuario);
    boolean eliminarUsuario(String correo);

    UsuarioVO autenticarUsuario(String username, String password);
    boolean esAdmin(String username);

}
