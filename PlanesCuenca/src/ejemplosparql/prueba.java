package ejemplosparql;

import java.io.File;
import java.io.PrintWriter;

import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.vocabulary.XSD;

public class prueba {

	public static void main(String[] args) 
	{
		
		/*
		// create OntModel
		OntModel model = ModelFactory.createOntologyModel();
		// read camera ontology
		readOntology( "/home/txmunoz/git/PlanesCuenca/PlanesCuenca/src/ontologia/camera.owl", model );
		// start traverse
		traverseStart( model, null );
		
		*/
		
		//  EJEMPLO PIZZA  
		
//		 	OntModel m = getModel();
//	        loadData( m );
//	        String prefix = "prefix pizza: <" + PIZZA_NS + ">\n" +
//	                        "prefix rdfs: <" + RDFS.getURI() + ">\n" +
//	                        "prefix owl: <" + OWL.getURI() + ">\n";
//
//
//	        showQuery( m,
//	                   prefix +
//	                   "select ?pizza where {?pizza a owl:Class ; " +
//	                   "                            rdfs:subClassOf ?restriction.\n" +
//	                   "                     ?restriction owl:onProperty pizza:hasTopping ;" +
//	                   "                            owl:someValuesFrom pizza:PeperoniSausageTopping" +
//	                   "}" );
		
		
		
	     OntModel model = null;
	        
	     // crear un modelo utilizando como razonador OWL_MEM_RULE_INF
	     model = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
	      
	     String NS = "http://www.maestriageti.com/planescuenca.owl#";
	     //model.setNsPrefix(NS, "http://www.maestriageti.com/planescuenca.owl#");
	     
	     OntClass ruta = model.createClass(NS +"Ruta");
	     
	     OntClass lugar = model.createClass(NS +"Lugar");
	     
	     OntClass localizacion = model.createClass(NS +"Localizacion");
	     
	     OntClass coordenadas = model.createClass(NS +"Coordenadas");
	    
	     OntClass bares = model.createClass(NS +"Discoteca");

	     OntClass floreria = model.createClass(NS +"Floreria");

	     
	     lugar.addSubClass(bares);
	     
	     lugar.addSubClass(floreria);

	     localizacion.addSubClass(coordenadas);

	     ObjectProperty tieneInicio = model.createObjectProperty(NS+"tieneInicio");
	     
	     tieneInicio.addDomain(ruta);//Clase a la que pertenece
	     tieneInicio.addRange(bares);//Tipo de la propiedad
	     
	     
	     DatatypeProperty nombre = model.createDatatypeProperty(NS+"nombre");
	     
	     nombre.addDomain(bares);//Clase a la que pertenece
	     nombre.addRange(XSD.xstring);//Tipo de la propiedad
	     //nombre.convertToFunctionalProperty();//Para que solo acepte un valor.
	     
	     
	     DatatypeProperty latitud = model.createDatatypeProperty(NS+"latitud");
	     
	     latitud.addDomain(bares);//Clase a la que pertenece
	     latitud.addRange(XSD.xdouble);//Tipo de la propiedad
	     
	     
	     DatatypeProperty precio = model.createDatatypeProperty(NS+"precio");
	     
	     precio.addDomain(bares);//Clase a la que pertenece
	     precio.addRange(XSD.decimal);//Tipo de la propiedad
	     
	     
	   //Creamos las instancias y damos valor a la propiedad Peso
	     Individual parola = model.createIndividual(NS+"Parola",bares);
	     
	     Individual indigo = model.createIndividual(NS+"Indigo",bares);
	     
	     
	     
//	     parola.setPropertyValue(nombre, model.createTypedLiteral("ParolaBar"));
//	     parola.setPropertyValue(precio, model.createTypedLiteral(10));
//	     parola.setPropertyValue(latitud, model.createTypedLiteral(788));
//	     
//	     
//	     indigo.setPropertyValue(nombre, model.createTypedLiteral("IndigoBar"));
//	     indigo.setPropertyValue(precio, model.createTypedLiteral(3));
//	     indigo.setPropertyValue(latitud, model.createTypedLiteral(20));

	     
	     
	     
	     try
	     {
	     
	   //Almacenamos la ontología en un fichero OWL (Opcional)
	     File file = new File("/home/txmunoz/Documentos/MAESTRIAGETI/Animales2.owl");
	     //Hay que capturar las Excepciones
	     if (!file.exists()){
	          file.createNewFile();
	     }
	     model.write(new PrintWriter(file));
	     
	     }
	     catch(Exception e)
	     {
	    	 
	     }
	     
	     System.out.println("termino de cargar");
	   
	     genera();
	     
		
	}
	
