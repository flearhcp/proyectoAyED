package GUI;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;
import Grafos.GrafoMapa;
// Estas importaciones se basan en suposiciones sobre la estructura de tu proyecto.
// Puede que necesites ajustarlas según los nombres y paquetes reales de tus clases.
import LectorJSON.*;
import contenedores.*;
import motor_matching_engine.*;
import recursos.Nodo;
import java.util.List;

public class ControladorGUI implements Initializable{
    @FXML private Canvas canvasMapa;
    @FXML private ListView<String> listaVehiculos;
    @FXML private ListView<String> listaDespacho;
    private GrafoMapa grafo;
    private MotorDespacho motor;
    private double factorZoom = 1.0;
    private double offsetX = 0.0;
    private double offsetY = 0.0;
    private double mouseAnchorX;
    private double mouseAnchorY;

    @Override
    public void initialize(URL location, ResourceBundle resource){
        // Limpia el lienzo. El grafo se dibujará una vez que se establezca a través de setGrafo().
        GraphicsContext gc = canvasMapa.getGraphicsContext2D();
        gc.setFill(Color.web("#1e1e1e"));
        gc.fillRect(0, 0, canvasMapa.getWidth(), canvasMapa.getHeight());
        mostrarVehiculoSimulado();

        //evento de zoom (con rueda de mouse)
        canvasMapa.setOnScroll(event -> {
            double factor = 1.1;
            if (event.getDeltaY() < 0){
                factor = 1/factor; //Zoom Out
            }
            factorZoom *= factor;

            //limitamos el zoom
            if (factorZoom<0.5) factorZoom =0.5;
            if (factorZoom> 20.0) factorZoom = 20.0;

            dibujarGrafo();
        });

        //eventos de paneo
        canvasMapa.setOnMousePressed(event -> {
            mouseAnchorX = event.getX();
            mouseAnchorY = event.getY();
        });

        canvasMapa.setOnMouseDragged(event ->{
            offsetX += (event.getX() -mouseAnchorX);
            offsetY += (event.getY() - mouseAnchorY);

            mouseAnchorX = event.getX();
            mouseAnchorY = event.getY();

            dibujarGrafo();;
        });
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
        pasajero = motor.usuarioRandom();
        flota = motor.generaFlota();
        asignado = motor.despacharViaje(pasajero, flota);
        
        listaDespacho.getItems().clear();
        
        if (asignado != null) {
            listaDespacho.getItems().add("1. Vehiculo: " + asignado.getID() + " - ETA: " + String.format("%.2f", asignado.getETA()) + " (Asignado)");

            ColaPrioridad<Vehiculo> cola = motor.getColaDePrioridad();
            int i = 2;
            while (!cola.estaVacia()) {
                Vehiculo v = cola.sacar();
                listaDespacho.getItems().add(i + ". Vehiculo: " + v.getID() + " - ETA: " + String.format("%.2f", v.getETA()));
                i++;
            }
        }
    }
    @FXML
    private void handleLimpiarSeleccion(){
        listaDespacho.getItems().clear();
    }
    public void setGrafo(GrafoMapa grafo){
        this.grafo = grafo;
    }

