package Solution;

import aima.search.framework.HeuristicFunction;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.framework.SuccessorFunction;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

import IA.Desastres.Centros;
import IA.Desastres.Grupos;

import java.util.Iterator;
import java.util.Properties;
import java.util.Scanner;

public class Demo {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Algoritmo a usar (HC / SA): ");
        String algoritmo = scanner.next().toUpperCase();

        System.out.print("Semilla (para centros y grupos): ");
        int semilla = scanner.nextInt();

        System.out.print("Numero de centros: ");
        int numCentros = scanner.nextInt();

        System.out.print("Numero de helicopteros por centro: ");
        int numHelicopterosPorCentro = scanner.nextInt();

        System.out.print("Numero de grupos a rescatar: ");
        int numGrupos = scanner.nextInt();

        System.out.print("Heuristica a usar (1 / 2): ");
        int opcionHeuristica = scanner.nextInt();

        System.out.print("Operador a usar (1: Mover, 2: Swap, 3: Invertir): ");
        int opcionOperador = scanner.nextInt();

        System.out.print("Generador inicial (1 / 2 / 3): ");
        int opcionGenerador = scanner.nextInt();

        State.todosLosCentros = new Centros(numCentros, numHelicopterosPorCentro, semilla);
        State.todosLosGrupos = new Grupos(numGrupos, semilla);

        int totalHelicopteros = numCentros * numHelicopterosPorCentro;
        State estadoInicial = new State(totalHelicopteros);

        InitialStateGenerator.generarSolucion(estadoInicial, opcionGenerador);

        System.out.println("\nEstado Inicial Generado con exito.");
        imprimirUbicacionesIniciales(estadoInicial);

        HeuristicFunction heuristica;
        if (opcionHeuristica == 1) {
            heuristica = new HeuristicFunction1();
        } else {
            heuristica = new HeuristicFunction2(1);
        }
        SuccessorFunction sucesores = null;

        if (algoritmo.equals("HC")) {
            scanner.close();
            switch (opcionOperador) {
                case 1: sucesores = new SuccessorFunction1HC(); break;
                case 2: sucesores = new SuccessorFunction2HC(); break;
                case 3: sucesores = new SuccessorFunction3HC(); break;
                default: System.out.println("Operador invalido."); return;
            }
            ejecutarHillClimbing(estadoInicial, sucesores, heuristica);

        } else if (algoritmo.equals("SA")) {
            switch (opcionOperador) {
                case 1: sucesores = new SuccessorFunction1SA(); break;
                case 2: sucesores = new SuccessorFunction2SA(); break;
                case 3: sucesores = new SuccessorFunction3SA(); break;
                default: System.out.println("Operador invalido."); return;
            }
            ejecutarSimulatedAnnealing(estadoInicial, sucesores, heuristica, scanner);

        } else {
            scanner.close();
            System.out.println("Algoritmo no reconocido. Usa HC o SA.");
        }
    }

    private static void ejecutarHillClimbing(State estadoInicial, SuccessorFunction sucesores, HeuristicFunction heuristica) {
        System.out.println("\nIniciando Hill Climbing...");
        try {
            Problem problem = new Problem(estadoInicial, sucesores, new RescueGoalTest(), heuristica);
            long tiempoInicio = System.currentTimeMillis();
            Search search = new HillClimbingSearch();
            SearchAgent agent = new SearchAgent(problem, search);


            long tiempoFin = System.currentTimeMillis();
            long tiempoTotal = tiempoFin - tiempoInicio;
            System.out.println("\nTiempo: " + tiempoTotal + " ms (" + (tiempoTotal / 1000.0) + " s)");
            imprimirResultados(agent, search, heuristica);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void ejecutarSimulatedAnnealing(State estadoInicial, SuccessorFunction sucesores, HeuristicFunction heuristica, Scanner scanner) {
        System.out.println("\nIniciando Simulated Annealing...");
        try {
            Problem problem = new Problem(estadoInicial, sucesores, new RescueGoalTest(), heuristica);

            System.out.print("Steps: ");
            int steps = scanner.nextInt();

            System.out.print("Steps/iter: ");
            int stepsIter = scanner.nextInt();

            System.out.print("K: ");
            int k = scanner.nextInt();

            System.out.print("Lambda: ");
            int lambda = scanner.nextInt();
            scanner.close();

            long tiempoInicio = System.currentTimeMillis();

            Search search = new SimulatedAnnealingSearch(steps, stepsIter, k, lambda);
            SearchAgent agent = new SearchAgent(problem, search);

            long tiempoFin = System.currentTimeMillis();
            long tiempoTotal = tiempoFin - tiempoInicio;
            System.out.println("\nTiempo: " + tiempoTotal + " ms (" + (tiempoTotal / 1000.0) + " s)");
            imprimirResultados(agent, search, heuristica);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void imprimirResultados(SearchAgent agent, Search search, HeuristicFunction heuristica) {
        System.out.println("\nInstrumentacion: ");
        printInstrumentation(agent.getInstrumentation());

        State estadoFinal = (State) search.getGoalState();

        if (estadoFinal != null) {
            double tiempoFinal = heuristica.getHeuristicValue(estadoFinal);
            imprimirAsignacionGrupos(estadoFinal);
            System.out.println("\n>> TIEMPO TOTAL: " + tiempoFinal + " minutos <<");

        } else {
            System.out.println("El algoritmo no ha devuelto un estado final.");
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

    private static void imprimirUbicacionesIniciales(State estado) {
        System.out.println("\nUbicacion Inicial de Centros y Helicopteros: ");
        int numCentros = State.todosLosCentros.size();
        int totalHelicopteros = estado.getNumHelicopteros();
        int helicosPorCentro = totalHelicopteros / numCentros;

        for (int c = 0; c < numCentros; c++) {
            int x = State.todosLosCentros.get(c).getCoordX();
            int y = State.todosLosCentros.get(c).getCoordY();

            System.out.println("Centro " + c + " en Coordenadas (" + x + ", " + y + ")");
            System.out.print("  -> Helicopteros estacionados aqui: [ ");

            for (int h = 0; h < helicosPorCentro; h++) {
                int idHelicoptero = (c * helicosPorCentro) + h;
                System.out.print(idHelicoptero + " ");
            }
            System.out.println("]");
        }
        System.out.println("---------------------------------------------------\n");
    }

    private static void imprimirAsignacionGrupos(State estado) {
        System.out.println("\n--- Asignacion de Grupos a Helicopteros ---");
        int numGrupos = State.todosLosGrupos.size();
        int numHelicopteros = estado.getNumHelicopteros();

        int[] asignacion = new int[numGrupos];

        for (int h = 0; h < numHelicopteros; h++) {
            for (int idGrupo : estado.getRuta(h)) {
                asignacion[idGrupo] = h;
            }
        }

        for (int i = 0; i < numGrupos; i++) {
            System.out.println("Grupo " + i + " -> Rescatado por Helicoptero " + asignacion[i]);
        }

        System.out.println("\n--- Rutas completas por Helicoptero ---");
        for (int h = 0; h < numHelicopteros; h++) {
            if (estado.getRuta(h).isEmpty()) {
                System.out.println("Helicoptero " + h + ": (Sin asignar)");
            } else {
                System.out.println("Helicoptero " + h + ": " + estado.getRuta(h));
            }
        }
    }
}