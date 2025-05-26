package Controles;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import aplicacion.Vistas.Alertas;
import aplicacion.application.HelloApplication;

public class MenuAdministradorController {

    @FXML
    private Button ButtonPelis;
    
    @FXML
    private Button ButtonUsers;
    
    @FXML
    private ImageView userImageView;
    
    @FXML
    private ImageView logoImageView;

    

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        // Inicialización de componentes si es necesario
    }

    @FXML
    private void handleCatalogo() {
        Alertas.mostrarAdvertencia("Funcionalidad en desarrollo, la sección de Catálogo estará disponible pronto.");
    }

    @FXML
    private void handleUsuarios() {
        try {
            // Cerrar la ventana actual
            Stage stage = (Stage) ButtonUsers.getScene().getWindow();
            stage.close();
            
            // Cargar la vista de gestión de usuarios
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ListaUsuarios.fxml"));
            Parent root = fxmlLoader.load();
            
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Gestión de Usuarios");
            newStage.show();
        } catch (IOException e) {
            Alertas.mostrarError("Error al cargar la gestión de usuarios: " + e.getMessage());
        }
    }

    @FXML
    private void handleCerrarSesion() {
        try {
            // Cerrar la ventana actual
            Stage stage = (Stage) ButtonPelis.getScene().getWindow();
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
