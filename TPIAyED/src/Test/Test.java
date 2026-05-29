package Test;
import CargaDatos.*;
import Grafos.*;
public class Test {

	public static void main(String[] args) throws Exception {
		LectorJson lector = new LectorJson("D:/Santi/Materias 2do/AyED/TPI/centroSalta.json");
		DatosMapa datos;
		datos = lector.generarDatosMapa();
		GrafoMapa mapa = new GrafoMapa(datos.getCantidadVertices(), datos);
		mapa.cargarGrafo();
	}

}
