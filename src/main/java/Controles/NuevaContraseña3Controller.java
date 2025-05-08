package Controles;

import aplicacion.DAO.UsuarioDAO;
import aplicacion.DAO.UsuarioDAOImp;
import aplicacion.VO.UsuarioVO;
import aplicacion.Vistas.Alertas;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.io.IOException;

public class NuevaContraseña3Controller {
    @FXML private PasswordField txtNuevaContraseña;
    @FXML private PasswordField txtConfirmarContraseña;

    private String correoUsuario;

    // Recibe el correo de la pantalla anterior
    public void setCorreoUsuario(String correo) {
        this.correoUsuario = correo;
    }

    @FXML
    private void handleCambiarContraseña() {
        String nuevaContraseña = txtNuevaContraseña.getText().trim();
        String confirmacion = txtConfirmarContraseña.getText().trim();

        // Validaciones
        if (nuevaContraseña.isEmpty() || confirmacion.isEmpty()) {
            Alertas.mostrarAdvertencia("Complete todos los campos");
            return;
        }

        if (!nuevaContraseña.equals(confirmacion)) {
            Alertas.mostrarAdvertencia("Las contraseñas no coinciden");
            return;
        }

        if (nuevaContraseña.length() < 8) {
            Alertas.mostrarAdvertencia("La contraseña tiene que tener más de 8 carácteres");
            return;
        }

        // Actualizar en la base de datos
        UsuarioDAO usuarioDAO = new UsuarioDAOImp();
        UsuarioVO usuario = usuarioDAO.obtenerPorCorreo(correoUsuario);

        if (usuario != null) {
            usuario.setContraseña(nuevaContraseña);
            usuario.setConfirmarContraseña(confirmacion);

            if (usuarioDAO.actualizarUsuario(usuario)) {
                Alertas.mostrarAdvertencia("Contraseña actualizada");
                cerrarVentana();
            } else {
                Alertas.mostrarError("Las contraseñas no coinciden");
            }
        }
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtNuevaContraseña.getScene().getWindow();
        stage.close();
        handleRegresar();
    }

    @FXML
    private void handleRegresar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/aplicacion/application/hello-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) txtNuevaContraseña.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            Alertas.mostrarError("No se puede acceder al inicio" + e.getMessage());
        }
    }
}