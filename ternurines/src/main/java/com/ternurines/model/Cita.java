package com.ternurines.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Cita {
    private int idCita;
    private int idMascota;
    private int idVeterinario;
    private int idRecepcionista;
    private LocalDate fecha;
    private LocalTime hora;
    private String motivo;
    private String estado;
 
    public Cita() {}
 
    public Cita(int idCita, int idMascota, int idVeterinario, int idRecepcionista,
                LocalDate fecha, LocalTime hora, String motivo, String estado) {
        this.idCita = idCita;
        this.idMascota = idMascota;
        this.idVeterinario = idVeterinario;
        this.idRecepcionista = idRecepcionista;
        this.fecha = fecha;
        this.hora = hora;
        this.motivo = motivo;
        this.estado = estado;
    }
 
    public int getIdCita() { return idCita; }
    public void setIdCita(int idCita) { this.idCita = idCita; }
 
    public int getIdMascota() { return idMascota; }
    public void setIdMascota(int idMascota) { this.idMascota = idMascota; }
 
    public int getIdVeterinario() { return idVeterinario; }
    public void setIdVeterinario(int idVeterinario) { this.idVeterinario = idVeterinario; }
 
    public int getIdRecepcionista() { return idRecepcionista; }
    public void setIdRecepcionista(int idRecepcionista) { this.idRecepcionista = idRecepcionista; }
 
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
 
    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }
 
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
 
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
 
    @Override
    public String toString() {
        return "Cita{idCita=" + idCita + ", idMascota=" + idMascota + ", fecha=" + fecha + ", hora=" + hora + ", estado='" + estado + "'}";
    }
}