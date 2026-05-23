package motor_matching_engine;

import java.util.List;
import java.util.Random;

import contenedores.*;
import LectorJSON.*;
public class MotorDespacho {

    public void despacharViaje(Usuario pasajero,Lista1DLinkedL<Vehiculo> flota){
        
    }
    private long retornaPuntoRandom(Lista1DLinkedL<Vertice> listaVertices){
        Random random = new Random();
        int indiceRandom = random.nextInt(listaVertices.tamanio());
        long devuelveID = (long) listaVertices.devolver(indiceRandom).getOsmID();
        return devuelveID;
    }
}
