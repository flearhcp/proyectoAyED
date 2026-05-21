package project.motor;

public class Arista {
    private Vertice origen;
    private Vertice destino;
    private Calle calle;
  /*   private double distancia;
    private double tiempo; */

    public Arista(Vertice origen,Vertice destino,Calle calle) {
        this.origen = origen;
        this.destino = destino;
        this.calle = calle;
     /*    this.distancia = distancia;
        this.tiempo = tiempo; */
    }

    public Calle getCalle() {
        return calle;
    }

    public Vertice getOrigen (){
        return this.origen;
    }

     public Vertice getDestino (){
        return this.destino;
    }
 
}
