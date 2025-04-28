package Controles;

import aplicacion.BaseDatos.DatabaseConnection;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.util.List;

public class AltaPeliculasController {

    DatabaseConnection databaseConnection = new DatabaseConnection();


    @FXML
    private TableView<Pelicula> tablaPeliculas;

    @FXML
    private TableColumn<Pelicula, String> colNombrePlanta;

    @FXML
    private TableColumn<Pelicula, String> colDescripcionPlanta;

    @FXML
    private TableColumn<Pelicula, String> colNombreCientifico;

    @FXML
    private TableColumn<Pelicula, String> colPropiedades;

    @FXML
    private TableColumn<Pelicula, String> colEfectosSecundarios;

    @FXML
    private ObservableList<Pelicula> ListaPeliculas;

    @FXML
    private TextField txtNombrePlanta;

    @FXML
    private TextArea txtDescripcion;

    @FXML
    private TextField txtNombreCientifico;

    @FXML
    private TableColumn<Pelicula, byte[]> colImagen;

    @FXML
    private TextArea txtPropiedades;

    @FXML
    private TextArea txtEfecSecundarios;

    @FXML
    private ImageView imgPreview;
    private byte[] currentImageBytes;

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
                System.out.println(e);
            }
        }
    }


    @FXML
    public void initialize() {
        // Inicializar la lista observable
        ListaPeliculas = FXCollections.observableArrayList();

        // 1. Configuración de columnas de texto
        colNombrePlanta.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDescripcionPlanta.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colNombreCientifico.setCellValueFactory(new PropertyValueFactory<>("nombreCientifico"));
        colPropiedades.setCellValueFactory(new PropertyValueFactory<>("propiedades"));
        colEfectosSecundarios.setCellValueFactory(new PropertyValueFactory<>("efectosSecundarios"));

        // 2. Configuración columna de imagen (primera columna)
        TableColumn<Pelicula, byte[]> colImagen = (TableColumn<Pelicula, byte[]>) tablaPeliculas.getColumns().get(0);
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

        // 3. Cargar datos iniciales
        try {
            List<Pelicula> peliculasDesdeBD = databaseConnection.obtenerPeliculas();
            ListaPeliculas.addAll(peliculasDesdeBD);
            tablaPeliculas.setItems(ListaPeliculas);

            // Log de diagnóstico
            System.out.println("Plantas cargadas: " + ListaPeliculas.size());
            if(!ListaPeliculas.isEmpty()) {
                System.out.println("Ejemplo primera planta: " + ListaPeliculas.get(0).getNombre());
            }
        } catch (Exception e) {
            System.err.println("Error inicializando datos: " + e.getMessage());
            e.printStackTrace();
        }

        // 4. Configurar selección de fila
        tablaPeliculas.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                actualizarCamposDesdeSeleccion(newValue);
            }
        });
    }

    private void actualizarCamposDesdeSeleccion(Pelicula pelicula) {
        txtNombrePlanta.setText(pelicula.getNombre());
        txtDescripcion.setText(pelicula.getDescripcion());
        txtNombreCientifico.setText(pelicula.getNombreCientifico());
        txtPropiedades.setText(pelicula.getPropiedades());
        txtEfecSecundarios.setText(pelicula.getEfectosSecundarios());

        // Cargar imagen seleccionada
        byte[] imagenBytes = pelicula.imagenProperty().get();
        if(imagenBytes != null && imagenBytes.length > 0) {
            imgPreview.setImage(new Image(new ByteArrayInputStream(imagenBytes)));
        }
    }

    @FXML
    void handleAltaPeliculas(ActionEvent event) {
        String nombre = txtNombrePlanta.getText();
        String descripcion = txtDescripcion.getText();
        String nombreCientifico = txtNombreCientifico.getText();
        String propiedades = txtPropiedades.getText();
        String efectosSecundarios = txtEfecSecundarios.getText();

        // Verificar si se ha seleccionado una imagen
        if (currentImageBytes == null) {
            System.out.println("Error Debe seleccionar una imagen");
            return;
        }

        // Lógica para agregar la planta
        if (databaseConnection.agregarPelicula(nombre, descripcion, nombreCientifico,
                propiedades, efectosSecundarios, currentImageBytes)) {
            databaseConnection.obtenerPeliculas();
            ListaPeliculas.setAll(databaseConnection.obtenerPeliculas()); // Recargar toda la lista
            tablaPeliculas.refresh();
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Éxito");
            alert.setHeaderText(null);
            alert.setContentText("Planta " + nombre + " agregada exitosamente.");
            alert.showAndWait();

            // Limpiar los campos de texto y áreas de texto
            txtNombrePlanta.clear();
            txtDescripcion.clear();
            txtNombreCientifico.clear();
            txtPropiedades.clear();
            txtEfecSecundarios.clear();
            currentImageBytes = null; // Resetear después de insertar

        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No se pudo agregar la planta " + nombre + ".");
            alert.showAndWait();
        }
    }




    @FXML
    void handleModificarPeliculas(ActionEvent event) {
        Pelicula selectedPelicula = tablaPeliculas.getSelectionModel().getSelectedItem();
        if (selectedPelicula != null) {
            String nombre = txtNombrePlanta.getText();
            String descripcion = txtDescripcion.getText();
            String nombreCientifico = txtNombreCientifico.getText();
            String propiedades = txtPropiedades.getText();
            String efectosSecundarios = txtEfecSecundarios.getText();

            // Verificar que los campos obligatorios no estén vacíos
            if (nombre.isEmpty() || descripcion.isEmpty() || nombreCientifico.isEmpty() || propiedades.isEmpty() || efectosSecundarios.isEmpty()) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Advertencia");
                alert.setHeaderText(null);
                alert.setContentText("Por favor, complete todos los campos obligatorios.");
                alert.showAndWait();
                return;
            }

            // Si no se selecciona una nueva imagen, mantener la imagen existente
            if (currentImageBytes == null) {
                currentImageBytes = selectedPelicula.imagenProperty().get();
            }

            // Lógica para modificar la planta, incluyendo la imagen
            if (databaseConnection.modificarPeliculas(selectedPelicula.getNombre(), nombre, descripcion,
                    nombreCientifico, propiedades, efectosSecundarios,
                    currentImageBytes)) {
                ListaPeliculas.setAll(databaseConnection.obtenerPeliculas()); // Recargar toda la lista
                tablaPeliculas.refresh(); // Refrescar la tabla para mostrar los cambios

                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Éxito");
                alert.setHeaderText(null);
                alert.setContentText("Planta " + nombre + " modificada exitosamente.");
                alert.showAndWait();

                // Limpiar los campos de texto y áreas de texto
                txtNombrePlanta.clear();
                txtDescripcion.clear();
                txtNombreCientifico.clear();
                txtPropiedades.clear();
                txtEfecSecundarios.clear();
                currentImageBytes = null; // Limpiar la imagen actual

            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("No se pudo modificar la planta " + nombre + ".");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, seleccione una planta para modificar.");
            alert.showAndWait();
        }
    }


    @FXML
    void handleEliminarPeliculas(ActionEvent event) {
        Pelicula selectedPelicula = tablaPeliculas.getSelectionModel().getSelectedItem();
        if (selectedPelicula != null) {
            String nombrePlanta = selectedPelicula.getNombre();
            if (databaseConnection.eliminarPlanta(nombrePlanta)) {
                ListaPeliculas.remove(selectedPelicula);
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Éxito");
                alert.setHeaderText(null);
                alert.setContentText("Planta " + nombrePlanta + " eliminada exitosamente.");
                alert.showAndWait();

                // limpiar los campos de texto y áreas de texto
                txtNombrePlanta.clear();
                txtDescripcion.clear();
                txtNombreCientifico.clear();
                txtPropiedades.clear();
                txtEfecSecundarios.clear();

            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("No se pudo eliminar la planta " + nombrePlanta + ".");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, seleccione una planta para eliminar.");
            alert.showAndWait();
        }
    }

    public void handleCerrarSesion(ActionEvent event) {
        openLoginWindow();
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }

    private void openLoginWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/aplicacion/application/hello-view.fxml")
            );
            Parent root = loader.load();

            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Panel de Usuario");
            newStage.show();
        } catch (IOException e) {
            System.err.println("Error cargando FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static class Pelicula {
        private final String nombre;
        private String descripcion;
        private String nombreCientifico;
        private String propiedades;
        private String efectosSecundarios;
        private final SimpleObjectProperty<byte[]> imagen = new SimpleObjectProperty<>();


        public Pelicula(String nombre, String descripcion, String nombreCientifico, String propiedades, String efectosSecundarios, byte[] imagen) {
            this.nombre = nombre;
            this.descripcion = descripcion;
            this.nombreCientifico = nombreCientifico;
            this.propiedades = propiedades;
            this.efectosSecundarios = efectosSecundarios;
            this.imagen.set(imagen);

        }

        public String getNombre() {
            return nombre;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public String getNombreCientifico() {
            return nombreCientifico;
        }

        public String getPropiedades() {
            return propiedades;
        }

        public String getEfectosSecundarios() {
            return efectosSecundarios;
        }

        public ObjectProperty<byte[]> imagenProperty() {
            return imagen;
        }

    }
}
