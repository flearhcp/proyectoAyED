package CargaDatos;

import org.json.JSONArray;
import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
//notas: debo cambiar todas las arrayList por las clases de listas enlazadas de la catedra y cargar todas las coordenadas de las calles para dibujar el mapa
public class LectorJson {

    private JSONObject json;

    public LectorJson(String ruta) throws Exception {

        String contenido = new String(Files.readAllBytes(Paths.get(ruta)));
        json = new JSONObject(contenido);
    }

    public DatosMapa generarDatosMapa() throws Exception {

        JSONArray elements = json.getJSONArray("elements");

        // CONTAR APARICIONES DE NODOS
        Map<Long, Integer> apariciones = new HashMap<>();
        Map<Long,Coordenada> coordenadasNodos = new HashMap<>();

        for (int i = 0; i < elements.length(); i++) {
            JSONObject obj = elements.getJSONObject(i);

            if (obj.getString("type").equals("way")) {
                JSONArray nodes = obj.getJSONArray("nodes");

                for (int j = 0; j < nodes.length(); j++) {
                    long nodeId = nodes.getLong(j);
                    apariciones.put(nodeId, apariciones.getOrDefault(nodeId, 0) + 1);
                }
            }
            else if (obj.getString("type").equals("node")){
                long nodeId = obj.getLong("id");
                double latitud = obj.getDouble("lat");
                double longitud = obj.getDouble("lon");
                coordenadasNodos.put(nodeId, new Coordenada(latitud,longitud));
            }
        }

        // ASIGNAR INDICES A INTERSECCIONES Y EXTREMOS
        Map<Long, Vertice> vertices = new HashMap<>();
        int contadorVertices = 0;

        for (int i = 0; i < elements.length(); i++) {
            JSONObject obj = elements.getJSONObject(i);

            if (obj.getString("type").equals("way")) {
                JSONArray nodes = obj.getJSONArray("nodes");

                for (int j = 0; j < nodes.length(); j++) {

                    long nodeId = nodes.getLong(j);
                    boolean esExtremo = (j == 0 || j == nodes.length() - 1);
                    boolean esInterseccion = apariciones.get(nodeId) > 1;  //si el nodo aparece en mas de una calle

                    if (esExtremo || esInterseccion) {

                        if (!vertices.containsKey(nodeId)) {
                            Coordenada coord = coordenadasNodos.getOrDefault(nodeId,Coordenada.INVALIDA);
                            Vertice vertice = new Vertice(contadorVertices++, nodeId, coord);
                            vertices.put(nodeId,vertice);
                        }
                    }
                }
            }
        }

     // CREAR aristas
        List <Arista> listaAristas = new ArrayList<>();

        for (int i = 0; i < elements.length(); i++) {
            JSONObject obj = elements.getJSONObject(i);

            if (obj.getString("type").equals("way")) {
                JSONArray nodes = obj.getJSONArray("nodes");
                JSONObject tags = obj.optJSONObject("tags");
                
                // Si la calle no tiene etiquetas, la saltamos para evitar errores
                if (tags == null) continue; 

                boolean oneway = tags.optString("oneway", "no").equals("yes");
                String nombre = tags.optString("name", "S/N");
                String tipo = tags.optString("highway", "unknown");
                
                // 1. OBTENER LA VELOCIDAD: Buscamos "maxspeed". Si no está, usamos nuestro método auxiliar.
                int velocidadKmH = tags.optInt("maxspeed", obtenerVelocidadPorDefecto(tipo));
                
                // Convertimos la velocidad de Km/h a Metros por Segundo (m/s) dividiendo por 3.6
                double velocidadMS = velocidadKmH / 3.6;

                Calle calle = new Calle(obj.getLong("id"), nombre, oneway, velocidadKmH, tipo);

                long ultimoVertice = -1;
                
                // Variables para calcular la distancia acumulada de la cuadra
                double distanciaAcumulada = 0.0;
                Coordenada coordAnterior = null;

                for (int j = 0; j < nodes.length(); j++) {
                    long actual = nodes.getLong(j);
                    Coordenada coordActual = coordenadasNodos.get(actual);

                    // 2. ACUMULAR DISTANCIA: Sumamos la distancia desde el nodo anterior al actual
                    // Esto incluye los "nodos curvos" del medio para que la longitud sea perfecta.
                    if (coordAnterior != null && coordActual != null) {
                        distanciaAcumulada += calcularDistanciaHaversine(coordAnterior, coordActual);
                    }
                    // Avanzamos el puntero de coordenadas
                    coordAnterior = coordActual;

                    boolean esVertice = vertices.containsKey(actual);

                    if (!esVertice) {
                        continue; // Si no es una esquina, pasamos al siguiente nodo de la cuadra
                    }

                    // Si es el primer vértice de la calle
                    if (ultimoVertice == -1) {
                        ultimoVertice = actual;
                        distanciaAcumulada = 0.0; // Reseteamos la distancia desde esta esquina inicial
                        continue;
                    }

                    // ¡LLEGAMOS A OTRA ESQUINA! Tenemos un segmento completo.
                    Vertice verticeU = vertices.get(ultimoVertice);
                    Vertice verticeV = vertices.get(actual);

                    // 3. CALCULAR EL PESO (ETA)
                    // FÍSICA: Tiempo (segundos) = Distancia (metros) / Velocidad (m/s)
                    double tiempoEtaSegundos = distanciaAcumulada / velocidadMS;

                    verticeU.agregarCalle(calle);
                    verticeV.agregarCalle(calle);
                    
                    // 4. GUARDAR LA ARISTA CON SUS NUEVOS VALORES CALCULADOS
                    listaAristas.add(new Arista(verticeU, verticeV, calle, distanciaAcumulada, tiempoEtaSegundos));
                    
                    // Si es doble mano, agregamos el viaje de vuelta (con el mismo peso/tiempo)
                    if (!oneway) {
                        listaAristas.add(new Arista(verticeV, verticeU, calle, distanciaAcumulada, tiempoEtaSegundos));
                    }
                    
                    // Nos preparamos para la siguiente cuadra de la misma calle
                    ultimoVertice = actual;
                    distanciaAcumulada = 0.0; // Reseteamos la distancia para empezar la próxima cuadra
                }
            }
        }
        List<Vertice> listaVertices = new ArrayList<>(vertices.values());
        listaVertices.sort((v1,v2)-> Integer.compare(v1.getIndice(), v2.getIndice()));
        return new DatosMapa(listaVertices,listaAristas);
        
    }
    private double calcularDistanciaHaversine(Coordenada c1, Coordenada c2) {
        if (!c1.esValida() || !c2.esValida()) return 0.0;

        final int R = 6371000; // Radio de la Tierra en metros
        double lat1 = Math.toRadians(c1.getLatitud());
        double lon1 = Math.toRadians(c1.getLongitud());
        double lat2 = Math.toRadians(c2.getLatitud());
        double lon2 = Math.toRadians(c2.getLongitud());

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(lat1) * Math.cos(lat2) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Devuelve la distancia en metros
    }
    private int obtenerVelocidadPorDefecto(String tipoHighway) {
        switch (tipoHighway) {
            case "primary": return 60;    // Avenidas principales
            case "secondary": return 40;  // Calles secundarias / Avenidas menores
            case "tertiary": return 40;   // Calles colectoras
            case "living_street": return 20; // Zonas de convivencia (ej. peatonales compartidas)
            case "residential": return 40;// Calles de barrio (por defecto en Salta urbana suele ser 40)
            default: return 30;           // Ante la duda, una velocidad intermedia conservadora
        }
    }
}
    





