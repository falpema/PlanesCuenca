package cargaCSV;



	import java.io.Serializable;
import java.math.BigDecimal;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

import org.primefaces.context.RequestContext;
import org.primefaces.event.map.PointSelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

	@ManagedBean
	public class GoogleMapView implements Serializable {
		
		private BigDecimal presupuesto;

		private Double latitud;
		
		private Double longitud;
		
		private MapModel model = new DefaultMapModel();
		 
		private MapModel simpleModel;
		  private String myMarker; //Your object
		  //prueba
	    @PostConstruct
	    public void init() {
	        simpleModel = new DefaultMapModel();
	          
	        //Shared coordinates
	        LatLng coord1 = new LatLng(-2.901133, -79.003719);
	        LatLng coord2 = new LatLng(-2.900649, -79.001293);
	        
	          
	        //Basic marker
	        simpleModel.addOverlay(new Marker(coord1, "BeerHOuse"));
	        simpleModel.addOverlay(new Marker(coord2, "Tiests"));
	        
	    }
	  
	    public MapModel getSimpleModel() {
	        return simpleModel;
	    }
	    
	    
	    
	    
	    public BigDecimal getPresupuesto() {
			return presupuesto;
		}

		public void setPresupuesto(BigDecimal presupuesto) {
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
	    	
	    	
	    	//System.out.println("Los datos para genera el plan son :"+latitud+longitud+presupuesto);
	       // return "anotherPage.xhtml";
	        
	    	// create OntModel
//			OntModel model = ModelFactory.createOntologyModel();
//			// read camera ontology
//			readOntology( "/home/txmunoz/git/PlanesCuenca/PlanesCuenca/src/ontologia/camera.owl", model );
//			// start traverse
//			traverseStart( model, null );
	        
	        
	    }
	    
	    
	    
	    
	    	    
	}
	

