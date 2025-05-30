package Controles;

import aplicacion.DAO.UsuarioDAO;
import aplicacion.DAO.UsuarioDAOImp;
import aplicacion.VO.UsuarioVO;
import aplicacion.Vistas.Alertas;
import aplicacion.application.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PanelUsuarioController implements Initializable {

    @FXML private AnchorPane rootPane;
    @FXML private TextField txtNombre;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtDireccion;
    @FXML private PasswordField txtPasswordActual;
    @FXML private PasswordField txtPasswordNueva;
    @FXML private PasswordField txtPasswordConfirm;
    @FXML private ImageView imgPreview;
    @FXML private Button btnSeleccionarImagen;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;
    @FXML private Button btnVolverMenu;
    
    private UsuarioDAO usuarioDAO;
    private UsuarioVO usuarioActual;
    private byte[] imagenBytes;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        usuarioDAO = new UsuarioDAOImp();
        
        // Cargar los datos del usuario actual
        cargarDatosUsuario();
        
        // Configurar los botones
        configureButtons();
    }
    
    private void cargarDatosUsuario() {
        try {
            // Obtener el usuario de la sesión actual
            if (aplicacion.application.SesionUsuario.getInstancia().haySesionActiva()) {
                usuarioActual = aplicacion.application.SesionUsuario.getInstancia().getUsuarioActual();
            } else {
                Alertas.mostrarError("No hay usuario con sesión activa. Por favor, inicie sesión.");
                volverAlMenu();
                return;
            }
            
            if (usuarioActual == null) {
                // Si no existe el usuario de prueba, intentamos obtener cualquier usuario no admin
                Alertas.mostrarAdvertencia("Usuario no encontrado. Por favor, inicie sesión nuevamente.");
                volverAlMenu();
                return;
            }
            
            // Mostrar los datos en los campos
            txtNombre.setText(usuarioActual.getNombre());
            txtCorreo.setText(usuarioActual.getCorreo());
            txtDireccion.setText(usuarioActual.getDireccion());
            
            // Cargar imagen si existe
            if (usuarioActual.getImagen() != null) {
                imagenBytes = usuarioActual.getImagen();
                Image image = new Image(new ByteArrayInputStream(imagenBytes));
                imgPreview.setImage(image);
            } else {
                // Cargar imagen por defecto
                Image defaultImage = new Image(getClass().getResourceAsStream("/aplicacion/Imgs/User.png"));
                imgPreview.setImage(defaultImage);
            }
        } catch (Exception e) {
            Alertas.mostrarError("Error al cargar datos del usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void configureButtons() {
        btnSeleccionarImagen.setOnAction(e -> handleSeleccionarImagen());
        btnGuardar.setOnAction(e -> handleGuardarCambios());
        btnCancelar.setOnAction(e -> handleCancelar());
        btnVolverMenu.setOnAction(e -> handleVolverMenu());
    }
    
    @FXML
    private void handleSeleccionarImagen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Imagen de Perfil");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );
        
        File file = fileChooser.showOpenDialog(rootPane.getScene().getWindow());
        if (file != null) {
            try {
                FileInputStream fis = new FileInputStream(file);
                imagenBytes = fis.readAllBytes();
                fis.close();
                
                Image image = new Image(file.toURI().toString());
                imgPreview.setImage(image);
                
                Alertas.mostrarExito("Imagen seleccionada correctamente");
            } catch (IOException e) {
                Alertas.mostrarError("Error al cargar la imagen: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    @FXML
    private void handleGuardarCambios() {
        // Validar campos obligatorios
        if (txtNombre.getText().trim().isEmpty() || txtDireccion.getText().trim().isEmpty()) {
            Alertas.mostrarError("Los campos Nombre y Dirección son obligatorios");
            return;
        }
        
        // Validar cambio de contraseña si se ha proporcionado
        if (!txtPasswordNueva.getText().isEmpty()) {
            // Verificar que se haya ingresado la contraseña actual
            if (txtPasswordActual.getText().isEmpty()) {
                Alertas.mostrarError("Debe ingresar su contraseña actual para cambiarla");
                return;
            }
            
            // Verificar que la contraseña actual sea correcta
            // No usamos la contraseña del objeto usuarioActual porque es null por seguridad
            // En su lugar, verificamos directamente contra la base de datos
            try {
                // Autenticar al usuario directamente con la base de datos
                UsuarioVO autenticado = usuarioDAO.autenticarUsuario(usuarioActual.getCorreo(), txtPasswordActual.getText());
                
                if (autenticado == null) {
                    Alertas.mostrarError("La contraseña actual es incorrecta");
                    return;
                }
                
                // Verificar que las contraseñas nuevas coincidan
                if (!txtPasswordNueva.getText().equals(txtPasswordConfirm.getText())) {
                    Alertas.mostrarError("Las contraseñas nuevas no coinciden");
                    return;
                }
                
                // Establecer la nueva contraseña
                usuarioActual.setContraseña(txtPasswordNueva.getText());
            } catch (Exception e) {
                Alertas.mostrarError("Error al verificar la contraseña: " + e.getMessage());
                return;
            }
        }
        
        try {
            // Actualizar los datos del usuario
            usuarioActual.setNombre(txtNombre.getText().trim());
            usuarioActual.setDireccion(txtDireccion.getText().trim());
            
            // Si se seleccionó una imagen, actualizarla
            if (imagenBytes != null) {
                usuarioActual.setImagen(imagenBytes);
            }
            
            // Guardar cambios en la base de datos
            if (usuarioDAO.actualizarUsuario(usuarioActual)) {
                Alertas.mostrarExito("Datos actualizados correctamente");
                
                // Limpiar campos de contraseña
                txtPasswordActual.clear();
                txtPasswordNueva.clear();
                txtPasswordConfirm.clear();
            } else {
                Alertas.mostrarError("Error al actualizar los datos");
            }
        } catch (Exception e) {
            Alertas.mostrarError("Error al guardar los cambios: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleCancelar() {
        // Recargar los datos del usuario para deshacer cambios
        cargarDatosUsuario();
        
        // Limpiar campos de contraseña
        txtPasswordActual.clear();
        txtPasswordNueva.clear();
        txtPasswordConfirm.clear();
        
        Alertas.mostrarAdvertencia("Cambios descartados");
    }
    
    @FXML
    private void handleVolverMenu() {
        try {
            // Cerrar la ventana actual
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.close();
            
            // Abrir el menú de usuario
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("MenuUsuario.fxml"));
            Parent root = loader.load();
            
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Menú Usuario - PelisPedia");
            newStage.show();
        } catch (IOException e) {
            Alertas.mostrarError("Error al volver al menú: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void volverAlMenu() {
        try {
            // Cerrar la ventana actual
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.close();
            
            // Abrir el menú de usuario
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("MenuUsuario.fxml"));
            Parent root = loader.load();
            
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Menú Usuario - PelisPedia");
            newStage.show();
        } catch (IOException e) {
            Alertas.mostrarError("Error al volver al menú: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
