package IA.probRescate;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.List;

public class RescueSuccessorFunction implements SuccessorFunction {

    @Override
    public List getSuccessors(Object state)
    {
        ArrayList retVal = new ArrayList();
        RescueState estadoActual = (RescueState) state;
        int numHelis = RescueState.numHelicopterosTotales;

        for (int h1 = 0 ; h1 < numHelis ; ++h1) {
            int numGruposH1 = estadoActual.getAsignaciones().get(h1).size();

            for (int idx = 0 ; idx < numGruposH1 ; ++idx) {
                for (int h2 = 0 ; h2 < numHelis ; ++h2) {
                    if (h1 != h2) {
                        RescueState nuevoEstado = new RescueState(estadoActual);
                        nuevoEstado.moverGrupo(h1, idx, h2);
                        String S = "Mover grupo (índice " + idx + ") de Heli " + h1 + " a Heli " + h2;
                        retVal.add(new Successor(S, nuevoEstado));
                    }
                }
            }
        }

        for (int h1 = 0 ; h1 < numHelis ; ++h1) {
            int numGruposH1 = estadoActual.getAsignaciones().get(h1).size();

            for (int idx1 = 0 ; idx1 < numGruposH1 ; ++idx1) {
                for (int h2 = 0 ; h2 < numHelis ; ++h2) {
                    int numGruposH2 = estadoActual.getAsignaciones().get(h2).size();
                    int startIdx2 = (h1 == h2) ? idx1 + 1 : 0;

                    for (int idx2 = startIdx2 ; idx2 < numGruposH2 ; ++idx2) {
                        RescueState nuevoEstado = new RescueState(estadoActual);
                        nuevoEstado.intercambiarGrupos(h1, idx1, h2, idx2);
                        String S = "Swap grupo (H" + h1 + ", pos " + idx1 + ") con (H" + h2 + ", pos " + idx2 + ")";
                        retVal.add(new Successor(S, nuevoEstado));
                    }
                }
            }
        }
        return retVal;
    }
}