package IA.probRescate;

import aima.search.framework.HeuristicFunction;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;

import java.util.Iterator;
import java.util.Properties;

public class RescueDemo {
    public static void main(String[] args)
    {
        int numCentros = 5;
        int numHelicopterosPorCentro = 2;
        int semillaCentros = 122144;
        int numGrupos = 100;
        int semillaGrupos = 5678;
        // inicializamos
        RescueState.initData(numCentros, numHelicopterosPorCentro, semillaCentros, numGrupos, semillaGrupos);
        // crear estado inicial
        RescueState estadoInicial = new RescueState();
        estadoInicial.generarSolucionInicialIterativa();

        System.out.println("Estado Inicial");
        System.out.println(estadoInicial.toString());

        ejecutarHillClimbing(estadoInicial);
    }

    private static void ejecutarHillClimbing(RescueState estadoInicial)
    {
        System.out.println("Hill Climbing");
        try {
            Problem problem = new Problem(estadoInicial, new RescueSuccessorFunction(), new RescueGoalTest(), new RescueHeuristicFunction());
            Search search = new HillClimbingSearch();
            SearchAgent agent = new SearchAgent(problem, search);

            System.out.println("Resultado Final");
            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());

            RescueState estadoFinal = (RescueState) search.getGoalState();

            System.out.println("Estado Final");
            System.out.println(estadoFinal.toString());

            RescueHeuristicFunction heuristica = new RescueHeuristicFunction();
            double tiempoFinal = heuristica.getHeuristicValue(estadoFinal);
            System.out.println("Tiempo total final: " + tiempoFinal + " minutos");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printInstrumentation(Properties properties)
    {
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