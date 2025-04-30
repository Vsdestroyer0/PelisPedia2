package Controles;

import aplicacion.application.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class RegistroController {
    // Campos de texto (manteniendo los mismos)
    @FXML private TextField txtUsername;
    @FXML private TextField txtEmail;
    @FXML private TextField txtSecurityQuestion;
    @FXML private TextField txtSecurityAnswer;
    @FXML private PasswordField txtPassword;
    @FXML private PasswordField txtConfirmPassword;
    @FXML private TextField txtVisiblePassword;
    @FXML private TextField txtVisibleConfirmPassword;
    @FXML private Button btnTogglePassword1;
    @FXML private Button btnTogglePassword2;

    private boolean passwordVisible = false;
    private boolean confirmPasswordVisible = false;

    @FXML
    public void initialize() {
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
    }

    @FXML
    public void togglePasswordVisibility(ActionEvent event) {
        if (event.getSource() == btnTogglePassword1) {
            passwordVisible = !passwordVisible;
            txtPassword.setVisible(!passwordVisible);
            txtVisiblePassword.setVisible(passwordVisible);
            btnTogglePassword1.setText(passwordVisible ? "🔒" : "👁️");
        } else if (event.getSource() == btnTogglePassword2) {
            confirmPasswordVisible = !confirmPasswordVisible;
            txtConfirmPassword.setVisible(!confirmPasswordVisible);
            txtVisibleConfirmPassword.setVisible(confirmPasswordVisible);
            btnTogglePassword2.setText(confirmPasswordVisible ? "🔒" : "👁️");
        }
    }

    @FXML
    public void handleRegister(ActionEvent event) {
        // Validaciones
        if (!validarCampos()) {
            return;
        }

        // Aquí iría la lógica para registrar al usuario
        // usuarioDAO.agregarUsuario(...);

        openLoginWindow();
    }

    private boolean validarCampos() {
        // Validar campos vacíos
        if (txtUsername.getText().isEmpty() ||
                txtEmail.getText().isEmpty() ||
                txtSecurityQuestion.getText().isEmpty() ||
                txtSecurityAnswer.getText().isEmpty() ||
                (txtPassword.getText().isEmpty() && txtVisiblePassword.getText().isEmpty()) ||
                (txtConfirmPassword.getText().isEmpty() && txtVisibleConfirmPassword.getText().isEmpty())) {
            mostrarError("Todos los campos son obligatorios");
            return false;
        }


        // Obtener contraseña del campo visible u oculto
        String password = passwordVisible ? txtVisiblePassword.getText() : txtPassword.getText();
        String confirmPassword = confirmPasswordVisible ? txtVisibleConfirmPassword.getText() : txtConfirmPassword.getText();

        // Validar coincidencia de contraseñas
        if (!password.equals(confirmPassword)) {
            mostrarError("Las contraseñas no coinciden");
            return false;
        }

        // Validar fortaleza de contraseña
        if (password.length() < 8) {
            mostrarError("La contraseña debe tener al menos 8 caracteres");
            return false;
        }

        return true;
    }

    private void mostrarError(String mensaje) {
        // Implementar lógica para mostrar mensaje de error en la UI
        System.err.println(mensaje);
        // Ejemplo con diálogo:
        // Alert alert = new Alert(Alert.AlertType.ERROR);
        // alert.setContentText(mensaje);
        // alert.show();
    }

    private void openLoginWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
            AnchorPane pane = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(new Scene(pane));
            stage.show();

            // Cerrar ventana actual
            Stage currentStage = (Stage) txtUsername.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            mostrarError("Error al abrir la ventana de login");
            e.printStackTrace();
        }
    }
}