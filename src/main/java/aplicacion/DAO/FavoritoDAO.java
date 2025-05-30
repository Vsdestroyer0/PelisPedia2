package aplicacion.DAO;

import aplicacion.VO.PeliculaVO;
import java.util.List;

/**
 * Interfaz para el acceso a datos de películas favoritas
 */
public interface FavoritoDAO {
    
    /**
     * Agrega una película a los favoritos del usuario
     * @param idUsuario ID del usuario
     * @param idPelicula ID de la película
     * @return true si se agregó correctamente, false en caso contrario
     */
    boolean agregarFavorito(int idUsuario, int idPelicula);
    
    /**
     * Elimina una película de los favoritos del usuario
     * @param idUsuario ID del usuario
     * @param idPelicula ID de la película
     * @return true si se eliminó correctamente, false en caso contrario
     */
    boolean eliminarFavorito(int idUsuario, int idPelicula);
    
    /**
     * Verifica si una película está en los favoritos del usuario
     * @param idUsuario ID del usuario
     * @param idPelicula ID de la película
     * @return true si la película está en favoritos, false en caso contrario
     */
    boolean esFavorito(int idUsuario, int idPelicula);
    
    /**
     * Obtiene todas las películas favoritas de un usuario
     * @param idUsuario ID del usuario
     * @return Lista de películas favoritas
     */
    List<PeliculaVO> obtenerFavoritos(int idUsuario);
}
