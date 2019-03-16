package pojos;

public class Resultado {

    public String nombre;
    public String direccion;
    public Double precio;
    public  Double latitud;
	
	public  Double longitud;
    

    
    
    
	public Resultado(String nombre, Double precio) {
		super();
		this.nombre = nombre;
		this.precio = precio;
	}

	public Resultado(String nombre, String direccion, Double precio) {
		super();
		this.nombre = nombre;
		this.direccion = direccion;
		this.precio = precio;
	}
	
	

	public Resultado(String nombre, String direccion, Double precio, Double latitud, Double longitud) {
		super();
		this.nombre = nombre;
		this.direccion = direccion;
		this.precio = precio;
		this.latitud = latitud;
		this.longitud = longitud;
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

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public Double getLatitud() {
		return latitud;
	}

	public void setLatitud(Double latitud) {
		this.latitud = latitud;
	}

	public Double getLongitud() {
		return longitud;
	}

	public void setLongitud(Double longitud) {
		this.longitud = longitud;
	}
    
    
    
	
}
