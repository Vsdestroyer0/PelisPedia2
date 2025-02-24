module aplicacion.application {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires com.almasb.fxgl.all;
    requires java.sql;

    opens aplicacion.application to javafx.fxml;
    exports aplicacion.application;
    exports aplicacion.Controles;
    opens aplicacion.Controles to javafx.fxml;
    exports Controles;
    opens Controles to javafx.fxml;
}