package recursos;

public class NodoDoble<T> {

	private T nodoInfo;
	private NodoDoble<T> prevNodo, nextNodo;
	
	public NodoDoble(T nodoInfo){
		this(nodoInfo,null,null);} 
	
	public NodoDoble(T nodoInfo, NodoDoble<T> nextNodo){
		this(nodoInfo,null,nextNodo);} 
	
	public NodoDoble(T nodoInfo, NodoDoble<T> prevNodo, NodoDoble<T> nextNodo){
		this.nodoInfo=nodoInfo;
		this.prevNodo=prevNodo; this.nextNodo=nextNodo; 
	}
	
	public void setPrevNodo(NodoDoble<T> prevNodo){
		this.prevNodo=prevNodo;
	}
	
	public NodoDoble<T> getPrevNodo(){
		return this.prevNodo; 
	}
	
	public void setNextNodo(NodoDoble<T> nextNodo){
		this.nextNodo=nextNodo;
	}
	
	public NodoDoble<T> getNextNodo(){
		return this.nextNodo; 
	}
	public void setNodoInfo(T nodoInfo){
		this.nodoInfo=nodoInfo; 
	}
	public T getNodoInfo(){
		return this.nodoInfo;
	}

}
