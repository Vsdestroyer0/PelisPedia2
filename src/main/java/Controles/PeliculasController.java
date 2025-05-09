package Controles;

import aplicacion.DAO.PeliculaDAO;
import aplicacion.DAO.PeliculaDAOImp;
import aplicacion.VO.PeliculaVO;
import aplicacion.Vistas.Alertas;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.ByteArrayInputStream;

public class PeliculasController {

    @FXML
    private TableView<PeliculaVO> tablePeliculas;

    @FXML
    private TableColumn<PeliculaVO, String> colNombrePlanta;

    @FXML
    private TableColumn<PeliculaVO, String> colDescripcionPlanta;


    @FXML
    private TableColumn<PeliculaVO, String> colPropiedades;

    @FXML
    private TableColumn<PeliculaVO, String> colEfectosSecundarios;

    @FXML
    private ObservableList<PeliculaVO> plantasList;

    @FXML
    private TextField txtNombrePlanta;

    @FXML
    private TextArea txtDescripcion;

    @FXML
    private TextField txtNombreCientifico;

    @FXML
    private TableColumn<PeliculaVO, byte[]> colImagen;

    @FXML
    private TextArea txtPropiedades;

    @FXML
    private TextArea txtEfecSecundarios;

    @FXML
    private ImageView imgPreview;
    private byte[] currentImageBytes;

    private PeliculaDAO peliculaDAO = new PeliculaDAOImp();
    private ObservableList<PeliculaVO> listaPeliculas = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Inicializar la lista observable
        plantasList = FXCollections.observableArrayList();

        // 1. Configuración de columnas de texto
        colNombrePlanta.setCellValueFactory(new PropertyValueFactory<>("Titulo"));
        colDescripcionPlanta.setCellValueFactory(new PropertyValueFactory<>("Clasificación"));
        colEfectosSecundarios.setCellValueFactory(new PropertyValueFactory<>("Stock"));
        colPropiedades.setCellValueFactory(new PropertyValueFactory<>("Descripción"));


        // 2. Configuración columna de imagen (primera columna)
        TableColumn<PeliculaVO, byte[]> colImagen = (TableColumn<PeliculaVO, byte[]>) tablePeliculas.getColumns().get(0);
        colImagen.setCellValueFactory(cellData -> {
            byte[] imagenBytes = cellData.getValue().imagenProperty().get();
            return new SimpleObjectProperty<>(imagenBytes);
        });

        // CellFactory para mostrar la imagen
        colImagen.setCellFactory(column -> new TableCell<>() {
            private final ImageView imageView = new ImageView();
            {
                imageView.setFitWidth(80);
                imageView.setFitHeight(80);
                imageView.setPreserveRatio(true);
            }

            @Override
            protected void updateItem(byte[] imagenBytes, boolean empty) {
                super.updateItem(imagenBytes, empty);
                if (empty || imagenBytes == null || imagenBytes.length == 0) {
                    setGraphic(null);
                } else {
                    try {
                        Image image = new Image(new ByteArrayInputStream(imagenBytes));
                        imageView.setImage(image);
                        setGraphic(imageView);
                    } catch (Exception e) {
                        setGraphic(new Label("Error de imagen"));
                        System.err.println("Error cargando imagen: " + e.getMessage());
                    }
                }
            }
        });
    }

    @FXML
    private void handleRentarPelicula() {
    }

    @FXML
    private void handleDevolverPelicula() {
        // Lógica para devolución similar a renta
    }

}