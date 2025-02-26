package aplicacion.BaseDatos;

import Controles.AltaPlantasController;

import java.sql.*;
import java.util.ArrayList;

import static java.sql.DriverManager.getConnection;

public class BaseDatos {
    private static Connection con;

    private static Statement consulta;
    private static ResultSet resultado;

    private final String Agregar_Usuario = "insert into Usuario (Nombre, Contraseña, Imagen, esAdmin) values (?,?,?,?)";
    private final String Agregar_Planta = "insert into Planta (Nombre, Descripcion, NombreCientifico, Propiedades, EfectosSecundarios) values (?,?,?,?,?)";

    public BaseDatos() {
        try{
            con = getConnection("jdbc:mysql://localhost:3306/PvZ", "Plantera", "1234");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean conectar(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            Connection con = getConnection("jdbc:mysql://localhost:3306/PvZ)", "Plantera", "1234");
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("Select * from usuarios");
            while (rs.next())
                System.out.println("Existe Algo en BD");
        }catch (Exception e){
            System.out.println(e);
            return false;
        }
        return true;
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

    public boolean agregarPlanta(String nombre, String descripcion, String nombreCientifico, String propiedades, String efectosSecundarios){
        PreparedStatement ps = null;
        try{
            ps = con.prepareStatement(Agregar_Planta);
            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.setString(3, nombreCientifico);
            ps.setString(4, propiedades);
            ps.setString(5, efectosSecundarios);
            System.out.println("Planta agregada");
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e){
            System.out.println(e);
            return false;

        }
    }

   public ArrayList<AltaPlantasController.Planta> obtenerPlantas(){
        ArrayList<AltaPlantasController.Planta> plantas = new ArrayList<>();
        try{
            consulta = con.createStatement();
            resultado = consulta.executeQuery("SELECT * FROM Planta");
            while(resultado.next()){
                AltaPlantasController.Planta planta = new AltaPlantasController.Planta(
                        resultado.getString("Nombre"),
                        resultado.getString("Descripcion"),
                        resultado.getString("nombreCientifico"),
                        resultado.getString("Propiedades"),
                        resultado.getString("efectosSecundarios")
                );
                plantas.add(planta);
            }
        } catch (Exception e){
            System.out.println(e);
        }
        return plantas;
    }


    public boolean modificarPlanta(String oldNombre, String nombre, String descripcion, String nombreCientifico, String propiedades, String efectosSecundarios) {
        PreparedStatement ps = null;
        try {
            String query = "UPDATE Planta SET Nombre = ?, Descripcion = ?, NombreCientifico = ?, Propiedades = ?, EfectosSecundarios = ? WHERE Nombre = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.setString(3, nombreCientifico);
            ps.setString(4, propiedades);
            ps.setString(5, efectosSecundarios);
            ps.setString(6, oldNombre);
            ps.executeUpdate();
            System.out.println("Planta modificada");
            return true;
        } catch (Exception e) {
            System.out.println(e);
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

