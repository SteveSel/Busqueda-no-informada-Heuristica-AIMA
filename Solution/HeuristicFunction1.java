package Solution;

import aima.search.framework.HeuristicFunction;
import IA.Desastres.Centro;
import IA.Desastres.Grupo;
import java.util.ArrayList;

public class HeuristicFunction1 implements HeuristicFunction {

    @Override
    public double getHeuristicValue(Object state) {
        State estado = (State) state;
        double tiempoTotalSuma = 0.0;

        int numHelicopteros = estado.getNumHelicopteros();
        int numCentros = State.todosLosCentros.size();
        int helicosPorCentro = numHelicopteros / numCentros;

        for (int h = 0; h < numHelicopteros; h++) {

            int idCentro = h / helicosPorCentro;
            Centro centro = State.todosLosCentros.get(idCentro);

            ArrayList<Integer> ruta = estado.getRuta(h);
            if (ruta.isEmpty()) continue;

            double tiempoRelojHelicoptero = 0.0;

            int actualX = centro.getCoordX();
            int actualY = centro.getCoordY();

            int gruposEnViaje = 0;
            int personasEnViaje = 0;

            for (int i = 0; i < ruta.size(); i++) {
                int idGrupo = ruta.get(i);
                Grupo grupo = State.todosLosGrupos.get(idGrupo);

                // Si se superan las restricciones, hay que volver al centro y empezar un nuevo viaje
                if (gruposEnViaje == 3 || (personasEnViaje + grupo.getNPersonas() > 15)) {
                    // Volver al centro
                    double distRetorno = Math.hypot(actualX - centro.getCoordX(), actualY - centro.getCoordY());
                    tiempoRelojHelicoptero += (distRetorno / 100.0) * 60.0;

                    // Penalización de 10 min por nueva salida
                    tiempoRelojHelicoptero += 10.0;

                    // Reiniciar variables para el nuevo viaje
                    actualX = centro.getCoordX();
                    actualY = centro.getCoordY();
                    gruposEnViaje = 0;
                    personasEnViaje = 0;
                }

                // Vuelo hacia el grupo
                double distVuelo = Math.hypot(actualX - grupo.getCoordX(), actualY - grupo.getCoordY());
                tiempoRelojHelicoptero += (distVuelo / 100.0) * 60.0;

                // Tiempo de rescate
                int multiplicadorTiempo = (grupo.getPrioridad() == 1) ? 2 : 1;
                tiempoRelojHelicoptero += grupo.getNPersonas() * multiplicadorTiempo;

                // Actualizar posiciones y contadores
                actualX = grupo.getCoordX();
                actualY = grupo.getCoordY();
                gruposEnViaje++;
                personasEnViaje += grupo.getNPersonas();
            }

            // Al terminar todos los grupos de la ruta, el helicóptero debe volver al centro final
            if (gruposEnViaje > 0) {
                double distRetorno = Math.hypot(actualX - centro.getCoordX(), actualY - centro.getCoordY());
                tiempoRelojHelicoptero += (distRetorno / 100.0) * 60.0;
            }

            tiempoTotalSuma += tiempoRelojHelicoptero;
        }

        return tiempoTotalSuma;
    }
}