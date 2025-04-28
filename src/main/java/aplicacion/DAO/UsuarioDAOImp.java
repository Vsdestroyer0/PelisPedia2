package aplicacion.DAO;
import aplicacion.VO.UsuarioVO;
import aplicacion.BaseDatos.DatabaseConnection;
import java.sql.*;

public class UsuarioDAOImp implements UsuarioDAO{

    @Override
    public boolean AgregarUsuario(UsuarioVO usuario) {
        String sql = "insert into Usuario (nombre, correo, contraseña, confirmarContraseña, preguntaSeguridad, respuestaSeguridad," +
                "direccion, esAdmin) values (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getCorreo());
            ps.setString(3, usuario.getContraseña());
            ps.setString(4, usuario.getConfirmarContraseña());
            ps.setString(5, usuario.getPreguntaSeguridad());
            ps.setString(6, usuario.getRespuestaSeguridad());
            ps.setString(7, usuario.getDirección());
            ps.setBoolean(8, usuario.isAdmin());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al agregar usuario" + e.getMessage());
            return false;
        }
    }



    @Override
    public UsuarioVO obtenerPorCorreo(String correo) {
        return null;
    }

    @Override
    public boolean actualizarUsuario(UsuarioVO usuario) {
        String sql = "UPDATE Usuario SET nombre=?, contraseña=?, preguntaSeguridad=?, "
                + "respuestaSeguridad=?, direccion=?, esAdmin=? WHERE correo=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getContraseña());
            ps.setString(3, usuario.getPreguntaSeguridad());
            ps.setString(4, usuario.getRespuestaSeguridad());
            ps.setString(5, usuario.getDirección());
            ps.setBoolean(6, usuario.isAdmin());
            ps.setString(7, usuario.getCorreo());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminarUsuario(String correo) {
        return false;
    }

    @Override
    public UsuarioVO autenticarUsuario(String username, String password) {
        String sql = "{call validacion_usuario(?, ?)}";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, username);
            cs.setString(2, password);

            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                return mapearUsuario(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error en autenticación: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean esAdmin(String username) {
        String sql = "{call obtener_admin(?, ?)}";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            // Asumiendo que necesitas password para validar
            // Si no es necesario, ajusta el procedimiento almacenado
            cs.setString(1, username);
            cs.setString(2, ""); // Password vacío si no se usa

            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                return rs.getBoolean("esAdmin");
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar admin: " + e.getMessage());
        }
        return false;
    }

    private UsuarioVO mapearUsuario(ResultSet rs) throws SQLException {
        return new UsuarioVO(
                rs.getString("id"),
                rs.getString("nombre"),
                rs.getString("correo"),
                null, // parametro contraseña vacio
                rs.getString("preguntaSeguridad"),
                rs.getString("respuestaSeguridad"),
                rs.getString("direccion"),
                rs.getBoolean("esAdmin")
        );
    }

}
