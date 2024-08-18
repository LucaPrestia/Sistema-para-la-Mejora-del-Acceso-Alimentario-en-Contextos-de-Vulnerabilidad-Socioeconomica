package ar.utn.sistema.entities.usuarios;

public enum TipoDocumento {
    DNI("Documento Nacional de Identidad"),
    CUIT("Código Único de Identificación Tributaria"),
    CUIL("Código Único de Identificación Laboral"),
    PASAPORTE("Pasaporte"),
    LE("Libreta de Enrolamiento"),
    LC("Libreta Cívica"),
    CI("Cédula de Identidad");

    private final String descripcion;

    TipoDocumento(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
