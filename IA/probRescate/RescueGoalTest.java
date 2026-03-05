package IA.probRescate;

import aima.search.framework.GoalTest;

public class RescueGoalTest implements GoalTest {

    @Override
    public boolean isGoalState(Object state)
    {
        // En búsqueda local desconocemos el estado final óptimo absoluto.
        // El algoritmo parará cuando no encuentre vecinos mejores (Hill Climbing)
        // o cuando se acabe el tiempo/temperatura (Simulated Annealing).
        return false;
    }
}
