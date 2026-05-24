package com.ternurines.model;

import java.util.Date;

public class Tratamiento {
    private int idTratamiento;
    private int idHistorial;
    private int idMedicamento;
    private String descripcion;
    private String dosis;
    private Date fechaInicio;
    private Date fechaFin;

    public Tratamiento(int idTratamiento, int idHistorial, int idMedicamento, String descripcion, String dosis,
            Date fechaInicio, Date fechaFin) {
        this.idTratamiento = idTratamiento;
        this.idHistorial = idHistorial;
        this.idMedicamento = idMedicamento;
        this.descripcion = descripcion;
        this.dosis = dosis;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public Tratamiento() {
    }

    public int getIdTratamiento() {
        return idTratamiento;
    }

    public void setIdTratamiento(int idTratamiento) {
        this.idTratamiento = idTratamiento;
    }

    public int getIdHistorial() {
        return idHistorial;
    }

    public void setIdHistorial(int idHistorial) {
        this.idHistorial = idHistorial;
    }

    public int getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(int idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }
}