package aplicacion.DAO;

import aplicacion.VO.TicketVO;
import aplicacion.VO.TicketDetalleVO;
import java.util.List;

public interface TicketDAO {
    
    /**
     * Crea un nuevo ticket en la base de datos
     * @param ticket El ticket a crear
     * @return true si se creó correctamente, false en caso contrario
     */
    boolean crearTicket(TicketVO ticket);
    
    /**
     * Crea un nuevo ticket en la base de datos con sus detalles
     * @param ticket El ticket a crear
     * @param detalles Lista de detalles del ticket
     * @return true si se creó correctamente, false en caso contrario
     */
    boolean crearTicket(TicketVO ticket, List<TicketDetalleVO> detalles);
    
    /**
     * Agrega un detalle al ticket
     * @param detalle El detalle a agregar
     * @return true si se agregó correctamente, false en caso contrario
     */
    boolean agregarDetalleTicket(TicketDetalleVO detalle);
    
    /**
     * Obtiene un ticket por su ID
     * @param idTicket ID del ticket a buscar
     * @return El ticket encontrado o null si no existe
     */
    TicketVO obtenerTicketPorId(int idTicket);
    
    /**
     * Obtiene un ticket por su número de ticket
     * @param numeroTicket Número del ticket a buscar
     * @return El ticket encontrado o null si no existe
     */
    TicketVO obtenerTicketPorNumero(String numeroTicket);
    
    /**
     * Obtiene todos los tickets de un usuario
     * @param idUsuario ID del usuario
     * @return Lista de tickets del usuario
     */
    List<TicketVO> obtenerTicketsUsuario(int idUsuario);
    
    /**
     * Obtiene todos los detalles de un ticket
     * @param idTicket ID del ticket
     * @return Lista de detalles del ticket
     */
    List<TicketDetalleVO> obtenerDetallesTicket(int idTicket);
    
    /**
     * Obtiene todos los tickets en el sistema (para el administrador)
     * @return Lista de todos los tickets
     */
    List<TicketVO> obtenerTodosLosTickets();
    
    /**
     * Actualiza el estado de un ticket
     * @param idTicket ID del ticket a actualizar
     * @param nuevoEstado Nuevo estado del ticket
     * @return true si se actualizó correctamente, false en caso contrario
     */
    boolean actualizarEstadoTicket(int idTicket, String nuevoEstado);
}
