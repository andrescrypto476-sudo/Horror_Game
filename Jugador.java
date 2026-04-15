public class Jugador {
    private Sala salaActual;
    private boolean escondido;
    private boolean vivo;

    public Jugador(Sala salaInicial) {
        this.salaActual = salaInicial;
        this.escondido = false;
        this.vivo = true;
    }

    public Sala getSalaActual() {
        return salaActual;
    }

    public void setSalaActual(Sala sala) {
        this.salaActual = sala;
    }

    public boolean estaEscondido() {
        return escondido;
    }

    public void setEscondido(boolean e) {
        this.escondido = e;
    }

    public boolean estaVivo() {
        return vivo;
    }

    public void setVivo(boolean v) {
        this.vivo = v;
    }
}
