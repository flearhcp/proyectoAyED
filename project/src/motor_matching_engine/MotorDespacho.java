package motor_matching_engine;

import java.util.Random;

import contenedores.*;
import Grafos.*;

//import Grafo.*;

public class MotorDespacho {

    public Vehiculo despacharViaje(Usuario pasajero,Lista1DLinkedL flota,GrafoMapa miGrafo){
        ColaPrioridad<Vehiculo> cola = new ColaPrioridad<>();
        Vehiculo elegido,aux;
        for (int i = 0; i < flota.tamanio(); i++) {
            aux = (Vehiculo)flota.devolver(i);
            double eta = miGrafo.Dijkstra(pasajero.getVerticeIDOrigen(),aux.getVerticeIDOrigen());
            aux.setActualEta(eta);
            cola.meter(aux);
        }
        if(!cola.estaVacia()){ //Aca deberia estar la opcion si el conductor acepta
            elegido = cola.sacar();
            System.out.println("Vehiculo elegido: "+elegido.getID()+" Asignado con ETA: "+ elegido.getETA());
            return elegido;
        }
        return null;
    }
    public Usuario usuarioRandom(GrafoMapa grafo){
        Usuario generado; Random posRandom;int posO,posD;
        posRandom = new Random();
        posD = posRandom.nextInt(grafo.getOrden());
        posO = posRandom.nextInt(grafo.getOrden());
        generado = new Usuario(posRandom.nextInt(), posO,posD);
        return generado;
    }
    public Vehiculo vehiculoRandom(GrafoMapa grafo){
        Vehiculo generado; Random posRandom;int posO;
        posRandom = new Random();
        posO = posRandom.nextInt(grafo.getOrden());
        generado = new Vehiculo(posRandom.nextInt(), posO);
        return generado;
    }

}
