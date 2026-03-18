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
        RescueState rescueState = (RescueState) state;
        ArrayList<ArrayList<ArrayList<Integer>>> plan = rescueState.getPlan();

        double tiempoTotalSuma = 0.0;
        double maxTiempoLlegadaPrioridad1 = 0.0;

        for (int h = 0; h < plan.size(); h++) {

            int idCentro = h / RescueState.helicosPorCentro;
            Centro centro = RescueState.centros.get(idCentro);

            ArrayList<ArrayList<Integer>> viajes = plan.get(h);
            double tiempoRelojHelicoptero = 0.0;

            for (int v = 0; v < viajes.size(); v++) {
                ArrayList<Integer> viaje = viajes.get(v);
                if (viaje.isEmpty()) continue;

                int actualX = centro.getCoordX();
                int actualY = centro.getCoordY();
                boolean llevaHeridos = false;

                for (Integer idGrupo : viaje) {
                    Grupo grupo = RescueState.grupos.get(idGrupo);

                    double distVuelo = Math.hypot(actualX - grupo.getCoordX(), actualY - grupo.getCoordY());
                    tiempoRelojHelicoptero += (distVuelo / 100.0) * 60.0;

                    int multiplicadorTiempo = (grupo.getPrioridad() == 1) ? 2 : 1;
                    tiempoRelojHelicoptero += grupo.getPersonas() * multiplicadorTiempo;

                    if (grupo.getPrioridad() == 1) {
                        llevaHeridos = true;
                    }

                    actualX = grupo.getCoordX();
                    actualY = grupo.getCoordY();
                }

                double distRetorno = Math.hypot(actualX - centro.getCoordX(), actualY - centro.getCoordY());
                tiempoRelojHelicoptero += (distRetorno / 100.0) * 60.0;

                if (llevaHeridos) {
                    if (tiempoRelojHelicoptero > maxTiempoLlegadaPrioridad1) {
                        maxTiempoLlegadaPrioridad1 = tiempoRelojHelicoptero;
                    }
                }

                if (v < viajes.size() - 1 && !viajes.get(v + 1).isEmpty()) {
                    tiempoRelojHelicoptero += 10.0;
                }
            }

            tiempoTotalSuma += tiempoRelojHelicoptero;
        }

        return tiempoTotalSuma + (pesoPrioridad1 * maxTiempoLlegadaPrioridad1);
    }
}