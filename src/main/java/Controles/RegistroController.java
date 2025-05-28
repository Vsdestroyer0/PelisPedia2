package Controles;

import aplicacion.DAO.UsuarioDAO;
import aplicacion.DAO.UsuarioDAOImp;
import aplicacion.VO.UsuarioVO;
import aplicacion.Vistas.Alertas;
import aplicacion.application.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class RegistroController {
    // Campos de texto (manteniendo los mismos)
    @FXML private TextField txtUsername;
    @FXML private TextField txtEmail;
    @FXML private TextField txtSecurityQuestion;
    @FXML private TextField txtSecurityAnswer;
    @FXML private TextField txtAddress; // Agregado para la dirección
    @FXML private PasswordField txtPassword;
    @FXML private PasswordField txtConfirmPassword;
    @FXML private TextField txtVisiblePassword;
    @FXML private TextField txtVisibleConfirmPassword;
    @FXML private Button btnTogglePassword1;
    @FXML private Button btnTogglePassword2;
    @FXML private Button btnRegistrar; // Botón para registrar
    @FXML private Button SeleccionImagen; // Botón para seleccionar imagen
    @FXML private ImageView imgPreview; // Vista previa de la imagen (si existe en el FXML)

    private boolean passwordVisible = false;
    private boolean confirmPasswordVisible = false;
    private byte[] imagenBytes = null; // Para almacenar la imagen seleccionada

    // Instancia del DAO para manipular usuarios
    private UsuarioDAO usuarioDAO;

    @FXML
    public void initialize() {
        usuarioDAO = new UsuarioDAOImp();
        // Configurar bindings bidireccionales
        txtVisiblePassword.visibleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                txtVisiblePassword.setText(txtPassword.getText());
            } else {
                txtPassword.setText(txtVisiblePassword.getText());
            }
        });

        txtVisibleConfirmPassword.visibleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                txtVisibleConfirmPassword.setText(txtConfirmPassword.getText());
            } else {
                txtConfirmPassword.setText(txtVisibleConfirmPassword.getText());
            }
        });

        // Ocultar campos de texto visibles inicialmente
        txtVisiblePassword.setVisible(false);
        txtVisibleConfirmPassword.setVisible(false);

        // Asignar el manejador de eventos al botón de registro
        btnRegistrar.setOnAction(this::handleRegister);
        
        // Asignar el manejador para seleccionar imagen
        SeleccionImagen.setOnAction(this::handleSeleccionarImagen);
    }

    @FXML
    void handleRegreso(ActionEvent event){
        openLoggin();
    }

    @FXML
    public void togglePasswordVisibility(ActionEvent event) {
        if (event.getSource() == btnTogglePassword1) {
            passwordVisible = !passwordVisible;
            txtPassword.setVisible(!passwordVisible);
            txtVisiblePassword.setVisible(passwordVisible);
            txtVisiblePassword.setText(txtPassword.getText());
            btnTogglePassword1.setText(passwordVisible ? "🔒" : "👁");
        } else if (event.getSource() == btnTogglePassword2) {
            confirmPasswordVisible = !confirmPasswordVisible;
            txtConfirmPassword.setVisible(!confirmPasswordVisible);
            txtVisibleConfirmPassword.setVisible(confirmPasswordVisible);
            txtVisibleConfirmPassword.setText(txtConfirmPassword.getText());
            btnTogglePassword2.setText(confirmPasswordVisible ? "🔒" : "👁");
        }
    }

    
    @FXML
    private void handleSeleccionarImagen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Imagen de Perfil");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );
        
        // Obtener la ventana actual
        Stage stage = (Stage) SeleccionImagen.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        
        if (selectedFile != null) {
            try {
                // Leer la imagen como bytes para almacenar en la base de datos
                try (FileInputStream fis = new FileInputStream(selectedFile)) {
                    imagenBytes = new byte[(int) selectedFile.length()];
                    fis.read(imagenBytes);
                }
                
                // Mostrar vista previa si hay un ImageView configurado
                if (imgPreview != null) {
                    Image image = new Image(selectedFile.toURI().toString());
                    imgPreview.setImage(image);
                }
                
                Alertas.mostrarExito("Imagen seleccionada correctamente");
            } catch (IOException e) {
                Alertas.mostrarError("Error al leer la imagen: " + e.getMessage());
                imagenBytes = null;
            }
        }
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        try {
            // Validar campos básicos
            if (txtUsername.getText().isEmpty() ||
                    txtEmail.getText().isEmpty() ||
                    (passwordVisible ? txtVisiblePassword.getText().isEmpty() : txtPassword.getText().isEmpty()) ||
                    (confirmPasswordVisible ? txtVisibleConfirmPassword.getText().isEmpty() : txtConfirmPassword.getText().isEmpty())) {

                Alertas.mostrarError("Error, todos los campos son obligatorios");
                return;
            }

            // Obtener contraseñas según visibilidad
            String password = passwordVisible ? txtVisiblePassword.getText() : txtPassword.getText();
            String confirmPassword = confirmPasswordVisible ? txtVisibleConfirmPassword.getText() : txtConfirmPassword.getText();

            // Validar coincidencia de contraseñas
            if (!password.equals(confirmPassword)) {
                Alertas.mostrarError("Error, las contraseñas no coinciden");
                return;
            }

            // Validar pregunta/respuesta seguridad
            if (txtSecurityQuestion.getText().isEmpty() || txtSecurityAnswer.getText().isEmpty()) {
                Alertas.mostrarError("Error, debe completar la pregunta y respuesta de seguridad");
                return;
            }

            // Crear usuario
            UsuarioVO nuevoUsuario = new UsuarioVO(
                    txtUsername.getText(),
                    txtEmail.getText(),
                    password,
                    txtSecurityQuestion.getText(),
                    txtSecurityAnswer.getText(),
                    txtAddress.getText(),
                    false // No es admin por defecto
            );
            
            // Asignar la imagen si fue seleccionada
            if (imagenBytes != null) {
                nuevoUsuario.setImagen(imagenBytes);
            }
            
            // Asegurarse de que el usuario está activo por defecto
            nuevoUsuario.setActivo(true);

            // Registrar en base de datos
            if (usuarioDAO.AgregarUsuario(nuevoUsuario)) {
                Alertas.mostrarAdvertencia("Exito, el usuario se registró correctamente");
                // Opcional: Limpiar campos después del registro
                limpiarCampos();
                openLoggin();
            } else {
                Alertas.mostrarError("Error, no se pudo registrar el usuario");
            }

        } catch (Exception e) {
            Alertas.mostrarError("Error crítico, ocurró el siguiente error " + e.getMessage());
        }
    }

    // Método opcional para limpiar campos después del registro
    private void limpiarCampos() {
        txtUsername.clear();
        txtEmail.clear();
        txtPassword.clear();
        txtVisiblePassword.clear();
        txtConfirmPassword.clear();
        txtVisibleConfirmPassword.clear();
        txtSecurityQuestion.clear();
        txtSecurityAnswer.clear();
        txtAddress.clear();
        
        // Limpiar imagen si hay vista previa
        if (imgPreview != null) {
            imgPreview.setImage(null);
        }
        imagenBytes = null;
    }

    @FXML
    private void openLoggin(){
        try{
            // Usar la ruta absoluta desde la raíz del classpath
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("aplicacion/application/hello-view.fxml"));

            if (loader.getLocation() == null) {
                // Si no funciona, intentar con una ruta relativa al paquete de HelloApplication
                loader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
            }

            if (loader.getLocation() == null) {
                // Si aún no funciona, intentar con una ruta absoluta desde la raíz
                loader = new FXMLLoader(getClass().getClassLoader().getResource("/aplicacion/application/hello-view.fxml"));
            }

            if (loader.getLocation() == null) {
                throw new IOException("No se pudo encontrar el archivo FXML: hello-view.fxml en la ruta esperada");
            }

            AnchorPane pane = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(new Scene(pane));

            // Cerrar la ventana actual
            Stage currentStage = (Stage) txtUsername.getScene().getWindow();
            currentStage.close();

            stage.show();
        } catch (Exception e){
            Alertas.mostrarError("Error al cargar la ventana de registro" + e.getMessage());
        }
    }

}