package com.ternurines.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class VistaAgendaDiaria {

    private int idCita;
    private LocalDate fecha;
    private LocalTime hora;
    private String veterinario;
    private String mascota;
    private String cliente;
    private String recepcionista;
    private String motivo;
    private String estado;

    public VistaAgendaDiaria() {
    }

    public VistaAgendaDiaria(int idCita, LocalDate fecha, LocalTime hora, String veterinario,
                             String mascota, String cliente, String recepcionista,
                             String motivo, String estado) {
        this.idCita = idCita;
        this.fecha = fecha;
        this.hora = hora;
        this.veterinario = veterinario;
        this.mascota = mascota;
        this.cliente = cliente;
        this.recepcionista = recepcionista;
        this.motivo = motivo;
        this.estado = estado;
    }

    public int getIdCita() {
        return idCita;
    }

    public void setIdCita(int idCita) {
        this.idCita = idCita;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(String veterinario) {
        this.veterinario = veterinario;
    }

    public String getMascota() {
        return mascota;
    }

    public void setMascota(String mascota) {
        this.mascota = mascota;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getRecepcionista() {
        return recepcionista;
    }

    public void setRecepcionista(String recepcionista) {
        this.recepcionista = recepcionista;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}