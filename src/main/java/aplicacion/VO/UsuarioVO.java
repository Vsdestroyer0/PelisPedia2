package aplicacion.VO;

public class UsuarioVO {
    private int id;
    private String nombre;
    private String correo;
    private String contraseña;
    private String confirmarContraseña;
    private String preguntaSeguridad;
    private String respuestaSeguridad;
    private String dirección;
    private boolean esAdmin;

    public UsuarioVO(String nombre, String correo, String contraseña, String confirmarContraseña,
                     String preguntaSeguridad, String respuestaSeguridad, String dirección,
                     boolean esAdmin) {
        this.nombre = nombre;
        this.correo = correo;
        this.contraseña = contraseña;
        this.confirmarContraseña = confirmarContraseña;
        this.preguntaSeguridad = preguntaSeguridad;
        this.respuestaSeguridad = respuestaSeguridad;
        this.dirección = dirección;
        this.esAdmin = esAdmin;
    }

    public int getId() {return id;}
    public void setId() {}

    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}

    public String getCorreo() {return correo;}
    public void setCorreo(String correo) {this.correo = correo;}

    public String getContraseña() {return contraseña;}
    public void setContraseña(String contraseña) {this.contraseña = contraseña;}

    public String getConfirmarContraseña() {return confirmarContraseña;}
    public void setConfirmarContraseña(String confirmarContraseña) {this.confirmarContraseña = confirmarContraseña;}

    public String getPreguntaSeguridad() {return preguntaSeguridad;}
    public void setPreguntaSeguridad(String preguntaSeguridad) {this.preguntaSeguridad = preguntaSeguridad;}

    public String getRespuestaSeguridad() {return respuestaSeguridad;}
    public void setRespuestaSeguridad(String respuestaSeguridad) {this.respuestaSeguridad = respuestaSeguridad;}

    public String getDirección() {return dirección;}
    public void setDirección(String dirección) {this.dirección = dirección;}

    public boolean isAdmin() {return esAdmin;}

}
