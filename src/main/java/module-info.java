module aplicacion.inventario_plantas {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires com.almasb.fxgl.all;

    opens aplicacion.inventario_plantas to javafx.fxml;
    exports aplicacion.inventario_plantas;
}