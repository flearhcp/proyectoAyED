package Usuario;

public class UsuarioPasajero extends Usuario{
	private String destino;
	public UsuarioPasajero(String nombre, String ubicacion) {
		super(nombre, ubicacion);
		this.destino = null;
	}
	public void setDestino(String destino) {
		this.destino = destino;
	}
	public String getDestino() {
		return this.destino;
	}
}
