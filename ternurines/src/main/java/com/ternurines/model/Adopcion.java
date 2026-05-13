package com.ternurines.model;

import java.time.LocalDate;

public class Adopcion {
    private int idAdoptante;
    private int idMascotaAdopcion;
    private LocalDate fechaAdopcion;

    public Adopcion() {
    }

    public int getIdAdoptante() {
        return idAdoptante;
    }

    public void setIdAdoptante(int idAdoptante) {
        this.idAdoptante = idAdoptante;
    }

    public int getIdMascotaAdopcion() {
        return idMascotaAdopcion;
    }

    public void setIdMascotaAdopcion(int idMascotaAdopcion) {
        this.idMascotaAdopcion = idMascotaAdopcion;
    }

    public LocalDate getFechaAdopcion() {
        return fechaAdopcion;
    }

    public void setFechaAdopcion(LocalDate fechaAdopcion) {
        this.fechaAdopcion = fechaAdopcion;
    }
}