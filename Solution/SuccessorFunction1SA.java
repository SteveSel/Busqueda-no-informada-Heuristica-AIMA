package Solution;
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/* Función para Simulated Annealing: Mover aleatorio */

public class SuccessorFunction1SA implements SuccessorFunction {
    public List getSuccessors(Object state) {
        ArrayList<Successor> sucesores = new ArrayList<>();
        State estadoActual = (State) state; // Casteamos igual que en HC
        int numHelicopteros = estadoActual.getNumHelicopteros();
        Random rand = new Random();

        int h1, h2, posOrigen, posDestino;

        h1 = rand.nextInt(numHelicopteros);
        while (estadoActual.getRuta(h1).isEmpty()) {
            h1 = rand.nextInt(numHelicopteros);
        }

        int numGruposH1 = estadoActual.getRuta(h1).size();

        posOrigen = rand.nextInt(numGruposH1);
        h2 = rand.nextInt(numHelicopteros);
        int numGruposH2 = estadoActual.getRuta(h2).size();

        int limiteInsercion = (h1 == h2) ? numGruposH1 - 1 : numGruposH2;

        if (limiteInsercion == 0) {
            posDestino = 0;
        } else {
            posDestino = rand.nextInt(limiteInsercion + 1);
        }

        State nuevoEstado = new State(estadoActual);
        int idGrupoMovido = nuevoEstado.getRuta(h1).remove(posOrigen);
        nuevoEstado.getRuta(h2).add(posDestino, idGrupoMovido);

        String descripcionAccion = "SA: Mover grupo " + idGrupoMovido + " de Heli " + h1 + " a Heli " + h2;
        sucesores.add(new Successor(descripcionAccion, nuevoEstado));

        return sucesores;
    }
}