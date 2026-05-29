package Uber;
import Grafos.GrafoMapa;
import Usuario.*;
import Contenedores.ColaSLinkedList;
public class Uber {
	private GrafoMapa mapa;
	private ColaSLinkedList autos, usuarios;
	private int cantAutos, cantUsuarios;
	public Uber(GrafoMapa mapa, int usuarios, int cantautos) {
		this.mapa = mapa;
		this.cantUsuarios = usuarios;
		this.cantAutos = cantautos;
		
	}
	private void generarVehiculos(int cant) {
		for(int i = 0; i < cant; i++) {
			
		}
	}
}
