package aplicacion.DAO;

import aplicacion.BaseDatos.DatabaseConnection;
import aplicacion.VO.PeliculaVO;
import aplicacion.Vistas.Alertas;
import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PeliculaDAOImp implements PeliculaDAO {

    @Override
    public boolean agregarPelicula(PeliculaVO pelicula) {
        String sql = "INSERT INTO peliculasGeneral (imagen, titulo, clasificacion, descripcion, precio) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            if (pelicula.getImagen() != null) {
                pstmt.setBinaryStream(1, new ByteArrayInputStream(pelicula.getImagen()), pelicula.getImagen().length);
            } else {
                pstmt.setNull(1, java.sql.Types.BLOB);
            }
            
            pstmt.setString(2, pelicula.getTitulo());
            pstmt.setString(3, pelicula.getClasificacion());
            pstmt.setString(4, pelicula.getDescripcion());
            pstmt.setDouble(5, pelicula.getPrecio());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    pelicula.setIdPelicula(generatedKeys.getInt(1));
                }
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            Alertas.mostrarError("Error al agregar película: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizarPelicula(PeliculaVO pelicula) {
        String sql = "UPDATE peliculasGeneral SET imagen = ?, titulo = ?, clasificacion = ?, descripcion = ?, precio = ? WHERE id_pelicula = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (pelicula.getImagen() != null) {
                pstmt.setBinaryStream(1, new ByteArrayInputStream(pelicula.getImagen()), pelicula.getImagen().length);
            } else {
                pstmt.setNull(1, java.sql.Types.BLOB);
            }
            
            pstmt.setString(2, pelicula.getTitulo());
            pstmt.setString(3, pelicula.getClasificacion());
            pstmt.setString(4, pelicula.getDescripcion());
            pstmt.setDouble(5, pelicula.getPrecio());
            pstmt.setInt(6, pelicula.getIdPelicula());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            Alertas.mostrarError("Error al actualizar película: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminarPelicula(int idPelicula) {
        String sql = "DELETE FROM peliculasGeneral WHERE id_pelicula = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idPelicula);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            Alertas.mostrarError("Error al eliminar película: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public PeliculaVO obtenerPeliculaPorId(int idPelicula) {
        String sql = "SELECT * FROM peliculasGeneral WHERE id_pelicula = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idPelicula);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                byte[] imagen = rs.getBytes("imagen");
                String titulo = rs.getString("titulo");
                String clasificacion = rs.getString("clasificacion");
                String descripcion = rs.getString("descripcion");
                double precio = rs.getDouble("precio");
                
                return new PeliculaVO(idPelicula, imagen, titulo, clasificacion, descripcion, precio);
            }
            
            return null;
            
        } catch (SQLException e) {
            Alertas.mostrarError("Error al obtener película: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PeliculaVO> obtenerTodasLasPeliculas() {
        String sql = "SELECT * FROM peliculasGeneral";
        List<PeliculaVO> peliculas = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                int idPelicula = rs.getInt("id_pelicula");
                byte[] imagen = rs.getBytes("imagen");
                String titulo = rs.getString("titulo");
                String clasificacion = rs.getString("clasificacion");
                String descripcion = rs.getString("descripcion");
                double precio = rs.getDouble("precio");
                
                peliculas.add(new PeliculaVO(idPelicula, imagen, titulo, clasificacion, descripcion, precio));
            }
            
            return peliculas;
            
        } catch (SQLException e) {
            Alertas.mostrarError("Error al obtener películas: " + e.getMessage());
            e.printStackTrace();
            return peliculas;
        }
    }

    @Override
    public List<PeliculaVO> buscarPeliculasPorTitulo(String titulo) {
        String sql = "SELECT * FROM peliculasGeneral WHERE titulo LIKE ?";
        List<PeliculaVO> peliculas = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + titulo + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int idPelicula = rs.getInt("id_pelicula");
                byte[] imagen = rs.getBytes("imagen");
                String tituloP = rs.getString("titulo");
                String clasificacion = rs.getString("clasificacion");
                String descripcion = rs.getString("descripcion");
                double precio = rs.getDouble("precio");
                
                peliculas.add(new PeliculaVO(idPelicula, imagen, tituloP, clasificacion, descripcion, precio));
            }
            
            return peliculas;
            
        } catch (SQLException e) {
            Alertas.mostrarError("Error al buscar películas por título: " + e.getMessage());
            e.printStackTrace();
            return peliculas;
        }
    }

    @Override
    public List<PeliculaVO> buscarPeliculasPorClasificacion(String clasificacion) {
        String sql = "SELECT * FROM peliculasGeneral WHERE clasificacion = ?";
        List<PeliculaVO> peliculas = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, clasificacion);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int idPelicula = rs.getInt("id_pelicula");
                byte[] imagen = rs.getBytes("imagen");
                String titulo = rs.getString("titulo");
                String clasificacionP = rs.getString("clasificacion");
                String descripcion = rs.getString("descripcion");
                double precio = rs.getDouble("precio");
                
                peliculas.add(new PeliculaVO(idPelicula, imagen, titulo, clasificacionP, descripcion, precio));
            }
            
            return peliculas;
            
        } catch (SQLException e) {
            Alertas.mostrarError("Error al buscar películas por clasificación: " + e.getMessage());
            e.printStackTrace();
            return peliculas;
        }
    }
}