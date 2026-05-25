package com.ternurines.model;



public class CitaServicio {
    private int idCita;
    private int idServicio;
 
    public CitaServicio() {}
 
    public CitaServicio(int idCita, int idServicio) {
        this.idCita = idCita;
        this.idServicio = idServicio;
    }
 
    public int getIdCita() { return idCita; }
    public void setIdCita(int idCita) { this.idCita = idCita; }
 
    public int getIdServicio() { return idServicio; }
    public void setIdServicio(int idServicio) { this.idServicio = idServicio; }
 
    @Override
    public String toString() {
        return "CitaServicio{idCita=" + idCita + ", idServicio=" + idServicio + "}";
    }
}
 

