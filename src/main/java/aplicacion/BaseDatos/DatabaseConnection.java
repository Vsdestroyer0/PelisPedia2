package aplicacion.BaseDatos;

import aplicacion.Vistas.Alertas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // MYSQL
    private static final String URL = "jdbc:mysql://localhost:3306/PelisPedia?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "Cine";
    private static final String PASSWORD = "1234";

    // conexión pa englobar
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // testeoDeConexiónEnCasoDeError
    public static void testConnection() {
        try (Connection conn = getConnection()) {
            Alertas.mostrarExito("Conexión exitosa a la base de datos");
        } catch (SQLException e) {
            throw new RuntimeException("Error al conectar a la base de datos", e);
        }
    }
}