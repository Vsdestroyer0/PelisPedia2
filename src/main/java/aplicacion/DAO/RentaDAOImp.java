package aplicacion.DAO;

import aplicacion.BaseDatos.DatabaseConnection;
import aplicacion.VO.PeliculaRentadaVO;
import aplicacion.VO.UsuarioVO;
import aplicacion.Vistas.Alertas;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RentaDAOImp implements RentaDAO {

    private final PeliculaDAO peliculaDAO = new PeliculaDAOImp();
    
    @Override
    public boolean agregarRenta(PeliculaRentadaVO renta) {
        // Primero verificamos si ya existe una renta activa para esta película y usuario
        if (verificarRentaExistente(renta.getIdUsuario(), renta.getIdPelicula())) {
            Alertas.mostrarAdvertencia("Esta película ya está rentada por ti. Puedes verla en 'Mis Rentas'");
            return false;
        }
        
        // La tabla actual tiene usuario VARCHAR en lugar de id_usuario INT
        // Obtenemos el nombre de usuario basado en el ID
        String nombreUsuario = obtenerNombreUsuario(renta.getIdUsuario());
        if (nombreUsuario == null) {
            Alertas.mostrarError("No se pudo encontrar el usuario para registrar la renta.");
            return false;
        }
        
        // Calcular fecha de entrega (2 horas después de la renta)
        LocalDateTime fechaEntrega = LocalDateTime.now().plusHours(2);
        
        String sql = "INSERT INTO peliculasRentadas (id_pelicula, usuario, imagen, fecha_renta, fecha_entrega, estado) VALUES (?, ?, ?, NOW(), ?, 'rentada')";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, renta.getIdPelicula());
            ps.setString(2, nombreUsuario);
            ps.setBytes(3, renta.getImagen());
            ps.setTimestamp(4, Timestamp.valueOf(fechaEntrega));
            
            int filasAfectadas = ps.executeUpdate();
            
            if (filasAfectadas > 0) {
                // Obtener el ID generado
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        renta.setIdRenta(rs.getInt(1));
                        return true;
                    }
                }
            }
            
            return false;
            
        } catch (SQLException e) {
            Alertas.mostrarError("Error al registrar la renta: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<PeliculaRentadaVO> obtenerRentasPorUsuario(int idUsuario) {
        List<PeliculaRentadaVO> rentas = new ArrayList<>();
        String nombreUsuario = obtenerNombreUsuario(idUsuario);
        
        if (nombreUsuario == null) {
            return rentas; // Lista vacía si no se encuentra el usuario
        }
        
        // Hacemos un JOIN con peliculasGeneral para obtener título, clasificación y precio
        String sql = "SELECT r.id_renta, r.id_pelicula, r.usuario, p.titulo, p.clasificacion, " +
                     "r.imagen, r.fecha_renta, r.fecha_entrega, r.estado, p.precio " +
                     "FROM peliculasRentadas r " +
                     "JOIN peliculasGeneral p ON r.id_pelicula = p.id_pelicula " +
                     "WHERE r.usuario = ? ORDER BY r.fecha_renta DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, nombreUsuario);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                PeliculaRentadaVO renta = new PeliculaRentadaVO();
                renta.setIdRenta(rs.getInt("id_renta"));
                renta.setIdPelicula(rs.getInt("id_pelicula"));
                renta.setIdUsuario(idUsuario);
                renta.setTitulo(rs.getString("titulo"));
                renta.setClasificacion(rs.getString("clasificacion"));
                renta.setImagen(rs.getBytes("imagen"));
                
                // Convertir Timestamp a LocalDateTime
                Timestamp fechaRentaTs = rs.getTimestamp("fecha_renta");
                if (fechaRentaTs != null) {
                    renta.setFechaRenta(fechaRentaTs.toLocalDateTime());
                }
                
                Timestamp fechaEntregaTs = rs.getTimestamp("fecha_entrega");
                if (fechaEntregaTs != null) {
                    renta.setFechaEntrega(fechaEntregaTs.toLocalDateTime());
                }
                
                renta.setEstado(rs.getString("estado"));
                renta.setPrecio(rs.getDouble("precio"));
                
                rentas.add(renta);
            }
            
        } catch (SQLException e) {
            Alertas.mostrarError("Error al obtener rentas: " + e.getMessage());
            e.printStackTrace();
        }
        
        return rentas;
    }

    @Override
    public List<PeliculaRentadaVO> obtenerRentasActivasPorUsuario(int idUsuario) {
        List<PeliculaRentadaVO> todasLasRentas = obtenerRentasPorUsuario(idUsuario);
        List<PeliculaRentadaVO> rentasActivas = new ArrayList<>();
        
        // Filtrar solo las rentas no expiradas y con estado "rentada"
        for (PeliculaRentadaVO renta : todasLasRentas) {
            if (!renta.isExpirado() && "rentada".equals(renta.getEstado())) {
                rentasActivas.add(renta);
            }
        }
        
        return rentasActivas;
    }

    @Override
    public boolean devolverRenta(int idRenta) {
        String sql = "UPDATE peliculasRentadas SET estado = 'devuelta', fecha_entrega = NOW() WHERE id_renta = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idRenta);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            Alertas.mostrarError("Error al devolver la película: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int actualizarRentasExpiradas() {
        int rentasActualizadas = 0;
        Connection con = null;
        PreparedStatement ps = null;
        
        try {
            con = DatabaseConnection.getConnection();
            
            // SQL para actualizar rentas expiradas (más de 2 horas desde la fecha_renta)
            String sql = "UPDATE peliculasRentadas SET estado = 'expirada' " +
                         "WHERE estado = 'rentada' AND " +
                         "TIMESTAMPDIFF(MINUTE, fecha_renta, NOW()) >= 120";
            
            ps = con.prepareStatement(sql);
            rentasActualizadas = ps.executeUpdate();
            
            if (rentasActualizadas > 0) {
                System.out.println(rentasActualizadas + " rentas han sido marcadas como expiradas");
            }
            
            return rentasActualizadas;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar rentas expiradas: " + e.getMessage());
            e.printStackTrace();
            return 0;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean verificarRentaExistente(int idUsuario, int idPelicula) {
        String nombreUsuario = obtenerNombreUsuario(idUsuario);
        
        if (nombreUsuario == null) {
            return false;
        }
        
        String sql = "SELECT COUNT(*) FROM peliculasRentadas " +
                     "WHERE usuario = ? AND id_pelicula = ? AND estado = 'rentada'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, nombreUsuario);
            ps.setInt(2, idPelicula);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            Alertas.mostrarError("Error al verificar renta existente: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    // Método auxiliar para obtener el nombre de usuario basado en el ID
    private String obtenerNombreUsuario(int idUsuario) {
        String sql = "SELECT nombre FROM Usuario WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getString("nombre");
            }
            
        } catch (SQLException e) {
            Alertas.mostrarError("Error al obtener nombre de usuario: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
}
