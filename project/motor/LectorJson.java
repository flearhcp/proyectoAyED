package project.motor;

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
                boolean oneway = tags.optString("oneway", "no").equals("yes");
                String nombre = tags.optString("name", "S/N");
                String tipo = tags.optString("highway", "unknown");
                int velocidad =tags.optInt("maxspeed", 40);
                Calle calle =new Calle(obj.getLong("id"), nombre, oneway,velocidad,tipo);


                long ultimoVertice = -1;
                for (int j = 0; j < nodes.length();j++) {
                    long actual = nodes.getLong(j);
                    boolean esVertice = vertices.containsKey(actual);

                    if (!esVertice) {
                        continue;
                    }

                    // primer vertice del 'way'/tramo
                    if (ultimoVertice == -1) {
                        ultimoVertice = actual;
                        continue;
                    }
                    Vertice verticeU = vertices.get(ultimoVertice);
                    Vertice verticeV = vertices.get(actual);

                    verticeU.agregarCalle(calle);
                    verticeV.agregarCalle(calle);
                    
                    listaAristas.add(new Arista(verticeU, verticeV, calle));
                    // doble mano
                    if (!oneway) {
                        listaAristas.add(new Arista(verticeV, verticeU, calle));
                    }
                    ultimoVertice = actual;
                }
            }
        }
        List<Vertice> listaVertices = new ArrayList<>(vertices.values());
        listaVertices.sort((v1,v2)-> Integer.compare(v1.getIndice(), v2.getIndice()));
        return new DatosMapa(listaVertices,listaAristas);
    }
}
    





