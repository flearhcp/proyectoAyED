package Test;

import LectorJSON.*;
import GUI.ControladorGUI;
import Grafos.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestGUI extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        String ruta = "Archivos Muestra Salta - Centro 5 Cuadras --20260518\\centroSalta.json";
        LectorJson lector = new LectorJson(ruta);
        DatosMapa datos = lector.generarDatosMapa();
        GrafoMapa grafo = new GrafoMapa(datos.getCantidadVertices(), datos);
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/ventana.fxml"));
        Parent root = loader.load(); 
        
        ControladorGUI controlador = loader.getController();
        controlador.setGrafo(grafo);
        
        primaryStage.setTitle("Sistema de despacho ETA - Salta MacroCentro");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        controlador.dibujarGrafo();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
