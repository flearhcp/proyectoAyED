package project.motor;

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
    }
    

