package Controles;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LogoutDialogController {
    private boolean confirmed = false;

    @FXML private Button btnYes;
    @FXML private Button btnNo;
    @FXML private StackPane rootPane;

    @FXML
    public void initialize() {
        // Añadir efectos hover a los botones
        setupButtonHoverEffects(btnYes);
        setupButtonHoverEffects(btnNo);

        // Animar la aparición del diálogo
        animateDialogAppearance();
    }

    private void setupButtonHoverEffects(Button button) {
        button.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), button);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();
        });

        button.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), button);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });
    }

    private void animateDialogAppearance() {
        // Hacer que el diálogo aparezca gradualmente
        rootPane.setOpacity(0);
        
        // Creamos una transición para el fondo
        FadeTransition fadeIn = new FadeTransition(Duration.millis(150), rootPane);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        
        // Hacemos que el contenido aparezca con una animación de escala
        VBox dialogContent = (VBox) rootPane.getChildren().get(0);
        dialogContent.setScaleX(0.8);
        dialogContent.setScaleY(0.8);
        
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), dialogContent);
        scaleUp.setFromX(0.8);
        scaleUp.setFromY(0.8);
        scaleUp.setToX(1.0);
        scaleUp.setToY(1.0);
        
        // Secuencia de animaciones
        SequentialTransition sequence = new SequentialTransition(fadeIn, scaleUp);
        sequence.play();
    }

    private void animateDialogDisappearance(Runnable onFinished) {
        // Animación para la desaparición
        VBox dialogContent = (VBox) rootPane.getChildren().get(0);
        
        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(100), dialogContent);
        scaleDown.setToX(0.8);
        scaleDown.setToY(0.8);
        
        FadeTransition fadeOut = new FadeTransition(Duration.millis(150), rootPane);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(e -> onFinished.run());
        
        SequentialTransition sequence = new SequentialTransition(scaleDown, fadeOut);
        sequence.play();
    }

    @FXML
    private void onYes() {
        confirmed = true;
        animateDialogDisappearance(this::close);
    }

    @FXML
    private void onNo() {
        confirmed = false;
        animateDialogDisappearance(this::close);
    }

    private void close() {
        // Cierra la ventana de diálogo
        Stage stage = (Stage) btnNo.getScene().getWindow();
        stage.close();
    }

    /**
     * @return true si el usuario confirmó cerrar sesión
     */
    public boolean isConfirmed() {
        return confirmed;
    }
}
