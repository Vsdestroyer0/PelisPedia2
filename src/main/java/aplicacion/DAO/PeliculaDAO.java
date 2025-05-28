package aplicacion.DAO;

import aplicacion.VO.PeliculaVO;
import java.util.List;

public interface PeliculaDAO {
    
    /**
     * Agrega una nueva película a la base de datos
     * @param pelicula La película a agregar
     * @return true si se agregó correctamente, false en caso contrario
     */
    boolean agregarPelicula(PeliculaVO pelicula);
    
    /**
     * Actualiza una película existente en la base de datos
     * @param pelicula La película con los datos actualizados
     * @return true si se actualizó correctamente, false en caso contrario
     */
    boolean actualizarPelicula(PeliculaVO pelicula);
    
    /**
     * Elimina una película de la base de datos por su ID
     * @param idPelicula ID de la película a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    boolean eliminarPelicula(int idPelicula);
    
    /**
     * Obtiene una película por su ID
     * @param idPelicula ID de la película a buscar
     * @return La película encontrada o null si no existe
     */
    PeliculaVO obtenerPeliculaPorId(int idPelicula);
    
    /**
     * Obtiene todas las películas de la base de datos
     * @return Lista de todas las películas
     */
    List<PeliculaVO> obtenerTodasLasPeliculas();
    
    /**
     * Busca películas por título
     * @param titulo Título o parte del título a buscar
     * @return Lista de películas que coinciden con la búsqueda
     */
    List<PeliculaVO> buscarPeliculasPorTitulo(String titulo);
    
    /**
     * Busca películas por clasificación
     * @param clasificacion Clasificación a buscar
     * @return Lista de películas que coinciden con la clasificación
     */
    List<PeliculaVO> buscarPeliculasPorClasificacion(String clasificacion);
}