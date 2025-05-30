package aplicacion.VO;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class CarritoItemVO {
    private int idItem;
    private final IntegerProperty idCarrito = new SimpleIntegerProperty();
    private final IntegerProperty idPelicula = new SimpleIntegerProperty();
    private final IntegerProperty cantidad = new SimpleIntegerProperty(1); // Por defecto 1
    private final DoubleProperty precioUnitario = new SimpleDoubleProperty();
    private final ObjectProperty<PeliculaVO> pelicula = new SimpleObjectProperty<>();
    
    // Constructor
    public CarritoItemVO(int idItem, int idCarrito, int idPelicula, int cantidad, double precioUnitario) {
        this.idItem = idItem;
        this.idCarrito.set(idCarrito);
        this.idPelicula.set(idPelicula);
        this.cantidad.set(cantidad);
        this.precioUnitario.set(precioUnitario);
    }
    
    // Constructor para nuevo item
    public CarritoItemVO(int idCarrito, PeliculaVO pelicula, int cantidad) {
        this.idCarrito.set(idCarrito);
        this.idPelicula.set(pelicula.getIdPelicula());
        this.cantidad.set(cantidad);
        this.precioUnitario.set(pelicula.getPrecio());
        this.pelicula.set(pelicula);
    }
    
    // Constructor para crear item desde idPelicula
    public CarritoItemVO(int idCarrito, int idPelicula, int cantidad, double precioUnitario) {
        this.idCarrito.set(idCarrito);
        this.idPelicula.set(idPelicula);
        this.cantidad.set(cantidad);
        this.precioUnitario.set(precioUnitario);
    }
    
    // Constructor vacío
    public CarritoItemVO() {
    }
    
    // Getters y Setters
    public int getIdItem() { return idItem; }
    public void setIdItem(int idItem) { this.idItem = idItem; }
    
    public int getIdCarrito() { return idCarrito.get(); }
    public void setIdCarrito(int idCarrito) { this.idCarrito.set(idCarrito); }
    public IntegerProperty idCarritoProperty() { return idCarrito; }
    
    public int getIdPelicula() { return idPelicula.get(); }
    public void setIdPelicula(int idPelicula) { this.idPelicula.set(idPelicula); }
    public IntegerProperty idPeliculaProperty() { return idPelicula; }
    
    public int getCantidad() { return cantidad.get(); }
    public void setCantidad(int cantidad) { this.cantidad.set(cantidad); }
    public IntegerProperty cantidadProperty() { return cantidad; }
    
    public double getPrecioUnitario() { return precioUnitario.get(); }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario.set(precioUnitario); }
    public DoubleProperty precioUnitarioProperty() { return precioUnitario; }
    
    public PeliculaVO getPelicula() { return pelicula.get(); }
    public void setPelicula(PeliculaVO pelicula) { this.pelicula.set(pelicula); }
    public ObjectProperty<PeliculaVO> peliculaProperty() { return pelicula; }
    
    // Calcular subtotal del item
    public double calcularSubtotal() {
        return getPrecioUnitario() * getCantidad();
    }
}
