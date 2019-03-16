package cargaCSV;



import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

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
import org.apache.jena.rdf.model.RDFNode;
import org.primefaces.context.RequestContext;
import org.primefaces.event.map.PointSelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import planesCuenca.CtrConsultarPlan;
import pojos.Resultado;

@ManagedBean(name="googleMapView")
@ApplicationScoped
	public class GoogleMapView implements Serializable {
		
		private boolean activarpanelprecios;
		
		private transient  List<Resultado> resultado= new java.util.ArrayList<>();
		
		private Double presupuesto;

		private Double latitud;
		
		private Double longitud;
		
		private MapModel model = new DefaultMapModel();
		 
		private MapModel simpleModel;
		
		private String myMarker; //Your object
		
	     
	    
		  //prueba
	    @PostConstruct
	    public void init() {
	        simpleModel = new DefaultMapModel();
	          
	        //Shared coordinates            lat     long
	        LatLng coord1 = new LatLng(-2.901133, -79.003719);
	        LatLng coord2 = new LatLng(-2.900649, -79.001293);
	        
	          
	        //Basic marker
	        simpleModel.addOverlay(new Marker(coord1, "BeerHOuse"));
	        simpleModel.addOverlay(new Marker(coord2, "Tiests"));
	        
	    }
	  
	    public MapModel getSimpleModel() {
	        return simpleModel;
	    }
	    

		
		public Double getPresupuesto() {
			return presupuesto;
		}

		public void setPresupuesto(Double presupuesto) {
			this.presupuesto = presupuesto;
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

		public void onPointSelect(PointSelectEvent event) {
//	    	Marker marker;
//			if (event.getOverlay() instanceof Marker) {
//				marker = (Marker) event.getOverlay();
//			} else {
//				marker = null;
//				if (event.getOverlay() instanceof Circle){
//					Circle c = (Circle) event.getOverlay();
//					
//					//this.onMapSearchLat=""+c.getCenter().getLat();
//					//this.onMapSearchLon=""+c.getCenter().getLng();
//				}
//				RequestContext requestContext = RequestContext.getCurrentInstance();  
//				 requestContext.execute("dlg.show();");
//			}
	    	  //LatLng latlng = event.getLatLng();
	    	  //LatLng coordnueva = new LatLng(latlng.getLat(), latlng.getLng());
	    	  //simpleModel.addOverlay(new Marker(coordnueva, "Punto Busqueda"));
	    	  LatLng location = event.getLatLng();
	    	  addMarker(location.getLat(), location.getLng());
	    	  
	      }
	    
	    
	    private void addMarker(double lat, double lng)
	    {
	      
	      // Clear out the model. I only want it holding one marker.
	      simpleModel = new DefaultMapModel();
	      Marker marker = new Marker(new LatLng(lat, lng), "mkr");
	      this.latitud=lat;
	      this.longitud=lng;
	      simpleModel.addOverlay(marker);
	      RequestContext.getCurrentInstance().execute("var currentMarker = new google.maps.Marker({ position:new google.maps.LatLng("
                  + lat + ", " + lng
                  + "), map:PF('map').getMap()});"
                  + "markers.push(currentMarker);");
	      
	    }
	    
	    public void testButtonAction(ActionEvent event) 
	    {

	    	
	    //generarPlanOntologia(this.presupuesto);
	    	
	    	
	    	
	       // return "anotherPage.xhtml";
	    	longitud = -79.00;
	    	latitud = -2.92;
	        
	        System.out.println("Los datos para genera el plan son :"+latitud+longitud+presupuesto);
	        
	        CtrConsultarPlan  cp = new CtrConsultarPlan();
	        cp.consultarPlan(presupuesto, latitud, longitud);

	        
			// FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se consulto correctamente el plan."));

	        
	    }
	    
	    
	    public void ConsultaPorPrecio()
	  		{		
	  	   	
	  	    	this.activarpanelprecios=true;
	  	    	
	  	    	String userdir = "";
	  	 		try {	
	  	 			userdir = cargaCSVtoRDF.class.getResource("/datosRestaurantesCuenca.geojson").toURI().getPath().substring(0, cargaCSVtoRDF.class.getResource("/datosRestaurantesCuenca.geojson").toURI().getPath().lastIndexOf("/"));
	  	 		} catch (URISyntaxException e3) {
	  	 			// TODO Auto-generated catch block
	  	 			e3.printStackTrace();
	  	 		}
	  	 			
	  	 			OntModel model;
	  	 			System.out.println(userdir);
	  	 			model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);	 
	  	 			model.read(userdir+ "/ontologia_general_cargada.owl","RDF/XML"); 
	  			    Consulta(model);
	  			    
	  		}
	  		
	  		public void Consulta(OntModel model) {
	  		
	  			Double valor=this.presupuesto;
	  			
	  			String SparQlIndividual=
	  		    	     "prefix ns:<http://www.semanticweb.org/usuario/ontologies/2019/2/ruta#>"+
	  		             "prefix rdfs:<http://www.w3.org/2000/01/rdf-schema#>"+
	  		             "prefix rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
	  		             "SELECT * { { " +
	  		             "select ?nombre ?precio ?latitud ?longitud ?direccion "+
	  		             "where { "
	  		             + "?dataP rdf:type ns:Discoteca ."
	  		             + "?dataP ns:nombre ?nombre ; "
	  		             + "	   ns:precio ?precio ; "
	  		             + "	   ns:latitud ?latitud ; "
	  		             + "	   ns:longitud ?longitud ; "
	  		             + "	   ns:direccion ?direccion . "
	  		             + " FILTER (?precio > "+valor+") . "
	  		             + " } } "
	  		             + " UNION { "
	  					 + "SELECT ?nombre ?precio ?latitud ?longitud ?direccion "
	  					 + "WHERE  "
	  					 + "{  "
	  			         + "  ?datosR rdf:type ns:Restaurant ."
	  			         + "  ?datosR ns:nombre ?nombre ; "
	  			         + "	      ns:precio ?precio ; "
	  			         + "	      ns:latitud ?latitud ; "
	  			         + "	      ns:longitud ?longitud ; "
	  			         + "	      ns:direccion ?direccion . "
	  		             + " FILTER (?precio > "+valor+") ."
	  			         + "} } }";	
	  		        
	  			
	  			
	  			Query query = QueryFactory.create(SparQlIndividual);		 
	  			// Ejecutar la consulta y obtener los resultados
	  			QueryExecution qe = QueryExecutionFactory.create(query, model);		 
	  			try {
	  			 	
	  			   ResultSet results = qe.execSelect();
	  			   //ResultSetFormatter.out(System.out, results, query) ;
	  			   while (results.hasNext()) 
	  			   {
	  			   System.out.println("llego a cargar los literales");
	  			   QuerySolution qs = results.next();
	  			   RDFNode prec = qs.get("precio") ;
	  			   RDFNode lati = qs.get("latitud") ;
	  			   RDFNode longi = qs.get("longitud") ;
	  			   RDFNode direccion = qs.get("direccion") ;
	  			   Resultado result=new Resultado(qs.getLiteral("nombre").toString(),direccion.asNode().getLiteralLexicalForm().toString(),prec.asNode().getLiteralLexicalForm().toString(), lati.asNode().getLiteralLexicalForm(),longi.asNode().getLiteralLexicalForm().toString());
	  			   resultado.add(result);
	  			   }
	  			   
	  			   
	  			} 
	  			finally 
	  			{ 
	  				qe.close() ; 
	  			}
	  			//forresultado
	  			
	  				
	  			
	  		}

	  		
	  		
//	  		public String getObtieneParent(String val) {
//	  			String res = ComponentResolver.resolve(val);
//	  			return res;
//	  		}
	  		
	  		public List<Resultado> getResultado() {
	  			return resultado;
	  		}

	  		public void setResultado(List<Resultado> resultado) {
	  			this.resultado = resultado;
	  		}

	  		public boolean isActivarpanelprecios() {
	  			return activarpanelprecios;
	  		}

	  		public void setActivarpanelprecios(boolean activarpanelprecios) {
	  			this.activarpanelprecios = activarpanelprecios;
	  		}

	
		
	    	    
	}
	

