package Solution;

import IA.Desastres.Centro;
import IA.Desastres.Grupo;
import java.util.Random;

public class InitialStateGenerator {

    /**
     * @param estado El estado vacío que vamos a rellenar
     * @param estrategia 1: Secuencial, 2: Avariciosa (Greedy), 3: Aleatoria
     */
    public static void generarSolucion(State estado, int estrategia) {
        int numHelicopteros = estado.getNumHelicopteros();
        int numGrupos = State.todosLosGrupos.size();

        // Arrays auxiliares para controlar si un grupo "cabe" en el viaje actual del helicóptero
        int[] personasVuelo = new int[numHelicopteros];
        int[] gruposVuelo = new int[numHelicopteros];

        switch (estrategia) {

            case 1: // Secuencial: O(g) | Calidad media
                int hActual = 0;
                for (int i = 0; i < numGrupos; i++) {
                    Grupo g = State.todosLosGrupos.get(i);

                    // Si no cabe (pasa de 15 personas o ya tiene 3 grupos), pasamos al siguiente helicóptero
                    if (personasVuelo[hActual] + g.getNPersonas() > 15 || gruposVuelo[hActual] == 3) {
                        hActual = (hActual + 1) % numHelicopteros;
                        // Al cambiar, simulamos que este helicóptero inicia un nuevo viaje limpio
                        personasVuelo[hActual] = 0;
                        gruposVuelo[hActual] = 0;
                    }

                    estado.getRuta(hActual).add(i);
                    personasVuelo[hActual] += g.getNPersonas();
                    gruposVuelo[hActual]++;
                }
                break;

            case 2: // Avariciosa (Greedy): O(g*h) | Calidad alta
                int numCentros = State.todosLosCentros.size();
                int helicosPorCentro = numHelicopteros / numCentros;

                for (int i = 0; i < numGrupos; i++) {
                    Grupo g = State.todosLosGrupos.get(i);
                    int mejorHeli = -1;
                    double minDist = Double.MAX_VALUE;

                    // 1. Buscamos el helicóptero más cercano que NO esté ocupado (que tenga hueco)
                    for (int h = 0; h < numHelicopteros; h++) {
                        if (personasVuelo[h] + g.getNPersonas() <= 15 && gruposVuelo[h] < 3) {
                            int idCentro = h / helicosPorCentro;
                            Centro c = State.todosLosCentros.get(idCentro);

                            // Distancia euclídea al centro del helicóptero
                            double dist = Math.hypot(g.getCoordX() - c.getCoordX(), g.getCoordY() - c.getCoordY());
                            if (dist < minDist) {
                                minDist = dist;
                                mejorHeli = h;
                            }
                        }
                    }

                    // 2. Si todos estaban "ocupados/llenos", buscamos el centro más cercano en absoluto
                    // y obligamos a ese helicóptero a hacer un nuevo viaje.
                    if (mejorHeli == -1) {
                        for (int h = 0; h < numHelicopteros; h++) {
                            int idCentro = h / helicosPorCentro;
                            Centro c = State.todosLosCentros.get(idCentro);
                            double dist = Math.hypot(g.getCoordX() - c.getCoordX(), g.getCoordY() - c.getCoordY());
                            if (dist < minDist) {
                                minDist = dist;
                                mejorHeli = h;
                            }
                        }
                        // Forzamos un viaje nuevo reseteando sus contadores
                        personasVuelo[mejorHeli] = 0;
                        gruposVuelo[mejorHeli] = 0;
                    }

                    estado.getRuta(mejorHeli).add(i);
                    personasVuelo[mejorHeli] += g.getNPersonas();
                    gruposVuelo[mejorHeli]++;
                }
                break;

            case 3: // Aleatoria: O(g) | Calidad pésima
                Random rand = new Random();
                for (int i = 0; i < numGrupos; i++) {
                    int hAleatorio = rand.nextInt(numHelicopteros);
                    estado.getRuta(hAleatorio).add(i);
                }
                break;
        }
    }
}