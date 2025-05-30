package aplicacion.DAO;

import aplicacion.BaseDatos.DatabaseConnection;
import aplicacion.VO.PeliculaVO;
import aplicacion.VO.TicketDetalleVO;
import aplicacion.VO.TicketVO;
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

public class TicketDAOImp implements TicketDAO {
    
    private PeliculaDAO peliculaDAO = new PeliculaDAOImp();



    @Override
    public boolean agregarDetalleTicket(TicketDetalleVO detalle) {
        String sql = "INSERT INTO ticket_detalle (id_ticket, id_pelicula, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, detalle.getIdTicket());
            pstmt.setInt(2, detalle.getIdPelicula());
            pstmt.setInt(3, detalle.getCantidad());
            pstmt.setDouble(4, detalle.getPrecioUnitario());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    detalle.setIdDetalle(generatedKeys.getInt(1));
                }
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            Alertas.mostrarError("Error al agregar detalle al ticket: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public TicketVO obtenerTicketPorId(int idTicket) {
        String sql = "SELECT * FROM tickets WHERE id_ticket = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idTicket);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String numeroTicket = rs.getString("numero_ticket");
                int idUsuario = rs.getInt("id_usuario");
                double montoTotal = rs.getDouble("monto_total");
                LocalDateTime fechaCompra = rs.getTimestamp("fecha_compra").toLocalDateTime();
                String estado = rs.getString("estado");
                
                TicketVO ticket = new TicketVO(idTicket, numeroTicket, idUsuario, montoTotal, fechaCompra, estado);
                
                // Cargar los detalles del ticket
                List<TicketDetalleVO> detalles = obtenerDetallesTicket(idTicket);
                for (TicketDetalleVO detalle : detalles) {
                    ticket.agregarDetalle(detalle);
                }
                
                return ticket;
            }
            
