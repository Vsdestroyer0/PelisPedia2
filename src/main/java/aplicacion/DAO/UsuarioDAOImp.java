package aplicacion.DAO;
import aplicacion.VO.UsuarioVO;
import aplicacion.BaseDatos.DatabaseConnection;
import aplicacion.Vistas.Alertas;

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
            Alertas.mostrarError("Error al agregar usuario");
            return false;
        }
    }

    @Override
    public UsuarioVO obtenerPorCorreo(String correo) {
        String sql = "SELECT * FROM Usuario WHERE correo = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapearUsuario(rs);
            }
        } catch (SQLException e) {
            Alertas.mostrarError("Error al obtener el correo");
        }
        return null;
    }

    // aplicacion/DAO/UsuarioDAOImp.java
    @Override
    public boolean actualizarUsuario(UsuarioVO usuario) {
        String sql = "UPDATE Usuario SET contraseña = ?, confirmarContraseña = ? WHERE correo = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuario.getContraseña());
            ps.setString(2, usuario.getConfirmarContraseña());
            ps.setString(3, usuario.getCorreo());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Alertas.mostrarError("Error al actualizar contraseña");
            return false;
        }
    }

    @Override
    public boolean eliminarUsuario(String correo) {
        String sql = "DELETE FROM Usuario WHERE correo = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, correo);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            Alertas.mostrarError("Error al eliminar usuario");
            return false;
        }
    }

    @Override
    public UsuarioVO autenticarUsuario(String username, String password) {
        String sql = "SELECT nombre, correo, preguntaSeguridad, respuestaSeguridad, direccion, esAdmin FROM Usuario WHERE correo = ? AND contraseña = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new UsuarioVO(
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        null,
                        rs.getString("preguntaSeguridad"),
                        rs.getString("respuestaSeguridad"),
                        rs.getString("direccion"),
                        rs.getBoolean("esAdmin")
                );
            }
        } catch (SQLException e) {
            Alertas.mostrarError("Error en autentificación ");
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
            Alertas.mostrarError("Error al verificar admin");
        }
        return false;
    }

    private UsuarioVO mapearUsuario(ResultSet rs) throws SQLException {
        return new UsuarioVO(
                rs.getString("nombre"),
                rs.getString("correo"),
                null, // contraseña vacía por seguridad
                rs.getString("preguntaSeguridad"),
                rs.getString("respuestaSeguridad"),
                rs.getString("direccion"),
                rs.getBoolean("esAdmin")
        );
    }



}
