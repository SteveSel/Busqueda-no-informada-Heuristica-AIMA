package Solution;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* Función para Hill Climbing que invierte un subconjunto de la ruta de un helicóptero */

public class SuccessorFunction3HC implements SuccessorFunction{

    @Override
    public List getSuccessors(Object state) {
        ArrayList<Successor> sucesores = new ArrayList<>();
        State estadoActual = (State) state;
        int numHelicopteros = estadoActual.getNumHelicopteros();

        for (int h = 0; h < numHelicopteros; h++) {
            int numGrupos = estadoActual.getRuta(h).size();

            if (numGrupos >= 2) {

                for (int pos1 = 0; pos1 < numGrupos - 1; pos1++) {

                    for (int pos2 = pos1 + 1; pos2 < numGrupos; pos2++) {

                        State nuevoEstado = new State(estadoActual);
                        Collections.reverse(nuevoEstado.getRuta(h).subList(pos1, pos2 + 1));

                        String descripcion = "HC Invertir: Heli " + h + " (pos " + pos1 + " a " + pos2 + ")";
                        sucesores.add(new Successor(descripcion, nuevoEstado));
                    }
                }
            }
        }
        return sucesores;
    }
}
