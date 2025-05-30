package Controles;

import aplicacion.Vistas.Alertas;
import aplicacion.application.HelloApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class ReportesController {

    @FXML
    private Button btnVolver;

    @FXML
    private Button btnMasRentadas;

    @FXML
    private Button btnClientesFrecuentes;

    @FXML
    private Button btnMasGuardadas;

    @FXML
    private TableView<PeliculaRenta> tablaReportes;

    @FXML
    private TableColumn<PeliculaRenta, String> colTitulo;

    @FXML
    private TableColumn<PeliculaRenta, Integer> colCantidad;

    // Clase interna para manejar los datos que se mostrarán
    public static class PeliculaRenta {
        private final String titulo;  // Puede ser título o cliente según contexto
        private final int cantidad;

        public PeliculaRenta(String titulo, int cantidad) {
            this.titulo = titulo;
            this.cantidad = cantidad;
        }

        public String getTitulo() {
            return titulo;
        }

        public int getCantidad() {
            return cantidad;
        }
    }

    // Datos para la conexión (ajusta según tu configuración)
    private final String url = "jdbc:mysql://localhost:3306/PelisPedia?useSSL=false&allowPublicKeyRetrieval=true";
    private final String usuario = "Cine";
    private final String password = "1234";

    @FXML
    public void initialize() {
        // Por defecto al cargar la vista, mostramos películas más rentadas
        configurarColumnasParaPeliculas();
        cargarRankingMasRentadas();
    }

    @FXML
    private void configurarColumnasParaPeliculas() {
        colTitulo.setText("Película");
        colCantidad.setText("Veces Rentada");
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
    }

    @FXML
    private void configurarColumnasParaClientes() {
        colTitulo.setText("Cliente");
        colCantidad.setText("Rentas");
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
    }

    @FXML
    private void cargarRankingMasRentadas() {
        ObservableList<PeliculaRenta> lista = FXCollections.observableArrayList();

        String sql = "SELECT p.titulo, COUNT(r.id_pelicula) AS cantidad " +
                "FROM peliculasGeneral p " +
                "JOIN peliculasRentadas r ON p.id_pelicula = r.id_pelicula " +
                "GROUP BY p.id_pelicula, p.titulo " +
                "ORDER BY cantidad DESC";

        try (Connection con = DriverManager.getConnection(url, usuario, password);
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new PeliculaRenta(rs.getString("titulo"), rs.getInt("cantidad")));
            }

            tablaReportes.setItems(lista);

        } catch (SQLException e) {
            e.printStackTrace();
            Alertas.mostrarError("Error al cargar reporte de películas: " + e.getMessage());
        }
    }

    @FXML
    private void cargarRankingClientesFrecuentes() {
        ObservableList<PeliculaRenta> lista = FXCollections.observableArrayList();

        String sql = "SELECT usuario AS cliente, COUNT(*) AS rentas " +
                "FROM peliculasRentadas " +
                "GROUP BY usuario " +
                "ORDER BY rentas DESC";

        try (Connection con = DriverManager.getConnection(url, usuario, password);
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new PeliculaRenta(rs.getString("cliente"), rs.getInt("rentas")));
            }

            tablaReportes.setItems(lista);

        } catch (SQLException e) {
            e.printStackTrace();
            Alertas.mostrarError("Error al cargar reporte de clientes frecuentes: " + e.getMessage());
        }
    }

    @FXML
    private void cargarRankingMasGuardadas() {
        ObservableList<PeliculaRenta> lista = FXCollections.observableArrayList();

        String sql = "SELECT p.titulo, COUNT(f.idPelicula) AS cantidad " +
                "FROM peliculasGeneral p " +
                "JOIN Favorito f ON p.id_pelicula = f.idPelicula " +
                "GROUP BY p.id_pelicula, p.titulo " +
                "ORDER BY cantidad DESC";

        try (Connection con = DriverManager.getConnection(url, usuario, password);
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new PeliculaRenta(rs.getString("titulo"), rs.getInt("cantidad")));
            }

            tablaReportes.setItems(lista);

        } catch (SQLException e) {
            e.printStackTrace();
            Alertas.mostrarError("Error al cargar reporte de películas guardadas: " + e.getMessage());
        }
    }

    @FXML
    private void onBtnPeliculasMasRentadasClicked() {
        configurarColumnasParaPeliculas();
        cargarRankingMasRentadas();
    }

    @FXML
    private void onBtnClientesFrecuentesClicked() {
        configurarColumnasParaClientes();
        cargarRankingClientesFrecuentes();
    }

    @FXML
    private void onBtnMasGuardadasClicked() {
        configurarColumnasParaPeliculasGuardadas();
        cargarRankingMasGuardadas();
    }

    @FXML
    private void handleVolverMenu() {
        try {
            // Cambiamos la ruta para que apunte al archivo correcto
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("MenuAdministrador.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnVolver.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Menú Principal");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alertas.mostrarError("No se pudo cargar el menú principal: " + e.getMessage());
        }
    }

    @FXML
    private void configurarColumnasParaPeliculasGuardadas() {
        colTitulo.setText("Película");
        colCantidad.setText("Veces Guardada");
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
    }

}
