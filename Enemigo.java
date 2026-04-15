import java.util.ArrayList;
public class Enemigo {
    private Sala salaActual;
    private String nombre;

    public Enemigo(Sala salaInicial) {
        this.salaActual = salaInicial;
        this.nombre = "Fane, el brujo";
    }

    public void mover(Mapa mapa, int[] ruidosTurno) {
        int maxRuido = 0;
        for (Sala s : mapa.getSalas()) {
            if (ruidosTurno[s.getId()] > maxRuido) {
                maxRuido = ruidosTurno[s.getId()];
            }
        }

        if (maxRuido == 0)
            return;

        ArrayList<Sala> candidatas = new ArrayList<>();
        for (Sala s : mapa.getSalas()) {
            if (ruidosTurno[s.getId()] == maxRuido) {
                candidatas.add(s);
            }
        }

        if (candidatas.size() == 1 && candidatas.get(0) == salaActual)
            return;

        Sala objetivo = null;
        int minDistancia = Integer.MAX_VALUE;

        for (Sala s : candidatas) {
            if (s == salaActual)
                continue;
            int dist = mapa.obtenerDistancia(salaActual, s);
            if (dist > 0 && dist < minDistancia) {
                minDistancia = dist;
                objetivo = s;
            }
        }

        if (objetivo == null)
            return;

        Sala siguientePaso = mapa.obtenerSiguientePaso(salaActual, objetivo);
        if (siguientePaso != null) {
            salaActual = siguientePaso;
        }
    }

    public Sala getSalaActual() {
        return salaActual;
    }

    public String getNombre() {
        return nombre;
    }
}
