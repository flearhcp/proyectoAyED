package Grafos;

import LectorJSON.Arista;
import LectorJSON.DatosMapa;

public class GrafoMapa extends GrafoDirigido{
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
	public double Dijkstra(int vertexOr,int vertexDes){
		double eta;
		this.Dijkstra(vertexOr);
		eta = (double)this.listaDistancia.devolver(vertexDes);
		return eta;
	}
	
}
