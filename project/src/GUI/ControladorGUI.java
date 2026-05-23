package GUI;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import motor_matching_engine.Vehiculo;

import java.net.URL;
import java.util.ResourceBundle;

import contenedores.ColaPrioridad;

public class ControladorGUI implements Initializable{
    @FXML private Canvas canvasMapa;
    @FXML private ListView<String> listaVehiculos;
    @FXML private ListView<String> listaDespacho;

    @Override
    public void initialize(URL location, ResourceBundle resource){
        dibujarMapaSimulado();
        mostrarVehiculoSimulado();
    }
    private void dibujarMapaSimulado(){
        GraphicsContext gc = canvasMapa.getGraphicsContext2D();
        gc.setFill(Color.web("#232323"));
        gc.fillRect(0, 0, canvasMapa.getWidth(), canvasMapa.getHeight());
        gc.setFill(Color.CHARTREUSE);
        gc.fillOval(100, 100, 15, 15);
        gc.fillOval(300, 100, 15, 15);
        gc.fillOval(200, 300, 15, 15);
        
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        gc.strokeLine(107, 107, 307, 107);
    }
    private void mostrarVehiculoSimulado(){
        listaVehiculos.getItems().addAll("Taxi 1 - Nodo 392350321", "Taxi 2 - Nodo 392350333", "Taxi 3 - Nodo 1288451");
    }
    @FXML
    private void handleNuevaSolicitud(){
        ColaPrioridad<Vehiculo> colaDespacho = new ColaPrioridad<>();

        Vehiculo t1 = new Vehiculo(1, 100); t1.setActualEta(8.5);
        Vehiculo t2 = new Vehiculo(2, 200); t2.setActualEta(3.2);
        Vehiculo t3 = new Vehiculo(3, 300); t3.setActualEta(5.4);

        colaDespacho.meter(t1);
        colaDespacho.meter(t2);
        colaDespacho.meter(t3);

        listaDespacho.getItems().clear();
        while(!colaDespacho.estaVacia()){
            Vehiculo prox = colaDespacho.sacar();
            listaDespacho.getItems().add("Vehiculo "+ prox.getID() + " | ETA: "+ prox.getETA() +" min");

        }
    }
    @FXML
    private void handleLimpiarSeleccion(){
        listaDespacho.getItems().clear();
    }
}
