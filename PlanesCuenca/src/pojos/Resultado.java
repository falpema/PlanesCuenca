package pojos;

public class Resultado {

    public String nombre;
    public String direccion;
    public String precio;
    


	public Resultado(String nombre, String direccion, String precio) {
		super();
		this.nombre = nombre;
		this.direccion = direccion;
		this.precio = precio;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getPrecio() {
		return precio;
	}

	public void setPrecio(String precio) {
		this.precio = precio;
	}
    
    
    
	
}
