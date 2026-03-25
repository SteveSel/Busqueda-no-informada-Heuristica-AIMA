package Solution;
import java.util.ArrayList;
import IA.Desastres.*;

public class State {
    public static Grupos todosLosGrupos;
    public static Centros todosLosCentros;

    private ArrayList<Integer>[] rutasHelicopteros;
    private int numHelicopteros;

    /* Genera un nuevo estado que es un clon exacto del estado proporcionado,
    permite crear y evaluar estados vecinos aplicando cambios en la ruta
    sin modificar por error la memoria del estado actual y original. */
    public State(State fatherState) {
        this.numHelicopteros = fatherState.numHelicopteros;
        // Se crea un 'new ArrayList' para desvincular las referencias de memoria
        this.rutasHelicopteros = new ArrayList[numHelicopteros];
        for (int i = 0; i < numHelicopteros; i++) {
            this.rutasHelicopteros[i] = new ArrayList<>(fatherState.rutasHelicopteros[i]);
        }
    }

    /* Prepara un estado completamente vacío, instanciando los vectores necesarios
    en memoria para evitar errores. Se utiliza exclusivamente al principio de la ejecución, justo antes de
    llamar a las estrategias de Generación del Estado Inicial*/
    public State(int numHelicopteros) {
        this.numHelicopteros = numHelicopteros;
        this.rutasHelicopteros = new ArrayList[numHelicopteros];
        for (int i = 0; i < numHelicopteros; i++) {
            this.rutasHelicopteros[i] = new ArrayList<>();
        }
    }

    public ArrayList<Integer> getRuta(int idHelicoptero) {
        return rutasHelicopteros[idHelicoptero];
    }

    public int getNumHelicopteros() {
        return numHelicopteros;
    }
}