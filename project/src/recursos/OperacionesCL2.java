package recursos;

public interface OperacionesCL2<T> {
	
	public int buscar(T elemento);
	public T devolver(int posicion);
	public void eliminar(int posicion);
	public void limpiar();
	public boolean estaVacia();		
	public int tamanio();
	
}
