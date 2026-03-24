package Solution;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SuccessorFunction3SA implements SuccessorFunction{

    @Override
    public List getSuccessors(Object state) {
        ArrayList<Successor> sucesores = new ArrayList<>();
        State estadoActual = (State) state;
        int numHelicopteros = estadoActual.getNumHelicopteros();
        Random rand = new Random();

        int h = rand.nextInt(numHelicopteros);
        while (estadoActual.getRuta(h).size() < 2) {
            h = rand.nextInt(numHelicopteros);
        }

        int numGrupos = estadoActual.getRuta(h).size();

        int pos1 = rand.nextInt(numGrupos);
        int pos2 = rand.nextInt(numGrupos);

        while (pos1 == pos2) {
            pos2 = rand.nextInt(numGrupos);
        }

        if (pos1 > pos2) {
            int temp = pos1;
            pos1 = pos2;
            pos2 = temp;
        }

        State nuevoEstado = new State(estadoActual);
        Collections.reverse(nuevoEstado.getRuta(h).subList(pos1, pos2 + 1));

        String descripcion = "SA Invertir: Heli " + h + " (pos " + pos1 + " a " + pos2 + ")";
        sucesores.add(new Successor(descripcion, nuevoEstado));

        return sucesores;
    }
}