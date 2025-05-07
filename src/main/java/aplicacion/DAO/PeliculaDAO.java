package aplicacion.DAO;

import aplicacion.VO.PeliculaVO;
import java.util.List;

public interface PeliculaDAO {
    List<PeliculaVO> obtenerTodasPeliculas();
    PeliculaVO obtenerPeliculaPorId(int id);
    boolean agregarPelicula(PeliculaVO pelicula);
    boolean actualizarStock(int idPelicula, int nuevoStock);
    boolean rentarPelicula(String usuario, int idPelicula);
    boolean devolverPelicula(int idRenta);
}