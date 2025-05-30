package aplicacion.VO;

import java.time.LocalDateTime;

public class PeliculaRentadaVO {
    private int idRenta;
    private int idPelicula;
    private int idUsuario;
    private String titulo;
    private String clasificacion;
    private byte[] imagen;
    private LocalDateTime fechaRenta;
    private LocalDateTime fechaEntrega;
    private String estado;
    private double precio;
    
    // Constructor completo
    public PeliculaRentadaVO(int idRenta, int idPelicula, int idUsuario, String titulo, 
                            String clasificacion, byte[] imagen, LocalDateTime fechaRenta, 
                            LocalDateTime fechaEntrega, String estado, double precio) {
        this.idRenta = idRenta;
        this.idPelicula = idPelicula;
        this.idUsuario = idUsuario;
        this.titulo = titulo;
        this.clasificacion = clasificacion;
        this.imagen = imagen;
        this.fechaRenta = fechaRenta;
        this.fechaEntrega = fechaEntrega;
        this.estado = estado;
        this.precio = precio;
    }
    
    // Constructor sin id (para nuevas rentas)
    public PeliculaRentadaVO(int idPelicula, int idUsuario, String titulo, 
                            String clasificacion, byte[] imagen, 
                            LocalDateTime fechaRenta, LocalDateTime fechaEntrega, 
                            String estado, double precio) {
        this.idPelicula = idPelicula;
        this.idUsuario = idUsuario;
        this.titulo = titulo;
        this.clasificacion = clasificacion;
        this.imagen = imagen;
        this.fechaRenta = fechaRenta;
        this.fechaEntrega = fechaEntrega;
        this.estado = estado;
        this.precio = precio;
    }
    
    // Constructor vacío
    public PeliculaRentadaVO() {
    }
    
    // Getters y setters
    public int getIdRenta() {
        return idRenta;
    }

    public void setIdRenta(int idRenta) {
        this.idRenta = idRenta;
    }

    public int getIdPelicula() {
        return idPelicula;
    }

    public void setIdPelicula(int idPelicula) {
        this.idPelicula = idPelicula;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public LocalDateTime getFechaRenta() {
        return fechaRenta;
    }

    public void setFechaRenta(LocalDateTime fechaRenta) {
        this.fechaRenta = fechaRenta;
    }

    public LocalDateTime getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDateTime fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
    
    // Método para calcular si la renta ya expiró (después de 2 horas)
    public boolean isExpirado() {
        if (fechaRenta == null) {
            return false;
        }
        
        LocalDateTime tiempoExpiracion = fechaRenta.plusHours(2);
        return LocalDateTime.now().isAfter(tiempoExpiracion);
    }
    
    // Método para obtener el tiempo restante en minutos
    public long getTiempoRestanteMinutos() {
        if (fechaRenta == null) {
            return 0;
        }
        
        LocalDateTime tiempoExpiracion = fechaRenta.plusHours(2);
        LocalDateTime ahora = LocalDateTime.now();
        
        if (ahora.isAfter(tiempoExpiracion)) {
            return 0;
        }
        
        // Calcular la diferencia en minutos
        long minutos = java.time.Duration.between(ahora, tiempoExpiracion).toMinutes();
        return minutos;
    }
}
