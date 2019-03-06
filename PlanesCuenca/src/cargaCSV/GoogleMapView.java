package mapas;

	import java.io.Serializable;
	import javax.annotation.PostConstruct;
	import javax.faces.bean.ManagedBean;
	import javax.faces.bean.SessionScoped;
	import org.primefaces.model.map.DefaultMapModel;
	import org.primefaces.model.map.LatLng;
	import org.primefaces.model.map.MapModel;
	import org.primefaces.model.map.Marker;

	@ManagedBean
	public class GoogleMapView implements Serializable {

		private MapModel simpleModel;
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
	}
	

