package com.ternurines.model;

import java.time.LocalDate;

public class MascotaAdopcion {
    private int idMascotaAdopcion;
    private int idRecepcionista;
    private String nombre;
    private String especie;
    private String raza;
    private int edad;
    private String estadoSalud;
    private String estadoAdopcion;
    private LocalDate fechaIngreso;

    public MascotaAdopcion() {
    }

    public int getIdMascotaAdopcion() {
        return idMascotaAdopcion;
    }

    public void setIdMascotaAdopcion(int idMascotaAdopcion) {
        this.idMascotaAdopcion = idMascotaAdopcion;
    }

    public int getIdRecepcionista() {
        return idRecepcionista;
    }

    public void setIdRecepcionista(int idRecepcionista) {
        this.idRecepcionista = idRecepcionista;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getEstadoSalud() {
        return estadoSalud;
    }

    public void setEstadoSalud(String estadoSalud) {
        this.estadoSalud = estadoSalud;
    }

    public String getEstadoAdopcion() {
        return estadoAdopcion;
    }

    public void setEstadoAdopcion(String estadoAdopcion) {
        this.estadoAdopcion = estadoAdopcion;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }
}