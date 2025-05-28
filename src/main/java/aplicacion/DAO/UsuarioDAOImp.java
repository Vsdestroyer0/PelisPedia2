package aplicacion.DAO;
import aplicacion.VO.UsuarioVO;
import aplicacion.BaseDatos.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import aplicacion.Vistas.Alertas;

public class UsuarioDAOImp implements UsuarioDAO{

    @Override
    public boolean AgregarUsuario(UsuarioVO usuario) {
        String sql = "insert into Usuario (nombre, correo, contraseña, preguntaSeguridad, respuestaSeguridad," +
                "direccion, esAdmin, activo) values (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){

                ps.setString(1, usuario.getNombre());
                ps.setString(2, usuario.getCorreo());
                ps.setString(3, usuario.getContraseña());
                ps.setString(4, usuario.getPreguntaSeguridad());
                ps.setString(5, usuario.getRespuestaSeguridad());
                ps.setString(6, usuario.getDireccion());
                ps.setBoolean(7, usuario.isAdmin());
                ps.setBoolean(8, usuario.isActivo());


            // Si hay imagen, añadirla después de la inserción
            int resultado = ps.executeUpdate();
            
            if (resultado > 0 && usuario.getImagen() != null) {
                // Actualizar la imagen en un segundo paso
                String updateImagen = "UPDATE Usuario SET Imagen = ? WHERE correo = ?";
                try (PreparedStatement psImg = conn.prepareStatement(updateImagen)) {
                    psImg.setBytes(1, usuario.getImagen());
                    psImg.setString(2, usuario.getCorreo());
                    psImg.executeUpdate();
                }
            }
            
            return resultado > 0;

        } catch (SQLException e) {
            Alertas.mostrarError("Error al agregar usuario" + e.getMessage());
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
            Alertas.mostrarError("Error al obtener usuario por correo: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean actualizarUsuario(UsuarioVO usuario) {
        // SQL base para actualizar datos básicos
        StringBuilder sql = new StringBuilder("UPDATE Usuario SET nombre = ?, direccion = ?, activo = ?");
        
        // Si la contraseña no es nula, incluirla en la actualización
        boolean actualizarContraseña = usuario.getContraseña() != null && !usuario.getContraseña().isEmpty();
        if (actualizarContraseña) {
            sql.append(", contraseña = ?, confirmarContraseña = ?");
        }
        
        sql.append(" WHERE correo = ?");
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getDireccion());
            ps.setBoolean(3, usuario.isActivo());
            
            int paramIndex = 4;
            
            // Si hay contraseña para actualizar, establecer los parámetros
            if (actualizarContraseña) {
                ps.setString(paramIndex++, usuario.getContraseña());
                ps.setString(paramIndex++, usuario.getContraseña()); // Usar la misma contraseña como confirmación
            }
            
            ps.setString(paramIndex, usuario.getCorreo());
            
            boolean resultado = ps.executeUpdate() > 0;
            
            // Si hay imagen, actualizarla en una consulta separada
            if (resultado && usuario.getImagen() != null) {
                String updateImagen = "UPDATE Usuario SET Imagen = ? WHERE correo = ?";
                try (PreparedStatement psImg = conn.prepareStatement(updateImagen)) {
                    psImg.setBytes(1, usuario.getImagen());
                    psImg.setString(2, usuario.getCorreo());
                    psImg.executeUpdate();
                }
            }
            
            return resultado;
        } catch (SQLException e) {
            Alertas.mostrarError("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

@Override
public UsuarioVO autenticarUsuario(String username, String password) {
    String sql = "SELECT nombre, correo, contraseña, preguntaSeguridad, respuestaSeguridad, direccion, esAdmin, activo " +
                "FROM Usuario WHERE correo = ?";
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            String storedPassword = rs.getString("contraseña");
            // Verificar que el usuario esté activo
            boolean activo = rs.getBoolean("activo");
            if (!activo) {
                Alertas.mostrarError("La cuenta está desactivada. Contacte con el administrador.");
                return null;
            }
            
            // Aquí deberías implementar la verificación de contraseña hasheada
            if (password.equals(storedPassword)) { // Temporal - Reemplazar con verificación de hash
                UsuarioVO usuario = new UsuarioVO(
                    rs.getString("nombre"),
                    rs.getString("correo"),
                    null, // No devolver la contraseña
                    rs.getString("preguntaSeguridad"),
                    rs.getString("respuestaSeguridad"),
                    rs.getString("direccion"),
                    rs.getBoolean("esAdmin")
                );
                usuario.setActivo(activo);
                
                // Obtener la imagen en una consulta separada para mejorar rendimiento
                String imgSql = "SELECT Imagen FROM Usuario WHERE correo = ?";
                try (PreparedStatement imgPs = conn.prepareStatement(imgSql)) {
                    imgPs.setString(1, username);
                    ResultSet imgRs = imgPs.executeQuery();
                    if (imgRs.next()) {
                        usuario.setImagen(imgRs.getBytes("Imagen"));
                    }
                }
                
                return usuario;
            }
        }
    } catch (SQLException e) {
        Alertas.mostrarError("Error al autenticar usuario: " + e.getMessage());
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
            Alertas.mostrarError("Error al verificar admin: " + e.getMessage());
        }
        return false;
    }

