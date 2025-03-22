package aplicacion.BaseDatos;

import java.sql.*;
import java.util.ArrayList;

import static java.sql.DriverManager.getConnection;

public class BaseDatos {
    private static Connection con;

    private static Statement consulta;
    private static ResultSet resultado;

    private final String Agregar_Usuario = "insert into Usuario (Nombre, Contraseña, Imagen, esAdmin) values (?,?,?,?)";
    private final String Agregar_Planta = "INSERT INTO Planta (Nombre, Descripcion, NombreCientifico, Propiedades, EfectosSecundarios, Imagen) VALUES (?,?,?,?,?,?)";


    public BaseDatos() {
        try {
            String url = "jdbc:mysql://localhost:3306/PvZ?useSSL=false&allowPublicKeyRetrieval=true";
            con = DriverManager.getConnection(url, "Plantera", "1234");
        } catch (SQLException e) {
            throw new RuntimeException("Error de conexión: " + e.getMessage());
        }
    }

    public void AgregarUsuario(String username, String password, Boolean tipoUsuario){
        PreparedStatement ps = null;
        try{
            ps = con.prepareStatement(Agregar_Usuario);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setNull(3, java.sql.Types.NULL);
            ps.setBoolean(4, tipoUsuario);
            ps.executeUpdate();
            System.out.println("Se ha agredado un usuario");
        } catch (Exception e){
            System.out.println(e);
        }
    }



    public boolean validar_Usuario(String username, String password){
        try{
            consulta = con.createStatement();
            resultado = consulta.executeQuery("call validacion_usuario('" + username + "', '" + password +"')");
            if (resultado.next()){
                return true;
            }
        }catch (Exception e){
            System.out.println(e);
        } return false;
    }

    public boolean esAdmin(String username, String password){
        try{
            consulta = con.createStatement();
            resultado = consulta.executeQuery("call validacion_usuario('" + username + "', '" + password +"')");
            if (resultado.next() && resultado.getInt("esAdmin") == 1){
                return true;
            }

        }catch (Exception e){
            System.out.println(e);
        } return false;
    }

    public boolean agregarPlanta(String nombre, String descripcion, String nombreCientifico,
                                 String propiedades, String efectosSecundarios, byte[] imagen) {

        try (PreparedStatement ps = con.prepareStatement(Agregar_Planta)) {
            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.setString(3, nombreCientifico);
            ps.setString(4, propiedades);
            ps.setString(5, efectosSecundarios);
            ps.setBytes(6, imagen);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al agregar planta: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<AltaPlantasController.Planta> obtenerPlantas() {
        ArrayList<AltaPlantasController.Planta> plantas = new ArrayList<>();
        String query = "SELECT Nombre, Descripcion, NombreCientifico, Propiedades, EfectosSecundarios, Imagen FROM Planta";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while(rs.next()) {
                Blob blob = rs.getBlob("Imagen");
                byte[] imagenBytes = null;

                if(blob != null) {
                    imagenBytes = blob.getBytes(1, (int) blob.length());
                    blob.free();
                }

                plantas.add(new AltaPlantasController.Planta(
                        rs.getString("Nombre"),
                        rs.getString("Descripcion"),
                        rs.getString("NombreCientifico"),
                        rs.getString("Propiedades"),
                        rs.getString("EfectosSecundarios"),
                        imagenBytes
                ));
            }
        } catch (Exception e) {
            System.err.println("Error al obtener plantas: " + e.getMessage());
        }
        return plantas;
    }



    public boolean modificarPlanta(String oldNombre, String nombre, String descripcion, String nombreCientifico, String propiedades, String efectosSecundarios, byte[] imagen) {
        String query;
        if (imagen != null) {
            query = "UPDATE Planta SET Nombre = ?, Descripcion = ?, NombreCientifico = ?, Propiedades = ?, EfectosSecundarios = ?, Imagen = ? WHERE Nombre = ?";
        } else {
            query = "UPDATE Planta SET Nombre = ?, Descripcion = ?, NombreCientifico = ?, Propiedades = ?, EfectosSecundarios = ? WHERE Nombre = ?";
        }

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.setString(3, nombreCientifico);
            ps.setString(4, propiedades);
            ps.setString(5, efectosSecundarios);

            if (imagen != null) {
                ps.setBytes(6, imagen);
                ps.setString(7, oldNombre);
            } else {
                ps.setString(6, oldNombre);
            }

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarPlanta(String nombre){
        try{
            PreparedStatement ps = con.prepareStatement("delete from Planta where nombre = ?");
            ps.setString(1, nombre);
            ps.executeUpdate();
            System.out.println("planta eliminada");
            return true;

        }catch (Exception e){
            System.out.println(e);
            return false;
        }
    }

}

