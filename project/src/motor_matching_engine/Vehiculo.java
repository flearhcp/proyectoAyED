package motor_matching_engine;

public class Vehiculo implements Comparable<Vehiculo> {
    private int ID; //Puede ser patente
    private long verticeIDOrigen;
    private double ETA;

    public Vehiculo(int ID, long verticeOrigen){
        this.ID = ID;
        this.verticeIDOrigen = verticeOrigen;
        this.ETA = 0.0;
    }
    public void setActualEta(double ETA){
        this.ETA = ETA;
    }
    @Override
    public int compareTo(Vehiculo otro){
        return Double.compare(this.ETA, otro.ETA);
    }
    public int getID() {
        return ID;
    }
    public long getVerticeIDOrigen() {
        return verticeIDOrigen;
    }
    public double getETA() {
        return ETA;
    }
    
}
