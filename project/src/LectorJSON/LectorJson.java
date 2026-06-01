package LectorJSON;

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
        //Actualizaciones: Con esta actualizacion los nodos se cargan sin que se repita las calles.
        JSONArray elements = json.getJSONArray("elements");
        Map<Long, Coordenada> coordenadasNodos1 = new HashMap<>();
        List<JSONObject> listaWays = new ArrayList<>();

        for (int i = 0; i < elements.length(); i++) {
            JSONObject obj = elements.getJSONObject(i);
            String type = obj.getString("type");
            
            if (type.equals("node")) {
                long nodeId = obj.getLong("id");
                double latitud = obj.getDouble("lat");
                double longitud = obj.getDouble("lon");
                coordenadasNodos1.put(nodeId, new Coordenada(latitud, longitud)); // Guardamos el nodo completo
            } else if (type.equals("way") && obj.has("tags")) {
                JSONObject tags = obj.getJSONObject("tags");
                if (tags.has("name")) {
                    listaWays.add(obj);
                }
            }
        }

        // 2. UNIFICAR TRAMOS POR NOMBRE
      /*   Map<String, List<JSONObject>> callesAgrupadas = new HashMap<>();
        for (JSONObject way : listaWays) {
            String nombre = way.getJSONObject("tags").getString("name");
            callesAgrupadas.computeIfAbsent(nombre, k -> new ArrayList<>()).add(way);
        } */

        // Reconstruimos el JSONArray de elementos pero ya unificados
       /*  JSONArray elements = new JSONArray();

        callesAgrupadas.forEach((nombreCalle, tramos) -> {
            JSONObject calleUnificada = new JSONObject();
            try {
                calleUnificada.put("type", "way");
                calleUnificada.put("id", tramos.get(0).getLong("id")); // Mantiene el primer ID
                
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Fusionar nodos secuencialmente evitando duplicados en empalmes
            List<Long> nodosFusionados = new ArrayList<>();
            for (JSONObject tramo : tramos) {
                JSONArray nodesTramo = new JSONArray();
                try {
                    nodesTramo = tramo.getJSONArray("nodes");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int j = 0; j < nodesTramo.length(); j++) {
                    long idNodo;
                    try {
                        idNodo = nodesTramo.getLong(j);
                        if (nodosFusionados.isEmpty() || nodosFusionados.get(nodosFusionados.size() - 1) != idNodo) {
                            nodosFusionados.add(idNodo);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                calleUnificada.put("nodes", new JSONArray(nodosFusionados));
                calleUnificada.put("tags", tramos.get(0).getJSONObject("tags")); // Conserva tags base
                elements.put(calleUnificada);
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }); */

        // CONTAR APARICIONES DE NODOS
        Map<Long, Integer> apariciones = new HashMap<>();
    

        for (JSONObject way : listaWays) {

            JSONArray nodes = way.getJSONArray("nodes");

            for (int i = 0; i < nodes.length(); i++) {
                long nodeId = nodes.getLong(i);
                apariciones.put(nodeId, apariciones.getOrDefault(nodeId, 0) + 1);
            }
                
        }
        System.out.println("Nodos puros encontrados y guardados: " + coordenadasNodos1.size());

        // ASIGNAR INDICES A INTERSECCIONES Y EXTREMOS
        Map<Long, Vertice> vertices = new HashMap<>();
        int contadorVertices = 0;

        for (JSONObject way : listaWays) {

            JSONArray nodes = way.getJSONArray("nodes");

            for (int j = 0; j < nodes.length(); j++) {

                long nodeId = nodes.getLong(j);
                boolean esExtremo = (j == 0 || j == nodes.length() - 1);
                boolean esInterseccion = apariciones.get(nodeId) > 1;  //si el nodo aparece en mas de una calle

                if (esExtremo || esInterseccion) {

                    if (!vertices.containsKey(nodeId)) {
                        Coordenada coord = coordenadasNodos1.getOrDefault(nodeId, Coordenada.INVALIDA);
                        Vertice vertice = new Vertice(contadorVertices++, nodeId, coord);
                        vertices.put(nodeId,vertice);
                    }
                }
            }
            
        }

        // CREAR aristas
        List <Arista> listaAristas = new ArrayList<>();
        Map<String, Calle> diccionarioCalles = new HashMap<>();

        for (JSONObject way : listaWays) {
            JSONObject tags = way.optJSONObject("tags");
            String nombreOriginal = (tags != null) ? tags.optString("name", "Calle sin nombre") : "Calle sin nombre";
            String nombreNorm = normalizarNombre(nombreOriginal);

            boolean oneway = tags.optString("oneway", "no").equals("yes");
            String tipo = tags.optString("highway", "unknown");
            int velocidadKmH =tags.optInt("maxspeed", obtenerVelocidadPorDefecto(tipo)); 
            double velocidadMS = velocidadKmH / 3.6;

            Calle calle = diccionarioCalles.computeIfAbsent(nombreNorm, k -> new Calle(way.optLong("id"), nombreNorm, oneway, velocidadKmH, tipo));
        
            JSONArray nodes = way.getJSONArray("nodes");
            
            long ultimoVertice = -1;

            int indiceUltimoVertice = 0;

            double distanciaAcumulada = 0.0;
            Coordenada coordAnterior = null;
            
            for (int j = 0; j < nodes.length();j++) {
                long actual = nodes.getLong(j);
                Coordenada coordActual = coordenadasNodos1.get(actual);

                if (coordAnterior != null && coordActual != null){
                    distanciaAcumulada += calcularDistanciaHaversine(coordAnterior, coordActual);
                }
                coordAnterior = coordActual;

                boolean esVertice = vertices.containsKey(actual);

                if (!esVertice) {
                    continue;
                }

                // primer vertice del 'way'/tramo
                if (ultimoVertice == -1) {
                    ultimoVertice = actual;
                    distanciaAcumulada = 0.0;
                    continue;
                }
                List<Coordenada> geometriaTramo = new ArrayList<>();
                for (int k = indiceUltimoVertice; k <= j; k++){
                    long idNodo = nodes.getLong(k);
                    geometriaTramo.add(coordenadasNodos1.get(idNodo));
                }
                Vertice verticeU = vertices.get(ultimoVertice);
                Vertice verticeV = vertices.get(actual);

                double tiempoEtaSegundos = distanciaAcumulada / velocidadMS;

                verticeU.agregarCalle(calle);
                verticeV.agregarCalle(calle);
                
                listaAristas.add(new Arista(verticeU, verticeV, calle, distanciaAcumulada, tiempoEtaSegundos, geometriaTramo));
                // doble mano
                if (!oneway) {
                    List<Coordenada> geometriaInvertida = new ArrayList<>(geometriaTramo);
                    Collections.reverse(geometriaInvertida);
                    listaAristas.add(new Arista(verticeV, verticeU, calle, distanciaAcumulada, tiempoEtaSegundos, geometriaInvertida));
                }
                ultimoVertice = actual;
                indiceUltimoVertice = j;
                distanciaAcumulada= 0.0;
            }
        
        }
        List<Vertice> listaVertices = new ArrayList<>(vertices.values());
        listaVertices.sort((v1,v2)-> Integer.compare(v1.getIndice(), v2.getIndice()));
        return new DatosMapa(listaVertices,listaAristas);
    }

    private String normalizarNombre(String nombre) {
    if (nombre == null) return "Desconocida";
    
    // Pasamos a minúsculas y quitamos espacios extra
    String norm = nombre.toLowerCase().trim().replaceAll("\\s+", " ");
    
    // Reemplazos comunes
    norm = norm.replace("av.", "avenida");
    norm = norm.replace("av ", "avenida ");
    norm = norm.replace("gral.", "general");
    norm = norm.replace("pje.", "pasaje");
    
    return norm;
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
