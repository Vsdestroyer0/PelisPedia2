package Controles;

import aplicacion.BaseDatos.BaseDatos;
import aplicacion.application.HelloApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class UsuarioController {

    BaseDatos baseDatos = new BaseDatos();

    @FXML
    private TableView<AltaPeliculasController.Planta> tablePlantas;

    @FXML
    private TableColumn<AltaPeliculasController.Planta, String> colNombrePlanta;

    @FXML
    private TableColumn<AltaPeliculasController.Planta, String> colDescripcionPlanta;

    @FXML
    private TableColumn<AltaPeliculasController.Planta, String> colNombreCientifico;

    @FXML
    private TableColumn<AltaPeliculasController.Planta, String> colPropiedades;

    @FXML
    private TableColumn<AltaPeliculasController.Planta, String> colEfectosSecundarios;

    @FXML
    private TableColumn<AltaPeliculasController.Planta, byte[]> colNombrePlanta1;

    @FXML
    private ObservableList<AltaPeliculasController.Planta> plantasList;

    @FXML
    private TextField txtBusqueda;

    private FilteredList<AltaPeliculasController.Planta> filteredPlantas;

    @FXML
    public void initialize() {
        plantasList = FXCollections.observableArrayList();

        // Configuración de las columnas
        colNombrePlanta.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDescripcionPlanta.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colNombreCientifico.setCellValueFactory(new PropertyValueFactory<>("nombreCientifico"));
        colPropiedades.setCellValueFactory(new PropertyValueFactory<>("propiedades"));
        colEfectosSecundarios.setCellValueFactory(new PropertyValueFactory<>("efectosSecundarios"));

        //aaa
        configurarColumnaImagen();
        Arrays.asList(colNombrePlanta, colDescripcionPlanta, colNombreCientifico,
                        colPropiedades, colEfectosSecundarios)
                .forEach(this::configurarColumnaTexto);

        tablePlantas.setItems(plantasList);
        obtenerPeliculas();

        // Filtrado de plantas
        filteredPlantas = new FilteredList<>(plantasList, p -> true);
        tablePlantas.setItems(filteredPlantas);

        txtBusqueda.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrarPeliculas();

        });
    }

    private <T> void configurarColumnaTexto(TableColumn<T, String> columna) {
        columna.setCellFactory(tc -> new TableCell<>() {
            private final Text text = new Text();
            private final StackPane pane = new StackPane();

            {
                pane.getChildren().add(text);
                pane.setPadding(new Insets(5));
                text.wrappingWidthProperty().bind(columna.widthProperty().subtract(15));

            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    text.setText(item);
                    setGraphic(pane);
                }
            }
        });
    }

    private void configurarColumnaImagen() {
        colNombrePlanta1.setCellValueFactory(cellData -> cellData.getValue().imagenProperty());

        colNombrePlanta1.setCellFactory(column -> new TableCell<>() {
            private final ImageView imageView = new ImageView();

            {
                imageView.setFitWidth(80);
                imageView.setFitHeight(80);
                imageView.setPreserveRatio(true);
            }

            @Override
            protected void updateItem(byte[] imagenBytes, boolean empty) {
                super.updateItem(imagenBytes, empty);
                if (empty || imagenBytes == null) {
                    setGraphic(null);
                } else {
                    try {
                        Image image = new Image(new ByteArrayInputStream(imagenBytes));
                        imageView.setImage(image);
                        setGraphic(imageView);
                    } catch (Exception e) {
                        setGraphic(new Label("Error imagen"));
                    }
                }
            }
        });
    }

    @FXML
    void handleCerrarSesion(ActionEvent event){
        openLoginWindow();
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }

    private void openLoginWindow() {
        try{
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
            AnchorPane pane = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(new Scene(pane));
            stage.show();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void filtrarPeliculas() {
        String filtro = txtBusqueda.getText().toLowerCase();

        filteredPlantas.setPredicate(planta -> {
            if (filtro.isEmpty()) return true;

            return planta.getNombre().toLowerCase().contains(filtro) ||
                    planta.getDescripcion().toLowerCase().contains(filtro) ||
                    planta.getNombreCientifico().toLowerCase().contains(filtro) ||
                    planta.getPropiedades().toLowerCase().contains(filtro) ||
                    planta.getEfectosSecundarios().toLowerCase().contains(filtro);
        });
    }

    public void obtenerPeliculas() {
        plantasList.clear();
        ArrayList<AltaPeliculasController.Planta> plantas = baseDatos.obtenerPeliculas();
        if (plantas != null){
            plantasList.addAll(plantas);
        }
    }



}