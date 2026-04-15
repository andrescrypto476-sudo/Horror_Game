public class Conexion {
    private Sala destino;
    private int ruidoMovimiento;
    private String descripcion;

    public Conexion(Sala destino, int ruido, String descripcion) {
        this.destino = destino;
        this.ruidoMovimiento = ruido;
        this.descripcion = descripcion;
    }

    public Sala getDestino() {
        return destino;
    }

    public int getRuidoMovimiento() {
        return ruidoMovimiento;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
