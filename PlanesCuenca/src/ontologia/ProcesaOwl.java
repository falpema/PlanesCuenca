package ontologia;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.lang.PipedRDFIterator;
import org.apache.jena.riot.lang.PipedRDFStream;
import org.apache.jena.riot.lang.PipedTriplesStream;
import org.apache.jena.util.iterator.ExtendedIterator;

import cargaCSV.cargaCSVtoRDF;

import org.apache.jena.util.FileManager;

public class ProcesaOwl {
	static String userdir= System.getProperty("user.dir");
	
	static String NS = "http://www.semanticweb.org/usuario/ontologies/2019/2/ruta#";
	
	/**
	 * Method to load rdf of restaurants in ontology
	 */
	public static void cargaRdfRestaurants() {
		try {
			userdir = cargaCSVtoRDF.class.getResource("/datosRestaurantesCuenca.geojson").toURI().getPath().substring(0, cargaCSVtoRDF.class.getResource("/datosRestaurantesCuenca.geojson").toURI().getPath().lastIndexOf("/"));
		} catch (URISyntaxException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		
		OntModel model = null;
		OntClass restaurant = null;
		model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		model.read(userdir + "/ontologia_general.owl", "RDF/XML");

		System.out.println("Clases/Instancias");
		System.out.println("=================");
		ExtendedIterator iteratorClasses = model.listClasses();
		while (iteratorClasses.hasNext()) {
			OntClass ontClass = (OntClass) iteratorClasses.next();

			if (ontClass.getLocalName().equals("Restaurant")) {
				restaurant = ontClass;
			}

			System.out.println(ontClass);
			ExtendedIterator iteratorInstances = ontClass.listInstances();
			while (iteratorInstances.hasNext()) {
				System.out.println("\t" + iteratorInstances.next());
			}

		}

		System.out.println("");

		DatatypeProperty nombre = model
				.getDatatypeProperty("http://www.semanticweb.org/usuario/ontologies/2019/2/ruta#nombre");
		DatatypeProperty precio = model
				.getDatatypeProperty("http://www.semanticweb.org/usuario/ontologies/2019/2/ruta#precio");
		DatatypeProperty latitud = model
				.getDatatypeProperty("http://www.semanticweb.org/usuario/ontologies/2019/2/ruta#latitud");
		DatatypeProperty longitud = model
				.getDatatypeProperty("http://www.semanticweb.org/usuario/ontologies/2019/2/ruta#longitud");

		Model modelrdf = FileManager.get().loadModel(userdir + "/restaurante.rdf", null, "RDF/XML");

		StmtIterator iter = modelrdf.listStatements();

		Integer contador = 0;
		String valor = "";
		String valorlatitud = "";
		String valorlongitud = "";
		String valorprecio = "";
		String valornombre = "";
		String nombrerecurso = "Restaurant";

		Individual instancia = null;
		while (iter.hasNext()) {

			contador++;
			Statement stmt = iter.nextStatement(); // get next statement
			Resource subject = stmt.getSubject(); // get the subject
			Property predicate = stmt.getPredicate(); // get the predicate
			RDFNode object = stmt.getObject(); // get the object
			System.out.println("..");
			System.out.println("Localname " + predicate.getLocalName() + " ");
			System.out.print(subject.toString());
			System.out.print(" predicado " + predicate.toString() + " ");
			System.out.print(" uri " + predicate.getURI() + " ");
			System.out.println("..");
			if (object instanceof Resource) {
				valor = object.toString();
				System.out.print("recurso " + object.toString());
			} else {
				// object is a literal
				
				valor = "\"" + object.toString() + "\"";
				System.out.print("\"" + object.toString() + "\"");
			}
			if (predicate.getLocalName().compareTo("geometry") == 0) {
				String[] internData = valor.split(",");
				valorlongitud = internData[0].substring(internData[0].indexOf("[")+1, internData[0].length());
				valorlatitud = internData[1].substring(0, internData[1].indexOf("]"));
			}

			if (predicate.getLocalName().compareTo("properties") == 0) {
				String[] internData = valor.split(",");
				for (String data : internData) {
					if (data.contains("name:")) {
						valornombre = data.substring(data.indexOf(":")+1, data.length());
					}
					if (data.contains("price:")) {
						valorprecio = data.substring(data.indexOf(":")+1, data.length());
					}
					if (data.contains("id:node")) {
						nombrerecurso = data.substring(data.indexOf("node/")+1, data.length());
					}
				}
			}

			int resto = contador % 14;

			if (resto == 0) {
				instancia = model.createIndividual(NS + nombrerecurso, restaurant);
				instancia.setPropertyValue(nombre, model.createTypedLiteral(valornombre));
				//instancia.setPropertyValue(latitud, model.createTypedLiteral(valorlatitud));
				instancia.setPropertyValue(latitud,model.createTypedLiteral(Double.valueOf(valorlatitud)));
				
				instancia.setPropertyValue(precio,model.createTypedLiteral(Double.valueOf(valorprecio) ));//model.createTypedLiteral(valorprecio));
				//instancia.setPropertyValue(longitud,model.createTypedLiteral(valorlongitud));
				instancia.setPropertyValue(longitud,model.createTypedLiteral(Double.valueOf(valorlongitud)));
				// model.createTypedLiteral(valorlongitud));
			}

		}
		
		 try
	     {
	     
	   //Almacenamos la ontología en un fichero OWL (Opcional)
	     File file = new File(userdir + "/ontologia_general_cargada.owl");
	     //Hay que capturar las Excepciones
	     if (!file.exists()){
	          file.createNewFile();
	     }
	     model.write(new PrintWriter(file));
	     System.out.println("===========================");
	     System.out.println("CARGA CORRECTA DEL RDF DE RESTAURANTE EN OWL");
	     System.out.println("===========================");
	     }
	     catch(Exception e)
	     {
	    	 
	     }

	}
	
	public static void carga() {
	try {	
		userdir = cargaCSVtoRDF.class.getResource("/datosRestaurantesCuenca.geojson").toURI().getPath().substring(0, cargaCSVtoRDF.class.getResource("/datosRestaurantesCuenca.geojson").toURI().getPath().lastIndexOf("/"));
	} catch (URISyntaxException e3) {
		// TODO Auto-generated catch block
		e3.printStackTrace();
	}
		OntModel model = null;	 
		OntClass bares = null;
		model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);	 
		model.read(userdir+ "/ontologia_general_cargada.owl","RDF/XML"); 
		 
		System.out.println( "Clases/Instancias");
		System.out.println( "=================");
		ExtendedIterator iteratorClasses = model.listClasses(); 
		while ( iteratorClasses.hasNext() ){
		    OntClass ontClass = (OntClass) iteratorClasses.next();
		    
		    if(ontClass.getLocalName().equals("Discoteca"))
		    {
		    	 bares = ontClass;	
		    }
		    
		    System.out.println( ontClass );		 
		    ExtendedIterator iteratorInstances = ontClass.listInstances();
		    while ( iteratorInstances.hasNext() ){
		       System.out.println( "\t"+iteratorInstances.next() );
		    }
		    
		}
		    System.out.println("");
		    
		    DatatypeProperty nombre = model.getDatatypeProperty("http://www.semanticweb.org/usuario/ontologies/2019/2/ruta#nombre");
		   
		    DatatypeProperty precio = model.getDatatypeProperty("http://www.semanticweb.org/usuario/ontologies/2019/2/ruta#precio");
		    
		    DatatypeProperty latitud = model.getDatatypeProperty("http://www.semanticweb.org/usuario/ontologies/2019/2/ruta#latitud");
		    
		    DatatypeProperty longitud = model.getDatatypeProperty("http://www.semanticweb.org/usuario/ontologies/2019/2/ruta#longitud");
		    
		    DatatypeProperty sector = model.getDatatypeProperty("http://www.semanticweb.org/usuario/ontologies/2019/2/ruta#sector");
		    
		    DatatypeProperty direccion = model.getDatatypeProperty("http://www.semanticweb.org/usuario/ontologies/2019/2/ruta#direccion");
		    
		    
		    Model modelrdf = FileManager.get().loadModel(userdir+ "/discoteca.rdf", null, "RDF/XML");
		 	
		    StmtIterator iter = modelrdf.listStatements();
		 	
		    Integer contador=0;
		    String valor="";
		    String valorlatitud="";
		    String valorlongitud="";
		    String valorprecio="";
		    String valornombre="";
		    String valorsector="";
		    String valordireccion="";
		    String nombrerecurso="Bar";
		    
		    Individual instancia = null;
		    while (iter.hasNext()) {
		 	    
		    	contador++;
		    	Statement stmt      = iter.nextStatement();  // get next statement
		 	    Resource  subject   = stmt.getSubject();     // get the subject
		 	    Property  predicate = stmt.getPredicate();   // get the predicate
		 	    RDFNode   object    = stmt.getObject();      // get the object

		 	   
		 	    System.out.println(predicate.getLocalName());
		 	    System.out.print(subject.toString());
		 	    System.out.print("predicado" + predicate.toString() + " ");
		 	    System.out.print("uri" + predicate.getURI() + " ");
		 	    
		 	   	    
		 	    
		 	    if (object instanceof Resource) {
		 	    	valor=object.toString();
		 	       System.out.print("recurso "+object.toString());
		 	    } else {
		 	    	// object is a literal
		 	    	System.out.println("valor");
		 	    	valor= object.asNode().getLiteralLexicalForm().toString();
		 	        System.out.print("\"" + object.toString() + "\"");
		 	    }
		 	   

		 	   switch (predicate.getLocalName())
		 	   {
		 	   
		 	    case "Nombre" :
		 		 nombrerecurso=object.toString().replace(" ", "").toUpperCase();
		 		 valornombre=valor.toString();
		 		 break;
		 	    case "Latitud":
		 	     valorlatitud=valor;
		 	     break;
		 	    case "Longitud":
		 	     valorlongitud=valor;
			 	 break;
		 	    case "Precio":
		 	     valorprecio=valor;
				 break;  
		 	    case "Sector":
			 	valorsector=valor;
				break;
			 	case "Direccion":
			 	valordireccion=valor;
				break;  
		 	   }
		 	  
		 	  int resto = contador%14;
		 	  
		 	  if(resto==0)
		 	  {
		 		instancia = model.createIndividual(NS+nombrerecurso,bares);
		 		instancia.setPropertyValue(nombre, model.createTypedLiteral(valornombre));
		 		instancia.setPropertyValue(latitud, model.createTypedLiteral(valorlatitud));
		 		instancia.setPropertyValue(precio, model.createTypedLiteral(Double.valueOf(valorprecio) ));
		 		instancia.setPropertyValue(longitud, model.createTypedLiteral(valorlongitud));
		 		//instancia.setPropertyValue(sector, model.createTypedLiteral(valorsector!=null?valorsector:""));
		 		instancia.setPropertyValue(direccion, model.createTypedLiteral(valordireccion!=null?valordireccion:""));
		 	  }
		 	  
		 	  
		 	} 
		    
		    		    
		     
		     try
		     {
		     
		   //Almacenamos la ontología en un fichero OWL (Opcional)
		     File file = new File(userdir+ "/ontologia_general_cargada.owl");
		     //Hay que capturar las Excepciones
		     if (!file.exists()){
		          file.createNewFile();
		     }
		     model.write(new PrintWriter(file));
		     
		     }
		     catch(Exception e)
		     {
		    	 
		     }
		
		
		//Consulta(model);
	}
	
	
	public static void Consulta(OntModel model) {
//		//RESULTADOS CON SPARQL							 			
//		String queryString = 
//		   "PREFIX po: <http://www.semanticweb.org/usuario/ontologies/2019/2/ruta#> "+
//		   "SELECT ?nf "+
//		   "WHERE  "+
//		   "{  "+
//		   "   po:r1 po:tieneFloreria ?nombre. "+
//		   "   ?nombre po:nombre ?nf "+
//		   "}";	
		
		
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
				 + "SELECT ?precio  "
				 + "WHERE  "
				 + "{  "
		         + "   ns:r1 ns:tieneFloreria ?nombre. "
		         + "   ?nombre ns:nombre ?precio "
		         + "} } }";	
	        
		
		
		Query query = QueryFactory.create(SparQlIndividual);		 
		// Ejecutar la consulta y obtener los resultados
		QueryExecution qe = QueryExecutionFactory.create(query, model);		 
		try {
		   ResultSet results = qe.execSelect();
		   ResultSetFormatter.out(System.out, results, query) ;
		} finally { qe.close() ; }
	}

	
	public static void main(String[] args) 
	{
		//cargaRdfRestaurants();
		carga();
	}
}
