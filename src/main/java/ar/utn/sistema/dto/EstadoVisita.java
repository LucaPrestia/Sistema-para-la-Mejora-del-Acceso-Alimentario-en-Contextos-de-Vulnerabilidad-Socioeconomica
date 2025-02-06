package ar.utn.sistema.dto;

import ar.utn.sistema.entities.usuarios.Tecnico;

public class EstadoVisita {
    private String descripcionTrabajo;
    private boolean solucionado;
    private Tecnico tecnico;

    public EstadoVisita(String descripcionTrabajo, boolean solucionado, Tecnico tecnico) {
        this.descripcionTrabajo = descripcionTrabajo;
        this.solucionado = solucionado;
        this.tecnico = tecnico;
    }

    public String getDescripcionTrabajo() {
        return descripcionTrabajo;
    }

    public boolean isSolucionado() {
        return solucionado;
    }

    public Tecnico getTecnico() {
        return tecnico;
    }
}
