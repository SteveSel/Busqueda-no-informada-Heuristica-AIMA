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
        // Parámetros del experimento 1 del PDF
        int numCentros = 5;
        int numHelicopterosPorCentro = 1;
        int semillaCentros = 1234;
        int numGrupos = 100;
        int semillaGrupos = 1234;

        // 1. Inicializamos los datos estáticos del State
        State.todosLosCentros = new Centros(numCentros, numHelicopterosPorCentro, semillaCentros);
        State.todosLosGrupos = new Grupos(numGrupos, semillaGrupos);

        // 2. Crear estado inicial
        int totalHelicopteros = numCentros * numHelicopterosPorCentro;
        State estadoInicial = new State(totalHelicopteros);
        estadoInicial.generarSolucionInicial();

        System.out.println("Estado Inicial Generado con éxito.");

        // 3. Ejecutar algoritmo
        ejecutarHillClimbing(estadoInicial);
    }

    private static void ejecutarHillClimbing(State estadoInicial) {
        System.out.println("Iniciando Hill Climbing...");
        try {
            // Pasamos nuestra función de Sucesores, GoalTest y Heurística
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
}