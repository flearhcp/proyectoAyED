package Test;

import LectorJSON.*;
import Grafos.*;
import motor_matching_engine.*;

public class TestMotor {
    public static void main(String[] args) {
        try {
            // La ruta al archivo JSON es relativa a la raíz de tu proyecto.
            // Si tu proyecto está en "D:\AYED_2026\practico_Final\project",
            // el lector buscará el archivo en "D:\AYED_2026\practico_Final\project\practico_Final\export.json".
            // Asegúrate de que la carpeta y el archivo existan en esa ubicación.
            LectorJson mapa = new LectorJson("practico_Final\\export.json");
            System.out.println("Archivo JSON cargado exitosamente.");
            
            // Aquí puedes continuar con la lógica para usar el objeto 'mapa'
        } catch (Exception e) {
            System.err.println("Error al leer el archivo JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
