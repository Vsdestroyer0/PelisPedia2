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
        // Inicialización de componentes si es necesario
    }

    @FXML
    private void handleCatalogo() {
        try {
            // Cerrar la ventana actual
            Stage stage = (Stage) ButtonPelis.getScene().getWindow();
            stage.close();
            
            // Intentar diferentes rutas para cargar el FXML
            FXMLLoader fxmlLoader = null;
            
            try {
                // Intento 1: Ruta relativa estándar
                fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ListaPeliculas.fxml"));
                if (fxmlLoader.getLocation() == null) {
                    // Intento 2: Ruta completa
                    fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("aplicacion/application/ListaPeliculas.fxml"));
                }
                
                if (fxmlLoader.getLocation() == null) {
                    throw new IOException("No se pudo encontrar el archivo FXML");
                }
                
                Parent root = fxmlLoader.load();
                Stage newStage = new Stage();
                newStage.setScene(new Scene(root));
                newStage.setTitle("Gestión de Películas");
                newStage.show();
            } catch (IOException e) {
                Alertas.mostrarError("Error al cargar el FXML: " + e.getMessage());
                e.printStackTrace(); // Mostrar stack trace para diagnóstico
            }
        } catch (Exception e) {
            Alertas.mostrarError("Error general: " + e.getMessage());
            e.printStackTrace();
        }
    }

@FXML
private void handleUsuarios() {
    try {
        // Cerrar la ventana actual
        Stage stage = (Stage) ButtonUsers.getScene().getWindow();
        stage.close();
        
        // Intentar diferentes rutas para cargar el FXML
        FXMLLoader fxmlLoader = null;
        
        try {
            // Intento 1: Ruta relativa estándar
            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ListaUsuarios.fxml"));
            if (fxmlLoader.getLocation() == null) {
                // Intento 2: Ruta completa
                fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("aplicacion/application/ListaUsuarios.fxml"));
            }
            
            if (fxmlLoader.getLocation() == null) {
                throw new IOException("No se pudo encontrar el archivo FXML");
            }
            
            Parent root = fxmlLoader.load();
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Gestión de Usuarios");
            newStage.show();
        } catch (IOException e) {
            Alertas.mostrarError("Error al cargar el FXML: " + e.getMessage());
            e.printStackTrace(); // Mostrar stack trace para diagnóstico
        }
    } catch (Exception e) {
        Alertas.mostrarError("Error general: " + e.getMessage());
        e.printStackTrace();
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
    
    @FXML
    private void handleTickets() {
        try {
            // Cerrar la ventana actual
            Stage stage = (Stage) ButtonPelis.getScene().getWindow();
            stage.close();
            
            // Intentar diferentes rutas para cargar el FXML
            FXMLLoader fxmlLoader = null;
            
            try {
                // Intento 1: Ruta relativa estándar
                fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ListaTickets.fxml"));
                if (fxmlLoader.getLocation() == null) {
                    // Intento 2: Ruta completa
                    fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("aplicacion/application/ListaTickets.fxml"));
                }
                
                if (fxmlLoader.getLocation() == null) {
                    throw new IOException("No se pudo encontrar el archivo FXML para tickets");
                }
                
                Parent root = fxmlLoader.load();
                Stage newStage = new Stage();
                newStage.setScene(new Scene(root));
                newStage.setTitle("Gestión de Tickets");
                newStage.show();
            } catch (IOException e) {
                Alertas.mostrarError("Error al cargar la pantalla de tickets: " + e.getMessage());
                e.printStackTrace(); // Mostrar stack trace para diagnóstico
            }
        } catch (Exception e) {
            Alertas.mostrarError("Error general: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
}
