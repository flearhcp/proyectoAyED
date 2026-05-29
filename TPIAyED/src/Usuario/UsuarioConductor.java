package Usuario;

public class UsuarioConductor extends Usuario{
	private boolean estadoLibre;
	public UsuarioConductor(String nombre, String ubicacion) {
		super(nombre, ubicacion);
		this.estadoLibre = true;
	}
	public boolean getEstadoLibre() {
		return this.estadoLibre;
	}
	public void setEstadoLibre(boolean estado) {
		this.estadoLibre = estado;
	}
}
