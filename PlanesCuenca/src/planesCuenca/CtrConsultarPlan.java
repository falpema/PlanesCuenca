package planesCuenca;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.ModelFactory;
import org.primefaces.context.RequestContext;

import cargaCSV.GoogleMapView;
import cargaCSV.cargaCSVtoRDF;
import pojos.Resultado;

/**
* query to sparql this ontology
* @author FABIAN PENALOZA mail falpema@gmail.com
*/
@ManagedBean(eager=true)
@ApplicationScoped
public class CtrConsultarPlan {
	public   List<Resultado> resultado;
	
	private Double presupuesto;

	private Double latitud;
	
	private Double longitud;
	
	private String openSparql  = "prefix ns:<http://www.semanticweb.org/usuario/ontologies/2019/2/ruta#>"+
            "prefix rdfs:<http://www.w3.org/2000/01/rdf-schema#>"+
            "prefix rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
            "SELECT * { { " +
            "select ?precio ?longitud ?latitud "+ //?latitud 
            "where { "
            + "?dataP rdf:type ns:Discoteca . "
            + "?dataP ns:nombre ?precio .  "
           // + "?dataP ns:latitud ?latitud FILTER (?latitud > "+(latitud - 0.05)+" && ?latitud <= "+(latitud + 0.05)+" ) ."
           // + "?dataP ns:longitud ?longitud FILTER (?longitud >= "+(longitud - 0.006)+" && ?longitud <= "+(longitud + 0.006)+" ) ."
           		 + "?dataP ns:longitud ?longitud   ."
           		 + "?dataP ns:latitud ?latitud   ."
            + " }  } " 
			 + " UNION { "
		    + "select ?precio ?longitud ?latitud  "+  //?latitud 
            "where { "
            + "?dataP rdf:type ns:Restaurant . "
            + "?dataP ns:nombre ?precio .  "
            //+ "?dataP ns:latitud ?latitud FILTER (?latitud > "+(latitud - 0.05)+" && ?latitud <= "+(latitud + 0.05)+" ) ."
          //  + "?dataP ns:longitud ?longitud FILTER (?longitud >= "+(longitud - 0.006)+" && ?longitud <= "+(longitud + 0.006)+" ) ."
       	 + "?dataP ns:longitud ?longitud   ."
       	+ "?dataP ns:latitud ?latitud   ."
            + "} } } ";
	private String respOpenSparql = "" ;
	
	
	public String getOpenSparql() {
		return openSparql;
	}


	public void setOpenSparql(String openSparql) {
		this.openSparql = openSparql;
	}


	public String getRespOpenSparql() {
		return respOpenSparql;
	}


	public void setRespOpenSparql(String respOpenSparql) {
		this.respOpenSparql = respOpenSparql;
	}

	

	public List<Resultado> getResultado() {
		  
		return resultado;
	}


	public void setResultado(List<Resultado> resultado) {
		this.resultado = resultado;
	}
	
	public  void consultarPlan(	){
		longitud = -79.00;
    	latitud = -2.92;
        
        System.out.println("Los datos para genera el plan son :"+latitud+longitud+presupuesto);
		consultarPlan(presupuesto, latitud, longitud);
	}

	public  void consultarPlan(	Double presupuesto,Double latitud, Double longitud){
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
		             "select ?nombre ?precio ?longitud ?latitud  "+ //
		             "where { "
		             + "?dataP rdf:type ns:Discoteca . "
		             + "?dataP ns:nombre ?nombre .  "
		             + "?dataP ns:precio ?precio .  "
		             + "?dataP ns:latitud ?latitud FILTER (?latitud > "+(latitud - 0.05)+" && ?latitud <= "+(latitud + 0.05)+" ) ."
		             + "?dataP ns:longitud ?longitud FILTER (?longitud >= "+(longitud - 0.006)+" && ?longitud <= "+(longitud + 0.006)+" ) ."
		            //		 + "?dataP ns:longitud ?longitud   ."
		             + " }  } " 
					 + " UNION { "
				    + "select ?nombre ?precio ?longitud ?latitud "+  // 
		             "where { "
		             + "?dataP rdf:type ns:Restaurant . "
		             + "?dataP ns:nombre ?nombre .  "
		             + "?dataP ns:precio ?precio .  "
		             + "?dataP ns:latitud ?latitud FILTER (?latitud > "+(latitud - 0.05)+" && ?latitud <= "+(latitud + 0.05)+" ) ."
		             + "?dataP ns:longitud ?longitud FILTER (?longitud >= "+(longitud - 0.006)+" && ?longitud <= "+(longitud + 0.006)+" ) ."
		        	 //+ "?dataP ns:longitud ?longitud   ."
		             + "} } } ";	
		        
			System.out.println(SparQlIndividual);
			
			resultado = new java.util.ArrayList<>() ;
			
			Query query = QueryFactory.create(SparQlIndividual);		 
			// Ejecutar la consulta y obtener los resultados
			QueryExecution qe = QueryExecutionFactory.create(query, model);		 
			try {
			   ResultSet results = qe.execSelect();
	
			   while (results.hasNext()) {
				  QuerySolution qs = results.next();
			   Resultado result=new Resultado(qs.getLiteral("nombre").toString(),qs.getLiteral("latitud").toString()+","+qs.getLiteral("longitud").toString(),qs.getLiteral("precio").toString());
			   resultado.add(result);
			   }
			 //  ResultSetFormatter.out(System.out, qe.execSelect(), query) ;
			} finally { qe.close() ; }
		System.out.println("Resultados Encontrados " +resultado.size());
		//FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se consulto correctamente el plan."));
			
		//RequestContext requestContext = RequestContext.getCurrentInstance();
		//requestContext.update("forubic:listado");
		//requestContext.execute("window.open('resultadoBusqUbic.jsf')");
		//RequestContext.getCurrentInstance().execute("window.open('resultadoBusqUbic.jsf')");
	}
	
	
	/**
	 * Consultas libres desde sparql
	 */
	public  void consultarSparql(){
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
				

			        
				System.out.println(openSparql);
				

				
				Query query = QueryFactory.create(openSparql);		 
				// Ejecutar la consulta y obtener los resultados
				QueryExecution qe = QueryExecutionFactory.create(query, model);		 
				try {
				   ResultSet results = qe.execSelect();
				 
				  // OutputStream oi = System.out;
				   respOpenSparql = ResultSetFormatter.asText(results);
				   ResultSetFormatter.out(System.out, results, query) ;
			//	ResultSetFormatter.out(oi, results, query) ;
				//respOpenSparql=oi.toString();
				   
				} finally { qe.close() ; }
			
			//FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se consulto correctamente el plan."));
			

		}

}
