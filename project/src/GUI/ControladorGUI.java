package GUI;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import motor_matching_engine.*;

import java.net.URL;
import java.util.ResourceBundle;

import Grafos.GrafoMapa;
import contenedores.ListaDoubleLinkedL;

public class ControladorGUI implements Initializable{
    @FXML private Canvas canvasMapa;
    @FXML private ListView<String> listaVehiculos;
    @FXML private ListView<String> listaDespacho;
    private GrafoMapa grafo;
    private MotorDespacho motor;

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
        Usuario pasajero;
        ListaDoubleLinkedL flota = new ListaDoubleLinkedL();
        Vehiculo asignado;
        this.motor = new MotorDespacho(grafo);
        pasajero = motor.usuarioRandom(grafo);
        flota = motor.generaFlota(grafo);
        asignado = motor.despacharViaje(pasajero, flota);
        
        if(asignado != null){
            listaDespacho.getItems().add("Viaje asignado auto: "+ asignado.getID());
        }
        
        listaDespacho.getItems().clear();
        
    }
    @FXML
    private void handleLimpiarSeleccion(){
        listaDespacho.getItems().clear();
    }
    public void setGrafo(GrafoMapa grafo){
        this.grafo = grafo;
    }
}
