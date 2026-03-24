package Solution;

import aima.search.framework.HeuristicFunction;
import IA.Desastres.Centro;
import IA.Desastres.Grupo;
import java.util.ArrayList;

public class HeuristicFunction2 implements HeuristicFunction {

    private double pesoPrioridad1;

    public HeuristicFunction2(double pesoPrioridad1) {
        this.pesoPrioridad1 = pesoPrioridad1;
    }

    @Override
    public double getHeuristicValue(Object state) {
        State estado = (State) state;
        double tiempoTotalSuma = 0.0;
        double maxTiempoLlegadaPrioridad1 = 0.0;

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
            boolean llevaHeridos = false;

            for (int i = 0; i < ruta.size(); i++) {
                int idGrupo = ruta.get(i);
                Grupo grupo = State.todosLosGrupos.get(idGrupo);

                // Control de restricciones
                if (gruposEnViaje == 3 || (personasEnViaje + grupo.getNPersonas() > 15)) {
                    double distRetorno = Math.hypot(actualX - centro.getCoordX(), actualY - centro.getCoordY());
                    tiempoRelojHelicoptero += (distRetorno / 100.0) * 60.0;

                    // Si llevábamos heridos en este viaje, registrar su tiempo de llegada
                    if (llevaHeridos && tiempoRelojHelicoptero > maxTiempoLlegadaPrioridad1) {
                        maxTiempoLlegadaPrioridad1 = tiempoRelojHelicoptero;
                    }

                    tiempoRelojHelicoptero += 10.0; // Penalización por reponer

                    actualX = centro.getCoordX();
                    actualY = centro.getCoordY();
                    gruposEnViaje = 0;
                    personasEnViaje = 0;
                    llevaHeridos = false;
                }

                double distVuelo = Math.hypot(actualX - grupo.getCoordX(), actualY - grupo.getCoordY());
                tiempoRelojHelicoptero += (distVuelo / 100.0) * 60.0;

                int multiplicadorTiempo = (grupo.getPrioridad() == 1) ? 2 : 1;
                tiempoRelojHelicoptero += grupo.getNPersonas() * multiplicadorTiempo;

                if (grupo.getPrioridad() == 1) {
                    llevaHeridos = true;
                }

                actualX = grupo.getCoordX();
                actualY = grupo.getCoordY();
                gruposEnViaje++;
                personasEnViaje += grupo.getNPersonas();
            }

            if (gruposEnViaje > 0) {
                double distRetorno = Math.hypot(actualX - centro.getCoordX(), actualY - centro.getCoordY());
                tiempoRelojHelicoptero += (distRetorno / 100.0) * 60.0;

                if (llevaHeridos && tiempoRelojHelicoptero > maxTiempoLlegadaPrioridad1) {
                    maxTiempoLlegadaPrioridad1 = tiempoRelojHelicoptero;
                }
            }

            tiempoTotalSuma += tiempoRelojHelicoptero;
        }

        return tiempoTotalSuma + (pesoPrioridad1 * maxTiempoLlegadaPrioridad1);
    }
}