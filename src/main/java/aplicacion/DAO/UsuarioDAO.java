package aplicacion.DAO;
import aplicacion.VO.UsuarioVO;

import java.sql.SQLException;
import java.util.List;

public interface UsuarioDAO {

    boolean AgregarUsuario(UsuarioVO usuario);
    UsuarioVO obtenerPorCorreo(String correo);
    boolean actualizarUsuario(UsuarioVO usuario);
    boolean eliminarUsuario(int idUsuario);
    boolean desactivarUsuario(int idUsuario);
    boolean activarUsuario(int idUsuario);

    UsuarioVO autenticarUsuario(String username, String password);
    boolean esAdmin(String username);
    List<UsuarioVO> listarUsuarios() throws SQLException;
}
