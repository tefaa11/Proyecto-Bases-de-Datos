package com.ternurines.model;

// =============================================
// 2. VETERINARIO
// =============================================
public class Veterinario {
    private int idVeterinario;
    private String nombre;
    private String documento;
    private String telefono;
    private String correo;
    private String especialidad;
    private String numLicencia;
    private String contrasena;
 
    public Veterinario() {}
 
    public Veterinario(int idVeterinario, String nombre, String documento, String telefono,
                       String correo, String especialidad, String numLicencia, String contrasena) {
        this.idVeterinario = idVeterinario;
        this.nombre = nombre;
        this.documento = documento;
        this.telefono = telefono;
        this.correo = correo;
        this.especialidad = especialidad;
        this.numLicencia = numLicencia;
        this.contrasena = contrasena;
    }
 
    public int getIdVeterinario() { return idVeterinario; }
    public void setIdVeterinario(int idVeterinario) { this.idVeterinario = idVeterinario; }
 
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
 
    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }
 
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
 
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
 
    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
 
    public String getNumLicencia() { return numLicencia; }
    public void setNumLicencia(String numLicencia) { this.numLicencia = numLicencia; }
 
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
 
    @Override
    public String toString() {
        return "Veterinario{idVeterinario=" + idVeterinario + ", nombre='" + nombre + "', especialidad='" + especialidad + "'}";
    }
}