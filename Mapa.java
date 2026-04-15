import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
public class Mapa {
    private ArrayList<Sala> salas;
    private Sala salaInicial;
    private Sala salaFinal;

    public Mapa() {
        this.salas = new ArrayList<>();
    }

    public void agregarSala(Sala s) {
        salas.add(s);
    }

    public Sala getSalaPorId(int id) {
        for (Sala s : salas) {
            if (s.getId() == id)
                return s;
        }
        return null;
    }

    public int obtenerDistancia(Sala origen, Sala destino) {
        if (origen == destino)
            return 0;

        boolean[] visitado = new boolean[salas.size() + 1];
        int[] distancia = new int[salas.size() + 1];
        Queue<Sala> cola = new LinkedList<>();

        visitado[origen.getId()] = true;
        distancia[origen.getId()] = 0;
        cola.add(origen);

        while (!cola.isEmpty()) {
            Sala actual = cola.poll();
            for (Conexion c : actual.getConexiones()) {
                Sala vecino = c.getDestino();
                if (!visitado[vecino.getId()]) {
                    visitado[vecino.getId()] = true;
                    distancia[vecino.getId()] = distancia[actual.getId()] + 1;
                    if (vecino == destino)
                        return distancia[vecino.getId()];
                    cola.add(vecino);
                }
            }
        }
        return -1;
    }

    public Sala obtenerSiguientePaso(Sala origen, Sala destino) {
        if (origen == destino)
            return origen;

        boolean[] visitado = new boolean[salas.size() + 1];
        Sala[] padre = new Sala[salas.size() + 1];
        Queue<Sala> cola = new LinkedList<>();

        visitado[origen.getId()] = true;
        padre[origen.getId()] = null;
        cola.add(origen);

        boolean encontrado = false;

        while (!cola.isEmpty() && !encontrado) {
            Sala actual = cola.poll();
            for (Conexion c : actual.getConexiones()) {
                Sala vecino = c.getDestino();
                if (!visitado[vecino.getId()]) {
                    visitado[vecino.getId()] = true;
                    padre[vecino.getId()] = actual;
                    if (vecino == destino) {
                        encontrado = true;
                        break;
                    }
                    cola.add(vecino);
                }
            }
        }

        if (!encontrado)
            return null;

        Sala paso = destino;
        while (padre[paso.getId()] != origen) {
            paso = padre[paso.getId()];
        }
        return paso;
    }

    public ArrayList<Sala> getSalas() {
        return salas;
    }

    public Sala getSalaInicial() {
        return salaInicial;
    }

    public Sala getSalaFinal() {
        return salaFinal;
    }

    public void setSalaInicial(Sala s) {
        salaInicial = s;
    }

    public void setSalaFinal(Sala s) {
        salaFinal = s;
        s.setSalidaFinal(true);
    }

