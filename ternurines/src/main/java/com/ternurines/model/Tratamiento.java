package com.ternurines.model;

import java.time.LocalDate;

public class Tratamiento {
    private Integer idTratamiento;
    private int idHistorial;
    private int idMedicamento;
    private String descripcion;
    private String dosis;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public Tratamiento(Integer idTratamiento, int idHistorial, int idMedicamento, String descripcion, String dosis,
            LocalDate fechaInicio, LocalDate fechaFin) {
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

    public Integer getIdTratamiento() {
        return idTratamiento;
    }

    public void setIdTratamiento(Integer idTratamiento) {
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

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }
}
