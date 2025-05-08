package aplicacion.DAO;

import aplicacion.VO.PeliculaVO;
import aplicacion.BaseDatos.DatabaseConnection;
import aplicacion.Vistas.Alertas;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PeliculaDAOImp implements PeliculaDAO {

    @Override
    public List<PeliculaVO> obtenerTodasPeliculas() {
        List<PeliculaVO> peliculas = new ArrayList<>();
        String sql = "CALL mostrarPelis()";  // Usar el procedimiento almacenado

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                peliculas.add(new PeliculaVO(
                        rs.getInt("id_pelicula"),
                        rs.getBytes("imagen"),
                        rs.getString("titulo"),
                        rs.getString("clasificacion"),
                        rs.getInt("stock")
                ));
            }
        } catch (SQLException e) {
            Alertas.mostrarError("Error al obtener películas");
        }
        return peliculas;
    }

    @Override
    public PeliculaVO obtenerPeliculaPorId(int id) {
        return null;
    }

    @Override
    public boolean agregarPelicula(PeliculaVO pelicula) {
        return false;
    }

    @Override
    public boolean actualizarStock(int idPelicula, int nuevoStock) {
        return false;
    }

    @Override
    public boolean rentarPelicula(String usuario, int idPelicula) {
        String sql = "{call rentar_pelicula(?, ?, ?)}";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, usuario);
            cs.setInt(2, idPelicula);
            cs.registerOutParameter(3, Types.VARCHAR);

            cs.execute();

            String mensaje = cs.getString(3);
            System.out.println(mensaje);
            return true;

        } catch (SQLException e) {
            Alertas.mostrarError("Error al rentar películas");
            return false;
        }
    }

    @Override
    public boolean devolverPelicula(int idRenta) {
        return false;
    }

}