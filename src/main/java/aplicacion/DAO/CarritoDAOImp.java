package aplicacion.DAO;

import aplicacion.BaseDatos.DatabaseConnection;
import aplicacion.VO.CarritoItemVO;
import aplicacion.VO.CarritoVO;
import aplicacion.VO.PeliculaVO;
import aplicacion.Vistas.Alertas;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CarritoDAOImp implements CarritoDAO {
    
    private PeliculaDAO peliculaDAO = new PeliculaDAOImp();

    @Override
    public boolean crearCarrito(CarritoVO carrito) {
        String sql = "INSERT INTO carrito (id_usuario, fecha_creacion) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, carrito.getIdUsuario());
            pstmt.setTimestamp(2, Timestamp.valueOf(carrito.getFechaCreacion()));
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    carrito.setIdCarrito(generatedKeys.getInt(1));
                }
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            Alertas.mostrarError("Error al crear carrito: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean agregarItemAlCarrito(CarritoItemVO item) {
        String sql = "INSERT INTO carrito_items (id_carrito, id_pelicula, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";
        
        try (Connection conn =  DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, item.getIdCarrito());
            pstmt.setInt(2, item.getIdPelicula());
            pstmt.setInt(3, item.getCantidad());
            pstmt.setDouble(4, item.getPrecioUnitario());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    item.setIdItem(generatedKeys.getInt(1));
                }
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            Alertas.mostrarError("Error al agregar item al carrito: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizarItemCarrito(CarritoItemVO item) {
        String sql = "UPDATE carrito_items SET cantidad = ?, precio_unitario = ? WHERE id_item = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, item.getCantidad());
            pstmt.setDouble(2, item.getPrecioUnitario());
            pstmt.setInt(3, item.getIdItem());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            Alertas.mostrarError("Error al actualizar item del carrito: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminarItemCarrito(int idItem) {
        String sql = "DELETE FROM carrito_items WHERE id_item = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idItem);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            Alertas.mostrarError("Error al eliminar item del carrito: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean vaciarCarrito(int idCarrito) {
        String sql = "DELETE FROM carrito_items WHERE id_carrito = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idCarrito);
            
            pstmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            Alertas.mostrarError("Error al vaciar carrito: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public CarritoVO obtenerCarritoUsuario(int idUsuario) {
        // Primero verificar si el usuario existe en la base de datos
        if (!usuarioExisteEnBaseDeDatos(idUsuario)) {
            Alertas.mostrarError("No se encontró el usuario en la base de datos. Por favor, inicie sesión nuevamente.");
            return null;
        }
        
        String sql = "SELECT * FROM carrito WHERE id_usuario = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int idCarrito = rs.getInt("id_carrito");
                LocalDateTime fechaCreacion = rs.getTimestamp("fecha_creacion").toLocalDateTime();
                
                CarritoVO carrito = new CarritoVO(idCarrito, idUsuario, fechaCreacion);
                
                // Cargar los items del carrito
                List<CarritoItemVO> items = obtenerItemsCarrito(idCarrito);
                for (CarritoItemVO item : items) {
                    carrito.agregarItem(item);
                }
                
                return carrito;
            } else {
                // Si no existe un carrito para el usuario, crear uno nuevo
                CarritoVO nuevoCarrito = new CarritoVO(idUsuario);
                if (crearCarrito(nuevoCarrito)) {
                    return nuevoCarrito;
                }
            }
            
            return null;
            
        } catch (SQLException e) {
            Alertas.mostrarError("Error al obtener carrito de usuario: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<CarritoItemVO> obtenerItemsCarrito(int idCarrito) {
        String sql = "SELECT * FROM carrito_items WHERE id_carrito = ?";
        List<CarritoItemVO> items = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idCarrito);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int idItem = rs.getInt("id_item");
                int idPelicula = rs.getInt("id_pelicula");
                int cantidad = rs.getInt("cantidad");
                double precioUnitario = rs.getDouble("precio_unitario");
                
                CarritoItemVO item = new CarritoItemVO(idItem, idCarrito, idPelicula, cantidad, precioUnitario);
                
                // Cargar la información de la película
                PeliculaVO pelicula = peliculaDAO.obtenerPeliculaPorId(idPelicula);
                if (pelicula != null) {
                    item.setPelicula(pelicula);
                }
                
                items.add(item);
            }
            
            return items;
            
        } catch (SQLException e) {
            Alertas.mostrarError("Error al obtener items del carrito: " + e.getMessage());
            e.printStackTrace();
            return items;
        }
    }

    @Override
    public CarritoItemVO buscarItemEnCarrito(int idCarrito, int idPelicula) {
        String sql = "SELECT * FROM carrito_items WHERE id_carrito = ? AND id_pelicula = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idCarrito);
            pstmt.setInt(2, idPelicula);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int idItem = rs.getInt("id_item");
                int cantidad = rs.getInt("cantidad");
                double precioUnitario = rs.getDouble("precio_unitario");
                
                CarritoItemVO item = new CarritoItemVO(idItem, idCarrito, idPelicula, cantidad, precioUnitario);
                
                // Cargar la información de la película
                PeliculaVO pelicula = peliculaDAO.obtenerPeliculaPorId(idPelicula);
                if (pelicula != null) {
                    item.setPelicula(pelicula);
                }
                
                return item;
            }
            
            return null;
            
        } catch (SQLException e) {
            Alertas.mostrarError("Error al buscar item en carrito: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private boolean usuarioExisteEnBaseDeDatos(int idUsuario) {
        String sql = "SELECT * FROM usuario WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();
            
            return rs.next();
            
        } catch (SQLException e) {
            Alertas.mostrarError("Error al verificar existencia de usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
