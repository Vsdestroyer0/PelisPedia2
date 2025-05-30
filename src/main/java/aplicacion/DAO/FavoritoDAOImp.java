package aplicacion.DAO;

import aplicacion.VO.PeliculaVO;
import aplicacion.Vistas.Alertas;
import aplicacion.BaseDatos.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de la interfaz FavoritoDAO para manejar las películas favoritas
 */
public class FavoritoDAOImp implements FavoritoDAO {

    private final PeliculaDAO peliculaDAO = new PeliculaDAOImp();
    
    @Override
    public boolean agregarFavorito(int idUsuario, int idPelicula) {
        String sql = "INSERT INTO Favorito (idUsuario, idPelicula) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Verificar si ya existe el favorito
            if (esFavorito(idUsuario, idPelicula)) {
                // Ya existe, no es necesario agregarlo de nuevo
                return true;
            }
            
            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idPelicula);
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            // Si ocurre un error de clave foránea, verificar si el usuario existe
            if (e.getMessage().contains("foreign key")) {
                Alertas.mostrarError("No se pudo agregar a favoritos: Verifica que el usuario y la película existan.");
            } else {
                Alertas.mostrarError("Error al agregar a favoritos: " + e.getMessage());
            }
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminarFavorito(int idUsuario, int idPelicula) {
        String sql = "DELETE FROM Favorito WHERE idUsuario = ? AND idPelicula = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idPelicula);
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            Alertas.mostrarError("Error al eliminar favorito: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean esFavorito(int idUsuario, int idPelicula) {
        String sql = "SELECT COUNT(*) FROM Favorito WHERE idUsuario = ? AND idPelicula = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idPelicula);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
            return false;
            
        } catch (SQLException e) {
            Alertas.mostrarError("Error al verificar favorito: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<PeliculaVO> obtenerFavoritos(int idUsuario) {
        List<PeliculaVO> favoritos = new ArrayList<>();
        String sql = "SELECT idPelicula FROM Favorito WHERE idUsuario = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                int idPelicula = rs.getInt("idPelicula");
                // Obtener la película completa usando el DAO de películas
                PeliculaVO pelicula = peliculaDAO.obtenerPeliculaPorId(idPelicula);
                if (pelicula != null) {
                    favoritos.add(pelicula);
                }
            }
            
            return favoritos;
            
        } catch (SQLException e) {
            Alertas.mostrarError("Error al obtener favoritos: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
