package aplicacion.VO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CarritoVO {
    private int idCarrito;
    private final IntegerProperty idUsuario = new SimpleIntegerProperty();
    private final ObjectProperty<LocalDateTime> fechaCreacion = new SimpleObjectProperty<>();
    private final ObservableList<CarritoItemVO> items = FXCollections.observableArrayList();
    
    // Constructor
    public CarritoVO(int idCarrito, int idUsuario, LocalDateTime fechaCreacion) {
        this.idCarrito = idCarrito;
        this.idUsuario.set(idUsuario);
        this.fechaCreacion.set(fechaCreacion);
    }
    
    // Constructor para nuevo carrito
    public CarritoVO(int idUsuario) {
        this.idUsuario.set(idUsuario);
        this.fechaCreacion.set(LocalDateTime.now());
    }
    
    // Getters y Setters
    public int getIdCarrito() { return idCarrito; }
    public void setIdCarrito(int idCarrito) { this.idCarrito = idCarrito; }
    
    public int getIdUsuario() { return idUsuario.get(); }
    public void setIdUsuario(int idUsuario) { this.idUsuario.set(idUsuario); }
    public IntegerProperty idUsuarioProperty() { return idUsuario; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion.get(); }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion.set(fechaCreacion); }
    public ObjectProperty<LocalDateTime> fechaCreacionProperty() { return fechaCreacion; }
    
    public ObservableList<CarritoItemVO> getItems() { return items; }
    
    // Métodos para gestionar los items del carrito
    public void agregarItem(CarritoItemVO item) {
        items.add(item);
    }
    
    public boolean eliminarItem(CarritoItemVO item) {
        return items.remove(item);
    }
    
    public void limpiarCarrito() {
        items.clear();
    }
    
    // Calcular el total del carrito
    public double calcularTotal() {
        return items.stream()
                .mapToDouble(item -> item.getPrecioUnitario() * item.getCantidad())
                .sum();
    }
}
