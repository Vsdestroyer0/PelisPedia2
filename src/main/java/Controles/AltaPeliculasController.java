package Controles;

import aplicacion.VO.PeliculaVO;
import aplicacion.Vistas.Alertas;
import aplicacion.application.HelloApplication;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

public class AltaPeliculasController {

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
    private Button btnFileChooser;
    @FXML
    private ImageView imgPreview;
    private byte[] currentImageBytes;

    @FXML private Button btnVolverMenu;
    @FXML
    private void handleVolverMenu() {
        try {
            // Cerrar la ventana actual
            Stage stage = (Stage) btnVolverMenu.getScene().getWindow();
            stage.close();
            
            // Cargar el menú de usuario
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("MenuAdministrador.fxml"));
            Parent root = loader.load();
            
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Menú Administrador - PelisPedia");
            newStage.show();
        } catch (IOException e) {
            Alertas.mostrarError("Error al volver al menú: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    void Filechooser(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen de planta");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try (FileInputStream fis = new FileInputStream(selectedFile);
                 ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    bos.write(buffer, 0, bytesRead);
                }

                currentImageBytes = bos.toByteArray();
                imgPreview.setImage(new Image(new ByteArrayInputStream(currentImageBytes)));
            } catch (IOException e) {
                Alertas.mostrarError("No se pudo seleccionar la imagen " + e.getMessage());
            }
        }
    }

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


}
