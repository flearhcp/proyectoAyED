package recursos;

// es para terminar de especificar las operaciones de una lista comun
public interface OperacionesCL3<T> {	
	public void insertar(T elemento, int posicion);
	public void reemplazar(T elemento, int posicion);
}
