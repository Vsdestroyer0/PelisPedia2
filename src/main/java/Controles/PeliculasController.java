package Controles;

import aplicacion.DAO.PeliculaDAO;
import aplicacion.DAO.PeliculaDAOImp;
import aplicacion.VO.PeliculaVO;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import java.io.ByteArrayInputStream;

public class PeliculasController {

    @FXML private TableView<PeliculaVO> tablaPeliculas;
    @FXML private TableColumn<PeliculaVO, ImageView> colImagen;
    @FXML private TableColumn<PeliculaVO, String> colTitulo;
    @FXML private TableColumn<PeliculaVO, String> colClasificacion;
    @FXML private TableColumn<PeliculaVO, Integer> colStock;
    @FXML private TextField txtBusqueda;

    private PeliculaDAO peliculaDAO = new PeliculaDAOImp();
    private ObservableList<PeliculaVO> listaPeliculas = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configurarColumnas();
        cargarPeliculas();
        configurarBusqueda();
    }

    private void configurarColumnas() {
        // Configurar celda de imagen
        colImagen.setCellValueFactory(cellData -> {
            byte[] imagenBytes = cellData.getValue().getImagen();
            ImageView imageView = new ImageView();
            if (imagenBytes != null) {
                Image imagen = new Image(new ByteArrayInputStream(imagenBytes));
                imageView.setImage(imagen);
                imageView.setFitWidth(100);
                imageView.setFitHeight(150);
                imageView.setPreserveRatio(true);
            }
            return new SimpleObjectProperty<>(imageView);
        });

        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colClasificacion.setCellValueFactory(new PropertyValueFactory<>("clasificacion"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
    }

    private void cargarPeliculas() {
        listaPeliculas.setAll(peliculaDAO.obtenerTodasPeliculas());
        tablaPeliculas.setItems(listaPeliculas);
    }

    private void configurarBusqueda() {
        FilteredList<PeliculaVO> filteredData = new FilteredList<>(listaPeliculas, p -> true);
        txtBusqueda.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredData.setPredicate(pelicula -> {
                if (newVal == null || newVal.isEmpty()) return true;
                String lowerCaseFilter = newVal.toLowerCase();
                return pelicula.getTitulo().toLowerCase().contains(lowerCaseFilter);
            });
        });
        tablaPeliculas.setItems(filteredData);
    }

    @FXML
    private void handleRentarPelicula() {
        PeliculaVO seleccionada = tablaPeliculas.getSelectionModel().getSelectedItem();
        if (seleccionada != null && seleccionada.getStock() > 0) {
            if (peliculaDAO.rentarPelicula("usuario@ejemplo.com", seleccionada.getIdPelicula())) {
                cargarPeliculas();
                mostrarAlerta("Éxito", "Película rentada", Alert.AlertType.INFORMATION);
            }
        } else {
            mostrarAlerta("Error", "No hay stock disponible", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleDevolverPelicula() {
        // Lógica para devolución similar a renta
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}