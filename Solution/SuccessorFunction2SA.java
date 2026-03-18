package Solution;
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/* Función para Simulated Annealing que swapea 2 grupos entre helicópteros */

public class SuccessorFunction2SA implements SuccessorFunction {
    @Override
    public List getSuccessors(Object state) {

        ArrayList<Successor> sucesores = new ArrayList<>();
        State estadoActual = (State) state;
        int numHelicopteros = estadoActual.getNumHelicopteros();
        Random rand = new Random();
        int h1, h2, pos1, pos2;
        h1 = rand.nextInt(numHelicopteros);

        while (estadoActual.getRuta(h1).isEmpty()) {
            h1 = rand.nextInt(numHelicopteros);
        }
        pos1 = rand.nextInt(estadoActual.getRuta(h1).size());

        h2 = rand.nextInt(numHelicopteros);
        while (estadoActual.getRuta(h2).isEmpty()) {
            h2 = rand.nextInt(numHelicopteros);
        }
        pos2 = rand.nextInt(estadoActual.getRuta(h2).size());

        // Por si los helicopteros y grupos son los mismos
        while (h1 == h2 && pos1 == pos2) {
            h2 = rand.nextInt(numHelicopteros);
            while (estadoActual.getRuta(h2).isEmpty()) {
                h2 = rand.nextInt(numHelicopteros);
            }
            pos2 = rand.nextInt(estadoActual.getRuta(h2).size());
        }

        State nuevoEstado = new State(estadoActual);

        int idGrupo1 = nuevoEstado.getRuta(h1).get(pos1);
        int idGrupo2 = nuevoEstado.getRuta(h2).get(pos2);

        nuevoEstado.getRuta(h1).set(pos1, idGrupo2);
        nuevoEstado.getRuta(h2).set(pos2, idGrupo1);

        String descripcionAccion = "SA Swap: Grupo " + idGrupo1 + " <-> Grupo " + idGrupo2;
        sucesores.add(new Successor(descripcionAccion, nuevoEstado));

        return sucesores;
    }
}