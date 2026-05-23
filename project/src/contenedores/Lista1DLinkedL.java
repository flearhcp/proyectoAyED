package contenedores;
import recursos.*;

// implementando una lista simple
public abstract class Lista1DLinkedL<T> extends Lista0DLinkedL<T> implements OperacionesCL3<T>{
	public void insertar(T elemento, int posicion){
		NodoDoble<T> node;
		if (posicion>tamanio() || posicion<0){
			System.out.println("Error insertar. Posicion inexistente ");
		}else{
			if (posicion==0){ // insercion al comienzo
				if (!estaVacia()){
					this.frenteL=new NodoDoble<T>(elemento, null, this.frenteL);
					this.frenteL.getNextNodo().setPrevNodo(this.frenteL);
				}else{
					this.frenteL=this.finalL=new NodoDoble<T>(elemento);					
				}
			}else{
				if (posicion==tamanio()){ // insercion al fin
					this.finalL = new NodoDoble<T>(elemento, this.finalL, null); // nuevo nodo fin
					this.finalL.getPrevNodo().setNextNodo(this.finalL); // reconexion penultimo nodo al nuevo fin
				}else{
					// insercion al medio					
					NodoDoble<T> prev, next;
					prev=this.frenteL;
					next=this.frenteL.getNextNodo();
					for (int counter=1; counter<posicion;counter++){
						prev=prev.getNextNodo(); next=next.getNextNodo();						
					}
					
					node = new NodoDoble<T>(elemento,prev,next);
					prev.setNextNodo(node); // actualizo referencias
					next.setPrevNodo(node);					
				}
			}			
			this.ultimo++; // incremento "ultima posicion" de lista
		}		
	}
	
	public void reemplazar(T elemento, int posicion){		
		if (estaVacia()){
			System.out.println("Error reemplazar. Lista vacia...");
		} else {
			if (posicion>=tamanio() || posicion<0){
				System.out.println("Error reemplazar. La posicion no existe..");
			}else{
				if (posicion==0){
					this.frenteL.setNodoInfo(elemento);
				}else{
					if (posicion==tamanio()-1){
						this.finalL.setNodoInfo(elemento);
					}else {
						NodoDoble<T> temp;
						temp=this.frenteL;
						
						for (int counter=0; counter<posicion;counter++){						
							temp=temp.getNextNodo();		
						}				
						
						temp.setNodoInfo(elemento);
					}
				}				
			}						
		}		
	}
	
	public abstract boolean iguales(T elementoL, T elemento);
	
	public int buscar(T elemento){		
		int posicion=-1; int contador=0;
		T unElemento;
		NodoDoble<T> temp;
		
		temp=this.frenteL;
		while (temp!=null && posicion==-1){
			unElemento=temp.getNodoInfo();
			if (iguales(unElemento,elemento)){
				posicion=contador;
			}else{
				temp=temp.getNextNodo();
				contador++;
			}
		}				
		return posicion;
	}

}
