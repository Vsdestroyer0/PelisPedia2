package Controles;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import aplicacion.Vistas.Alertas;
import aplicacion.application.HelloApplication;

public class MenuUsuarioController {

    @FXML
    private Button ButtonCatalogo;
    @FXML
    private Button ButtonMisRentas;
    @FXML
    private Button ButtonFav;
    @FXML
    private Button ButtonMiPerfil;

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        // Inicialización si es necesaria
    }

    @FXML
    private void handleCatalogo() {
        Alertas.mostrarAdvertencia("Funcionalidad en desarrollo, la sección de Catálogo estará disponible pronto.");
    }

    @FXML
    private void handleMisRentas() {
        // Implementar lógica para ver las rentas
        Alertas.mostrarAdvertencia("Funcionalidad en desarrollo, la sección de Mis Rentas estará disponible pronto.");
    }

    @FXML
    private void handleFavoritos() {
        // Implementar lógica para ver favoritos
        Alertas.mostrarAdvertencia("Funcionalidad en desarrollo, la sección de Favoritos estará disponible pronto.");
    }

    @FXML
    private void handleMiPerfil() {
        Alertas.mostrarAdvertencia("Funcionalidad en desarrollo, la sección de Mi Perfil estará disponible pronto.");
    }

    @FXML
    private void handleCerrarSesion() {
        try {
            // Cerrar la ventana actual
            Stage stage = (Stage) ButtonMiPerfil.getScene().getWindow();
            stage.close();
            
            // Cargar la pantalla de inicio de sesión
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
            Parent root = fxmlLoader.load();
            
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Inicio de Sesión");
            newStage.show();
        } catch (IOException e) {
            Alertas.mostrarError("Error al cerrar sesión: " + e.getMessage());
        }
    }
}