    private UsuarioVO mapearUsuario(ResultSet rs) throws SQLException {
        UsuarioVO usuario = new UsuarioVO(
                rs.getString("nombre"),
                rs.getString("correo"),
                null, // contraseña vacía por seguridad
                rs.getString("preguntaSeguridad"),
                rs.getString("respuestaSeguridad"),
                rs.getString("direccion"),
                rs.getBoolean("esAdmin")
        );
        
        // Agregar el campo activo
        try {
            usuario.setActivo(rs.getBoolean("activo"));
        } catch (SQLException e) {
            // Si la columna no existe, establecer activo como true por defecto
            usuario.setActivo(true);
        }
        
        // Agregar la imagen si existe
        try {
            byte[] imagenBytes = rs.getBytes("Imagen");
            if (imagenBytes != null) {
                usuario.setImagen(imagenBytes);
            }
        } catch (SQLException e) {
            // Si la columna no existe o hay error, la imagen queda como null
        }
        
        return usuario;
    }

    public List<UsuarioVO> listarUsuarios() throws SQLException {
        String sql = "SELECT * FROM Usuario WHERE esAdmin = false";
        List<UsuarioVO> lista = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {

            while (rs.next()) {
                UsuarioVO u = new UsuarioVO(
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        null,
                        rs.getString("preguntaSeguridad"),
                        rs.getString("respuestaSeguridad"),
                        rs.getString("direccion"),
                        rs.getBoolean("esAdmin")
                );
                
                // Obtener estado activo
                try {
                    u.setActivo(rs.getBoolean("activo"));
                } catch (SQLException e) {
                    // Si la columna no existe, establecer como true por defecto
                    u.setActivo(true);
                }
                
                // Obtener imagen
                try {
                    byte[] imagenBytes = rs.getBytes("Imagen");
                    if (imagenBytes != null) {
                        u.setImagen(imagenBytes);
                    }
                } catch (SQLException e) {
                    // Si la columna no existe, la imagen queda como null
                    if (lista.isEmpty()) {
                        System.out.println("Aviso: La columna 'Imagen' no existe en la tabla Usuario. Se usarán imágenes por defecto.");
                    }
                }
                
                lista.add(u);
            }
        } catch (SQLException e) {
            Alertas.mostrarError("Error al listar usuarios: " + e.getMessage());
            throw e;
        }
        return lista;
    }

    @Override
    public boolean eliminarUsuario(int idUsuario) {
        String sql = "DELETE FROM Usuario WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            Alertas.mostrarError("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean desactivarUsuario(int idUsuario) {
        String sql = "UPDATE Usuario SET activo = false WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            Alertas.mostrarError("Error al desactivar usuario: " + e.getMessage());
            return false;
        }
    }
    public boolean activarUsuario(int idUsuario) {
        String sql = "UPDATE Usuario SET activo = true WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            Alertas.mostrarError("Error al activar usuario: " + e.getMessage());
            return false;
        }
    }
}