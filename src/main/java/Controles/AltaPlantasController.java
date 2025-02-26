package Controles;


import aplicacion.application.HelloApplication;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class AltaPlantasController {

    aplicacion.BaseDatos.BaseDatos baseDatos = new aplicacion.BaseDatos.BaseDatos();



    @FXML
    private TableView<Planta> tablePlantas;

    @FXML
    private TableColumn<Planta, String> colNombrePlanta;

    @FXML
    private TableColumn<Planta, String> colDescripcionPlanta;

    @FXML
    private TableColumn<Planta, String> colNombreCientifico;

    @FXML
    private TableColumn<Planta, String> colPropiedades;

    @FXML
    private TableColumn<Planta, String> colEfectosSecundarios;

    @FXML
    private ObservableList<Planta> plantasList;

    @FXML
    private TextField txtNombrePlanta;

    @FXML
    private TextArea txtDescripcion;

    @FXML
    private TextField txtNombreCientifico;

    @FXML
    private TextArea txtPropiedades;

    @FXML
    private TextArea txtEfecSecundarios;

    @FXML
    public void initialize() {
        plantasList = FXCollections.observableArrayList();
        colNombrePlanta.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDescripcionPlanta.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colNombreCientifico.setCellValueFactory(new PropertyValueFactory<>("nombreCientifico"));
        colPropiedades.setCellValueFactory(new PropertyValueFactory<>("propiedades"));
        colEfectosSecundarios.setCellValueFactory(new PropertyValueFactory<>("efectosSecundarios"));
        tablePlantas.setItems(plantasList);
        obtenerPlantas();

        // Add listener to populate fields when a plant is selected
        tablePlantas.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                txtNombrePlanta.setText(newValue.getNombre());
                txtDescripcion.setText(newValue.getDescripcion());
                txtNombreCientifico.setText(newValue.getNombreCientifico());
                txtPropiedades.setText(newValue.getPropiedades());
                txtEfecSecundarios.setText(newValue.getEfectosSecundarios());
            }
        });
    }


    @FXML
    void handleAltaPlanta(ActionEvent event) {
        String nombre = txtNombrePlanta.getText();
        String descripcion = txtDescripcion.getText();
        String nombreCientifico = txtNombreCientifico.getText();
        String propiedades = txtPropiedades.getText();
        String efectosSecundarios = txtEfecSecundarios.getText();

        if (baseDatos.agregarPlanta(nombre, descripcion, nombreCientifico, propiedades, efectosSecundarios)) {
            obtenerPlantas();
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Éxito");
            alert.setHeaderText(null);
            alert.setContentText("Planta " + nombre + " agregada exitosamente.");
            alert.showAndWait();

            // Clear the text fields and text areas
            txtNombrePlanta.clear();
            txtDescripcion.clear();
            txtNombreCientifico.clear();
            txtPropiedades.clear();
            txtEfecSecundarios.clear();
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No se pudo agregar la planta " + nombre + ".");
            alert.showAndWait();
        }
    }

    @FXML
    void handleModificarPlanta(ActionEvent event) {
        Planta selectedPlanta = tablePlantas.getSelectionModel().getSelectedItem();
        if (selectedPlanta != null) {
            String nombre = txtNombrePlanta.getText();
            String descripcion = txtDescripcion.getText();
            String nombreCientifico = txtNombreCientifico.getText();
            String propiedades = txtPropiedades.getText();
            String efectosSecundarios = txtEfecSecundarios.getText();

            if (baseDatos.modificarPlanta(selectedPlanta.getNombre(), nombre, descripcion, nombreCientifico, propiedades, efectosSecundarios)) {
                obtenerPlantas();
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Éxito");
                alert.setHeaderText(null);
                alert.setContentText("Planta " + nombre + " modificada exitosamente.");
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
    void handleEliminarPlanta(ActionEvent event) {
        Planta selectedPlanta = tablePlantas.getSelectionModel().getSelectedItem();
        if (selectedPlanta != null) {
            String nombrePlanta = selectedPlanta.getNombre();
            if (baseDatos.eliminarPlanta(nombrePlanta)) {
                plantasList.remove(selectedPlanta);
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

    public static class Planta {
        private final String nombre;
        private String descripcion;
        private String nombreCientifico;
        private String propiedades;
        private String efectosSecundarios;

        public Planta(String nombre, String descripcion, String nombreCientifico, String propiedades, String efectosSecundarios) {
            this.nombre = nombre;
            this.descripcion = descripcion;
            this.nombreCientifico = nombreCientifico;
            this.propiedades = propiedades;
            this.efectosSecundarios = efectosSecundarios;
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
    }



    @FXML
    void handleCerrarSesion(ActionEvent event){
        openLoginWindow();
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }

    private void openLoginWindow() {
        try{
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/aplicacion/application/PanelUsuario.fxml")
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

    public void obtenerPlantas() {
        plantasList.clear();
        ArrayList<Planta> plantas = baseDatos.obtenerPlantas();
        if (plantas != null) {
            plantasList.addAll(plantas);
        }
    }
}
