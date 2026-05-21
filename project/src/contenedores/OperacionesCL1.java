package contenedores;
public interface OperacionesCL1<T>{
  void meter(T elemento);
  T sacar();
  void limpiar();
  boolean estaVacia();
}