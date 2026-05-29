package LectorJSON;

import java.util.List;


public class DatosMapa {
    private List<Vertice> vertices;
    private List<Arista> aristas;

    public DatosMapa(List<Vertice> vertices,List<Arista> aristas) {
        this.vertices = vertices;
        this.aristas = aristas;
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
        return vertices.get(getCantidadVertices() - 1).getLatitud();
    }

    public double getMinLat() {
        return vertices.get(0).getLatitud();
    }

    public double getMinLon() {
        return vertices.get(0).getLongitud();
    }

    public double getMaxLon() {
        return vertices.get(getCantidadVertices() - 1).getLongitud();
    }

}
    

