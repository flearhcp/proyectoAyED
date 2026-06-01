package LectorJSON;

import java.util.List;


public class DatosMapa {
    private List<Vertice> vertices;
    private List<Arista> aristas;
    private double minLat;
    private double maxLat;
    private double minLon;
    private double maxLon;


    public DatosMapa(List<Vertice> vertices,List<Arista> aristas) {
        this.vertices = vertices;
        this.aristas = aristas;
       calcularLimites();
    }

    private void calcularLimites() {
        this.minLat= Double.MAX_VALUE;
        this.maxLat = -Double.MAX_VALUE;
        this.minLon = Double.MAX_VALUE;
        this.maxLon = -Double.MAX_VALUE;

        for (Vertice v : vertices){
            if (v.getLatitud() < this.minLat){
                this.minLat = v.getLatitud();
            }
            if (v.getLatitud() > this.maxLat){
                this.maxLat = v.getLatitud();
            }
            if (v.getLongitud() < this.minLon){
                this.minLon = v.getLongitud();
            }
            if (v.getLongitud() > this.maxLon){
                this.maxLon = v.getLongitud();
            }

        }
    }

    public int getCantidadVertices() {
        return vertices.size();
    }

    public List<Vertice> getVertices () {
        return this.vertices;
    }
    
    public List<Arista> getAristas () {
        return this.aristas;
    }

    public double getMaxLat() {
        return this.maxLat;
    }

    public double getMinLat() {
        return this.minLat;
    }

    public double getMinLon() {
        return this.minLon;
    }

    public double getMaxLon() {
        return this.maxLon;
    }

    }
    