            return null;
            
        } catch (SQLException e) {
            Alertas.mostrarError("Error al obtener ticket: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public TicketVO obtenerTicketPorNumero(String numeroTicket) {
        String sql = "SELECT * FROM tickets WHERE numero_ticket = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, numeroTicket);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int idTicket = rs.getInt("id_ticket");
                int idUsuario = rs.getInt("id_usuario");
                double montoTotal = rs.getDouble("monto_total");
                LocalDateTime fechaCompra = rs.getTimestamp("fecha_compra").toLocalDateTime();
                String estado = rs.getString("estado");
                
                TicketVO ticket = new TicketVO(idTicket, numeroTicket, idUsuario, montoTotal, fechaCompra, estado);
                
                // Cargar los detalles del ticket
                List<TicketDetalleVO> detalles = obtenerDetallesTicket(idTicket);
                for (TicketDetalleVO detalle : detalles) {
                    ticket.agregarDetalle(detalle);
                }
                
                return ticket;
            }
            
            return null;
            
        } catch (SQLException e) {
            Alertas.mostrarError("Error al obtener ticket por número: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<TicketVO> obtenerTicketsUsuario(int idUsuario) {
        String sql = "SELECT * FROM tickets WHERE id_usuario = ? ORDER BY fecha_compra DESC";
        List<TicketVO> tickets = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int idTicket = rs.getInt("id_ticket");
                String numeroTicket = rs.getString("numero_ticket");
                double montoTotal = rs.getDouble("monto_total");
                LocalDateTime fechaCompra = rs.getTimestamp("fecha_compra").toLocalDateTime();
                String estado = rs.getString("estado");
                
                TicketVO ticket = new TicketVO(idTicket, numeroTicket, idUsuario, montoTotal, fechaCompra, estado);
                
                // Cargar los detalles del ticket
                List<TicketDetalleVO> detalles = obtenerDetallesTicket(idTicket);
                for (TicketDetalleVO detalle : detalles) {
                    ticket.agregarDetalle(detalle);
                }
                
                tickets.add(ticket);
            }
            
            return tickets;
            
        } catch (SQLException e) {
            Alertas.mostrarError("Error al obtener tickets de usuario: " + e.getMessage());
            e.printStackTrace();
            return tickets;
        }
    }

    @Override
    public List<TicketDetalleVO> obtenerDetallesTicket(int idTicket) {
        String sql = "SELECT * FROM ticket_detalle WHERE id_ticket = ?";
        List<TicketDetalleVO> detalles = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idTicket);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int idDetalle = rs.getInt("id_detalle");
                int idPelicula = rs.getInt("id_pelicula");
                int cantidad = rs.getInt("cantidad");
                double precioUnitario = rs.getDouble("precio_unitario");
                
                TicketDetalleVO detalle = new TicketDetalleVO(idDetalle, idTicket, idPelicula, cantidad, precioUnitario);
                
                // Cargar la información de la película
                PeliculaVO pelicula = peliculaDAO.obtenerPeliculaPorId(idPelicula);
                if (pelicula != null) {
                    detalle.setPelicula(pelicula);
                }
                
                detalles.add(detalle);
            }
            
            return detalles;
            
        } catch (SQLException e) {
            Alertas.mostrarError("Error al obtener detalles del ticket: " + e.getMessage());
            e.printStackTrace();
            return detalles;
        }
    }

    @Override
    public List<TicketVO> obtenerTodosLosTickets() {
        String sql = "SELECT * FROM tickets ORDER BY fecha_compra DESC";
        List<TicketVO> tickets = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                int idTicket = rs.getInt("id_ticket");
                String numeroTicket = rs.getString("numero_ticket");
                int idUsuario = rs.getInt("id_usuario");
                double montoTotal = rs.getDouble("monto_total");
                LocalDateTime fechaCompra = rs.getTimestamp("fecha_compra").toLocalDateTime();
                String estado = rs.getString("estado");
                
                TicketVO ticket = new TicketVO(idTicket, numeroTicket, idUsuario, montoTotal, fechaCompra, estado);
                tickets.add(ticket);
            }
            
            return tickets;
            
        } catch (SQLException e) {
            Alertas.mostrarError("Error al obtener todos los tickets: " + e.getMessage());
            e.printStackTrace();
            return tickets;
        }
    }

    @Override
    public boolean actualizarEstadoTicket(int idTicket, String nuevoEstado) {
        String sql = "UPDATE tickets SET estado = ? WHERE id_ticket = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nuevoEstado);
            pstmt.setInt(2, idTicket);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            Alertas.mostrarError("Error al actualizar estado del ticket: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean crearTicket(TicketVO ticket) {
        String sql = "INSERT INTO tickets (numero_ticket, id_usuario, monto_total, fecha_compra, estado) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, ticket.getNumeroTicket());
            pstmt.setInt(2, ticket.getIdUsuario());
            pstmt.setDouble(3, ticket.getMontoTotal());
            pstmt.setTimestamp(4, Timestamp.valueOf(ticket.getFechaCompra()));
            pstmt.setString(5, ticket.getEstado());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    ticket.setIdTicket(generatedKeys.getInt(1));
                }
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            Alertas.mostrarError("Error al crear ticket: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean crearTicket(TicketVO ticket, List<TicketDetalleVO> detalles) {
        // Si el número de ticket no está establecido, generarlo
        if (ticket.getNumeroTicket() == null || ticket.getNumeroTicket().isEmpty()) {
            ticket.setNumeroTicket(TicketVO.generarNumeroTicket());
        }
        
        // Intentar crear el ticket en la base de datos
        if (crearTicket(ticket)) {
            // Si se creó correctamente, agregar cada detalle
            boolean todosDetallesTodo = true;
            
            for (TicketDetalleVO detalle : detalles) {
                // Establecer el ID del ticket en cada detalle
                detalle.setIdTicket(ticket.getIdTicket());
                
                // Agregar el detalle
                if (!agregarDetalleTicket(detalle)) {
                    todosDetallesTodo = false;
                }
            }
            
            return todosDetallesTodo;
        }
        
        return false;
    }
}
