package Solution;
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;
import java.util.ArrayList;
import java.util.List;

/* Función para Hill Climbing que lo que hace es mover un grupo de un helicóptero a otro */

public class SuccessorFunction1HC implements SuccessorFunction {
    public List getSuccessors(Object state) {
        ArrayList<Successor> sucesores = new ArrayList<>();

        State estadoActual = (State) state; // Casteamos el Object a State porque AIMA no conoce nuestras clases
        int numHelicopteros = estadoActual.getNumHelicopteros();

        for (int h1 = 0; h1 < numHelicopteros; h1++) {
            int numGruposH1 = estadoActual.getRuta(h1).size();

            for (int posOrigen = 0; posOrigen < numGruposH1; posOrigen++) {

                for (int h2 = 0; h2 < numHelicopteros; h2++) {
                    int numGruposH2 = estadoActual.getRuta(h2).size();
                    int limiteInsercion = (h1 == h2) ? numGruposH1 - 1 : numGruposH2;

                    for (int posDestino = 0; posDestino <= limiteInsercion; posDestino++) {

                        if (h1 == h2 && posOrigen == posDestino) {
                            continue;
                        }
                        State nuevoEstado = new State(estadoActual);
                        int idGrupoMovido = nuevoEstado.getRuta(h1).remove(posOrigen);

                        nuevoEstado.getRuta(h2).add(posDestino, idGrupoMovido);

                        String descripcionAccion = "Mover grupo " + idGrupoMovido +
                                " de Heli " + h1 + " a Heli " + h2;
                        sucesores.add(new Successor(descripcionAccion, nuevoEstado));
                    }
                }
            }
        }
        return sucesores;
    }
}