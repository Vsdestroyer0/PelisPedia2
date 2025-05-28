package aplicacion.VO;

public class UsuarioVO {
    private String id;
    private String nombre;
    private String correo;
    private String contraseña;
    private String confirmarContraseña;
    private String preguntaSeguridad;
    private String respuestaSeguridad;
    private String dirección;
    private boolean admin;
    private byte[] imagen;
    private boolean activo;

    // Constructor completo
    public UsuarioVO(String nombre, String correo, String contraseña,
                     String preguntaSeguridad, String respuestaSeguridad,
                     String dirección, boolean admin) {
        this.nombre = nombre;
        this.correo = correo;
        this.contraseña = contraseña;
        this.confirmarContraseña = contraseña; // La contraseña confirmada es la misma
        this.preguntaSeguridad = preguntaSeguridad;
        this.respuestaSeguridad = respuestaSeguridad;
        this.dirección = dirección;
        this.admin = admin;
        this.imagen = null;
        this.activo = true; // Por defecto, los usuarios están activos
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getConfirmarContraseña() {
        return confirmarContraseña;
    }

    public void setConfirmarContraseña(String confirmarContraseña) {
        this.confirmarContraseña = confirmarContraseña;
    }

    public String getPreguntaSeguridad() {
        return preguntaSeguridad;
    }

    public void setPreguntaSeguridad(String preguntaSeguridad) {
        this.preguntaSeguridad = preguntaSeguridad;
    }

    public String getRespuestaSeguridad() {
        return respuestaSeguridad;
    }

    public void setRespuestaSeguridad(String respuestaSeguridad) {
        this.respuestaSeguridad = respuestaSeguridad;
    }

    public String getDireccion() {
        return dirección;
    }

    public void setDireccion(String dirección) {
        this.dirección = dirección;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "UsuarioVO{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' +
                ", preguntaSeguridad='" + preguntaSeguridad + '\'' +
                ", respuestaSeguridad='" + respuestaSeguridad + '\'' +
                ", dirección='" + dirección + '\'' +
                ", admin=" + admin +
                ", imagen=" + (imagen != null ? "presente" : "ausente") +
                ", activo=" + activo +
                '}';
    }
}
