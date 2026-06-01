package Grafos;

import LectorJSON.Arista;
import LectorJSON.DatosMapa;
import contenedores.ListaDoubleLinkedL;

public class GrafoMapa extends AbsGrafoD{
	protected DatosMapa datos;

	public GrafoMapa(int orden, DatosMapa datos) {
		super(orden);
		this.datos = datos;
		this.inicializarMapa();
	}
	@Override
	public void cargarGrafo(){
		for(Arista arista: this.datos.getAristas()) {
			this.matrizCosto.actualizar(arista.getPeso(), arista.getOrigen().getIndice(), arista.getDestino().getIndice());
		}
	}
	
	private void inicializarMapa() {
		for(int i = 0; i < this.getOrden(); i++) {
			for(int j = 0; j < this.getOrden(); j++) {
				if(!(i == j)) {
					this.matrizCosto.actualizar(infinito, i, j);
				}else {
					this.matrizCosto.actualizar(0., i, j);
				}
			}
		}
	}
	/**
	 * Calcula el costo (ETA) desde un vértice de origen a uno de destino utilizando el algoritmo de Dijkstra.
	 * Este método primero ejecuta el algoritmo completo desde el origen y luego devuelve el costo específico
	 * hacia el destino.
	 * @param vertexOr Vértice de origen.
	 * @param vertexDes Vértice de destino.
	 * @return El costo del camino más corto.
	 */
	public double calcularCostoDijkstra(int vertexOr, int vertexDes){
		this.Dijkstra(vertexOr);
		return (double)this.listaDistancia.devolver(vertexDes);
	}
	/**
	 * Calcula el camino de Dijkstra desde la posición del Vehiculo hasta la posición del Pasajero.
	 * El metodo devuelve una Lista enlazada Doble donde se encuentra el camino a recorrer en el grafo.
	 * @param posVehiculo Vértice de origen.
	 * @param posPasajero Vértice de destino.
	 * @return El camino del costo más corto.
	 */
	public ListaDoubleLinkedL caminoDijkstra(int posVehiculo, int posPasajero){
		ListaDoubleLinkedL camino = new ListaDoubleLinkedL();
		int actual,predecesor;
		this.Dijkstra(posVehiculo);
		actual = posPasajero;
		camino.insertar(actual, 0);
		predecesor = (int)this.listaCamino.devolver(actual);
		while (predecesor != -1) {
			camino.insertar(predecesor, 0);
			actual = predecesor;
			predecesor = (int)this.listaCamino.devolver(actual);
		}
		return camino;		
	}
	public DatosMapa getDatosMapa(){ return this.datos;}
}
