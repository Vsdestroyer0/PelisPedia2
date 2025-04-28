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
    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtSecurityQuestion;

    @FXML
    private TextField txtSecurityAnswer;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private PasswordField txtConfirmPassword;

    @FXML
    private TextField txtVisiblePassword;

    @FXML
    private TextField txtVisibleConfirmPassword;

    @FXML
    private Button btnTogglePassword1;

    @FXML
    private Button btnTogglePassword2;

    @FXML
    public void initialize() {
        // Sincronizar los campos de texto visibles con los campos de contraseña
        txtVisiblePassword.textProperty().bindBidirectional(txtPassword.textProperty());
        txtVisibleConfirmPassword.textProperty().bindBidirectional(txtConfirmPassword.textProperty());
    }

    @FXML
    public void togglePasswordVisibility(ActionEvent event) {
        // Determinar qué botón fue presionado
        Button sourceButton = (Button) event.getSource();

        if (sourceButton.equals(btnTogglePassword1)) {
            // Toggle para el primer campo de contraseña
            boolean isVisible = txtVisiblePassword.isVisible();
            txtVisiblePassword.setVisible(!isVisible);
            txtPassword.setVisible(isVisible);

            // Cambiar el texto del botón
            btnTogglePassword1.setText(isVisible ? "👁️" : "🔒");
        } else if (sourceButton.equals(btnTogglePassword2)) {
            // Toggle para el segundo campo de contraseña
            boolean isVisible = txtVisibleConfirmPassword.isVisible();
            txtVisibleConfirmPassword.setVisible(!isVisible);
            txtConfirmPassword.setVisible(isVisible);

            // Cambiar el texto del botón
            btnTogglePassword2.setText(isVisible ? "👁" : "🔒");
        }
    }

    @FXML
    public void handleRegister(ActionEvent event) {
        // Validar que las contraseñas coincidan
        if (!txtPassword.getText().equals(txtConfirmPassword.getText())) {
            System.out.println("Las contraseñas no coinciden");
            return;
        }
        openLoginWindow();
    }

    private void openLoginWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
            AnchorPane pane = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(new Scene(pane));
            stage.show();

            // Opcional: cerrar la ventana actual
            Stage currentStage = (Stage) txtUsername.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}