    public void dibujarGrafo() {
        if (this.grafo == null) {
            System.err.println("El grafo es nulo, no se puede dibujar.");
            return;
        }

        GraphicsContext gc = canvasMapa.getGraphicsContext2D();
        // Limpiar el lienzo con el color de fondo
        gc.setFill(Color.web("#1e1e1e"));
        gc.fillRect(0, 0, canvasMapa.getWidth(), canvasMapa.getHeight());

        /*  --- ASUNCIONES SOBRE TU CÓDIGO ---
         El siguiente código asume que tus clases tienen ciertos métodos.
         Si no existen, necesitarás implementarlos en las clases correspondientes.
         1. GrafoMapa tiene un método para obtener el objeto DatosMapa: getDatosMapa()
         2. DatosMapa tiene métodos para obtener los límites del mapa: getMinLat(), getMaxLat(), getMinLon(), getMaxLon()
         3. DatosMapa puede obtener un Nodo por su índice: getNodoPorIndice(int indice)
         4. La clase Nodo tiene métodos para obtener latitud y longitud: getLat(), getLon()
         5. GrafoMapa puede darte la lista de adyacencia de un vértice: getAdyacentes(int vertice)
         6. La lista de adyacencia contiene objetos Arco con el índice del vértice destino: getDestino()
 */
        DatosMapa datos = grafo.getDatosMapa(); // Asunción 1
        if (datos == null) {
            System.err.println("DatosMapa es nulo, no se puede dibujar el grafo.");
            return;
        }

        double minLat = datos.getMinLat(); // Asunción 2
        double maxLat = datos.getMaxLat();
        double minLon = datos.getMinLon();
        double maxLon = datos.getMaxLon();

        // Dibujar aristas
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(1.0);

        // Dibujar aristas (Conectando los vértices usando la lista real de Aristas)

    // Recorremos la lista de aristas que cargó el LectorJSON en DatosMapa
        for (Arista arista : datos.getAristas()) {
            List<Coordenada> geometria = arista.getGeometria();
          /*   Coordenada medio = geometria.get(geometria.size()/2); //nombre de calles
            double xTexto = scale(medio.getLongitud(), minLon,maxLon,0,canvasMapa.getWidth());
            double yTexto = canvasMapa.getHeight() - scale(medio.getLatitud(), minLat, maxLat, 0, canvasMapa.getHeight());
            gc.setFill(Color.WHITE);
            gc.setFont(new Font("Arial",10));
            gc.fillText(arista.getCalle().getNombre(), xTexto, yTexto);
 */
            if (geometria != null && !geometria.isEmpty()){
                gc.beginPath(); //comienza nuevo trazo para esta arista
                Coordenada inicio = geometria.get(0);
                gc.moveTo(scale(inicio.getLongitud(), minLon, maxLon, 0, canvasMapa.getWidth(), true),scale(inicio.getLatitud(),maxLat,minLat,0, canvasMapa.getHeight(), false));
                
                for (int i=1; i<geometria.size(); i++){
                    Coordenada c = geometria.get(i);
                    double px = scale(c.getLongitud(),minLon,maxLon,0,canvasMapa.getWidth(),true);
                    double py = scale(c.getLatitud(),maxLat, minLat,  0, canvasMapa.getHeight(), false);
                    gc.lineTo(px,py);
                }
                gc.stroke();
            }
            
            // Buscamos los vértices correspondientes en la lista de vértices
           /*  Vertice origen = arista.getOrigen();
            Vertice destino = arista.getDestino();
            
            if (origen != null && destino != null) {
                // Calculamos las coordenadas en píxeles para la pantalla
                double x1 = scale(origen.getLongitud(), minLon, maxLon, 0, canvasMapa.getWidth());
                double y1 = canvasMapa.getHeight() -  scale(origen.getLatitud(),  minLat,maxLat, 0, canvasMapa.getHeight());
                
                double x2 = scale(destino.getLongitud(), minLon, maxLon, 0, canvasMapa.getWidth());
                double y2 = canvasMapa.getHeight() - scale(destino.getLatitud(),  minLat,maxLat, 0, canvasMapa.getHeight());
                
                // Dibujamos la línea de la calle/ruta en el mapa
                gc.strokeLine(x1, y1, x2, y2);
            } */
        }
     
        // Dibujar nodos (Puntos/Intersecciones en el mapa)
        gc.setFill(Color.DODGERBLUE); // O el color que prefieras que use el mapa

        for (int i = 0; i < datos.getVertices().size(); i++) {
            Vertice vertice = datos.getVertices().get(i);
            
            if (vertice != null) {
                // Convertimos la latitud y longitud a pixeles en el canvas
                double x = scale(vertice.getLongitud(), minLon, maxLon, 0, canvasMapa.getWidth(),true);
                double y = scale(vertice.getLatitud(), maxLat, minLat, 0, canvasMapa.getHeight(),false);
                
                // Dibujamos un círculo pequeño (de radio 3, por ejemplo) en cada vértice
                double radio = 3.0;
                gc.fillOval(x - radio, y - radio, radio * 2, radio * 2);
            }
        }
        
    }
     /*   for (int i = 0; i < grafo.getOrden(); i++) {
            Vertice origen = datos.getNodoPorIndice(i); // Asunción 3 y 4
            if (origen == null) continue;

            double x1 = scale(origen.getLongitud(), minLon, maxLon, 0, canvasMapa.getWidth());
            double y1 = scale(origen.getLatitud(), maxLat, minLat, 0, canvasMapa.getHeight()); // Se invierte Latitud para el eje Y

            ListaDoubleLinkedL adyacentes = grafo.getAdyacentes(i); // Asunción 5
            if (adyacentes == null) continue;

            for (int j = 0; j < adyacentes.tamanio(); j++) {
                Arco arco = (Arco) adyacentes.devolver(j); // Asunción 6
                Nodo destino = datos.getNodoPorIndice(arco.getDestino());
                if (destino == null) continue;

                double x2 = scale(destino.getLon(), minLon, maxLon, 0, canvasMapa.getWidth());
                double y2 = scale(destino.getLat(), maxLat, minLat, 0, canvasMapa.getHeight());

                gc.strokeLine(x1, y1, x2, y2);
            }
        } */

       /*  // Dibujar nodos
        gc.setFill(Color.DODGERBLUE);
        for (int i = 0; i < grafo.getOrden(); i++) {
            Nodo nodo = datos.getNodoPorIndice(i); // Asunción 3 y 4
            if (nodo == null) continue;

            double x = scale(nodo.getLon(), minLon, maxLon, 0, canvasMapa.getWidth());
            double y = scale(nodo.getLat(), maxLat, minLat, 0, canvasMapa.getHeight());
            gc.fillOval(x - 1.5, y - 1.5, 3, 3);
        }
    } */


    private double scale(double value, double dataMin, double dataMax, double pixelMin, double pixelMax, boolean esEjeX) {
        double padding = 20.0;
        double valorEscalado = (pixelMin + padding) + ((value - dataMin) * (pixelMax - pixelMin - (2 * padding))) / (dataMax - dataMin);
        double centro = esEjeX ? canvasMapa.getWidth() / 2.0 : canvasMapa.getHeight() / 2.0;
        if (esEjeX){
            return (valorEscalado -centro) * factorZoom + centro + offsetX;
        }else {
            return (valorEscalado - centro) * factorZoom + centro + offsetY;
        }
        
    }
}
