package aplicacion.VO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TicketVO {
    private int idTicket;
    private final StringProperty numeroTicket = new SimpleStringProperty();
    private final IntegerProperty idUsuario = new SimpleIntegerProperty();
    private final DoubleProperty montoTotal = new SimpleDoubleProperty();
    private final ObjectProperty<LocalDateTime> fechaCompra = new SimpleObjectProperty<>();
    private final StringProperty estado = new SimpleStringProperty("pagado");
    private final ObservableList<TicketDetalleVO> detalles = FXCollections.observableArrayList();
    
    // Constructor vacío
    public TicketVO() {
        this.fechaCompra.set(LocalDateTime.now());
    }
    
    // Constructor
    public TicketVO(int idTicket, String numeroTicket, int idUsuario, double montoTotal, LocalDateTime fechaCompra, String estado) {
        this.idTicket = idTicket;
        this.numeroTicket.set(numeroTicket);
        this.idUsuario.set(idUsuario);
        this.montoTotal.set(montoTotal);
        this.fechaCompra.set(fechaCompra);
        this.estado.set(estado);
    }
    
    // Constructor para nuevo ticket
    public TicketVO(String numeroTicket, int idUsuario, double montoTotal) {
        this.numeroTicket.set(numeroTicket);
        this.idUsuario.set(idUsuario);
        this.montoTotal.set(montoTotal);
        this.fechaCompra.set(LocalDateTime.now());
    }
    
    // Getters y Setters
    public int getIdTicket() { return idTicket; }
    public void setIdTicket(int idTicket) { this.idTicket = idTicket; }
    
    public String getNumeroTicket() { return numeroTicket.get(); }
    public void setNumeroTicket(String numeroTicket) { this.numeroTicket.set(numeroTicket); }
    public StringProperty numeroTicketProperty() { return numeroTicket; }
    
    public int getIdUsuario() { return idUsuario.get(); }
    public void setIdUsuario(int idUsuario) { this.idUsuario.set(idUsuario); }
    public IntegerProperty idUsuarioProperty() { return idUsuario; }
    
    public double getMontoTotal() { return montoTotal.get(); }
    public void setMontoTotal(double montoTotal) { this.montoTotal.set(montoTotal); }
    public DoubleProperty montoTotalProperty() { return montoTotal; }
    
    // Alias para setMontoTotal para compatibilidad
    public void setTotal(double total) { 
        this.montoTotal.set(total); 
    }
    
    public LocalDateTime getFechaCompra() { return fechaCompra.get(); }
    public void setFechaCompra(LocalDateTime fechaCompra) { this.fechaCompra.set(fechaCompra); }
    public ObjectProperty<LocalDateTime> fechaCompraProperty() { return fechaCompra; }
    
    public String getEstado() { return estado.get(); }
    public void setEstado(String estado) { this.estado.set(estado); }
    public StringProperty estadoProperty() { return estado; }
    
    public ObservableList<TicketDetalleVO> getDetalles() { return detalles; }
    
    // Métodos para gestionar los detalles del ticket
    public void agregarDetalle(TicketDetalleVO detalle) {
        detalles.add(detalle);
    }
    
    // Generar un número de ticket aleatorio (formato: TIK-YYYYMMDD-XXXX)
    public static String generarNumeroTicket() {
        LocalDateTime ahora = LocalDateTime.now();
        String fecha = String.format("%04d%02d%02d", ahora.getYear(), ahora.getMonthValue(), ahora.getDayOfMonth());
        // Generar 4 dígitos aleatorios
        int random = (int)(Math.random() * 10000);
        return "TIK-" + fecha + "-" + String.format("%04d", random);
    }
}