    public void construirMapa() {

        // === CREACION DE SALAS ===
        // Sala(id, nombre, descripcion, escondite, ruidoBase, frecuenciaRuido)
        // frecuenciaRuido: -1=sin ruido, 0=constante, N=cada N turnos

        Sala s1 = new Sala(1, "Vestibulo de Entrada",
                "Un amplio vestibulo con techos abovedados. El polvo danza en la tenue luz\n"
                        + "que se filtra por grietas en el techo. Columnas de piedra tallada flanquean\n"
                        + "la estancia. Un escritorio de recepcion cubierto de telaranias domina el centro.",
                true, 0, -1);

        Sala s2 = new Sala(2, "Recepcion Abandonada",
                "Mostradores de madera podrida y ficheros metalicos oxidados llenan esta sala.\n"
                        + "Papeles amarillentos cubren el suelo como hojas caidas en otonio.\n"
                        + "El olor a tinta vieja y humedad impregna cada rincon.",
                false, 0, -1);

        Sala s3 = new Sala(3, "Sala de Lectura Principal",
                "Mesas de roble macizo se extienden en hileras bajo candelabros apagados.\n"
                        + "Un enorme reloj de pendulo en la pared marca un tiempo olvidado.\n"
                        + "De vez en cuando, su mecanismo cobra vida y resuena por la sala.",
                true, 2, 3);

        Sala s4 = new Sala(4, "Seccion de Historia",
                "Estanterias de tres metros de altura forman pasillos claustrofobicos.\n"
                        + "Los lomos de los libros muestran titulos en idiomas que no reconoces.\n"
                        + "El aire aqui es denso, como si la historia misma pesara.",
                false, 0, -1);

        Sala s5 = new Sala(5, "Hemeroteca",
                "Terminales de microfichas parpadean con una luz verdosa enfermiza.\n"
                        + "Sus ventiladores giran sin cesar, emitiendo un zumbido constante.\n"
                        + "Periodicos de decadas pasadas se apilan en torres precarias.",
                true, 3, 0);

        Sala s6 = new Sala(6, "Archivo de Manuscritos",
                "Cajas de archivo etiquetadas a mano cubren las paredes del suelo al techo.\n"
                        + "El aire huele a pergamino antiguo y cera de sellado.\n"
                        + "Una mesa de catalogacion con una lampara de laton ocupa el centro.",
                true, 0, -1);

        Sala s7 = new Sala(7, "Catacumbas de Manuscritos",
                "Tuneles excavados en la roca viva albergan nichos repletos de pergaminos.\n"
                        + "La humedad gotea desde el techo formando charcos en el suelo irregular.\n"
                        + "El eco de cada paso reverbera interminablemente en la oscuridad.",
                false, 0, -1);

        Sala s8 = new Sala(8, "Pasillo de los Susurros",
                "Un corredor largo y estrecho con paredes curvas que amplifican cada sonido.\n"
                        + "Las paredes parecen respirar, expandiendose y contrayendose con corrientes.\n"
                        + "Dicen que los susurros de los lectores del pasado aun resuenan aqui.",
                false, 2, 2);

        Sala s9 = new Sala(9, "Sala de Cartografia",
                "Mapas antiguos cubren cada superficie: paredes, mesas, incluso el techo.\n"
                        + "Una enorme mesa de luz en el centro muestra un plano borroso del edificio.\n"
                        + "Instrumentos de navegacion oxidados cuelgan de ganchos en las paredes.",
                true, 0, -1);

        Sala s10 = new Sala(10, "Boveda de Libros Prohibidos",
                "Una camara circular protegida por una reja de hierro forjado.\n"
                        + "Los libros aqui estan encadenados a los estantes con candados corroidos.\n"
                        + "Simbolos extranos adornan el techo abovedado, brillando tenuemente.",
                false, 0, -1);

        Sala s11 = new Sala(11, "Laboratorio de Restauracion",
                "Mesas de trabajo con lupas, pinceles y frascos de quimicos de conservacion.\n"
                        + "Laminas de papel de arroz cuelgan de tendederos sobre las mesas.\n"
                        + "El olor a acetona y cola de archivo es casi abrumador.",
                true, 0, -1);

        Sala s12 = new Sala(12, "Sala de Altavoces",
                "Una sala de control con altavoces empotrados en las paredes.\n"
                        + "Los altavoces emiten en bucle definiciones con voz metalica:\n"
                        + "\"Aberracion: desviacion de lo normal...\" El sonido nunca se detiene.",
                false, 4, 0);

        Sala s13 = new Sala(13, "Deposito Subterraneo",
                "Un almacen humedo y frio lleno de cajas sin etiquetar.\n"
                        + "Tuberias oxidadas recorren el techo bajo, goteando agua negruzca.\n"
                        + "El suelo de concreto esta agrietado, revelando tierra debajo.",
                true, 0, -1);

        Sala s14 = new Sala(14, "Oficina del Archivista",
                "Un despacho circular con un escritorio de caoba cubierto de polvo.\n"
                        + "Libros apilados en torres inestables rodean un sillon de cuero rasgado.\n"
                        + "Un retrato borroso en la pared muestra a alguien... o algo.",
                false, 0, -1);

        Sala s15 = new Sala(15, "Pasadizo Secreto",
                "Un tunel estrecho oculto detras de una estanteria falsa.\n"
                        + "Las paredes estan marcadas con araniazos, como si alguien hubiera\n"
                        + "intentado escapar antes. Una corriente de aire fresco viene del fondo.",
                false, 0, -1);

        Sala s16 = new Sala(16, "Salida de Emergencia",
                "Una puerta de metal con una barra de panico y una senial de EXIT!\n"
                        + "La luz del exterior se filtra por las rendijas.\n"
                        + "La libertad esta al otro lado.",
                false, 0, -1);

        agregarSala(s1);
        agregarSala(s2);
        agregarSala(s3);
        agregarSala(s4);
        agregarSala(s5);
        agregarSala(s6);
        agregarSala(s7);
        agregarSala(s8);
        agregarSala(s9);
        agregarSala(s10);
        agregarSala(s11);
        agregarSala(s12);
        agregarSala(s13);
        agregarSala(s14);
        agregarSala(s15);
        agregarSala(s16);

        setSalaInicial(s1);
        setSalaFinal(s16);

        // === CONEXIONES DIRIGIDAS (agregarConexion: destino, ruido, descripcion) ===

        // Sala 1: Vestibulo de Entrada
        s1.agregarConexion(s2, 2, "Una puerta de madera gastada. Las bisagras parecen algo flojas.");
        s1.agregarConexion(s3, 3, "Una escalera de caracol de hierro. Los peldanios se ven oxidados y chirriantes.");

        // Sala 2: Recepcion Abandonada
        s2.agregarConexion(s1, 2, "La puerta de madera por donde llegaste. Cruje un poco al abrirla.");
        s2.agregarConexion(s4, 1, "Un arco de piedra sin puerta. El paso parece silencioso.");
        s2.agregarConexion(s8, 3, "Una escalera de servicio metalica que desciende a las profundidades.");

        // Sala 3: Sala de Lectura Principal
        s3.agregarConexion(s5, 2, "Una puerta de vidrio esmerilado. Se desliza con cierta suavidad.");
        s3.agregarConexion(s8, 4, "Una escalera metalica oxidada que baja. Cada peldanio parece una alarma.");

        // Sala 4: Seccion de Historia
        s4.agregarConexion(s3, 1, "Un pasillo alfombrado. La alfombra amortigua tus pasos.");
        s4.agregarConexion(s7, 3, "Una trampilla en el suelo. La madera cruje al levantarla.");
        s4.agregarConexion(s5, 2, "Una puerta lateral entre estanterias. Gira con un ligero chirrido.");

        // Sala 5: Hemeroteca
        s5.agregarConexion(s4, 2, "Una puerta batiente de vaiven. Hace un golpe seco al cerrarse.");
        s5.agregarConexion(s6, 1, "Un pasadizo estrecho entre estanterias. Casi silencioso.");

        // Sala 6: Archivo de Manuscritos
        s6.agregarConexion(s9, 2, "Una puerta reforzada de metal. Es pesada pero bien engrasada.");
        s6.agregarConexion(s7, 3, "Una escalera de piedra descendente. Los escalones estan desgastados.");

        // Sala 7: Catacumbas de Manuscritos
        s7.agregarConexion(s8, 2, "Un tunel estrecho excavado en la roca. Tu paso resuena un poco.");
        s7.agregarConexion(s10, 4, "Una reja de hierro pesada. Rechina fuertemente al abrirla.");

        // Sala 8: Pasillo de los Susurros
        s8.agregarConexion(s12, 2, "Una puerta cortafuegos de metal. Se abre con un clic metalico.");
        s8.agregarConexion(s7, 2, "Una abertura irregular en el muro. Hay que agacharse para pasar.");

        // Sala 9: Sala de Cartografia
        s9.agregarConexion(s11, 1, "Una puerta corredera moderna. Se desliza sin apenas sonido.");
        s9.agregarConexion(s6, 2, "La puerta reforzada de vuelta al archivo. Pesada pero silenciosa.");

        // Sala 10: Boveda de Libros Prohibidos
        s10.agregarConexion(s11, 2, "Un pasillo estrecho y oscuro. El suelo es de piedra lisa.");
        s10.agregarConexion(s14, 3, "Una puerta de roble macizo con herrajes de bronce. Pesa una tonelada.");
        s10.agregarConexion(s4, 3, "Una escalera oculta ascendente. Los peldanios de piedra crujen.");

        // Sala 11: Laboratorio de Restauracion
        s11.agregarConexion(s13, 2, "Un montacargas manual. Las cadenas traquetean al descender.");
        s11.agregarConexion(s9, 1, "La puerta corredera de vuelta. Silenciosa como siempre.");

        // Sala 12: Sala de Altavoces
        s12.agregarConexion(s10, 3, "Una escalera de caracol descendente. Los peldanios de metal resuenan.");
        s12.agregarConexion(s13, 2, "Un conducto de ventilacion amplio. Puedes arrastrarte por el.");

        // Sala 13: Deposito Subterraneo
        s13.agregarConexion(s15, 1, "Una grieta en la pared. Apenas cabe una persona. Silenciosa.");
        s13.agregarConexion(s12, 2, "El conducto de ventilacion. Tu respiracion resuena dentro.");

        // Sala 14: Oficina del Archivista (INICIO DEL ENEMIGO)
        s14.agregarConexion(s7, 2, "Un pasaje secreto detras de una estanteria. Se abre con un clic.");
        s14.agregarConexion(s10, 3, "La puerta de roble. Gime al abrirse como si protestara.");

        // Sala 15: Pasadizo Secreto
        s15.agregarConexion(s16, 1, "Una puerta de emergencia con barra de panico. Un paso hacia la libertad.");
        s15.agregarConexion(s14, 4, "Un tunel oscuro y resonante. Cada paso es una sentencia.");

        // Sala 16: Salida de Emergencia (FIN - sin conexiones de salida)
    }
}
