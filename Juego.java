import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Juego {
    private static final String RESET = "\033[0m";
    private static final String ROJO = "\033[1;31m";
    private static final String VERDE = "\033[1;32m";
    private static final String AMARILLO = "\033[1;33m";
    private static final String AZUL = "\033[1;34m";
    private static final String MAGENTA = "\033[1;35m";
    private static final String CYAN = "\033[1;36m";
    private static final String BLANCO = "\033[1;37m";
    private static final String TENUE = "\033[2m";
    private static final String ITALIC = "\033[3m";

    private Mapa mapa;
    private Jugador jugador;
    private Enemigo enemigo;
    private int turno;
    private int[] ruidosTurno;
    private Scanner scanner;
    private Random random;
    private boolean juegoTerminado;
    private boolean jugadorGano;

    public Juego() {
        mapa = new Mapa();
        mapa.construirMapa();
        jugador = new Jugador(mapa.getSalaInicial());
        enemigo = new Enemigo(mapa.getSalaPorId(14));
        turno = 0;
        ruidosTurno = new int[mapa.getSalas().size() + 1];
        scanner = new Scanner(System.in);
        random = new Random();
        juegoTerminado = false;
        jugadorGano = false;
    }

    public void iniciar() {
        mostrarIntroduccion();

        while (!juegoTerminado) {
            turno++;

            for (int i = 0; i < ruidosTurno.length; i++) {
                ruidosTurno[i] = 0;
            }
            for (Sala s : mapa.getSalas()) {
                ruidosTurno[s.getId()] += s.getRuidoAmbiente(turno);
            }

            mostrarEstadoTurno();

            procesarAccionJugador();

            if (jugador.getSalaActual().esSalidaFinal()) {
                juegoTerminado = true;
                jugadorGano = true;
                mostrarVictoria();
                break;
            }

            enemigo.mover(mapa, ruidosTurno);
            mostrarMovimientoEnemigo();

            if (enemigo.getSalaActual() == jugador.getSalaActual()) {
                if (!jugador.estaEscondido()) {
                    juegoTerminado = true;
                    jugadorGano = false;
                    mostrarDerrota();
                } else {
                    mostrarEscondidoExitoso();
                }
            }

            jugador.setEscondido(false);

            if (!juegoTerminado) {
                System.out.println("\n" + TENUE + "Presiona ENTER para continuar..." + RESET);
                scanner.nextLine();
            }
        }
        scanner.close();
    }

    private void procesarAccionJugador() {
        boolean accionValida = false;

        while (!accionValida) {
            mostrarOpciones();
            String entrada = scanner.nextLine().trim().toUpperCase();

            switch (entrada) {
                case "M":
                    accionValida = procesarMovimiento();
                    break;
                case "E":
                    accionValida = procesarEsconderse();
                    break;
                case "D":
                    accionValida = procesarDistraccion();
                    break;
                case "W":
                    accionValida = procesarEsperar();
                    break;
                default:
                    System.out.println(ROJO + "  Opcion no valida. Intenta de nuevo." + RESET);
            }
        }
    }

    private boolean procesarMovimiento() {
        Sala actual = jugador.getSalaActual();
        ArrayList<Conexion> conexiones = actual.getConexiones();

        if (conexiones.isEmpty()) {
            System.out.println(ROJO + "  No hay salidas desde esta sala!" + RESET);
            return false;
        }

        System.out.println("\n" + CYAN + "  Conexiones disponibles:" + RESET);
        for (int i = 0; i < conexiones.size(); i++) {
            Conexion c = conexiones.get(i);
            System.out.println("    " + VERDE + "[" + (i + 1) + "] " + RESET
                    + c.getDestino().getNombre());
            System.out.println("        " + ITALIC + TENUE + c.getDescripcion() + RESET);
        }

        System.out.print("\n  " + AMARILLO + "Elige una puerta (0 para cancelar): " + RESET);
        String entrada = scanner.nextLine().trim();

        try {
            int opcion = Integer.parseInt(entrada);
            if (opcion == 0)
                return false;
            if (opcion < 1 || opcion > conexiones.size()) {
                System.out.println(ROJO + "  Opcion no valida." + RESET);
                return false;
            }

            Conexion elegida = conexiones.get(opcion - 1);
            Sala destino = elegida.getDestino();
            int ruido = elegida.getRuidoMovimiento();

            ruidosTurno[destino.getId()] += ruido;

            jugador.setSalaActual(destino);
            System.out.println("\n  " + CYAN + "Te mueves hacia: " + BLANCO
                    + destino.getNombre() + RESET);
            System.out.println("  " + TENUE + "Tus pasos generan eco en la sala..." + RESET);
            return true;

        } catch (NumberFormatException e) {
            System.out.println(ROJO + "  Entrada no valida." + RESET);
            return false;
        }
    }

    private boolean procesarEsconderse() {
        Sala actual = jugador.getSalaActual();

        if (!actual.tieneEscondite()) {
            System.out.println(ROJO + "  No hay donde esconderse en esta sala." + RESET);
            return false;
        }

        if (actual.isEsconditeUsado()) {
            System.out.println(
                    ROJO + "  Ya usaste el escondite de esta sala. No puedes esconderte aqui de nuevo." + RESET);
            return false;
        }

        actual.usarEscondite();
        jugador.setEscondido(true);
        System.out.println("\n  " + MAGENTA + "Te escondes entre las sombras de la sala..." + RESET);
        System.out.println("  " + TENUE + "Contienes la respiracion. No generas ruido." + RESET);
        return true;
    }

    private boolean procesarDistraccion() {
        Sala actual = jugador.getSalaActual();
        int ruido = 3 + random.nextInt(3);
        boolean exito = random.nextInt(100) < 60;

        if (exito && !actual.getConexiones().isEmpty()) {
            int indice = random.nextInt(actual.getConexiones().size());
            Sala destino = actual.getConexiones().get(indice).getDestino();
            ruidosTurno[destino.getId()] += ruido;
            System.out.println("\n  " + VERDE + "Exito! " + RESET
                    + "Lanzas un objeto hacia " + CYAN + destino.getNombre() + RESET + ".");
            System.out.println("  " + TENUE + "El estruendo resuena en esa direccion. (" + ruido + " ruido)" + RESET);
        } else {
            ruidosTurno[actual.getId()] += ruido;
            System.out.println("\n  " + ROJO + "Fallo! " + RESET
                    + "Tropiezas y el ruido resuena en tu propia sala.");
            System.out.println("  " + TENUE + "El eco delata tu posicion... (" + ruido + " ruido)" + RESET);
        }
        return true;
    }

    private boolean procesarEsperar() {
        System.out.println("\n  " + TENUE + "Te quedas inmovil, apenas respirando..." + RESET);
        System.out.println("  " + TENUE + "No generas ningun ruido." + RESET);
        return true;
    }

    // ===================== VISUALIZACION =====================

    private void mostrarEstadoTurno() {
        Sala actual = jugador.getSalaActual();

        System.out.println("\n" + AMARILLO
                + "================================================================" + RESET);
        System.out.println(BLANCO + "                        TURNO " + turno + RESET);
        System.out.println(AMARILLO
                + "================================================================" + RESET);

        // Sala actual
        System.out.println("\n  " + CYAN + "Ubicacion: " + BLANCO + actual.getNombre() + RESET);
        System.out.println("  " + TENUE + "─────────────────────────────────────" + RESET);
        System.out.println("  " + actual.getDescripcion());

        // Info de escondite
        if (actual.tieneEscondite()) {
            if (!actual.isEsconditeUsado()) {
                System.out.println("\n  " + VERDE + "[Hay un escondite disponible en esta sala]" + RESET);
            } else {
                System.out.println("\n  " + TENUE + "[Escondite ya utilizado]" + RESET);
            }
        }

        // Ruido ambiental de la sala
        int ruidoAmb = actual.getRuidoAmbiente(turno);
        if (ruidoAmb > 0) {
            System.out.println("  " + AMARILLO + "[Esta sala emite ruido ambiental]" + RESET);
        }

        // Proximidad del enemigo
        mostrarProximidadEnemigo();
    }

    private void mostrarProximidadEnemigo() {
        int distEnemJug = mapa.obtenerDistancia(enemigo.getSalaActual(), jugador.getSalaActual());
        int distJugEnem = mapa.obtenerDistancia(jugador.getSalaActual(), enemigo.getSalaActual());

        int distancia;
        if (distEnemJug >= 0 && distJugEnem >= 0) {
            distancia = Math.min(distEnemJug, distJugEnem);
        } else if (distEnemJug >= 0) {
            distancia = distEnemJug;
        } else if (distJugEnem >= 0) {
            distancia = distJugEnem;
        } else {
            distancia = 99;
        }

        System.out.println();
        if (distancia <= 1) {
            System.out.println("  " + ROJO + ">>> SIENTES UNA PRESENCIA SOFOCANTE MUY CERCA... <<<" + RESET);
            System.out.println("  " + ROJO + ">>> Fane esta casi sobre ti! <<<" + RESET);
        } else if (distancia == 2) {
            System.out
                    .println("  " + AMARILLO + ">> Escuchas susurros arcanos arrastrándose por las paredes..." + RESET);
        } else if (distancia == 3) {
            System.out.println("  " + AMARILLO + "> Un eco distante resuena por los pasillos..." + RESET);
        } else if (distancia <= 5) {
            System.out.println("  " + TENUE + "Percibes una vibracion leve en el aire..." + RESET);
        } else {
            System.out.println("  " + TENUE + "Todo esta en silencio... por ahora." + RESET);
        }
    }

    private void mostrarOpciones() {
        System.out.println("\n  " + BLANCO + "¿Que deseas hacer?" + RESET);
        System.out.println("    " + VERDE + "[M]" + RESET + " Moverse a otra sala");
        System.out.println("    " + MAGENTA + "[E]" + RESET + " Esconderse en esta sala");
        System.out.println("    " + AMARILLO + "[D]" + RESET + " Distraer (hacer ruido)");
        System.out.println("    " + AZUL + "[W]" + RESET + " Esperar (no hacer nada)");
        System.out.print("\n  > ");
    }

    private void mostrarMovimientoEnemigo() {
        int distEnemJug = mapa.obtenerDistancia(enemigo.getSalaActual(), jugador.getSalaActual());
        int distJugEnem = mapa.obtenerDistancia(jugador.getSalaActual(), enemigo.getSalaActual());

        int distancia;
        if (distEnemJug >= 0 && distJugEnem >= 0) {
            distancia = Math.min(distEnemJug, distJugEnem);
        } else if (distEnemJug >= 0) {
            distancia = distEnemJug;
        } else if (distJugEnem >= 0) {
            distancia = distJugEnem;
        } else {
            distancia = 99;
        }

        System.out.println("\n  " + TENUE + "--- Fane se mueve... ---" + RESET);

        if (distancia <= 1) {
            System.out.println("  " + ROJO + "Sus pasos resuenan justo al otro lado de la puerta!" + RESET);
        } else if (distancia == 2) {
            System.out.println("  " + AMARILLO + "Escuchas canticos lejanos acercandose..." + RESET);
        } else if (distancia <= 4) {
            System.out.println("  " + TENUE + "Un murmullo lejano se desvanece en la oscuridad." + RESET);
        } else {
            System.out.println("  " + TENUE + "Silencio. Fane parece estar lejos." + RESET);
        }
    }

    private void mostrarEscondidoExitoso() {
        System.out.println("\n  " + ROJO
                + "================================================================" + RESET);
        System.out.println("  " + ROJO + "  FANE ENTRA EN LA SALA!" + RESET);
        System.out.println("  " + ROJO
                + "================================================================" + RESET);
        System.out.println("  " + MAGENTA
                + "  Contienes la respiracion. Fane recorre la sala con sus manos" + RESET);
        System.out.println("  " + MAGENTA
                + "  huesudas, buscando... pero no te encuentra." + RESET);
        System.out.println("  " + VERDE
                + "  Tu escondite te ha salvado... esta vez." + RESET);
    }

    private void mostrarVictoria() {
        System.out.println("\n\n");
        System.out.println(VERDE + "  ╔════════════════════════════════════════════════════╗" + RESET);
        System.out.println(VERDE + "  ║                                                    ║" + RESET);
        System.out.println(VERDE + "  ║              H A S   E S C A P A D O                ║" + RESET);
        System.out.println(VERDE + "  ║                                                    ║" + RESET);
        System.out.println(VERDE + "  ╚════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
        System.out.println("  " + CYAN + "Empujas la barra de panico con todas tus fuerzas." + RESET);
        System.out.println("  " + CYAN + "La puerta de metal cede con un chirrido ensordecedor." + RESET);
        System.out.println("  " + CYAN + "La luz del exterior te ciega momentaneamente..." + RESET);
        System.out.println("  " + CYAN + "pero estas libre." + RESET);
        System.out.println();
        System.out.println("  " + BLANCO + "Turnos totales: " + turno + RESET);
        System.out.println("  " + VERDE + "Has escapado de El Archivo Olvidado." + RESET);
        System.out.println("  " + TENUE + "Fane aun espera en la oscuridad..." + RESET);
    }

    private void mostrarDerrota() {
        System.out.println("\n\n");
        System.out.println(ROJO + "  ╔════════════════════════════════════════════════════╗" + RESET);
        System.out.println(ROJO + "  ║                                                    ║" + RESET);
        System.out.println(ROJO + "  ║          F A N E   T E   A T R A P O                ║" + RESET);
        System.out.println(ROJO + "  ║                                                    ║" + RESET);
        System.out.println(ROJO + "  ╚════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
        System.out.println("  " + ROJO + "Sientes unas manos heladas cerrarse alrededor de tu cuello." + RESET);
        System.out.println("  " + ROJO + "Los susurros de Fane llenan tu mente:" + RESET);
        System.out.println("  " + ITALIC + "\"Nadie sale de mi archivo... nadie.\"" + RESET);
        System.out.println();
        System.out.println("  " + BLANCO + "Turno de la captura: " + turno + RESET);
        System.out.println("  " + TENUE + "El Archivo Olvidado reclama otra alma..." + RESET);
    }

    private void mostrarIntroduccion() {
        System.out.println(ROJO);
        System.out.println("  ███████╗██╗          █████╗ ██████╗  ██████╗██╗  ██╗██╗██╗   ██╗ ██████╗ ");
        System.out.println("  ██╔════╝██║         ██╔══██╗██╔══██╗██╔════╝██║  ██║██║██║   ██║██╔═══██╗");
        System.out.println("  █████╗  ██║         ███████║██████╔╝██║     ███████║██║██║   ██║██║   ██║");
        System.out.println("  ██╔══╝  ██║         ██╔══██║██╔══██╗██║     ██╔══██║██║╚██╗ ██╔╝██║   ██║");
        System.out.println("  ███████╗███████╗    ██║  ██║██║  ██║╚██████╗██║  ██║██║ ╚████╔╝ ╚██████╔╝");
        System.out.println("  ╚══════╝╚══════╝    ╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝╚═╝  ╚═══╝   ╚═════╝ ");
        System.out.println(RESET);
        System.out.println(AMARILLO + "             O  L  V  I  D  A  D  O" + RESET);
        System.out.println();
        System.out.println(TENUE + "  ════════════════════════════════════════════════════════════" + RESET);
        System.out.println();
        System.out.println("  " + CYAN + "Despiertas en el suelo frio de una biblioteca subterranea." + RESET);
        System.out.println("  " + CYAN + "No recuerdas como llegaste aqui. Las luces parpadean." + RESET);
        System.out.println("  " + CYAN + "El aire huele a papel viejo y humedad." + RESET);
        System.out.println();
        System.out.println("  " + ROJO + "No estas solo." + RESET);
        System.out.println();
        System.out.println("  " + AMARILLO + "Fane, el brujo, acecha en las profundidades." + RESET);
        System.out.println("  " + AMARILLO + "Es ciego, pero se guia por el eco de tus pasos." + RESET);
        System.out.println("  " + AMARILLO + "Cada movimiento que hagas genera ruido." + RESET);
        System.out.println("  " + AMARILLO + "Cada puerta que cruces podria ser tu ultima." + RESET);
        System.out.println();
        System.out.println("  " + VERDE + "OBJETIVO: " + RESET + "Encuentra la salida de emergencia y escapa.");
        System.out.println();
        System.out.println("  " + BLANCO + "CONTROLES:" + RESET);
        System.out.println("    " + VERDE + "[M]" + RESET + " Moverse         "
                + MAGENTA + "[E]" + RESET + " Esconderse");
        System.out.println("    " + AMARILLO + "[D]" + RESET + " Distraer        "
                + AZUL + "[W]" + RESET + " Esperar");
        System.out.println();
        System.out.println(TENUE + "  ════════════════════════════════════════════════════════════" + RESET);
        System.out.println();
        System.out.println("  " + TENUE + "Presiona ENTER para comenzar tu escape..." + RESET);
        scanner.nextLine();
    }
}