	public static final String PIZZA_NS = "http://www.maestriageti.com/planescuenca.owl#";
	
	
	public static void genera()
	{
		//ejemplos
//	 	OntModel m = getModel();
//        loadData( m );
//       
//        
////        String prefix = "prefix pizza: <" + PIZZA_NS + ">\n" +
////                        "prefix rdfs: <" + RDFS.getURI() + ">\n" +
////                        "prefix owl: <" + OWL.getURI() + ">\n";
////
////
////        showQuery( m,
////                   prefix +
////                   "select ?pizza where {?pizza a owl:Class ; " +
////                   "                            rdfs:subClassOf ?restriction.\n" +
////                   "                     ?restriction owl:onProperty pizza:hasTopping ;" +
////                   "                            owl:someValuesFrom pizza:PeperoniSausageTopping" +
////                   "}" );
//	
//        
//        
//        readModel(m);
        
		System.out.println("kkkk");
        
        OntModel model = null;
        
     // crear un modelo utilizando como razonador OWL_MEM_RULE_INF
     model = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM_RULE_INF );
      
     System.out.println("dddd2");
     // abrir el archivo con la ontología
     java.io.InputStream in = FileManager.get().open("/home/txmunoz/Documentos/MAESTRIAGETI/Animales2.owl" );
     if (in == null) {
         throw new IllegalArgumentException("Archivo no encontrado");
     }
      
     // leer el archivo RDF/XML
     model.read(in, "");
      
     //
     // El siguiente código muestra un listado con las clases e instancias existentes en el modelo
     //
     System.out.println( "Clases/Instancias");
     System.out.println( "=================");
     ExtendedIterator iteratorClasses = model.listClasses(); 
     while ( iteratorClasses.hasNext() ){
         OntClass ontClass = (OntClass) iteratorClasses.next();
         System.out.println( ontClass );
      
         ExtendedIterator iteratorInstances = ontClass.listInstances();
         while ( iteratorInstances.hasNext() ){
        System.out.println( "\t"+iteratorInstances.next() );
         }
         System.out.println("");
     }
     
     System.out.println("llegando a sparql");

     String SparQlIndividual=
    	     "prefix ns:<http://www.maestriageti.com/planescuenca.owl#>"+
             "prefix rdfs:<http://www.w3.org/2000/01/rdf-schema#>"+
             "prefix rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+

             
             "select ?dataP ?precio ?latitud "+
             "where {"
             + "?dataP rdf:type ns:Discoteca . "
             + "?dataP ns:precio ?precio ; " 
             + "       ns:latitud ?latitud . \n" 
             + " FILTER ( ?precio > 2) . }";
     
     
// String prefix = "prefix pizza: <" + PIZZA_NS + ">\n" +
// "prefix rdfs: <" + RDFS.getURI() + ">\n" +
// "prefix owl: <" + OWL.getURI() + ">\n";
//
//
//showQuery( model,
//prefix +
//"select ?restriction where {?pizza a owl:Class ; " +
//"                            rdfs:subClassOf ?restriction. \n" +
//"                     ?restriction pizza:nombre ?datos ." +
//"}" );
     
        showQuery( model, SparQlIndividual);
        
	}



    protected static OntModel getModel() {
        return ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM_RULE_INF );
    }

    protected static void loadData( Model m ) {
        FileManager.get().readModel( m,  "/home/txmunoz/git/PlanesCuenca/PlanesCuenca/src/recursos/ontologia_ruta.owl" );
    }

    protected static void showQuery( Model m, String q ) {
        Query query = QueryFactory.create( q );
        QueryExecution qexec = QueryExecutionFactory.create( query, m );
        try {
            ResultSet results = qexec.execSelect();
            ResultSetFormatter.out( results, m );
        }
        finally {
            qexec.close();
        }
        
        
      
    }
    
	
	
	
	
}
