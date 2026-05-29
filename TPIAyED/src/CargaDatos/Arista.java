package CargaDatos;

public class Arista {
    private Vertice origen;
    private Vertice destino;
    private Calle calle;
    private double peso; //calculado en segundos
    private double distanciaMetros;

    public Arista(Vertice origen, Vertice destino, Calle calle, double peso, double distanciaMetros) {
        this.origen = origen;
        this.destino = destino;
        this.calle = calle;
        this.peso = peso;
        this.distanciaMetros = distanciaMetros;
    }

    public double getPeso(){
        return this.peso;
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
     public double getDistanciaMetros() {
    	 return this.distanciaMetros;
     }
 
}
