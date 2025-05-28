module aplicacion.application {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires com.almasb.fxgl.all;
    requires java.sql;

    opens aplicacion.application to javafx.fxml;
    opens aplicacion.VO to javafx.base, javafx.controls, javafx.fxml;
    opens aplicacion.DAO to javafx.base, javafx.fxml;
    
    exports aplicacion.application;
    exports Controles;
    exports aplicacion.VO;
    opens Controles to javafx.fxml;
}
