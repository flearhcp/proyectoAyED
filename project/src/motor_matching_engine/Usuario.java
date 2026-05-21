package motor_matching_engine;

public class Usuario {
    private int ID;
    private long verticeIDOrigen;
    private long verticeIDDestino;

    public Usuario(int ID,long verticeOrigen, long verticeDestino){
        this.ID = ID;
        this.verticeIDOrigen = verticeOrigen;
        this.verticeIDDestino = verticeDestino;
    }

    public int getID() {
        return ID;
    }

    public void setID(int iD) {
        ID = iD;
    }

    public long getVerticeIDOrigen() {
        return verticeIDOrigen;
    }

    public void setVerticeIDOrigen(long verticeIDOrigen) {
        this.verticeIDOrigen = verticeIDOrigen;
    }

    public long getVerticeIDDestino() {
        return verticeIDDestino;
    }

    public void setVerticeIDDestino(long verticeIDDestino) {
        this.verticeIDDestino = verticeIDDestino;
    }
    
}
