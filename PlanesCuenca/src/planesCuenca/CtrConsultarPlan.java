package planesCuenca;

import java.math.BigDecimal;
import java.net.URISyntaxException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.ModelFactory;

import cargaCSV.cargaCSVtoRDF;

/**
* query to sparql this ontology
* @author FABIAN PENALOZA mail falpema@gmail.com
*/
@ManagedBean
@ViewScoped
public class CtrConsultarPlan {
	
	public static void consultarPlan(	BigDecimal presupuesto,Double latitud, Double longitud){
	 String userdir = "";
		try {	
			userdir = cargaCSVtoRDF.class.getResource("/datosRestaurantesCuenca.geojson").toURI().getPath().substring(0, cargaCSVtoRDF.class.getResource("/datosRestaurantesCuenca.geojson").toURI().getPath().lastIndexOf("/"));
		} catch (URISyntaxException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
			
			OntModel model;
			
			model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);	 
			model.read(userdir+ "/ontologia_general_cargada.owl","RDF/XML"); 
			
			String SparQlIndividual=
		    	     "prefix ns:<http://www.semanticweb.org/usuario/ontologies/2019/2/ruta#>"+
		             "prefix rdfs:<http://www.w3.org/2000/01/rdf-schema#>"+
		             "prefix rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
		             "SELECT * { { " +
		             "select ?precio "+
		             "where { "
		             + "?dataP rdf:type ns:Discoteca . "
		             + "?dataP ns:nombre ?precio . }  } " 
					 + " UNION { "
				    + "select ?precio "+
		             "where { "
		             + "?dataP rdf:type ns:Restaurant . "
		             + "?dataP ns:nombre ?precio . } "
			         + "} } ";	
		        
			
			

			
			Query query = QueryFactory.create(SparQlIndividual);		 
			// Ejecutar la consulta y obtener los resultados
			QueryExecution qe = QueryExecutionFactory.create(query, model);		 
			try {
			   ResultSet results = qe.execSelect();
			   ResultSetFormatter.out(System.out, results, query) ;
			} finally { qe.close() ; }
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se consulto correctamente el plan."));

	}

}
