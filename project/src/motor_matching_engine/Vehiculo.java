package motor_matching_engine;

import java.util.Random;

public class Vehiculo implements Comparable<Vehiculo> {
    private String ID; //Puede ser patente
    private long IDVertice;
    private int verticeIndiceOrigen;
    private double ETA;

    public Vehiculo(int ID, int posOrigen){
        this.ID = setID();
        this.verticeIndiceOrigen = posOrigen;
        this.ETA = 0.0;
    }
    public void setActualEta(double ETA){
        this.ETA = ETA;
    }
    @Override
    public int compareTo(Vehiculo otro){
        return Double.compare(this.ETA, otro.ETA);
    }
    public String getID() {
        return ID;
    }
    public long getIDvertice(){
        return IDVertice;
    }
    public int getVerticeIndiceOrigen() {
        return verticeIndiceOrigen;
    }
    public double getETA() {
        return ETA;
    }
    private String setID(){
        StringBuilder pat; Random ran; int numran; char letter;
        pat = new StringBuilder();
        ran = new Random();
        for (int i = 0; i < 3; i++) {
            letter = (char)('A'+ran.nextInt(25));
            pat.append(letter);
        }
        pat.append(" ");
        for (int i = 0; i < 3; i++) {
            numran = ran.nextInt(10);
            pat.append(numran);
        }
        return pat.toString();
    }
}
