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
import aplicacion.application.SesionUsuario;
import Controles.MisRentasController;

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
    private Button ButtonSalir;


    @FXML private Button btnVolverMenu;
    @FXML
    private void handleVolverMenu() {
        try {
            // Cerrar la ventana actual
            Stage stage = (Stage) btnVolverMenu.getScene().getWindow();
            stage.close();
            
            // Cargar el menú de usuario
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
            Parent root = loader.load();
            
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Bienvenido a PelisPedia");
            newStage.show();
        } catch (IOException e) {
            Alertas.mostrarError("Error al volver al menú: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        // Inicialización si es necesaria
    }

    @FXML
    private void handleCatalogo() {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("ListaPeliculas.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Catálogo de Películas - PelisPedia");
            
            // Cerrar la ventana actual
            Stage currentStage = (Stage) ButtonCatalogo.getScene().getWindow();
            currentStage.close();
            
            stage.show();
        } catch (IOException e) {
            Alertas.mostrarError("Error al abrir el catálogo de películas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMisRentas() {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("MisRentas.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Mis Películas Rentadas - PelisPedia");
            
            // Cerrar la ventana actual
            Stage currentStage = (Stage) ButtonMisRentas.getScene().getWindow();
            currentStage.close();
            
            // Verificar si el controlador implementa el método detenerTemporizadores
            Object controller = loader.getController();
            if (controller instanceof MisRentasController) {
                // Agregar un listener para detener los temporizadores al cerrar la ventana
                stage.setOnCloseRequest(event -> {
                    ((MisRentasController) controller).detenerTemporizadores();
                });
            }
            
            stage.show();
        } catch (IOException e) {
            Alertas.mostrarError("Error al abrir mis películas rentadas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleFavoritos() {
        try {
            // Verificar si el archivo FXML existe
            URL resource = HelloApplication.class.getResource("Favoritos.fxml");
            if (resource == null) {
                // Si no existe, mostrar mensaje de funcionalidad en desarrollo
                Alertas.mostrarAdvertencia("La sección de Favoritos estará disponible pronto. Estamos trabajando en implementarla.");
                return;
            }
            
            FXMLLoader loader = new FXMLLoader(resource);
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Mis Favoritos - PelisPedia");
            
            // Cerrar la ventana actual
            Stage currentStage = (Stage) ButtonFav.getScene().getWindow();
            currentStage.close();
            
            stage.show();
        } catch (IOException e) {
            // Si hay un error al cargar, implementar una versión simple de la vista
            Alertas.mostrarAdvertencia("La sección de Favoritos estará disponible pronto. Estamos trabajando en implementarla.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMiPerfil() {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("PanelUsuario.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Mi Perfil - PelisPedia");
            
            // Cerrar la ventana actual
            Stage currentStage = (Stage) ButtonMiPerfil.getScene().getWindow();
            currentStage.close();
            
            stage.show();
        } catch (IOException e) {
            Alertas.mostrarError("Error al abrir mi perfil: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSalir() {
        try {
            // Cerrar la sesión de usuario
            SesionUsuario.getInstancia().cerrarSesion();
            
            // Cerrar la ventana actual
            Stage stage = (Stage) ButtonSalir.getScene().getWindow();
            stage.close();
            
            // Cargar la pantalla de inicio de sesión
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
            Parent root = fxmlLoader.load();
            
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Inicio de Sesión - PelisPedia");
            newStage.show();
        } catch (IOException e) {
            Alertas.mostrarError("Error al cerrar sesión: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
