package Solution;
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;
import java.util.ArrayList;
import java.util.List;

/* Función para Hill Climbing que swapea 2 grupos entre helicópteros */

public class SuccessorFunction2HC implements SuccessorFunction {
    @Override
    public List getSuccessors(Object state) {
        ArrayList<Successor> sucesores = new ArrayList<>();

        State estadoActual = (State) state; // Casteamos el object a state
        int numHelicopteros = estadoActual.getNumHelicopteros();

        for (int h1 = 0; h1 < numHelicopteros; h1++) {
            int numGruposH1 = estadoActual.getRuta(h1).size();

            for (int pos1 = 0; pos1 < numGruposH1; pos1++) {

                for (int h2 = h1; h2 < numHelicopteros; h2++) {
                    int numGruposH2 = estadoActual.getRuta(h2).size();

                    //Para no swapear el mismo grupo
                    int inicioPos2 = (h1 == h2) ? pos1 + 1 : 0;

                    for (int pos2 = inicioPos2; pos2 < numGruposH2; pos2++) {
                        State nuevoEstado = new State(estadoActual);

                        int idGrupo1 = nuevoEstado.getRuta(h1).get(pos1);
                        int idGrupo2 = nuevoEstado.getRuta(h2).get(pos2);

                        nuevoEstado.getRuta(h1).set(pos1, idGrupo2);
                        nuevoEstado.getRuta(h2).set(pos2, idGrupo1);

                        String descripcionAccion = "HC Swap: Grupo " + idGrupo1 + " (Heli " + h1 + ") <-> Grupo " + idGrupo2 + " (Heli " + h2 + ")";
                        sucesores.add(new Successor(descripcionAccion, nuevoEstado));
                    }
                }
            }
        }
        return sucesores;
    }
}