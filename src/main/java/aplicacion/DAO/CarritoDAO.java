package aplicacion.DAO;

import aplicacion.VO.CarritoVO;
import aplicacion.VO.CarritoItemVO;
import java.util.List;

public interface CarritoDAO {
    
    /**
     * Crea un nuevo carrito en la base de datos
     * @param carrito El carrito a crear
     * @return true si se creó correctamente, false en caso contrario
     */
    boolean crearCarrito(CarritoVO carrito);
    
    /**
     * Agrega un item al carrito
     * @param item El item a agregar
     * @return true si se agregó correctamente, false en caso contrario
     */
    boolean agregarItemAlCarrito(CarritoItemVO item);
    
    /**
     * Actualiza la cantidad de un item en el carrito
     * @param item El item con la cantidad actualizada
     * @return true si se actualizó correctamente, false en caso contrario
     */
    boolean actualizarItemCarrito(CarritoItemVO item);
    
    /**
     * Elimina un item del carrito
     * @param idItem ID del item a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    boolean eliminarItemCarrito(int idItem);
    
    /**
     * Vacía todos los items del carrito de un usuario
     * @param idCarrito ID del carrito a vaciar
     * @return true si se vació correctamente, false en caso contrario
     */
    boolean vaciarCarrito(int idCarrito);
    
    /**
     * Obtiene el carrito de un usuario
     * @param idUsuario ID del usuario
     * @return El carrito del usuario o null si no existe
     */
    CarritoVO obtenerCarritoUsuario(int idUsuario);
    
    /**
     * Obtiene todos los items de un carrito
     * @param idCarrito ID del carrito
     * @return Lista de items en el carrito
     */
    List<CarritoItemVO> obtenerItemsCarrito(int idCarrito);
    
    /**
     * Verifica si una película ya está en el carrito
     * @param idCarrito ID del carrito
     * @param idPelicula ID de la película
     * @return El item si existe, null en caso contrario
     */
    CarritoItemVO buscarItemEnCarrito(int idCarrito, int idPelicula);
}
