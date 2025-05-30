package aplicacion.DAO;

import aplicacion.VO.PeliculaRentadaVO;
import java.util.List;

/**
 * Interfaz para el acceso a datos de películas rentadas
 */
public interface RentaDAO {
    
    /**
     * Agrega una nueva película rentada
     * @param renta Datos de la renta
     * @return true si se agregó correctamente, false en caso contrario
     */
    boolean agregarRenta(PeliculaRentadaVO renta);
    
    /**
     * Obtiene todas las rentas de un usuario
     * @param idUsuario ID del usuario
     * @return Lista de películas rentadas
     */
    List<PeliculaRentadaVO> obtenerRentasPorUsuario(int idUsuario);
    
    /**
     * Obtiene las rentas activas (no expiradas) de un usuario
     * @param idUsuario ID del usuario
     * @return Lista de películas rentadas activas
     */
    List<PeliculaRentadaVO> obtenerRentasActivasPorUsuario(int idUsuario);
    
    /**
     * Marca una renta como devuelta
     * @param idRenta ID de la renta
     * @return true si se actualizó correctamente, false en caso contrario
     */
    boolean devolverRenta(int idRenta);
    
    /**
     * Actualiza el estado de todas las rentas expiradas (más de 2 horas)
     * @return Número de rentas actualizadas
     */
    int actualizarRentasExpiradas();
    
    /**
     * Verifica si un usuario ya ha rentado una película específica y aún está activa
     * @param idUsuario ID del usuario
     * @param idPelicula ID de la película
     * @return true si la película ya está rentada por el usuario, false en caso contrario
     */
    boolean verificarRentaExistente(int idUsuario, int idPelicula);
}
