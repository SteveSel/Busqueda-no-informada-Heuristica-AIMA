package IA.probRescate;

import aima.search.framework.HeuristicFunction;
import IA.Desastres.Centro;
import IA.Desastres.Grupo;

import java.util.ArrayList;

public class RescueHeuristicFunction implements HeuristicFunction {

    @Override
    public double getHeuristicValue(Object state)
    {
        RescueState rescueState = (RescueState) state;
        double tiempoTotal = 0;

        // sumar el tiempo de todos los helicópteros
        for (int i = 0 ; i < RescueState.numHelicopterosTotales ; ++i) {
            ArrayList<Integer> ruta = rescueState.getAsignaciones().get(i);
            // el helicóptero no tiene grupo asignado, no gasta tiempo
            if (ruta.isEmpty()) continue;
            Centro centro = rescueState.getCentroDeHelicoptero(i);
            int cargaActual = 0;
            int gruposEnViaje = 0;
            double posXActual = centro.getCoordX();
            double posYActual = centro.getCoordY();

            for (Integer idGrupo : ruta) {
                Grupo grupo = RescueState.grupos.get(idGrupo);
                // comprobar si las restricciones nos obligan a volver a la base y hacer otra salida
                // > 15 personas o 3 grupos
                if (cargaActual + grupo.getNPersonas() > 15 || gruposEnViaje == 3) {
                    // volver al centro
                    tiempoTotal += calcularTiempoVuelo(posXActual, posYActual, centro.getCoordX(), centro.getCoordY());
                    // esperar 10 min antes de volver a salir
                    tiempoTotal += 10;
                    // reseteamos
                    cargaActual = 0;
                    gruposEnViaje = 0;
                    posXActual = centro.getCoordX();
                    posYActual = centro.getCoordY();
                }
                // grupo a rescatar
                tiempoTotal += calcularTiempoVuelo(posXActual, posYActual, grupo.getCoordX(), grupo.getCoordY());
                // 2 minutos por persona if prioridad else 1 minuto
                int tiempoRescatePorPersona = (grupo.getPrioridad() == 1) ? 2 : 1;
                tiempoTotal += grupo.getNPersonas() * tiempoRescatePorPersona;

                cargaActual += grupo.getNPersonas();
                ++gruposEnViaje;
                posXActual = centro.getCoordX();
                posYActual = centro.getCoordY();
            }
            if (gruposEnViaje > 0) {
                tiempoTotal += calcularTiempoVuelo(posXActual, posYActual, centro.getCoordX(), centro.getCoordY());
            }
        }
        return tiempoTotal;
    }

    // distancia euclidiana
    // velocidad = 100 km/h, en minutos es 10/6 km/min
    // tiempo = dist/velocidad = dist * 6/10
    private double calcularTiempoVuelo(double x1, double y1, double x2, double y2)
    {
        double dist = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        return dist * 0.6;
    }
}