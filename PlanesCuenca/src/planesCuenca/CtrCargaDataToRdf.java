package planesCuenca;
import javax.faces.bean.*;



/**
*
* @author FABIAN PENALOZA
*/
@ManagedBean
@ViewScoped
public class CtrCargaDataToRdf {
	
	private String titulo = "Json TO Rdf";

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

}
