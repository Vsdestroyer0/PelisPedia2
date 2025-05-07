package aplicacion.VO;

import java.time.LocalDateTime;

public class RentaVO {
    private int idRenta;
    private int idPelicula;
    private String usuario;
    private LocalDateTime fechaRenta;
    private LocalDateTime fechaEntrega;
    private String estado;

    // Constructor
    public RentaVO(int idRenta, int idPelicula, String usuario, LocalDateTime fechaRenta, LocalDateTime fechaEntrega, String estado) {
        this.idRenta = idRenta;
        this.idPelicula = idPelicula;
        this.usuario = usuario;
        this.fechaRenta = fechaRenta;
        this.fechaEntrega = fechaEntrega;
        this.estado = estado;
    }

    // Getters
    public int getIdRenta() { return idRenta; }
    public int getIdPelicula() { return idPelicula; }
    public String getUsuario() { return usuario; }
    public LocalDateTime getFechaRenta() { return fechaRenta; }
    public LocalDateTime getFechaEntrega() { return fechaEntrega; }
    public String getEstado() { return estado; }
}