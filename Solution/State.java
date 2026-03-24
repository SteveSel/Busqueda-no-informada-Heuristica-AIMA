package Solution;
import java.util.ArrayList;
import IA.Desastres.*;

public class State {
    public static Grupos todosLosGrupos;
    public static Centros todosLosCentros;

    private ArrayList<Integer>[] rutasHelicopteros;
    private int numHelicopteros;

    public State(State fatherState) {
        this.numHelicopteros = fatherState.numHelicopteros;
        this.rutasHelicopteros = new ArrayList[numHelicopteros];
        for (int i = 0; i < numHelicopteros; i++) {
            this.rutasHelicopteros[i] = new ArrayList<>(fatherState.rutasHelicopteros[i]);
        }
    }

    public State(int numHelicopteros) {
        this.numHelicopteros = numHelicopteros;
        this.rutasHelicopteros = new ArrayList[numHelicopteros];
        for (int i = 0; i < numHelicopteros; i++) {
            this.rutasHelicopteros[i] = new ArrayList<>();
        }
    }

    public void generarSolucionInicial() {
        for (int i = 0; i < todosLosGrupos.size(); i++) {
            int helicopteroAsignado = i % numHelicopteros;
            rutasHelicopteros[helicopteroAsignado].add(i);
        }
    }

    public ArrayList<Integer> getRuta(int idHelicoptero) {
        return rutasHelicopteros[idHelicoptero];
    }

    public int getNumHelicopteros() {
        return numHelicopteros;
    }
}