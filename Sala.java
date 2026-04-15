import java.util.ArrayList;
public class Sala {
    private int id;
    private String nombre;
    private String descripcion;
    private boolean puedeEsconderse;
    private boolean esconditeUsado;
    private int ruidoBase;
    private int frecuenciaRuido;
    private boolean esSalidaFinal;
    private ArrayList<Conexion> conexiones;

    public Sala(int id, String nombre, String descripcion,
            boolean escondite, int ruidoBase, int frecuenciaRuido) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.puedeEsconderse = escondite;
        this.esconditeUsado = false;
        this.ruidoBase = ruidoBase;
        this.frecuenciaRuido = frecuenciaRuido;
        this.esSalidaFinal = false;
        this.conexiones = new ArrayList<>();
    }

    public void agregarConexion(Sala destino, int ruido, String descripcion) {
        this.conexiones.add(new Conexion(destino, ruido, descripcion));
    }

    public int getRuidoAmbiente(int turno) {
        if (frecuenciaRuido == -1)
            return 0;
        if (frecuenciaRuido == 0)
            return ruidoBase;
        if (turno > 0 && turno % frecuenciaRuido == 0)
            return ruidoBase;
        return 0;
    }

    public boolean puedeEsconderseAqui() {
        return puedeEsconderse && !esconditeUsado;
    }

    public void usarEscondite() {
        this.esconditeUsado = true;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean tieneEscondite() {
        return puedeEsconderse;
    }

    public boolean isEsconditeUsado() {
        return esconditeUsado;
    }

    public boolean esSalidaFinal() {
        return esSalidaFinal;
    }

    public void setSalidaFinal(boolean b) {
        esSalidaFinal = b;
    }

    public ArrayList<Conexion> getConexiones() {
        return conexiones;
    }

    public int getRuidoBase() {
        return ruidoBase;
    }

    public int getFrecuenciaRuido() {
        return frecuenciaRuido;
    }
}
