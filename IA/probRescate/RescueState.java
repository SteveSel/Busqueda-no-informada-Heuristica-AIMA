package IA.probRescate;

import IA.Desastres.Centro;
import IA.Desastres.Centros;
import IA.Desastres.Grupo;
import IA.Desastres.Grupos;

import java.util.ArrayList;
import java.util.Collections;

public class RescueState {

    public static Centros centros;
    public static Grupos grupos;
    public static int numHelicopterosPorCentro;
    public static int numHelicopterosTotales;

    private ArrayList<ArrayList<Integer>> asignaciones;

    public static void initData(int nCentros, int nHelicopteros, int seedCentros, int nGrupos, int seedGrupos)
    {
        centros = new Centros(nCentros, nHelicopteros, seedCentros);
        grupos = new Grupos(nGrupos, seedGrupos);
        numHelicopterosPorCentro = nHelicopteros;
        numHelicopterosTotales = nCentros * nHelicopteros;
    }

    public RescueState()
    {
        asignaciones = new ArrayList<>(numHelicopterosTotales);

        for (int i = 0 ; i < numHelicopterosTotales ; ++i) {
            asignaciones.add(new ArrayList<Integer>());
        }
    }

    public RescueState(RescueState otroEstado)
    {
        this.asignaciones = new ArrayList<>(numHelicopterosTotales);

        for (ArrayList<Integer> ruta : otroEstado.asignaciones) {
            this.asignaciones.add(new ArrayList<>(ruta));
        }
    }

    public void generarSolucionInicialIterativa()
    {
        for (int i = 0 ; i < grupos.size() ; ++i) {
            int idHelicoptero = i % numHelicopterosTotales;
            asignaciones.get(idHelicoptero).add(i);
        }
    }

    public void moverGrupo(int idHelicopteroOrigen, int indiceEnLista, int idHelicopteroDestino)
    {
        Integer idGrupo = asignaciones.get(idHelicopteroOrigen).remove(indiceEnLista);
        asignaciones.get(idHelicopteroDestino).add(idGrupo);
    }

    public void intercambiarGrupos(int h1, int idx1, int h2, int idx2)
    {
        Integer idGrupo1 = asignaciones.get(h1).get(idx1);
        Integer idGrupo2 = asignaciones.get(h2).get(idx2);
        asignaciones.get(h1).set(idx1, idGrupo2);
        asignaciones.get(h2).set(idx2, idGrupo1);
    }

    public void invertirRuta(int idHelicoptero)
    {
        Collections.reverse(asignaciones.get(idHelicoptero));
    }

    public ArrayList<ArrayList<Integer>> getAsignaciones()
    {
        return asignaciones;
    }

    public Centro getCentroDeHelicoptero(int idHelicoptero)
    {
        int idCentro = idHelicoptero / numHelicopterosPorCentro;
        return centros.get(idCentro);
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Asignación de Grupos a Helicópteros:\n");

        for (int i = 0 ; i < asignaciones.size() ; ++i) {
            sb.append("Heli ").append(i).append(": ");

            if (asignaciones.get(i).isEmpty()) {
                sb.append("[Sin grupos asignados]\n");
            }
            else {
                sb.append(asignaciones.get(i).toString()).append("\n");
            }
        }
        return sb.toString();
    }
}