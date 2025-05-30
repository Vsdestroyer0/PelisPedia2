package aplicacion.VO;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class TicketDetalleVO {
    private int idDetalle;
    private final IntegerProperty idTicket = new SimpleIntegerProperty();
    private final IntegerProperty idPelicula = new SimpleIntegerProperty();
    private final IntegerProperty cantidad = new SimpleIntegerProperty(1);
    private final DoubleProperty precioUnitario = new SimpleDoubleProperty();
    private final ObjectProperty<PeliculaVO> pelicula = new SimpleObjectProperty<>();
    
    // Constructor
    public TicketDetalleVO(int idDetalle, int idTicket, int idPelicula, int cantidad, double precioUnitario) {
        this.idDetalle = idDetalle;
        this.idTicket.set(idTicket);
        this.idPelicula.set(idPelicula);
        this.cantidad.set(cantidad);
        this.precioUnitario.set(precioUnitario);
    }
    
    // Constructor para nuevo detalle
    public TicketDetalleVO(int idTicket, PeliculaVO pelicula, int cantidad, double precioUnitario) {
        this.idTicket.set(idTicket);
        this.idPelicula.set(pelicula.getIdPelicula());
        this.cantidad.set(cantidad);
        this.precioUnitario.set(precioUnitario);
        this.pelicula.set(pelicula);
    }
    
    // Constructor desde CarritoItemVO
    public TicketDetalleVO(int idTicket, CarritoItemVO item) {
        this.idTicket.set(idTicket);
        this.idPelicula.set(item.getIdPelicula());
        this.cantidad.set(item.getCantidad());
        this.precioUnitario.set(item.getPrecioUnitario());
        this.pelicula.set(item.getPelicula());
    }
    
    // Constructor vacío
    public TicketDetalleVO() {
    }
    
    // Getters y Setters
    public int getIdDetalle() { return idDetalle; }
    public void setIdDetalle(int idDetalle) { this.idDetalle = idDetalle; }
    
    public int getIdTicket() { return idTicket.get(); }
    public void setIdTicket(int idTicket) { this.idTicket.set(idTicket); }
    public IntegerProperty idTicketProperty() { return idTicket; }
    
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
    
    // Calcular subtotal
    public double calcularSubtotal() {
        return getPrecioUnitario() * getCantidad();
    }
    
    // Método para establecer el subtotal (aunque realmente se calcula)
    public void setSubtotal(double subtotal) {
        // En realidad, no hay un campo para subtotal, 
        // pero proporcionamos este método para compatibilidad
        // El subtotal real se calcula en calcularSubtotal()
    }
}
