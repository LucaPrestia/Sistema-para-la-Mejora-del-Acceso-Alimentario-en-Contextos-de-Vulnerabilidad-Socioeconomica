package ar.utn.sistema.entities.colaboracion;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

public enum TipoFrecuencia {
    UNICA,
    SEMANAL,
    MENSUAL,
    ANUAL
}
