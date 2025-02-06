package ar.utn.sistema.entities.notificacion;

public enum PreferenciaNotificacion {
    POCAS_VIANDAS("Notificación heladera vacía", "En caso de que queden únicamente N viandas, notificar para traer más viandas a la heladera."),
    HELADERA_LLENA("Notificación heladera llena", "En caso de que queden únicamente N lugares para guardar viandas, notificar para distribuir viandas a otras heladeras cercanas."),
    DESPERFECTO("Notificación desperfecto heladera", "En caso de que la heladera sufra un desperfecto, notificar para distribuir todas las viandas a otras heladeras.");
    private final String nombre;
    private final String descripcion;

    PreferenciaNotificacion(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
    public String getNombre() {return nombre;}
}
