package planesCuenca;
import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;



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
	
	public void CargarRdfRestaurantes(){
		
		 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Cargar de Datos correcta."));
	}

}
