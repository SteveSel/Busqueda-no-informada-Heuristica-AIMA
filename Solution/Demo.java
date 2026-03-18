package Solution;

import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;

import IA.Desastres.Centros;
import IA.Desastres.Grupos;

import java.util.Iterator;
import java.util.Properties;

public class Demo {

    public static void main(String[] args) {
        int numCentros = 5;
        int numHelicopterosPorCentro = 1;
        int semillaCentros = 1234;
        int numGrupos = 100;
        int semillaGrupos = 1234;

        State.todosLosCentros = new Centros(numCentros, numHelicopterosPorCentro, semillaCentros);
        State.todosLosGrupos = new Grupos(numGrupos, semillaGrupos);

        int totalHelicopteros = numCentros * numHelicopterosPorCentro;
        State estadoInicial = new State(totalHelicopteros);
        InitialStateGenerator.generarSolucion(estadoInicial, 2);

        System.out.println("Estado Inicial Generado con éxito.");

        imprimirUbicacionesIniciales(estadoInicial);

        ejecutarHillClimbing(estadoInicial);
    }

    private static void imprimirUbicacionesIniciales(State estado) {
        System.out.println("\n--- Ubicación Inicial de Centros y Helicópteros ---");
        int numCentros = State.todosLosCentros.size();
        int totalHelicopteros = estado.getNumHelicopteros();
        int helicosPorCentro = totalHelicopteros / numCentros;

        for (int c = 0; c < numCentros; c++) {
            // Obtenemos las coordenadas del centro
            int x = State.todosLosCentros.get(c).getCoordX();
            int y = State.todosLosCentros.get(c).getCoordY();

            System.out.println("Centro " + c + " en Coordenadas (" + x + ", " + y + ")");
            System.out.print("  -> Helicópteros estacionados aquí: [ ");

            // Calculamos qué IDs de helicóptero le tocan a este centro
            for (int h = 0; h < helicosPorCentro; h++) {
                int idHelicoptero = (c * helicosPorCentro) + h;
                System.out.print(idHelicoptero + " ");
            }
            System.out.println("]");
        }
        System.out.println("---------------------------------------------------\n");
    }

    private static void ejecutarHillClimbing(State estadoInicial) {
        System.out.println("Iniciando Hill Climbing...");
        try {
            Problem problem = new Problem(estadoInicial,
                    new SuccessorFunction1HC(), // Puedes cambiarlo por el 2HC
                    new RescueGoalTest(),
                    new HeuristicFunction1());  // Puedes cambiarlo por la 2

            Search search = new HillClimbingSearch();
            SearchAgent agent = new SearchAgent(problem, search);

            System.out.println("\n--- Resultado Final ---");
            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());

            State estadoFinal = (State) search.getGoalState();

            if (estadoFinal != null) {
                HeuristicFunction1 heuristica = new HeuristicFunction1();
                double tiempoFinal = heuristica.getHeuristicValue(estadoFinal);
                System.out.println("Tiempo total final: " + tiempoFinal + " minutos");


                imprimirAsignacionGrupos(estadoFinal);

            } else {
                System.out.println("El algoritmo no ha devuelto un estado final.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printInstrumentation(Properties properties) {
        Iterator keys = properties.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            String property = properties.getProperty(key);
            System.out.println(key + ": " + property);
        }
    }

    private static void printActions(java.util.List actions) {
        for (Object action : actions) {
            System.out.println(action.toString());
        }
    }

    private static void imprimirAsignacionGrupos(State estado) {
        System.out.println("\n--- Asignación de Grupos a Helicópteros ---");
        int numGrupos = State.todosLosGrupos.size();
        int numHelicopteros = estado.getNumHelicopteros();

        int[] asignacion = new int[numGrupos];

        for (int h = 0; h < numHelicopteros; h++) {
            for (int idGrupo : estado.getRuta(h)) {
                asignacion[idGrupo] = h;
            }
        }

        for (int i = 0; i < numGrupos; i++) {
            System.out.println("Grupo " + i + " -> Rescatado por Helicóptero " + asignacion[i]);
        }

        System.out.println("\n--- Rutas completas por Helicóptero ---");
        for (int h = 0; h < numHelicopteros; h++) {
            if (estado.getRuta(h).isEmpty()) {
                System.out.println("Helicóptero " + h + ": (Sin asignar)");
            } else {
                System.out.println("Helicóptero " + h + ": " + estado.getRuta(h));
            }
        }
    }
}