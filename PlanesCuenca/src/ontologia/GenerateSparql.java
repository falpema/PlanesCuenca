package ontologia;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
 
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDFS;

import cargaCSV.cargaCSVtoRDF;
 
 
public class GenerateSparql 
{
	public static void readOntology( String file, OntModel model )
	{
		InputStream in = null;
		try
		{
			in = new FileInputStream( file );
			model.read( in, "RDF/XML" );
			in.close();
		} catch ( IOException e ) 
		{
			e.printStackTrace();
		} 
	}
	
	private static List<String> getRoots( OntModel model )
	{
		List<String> roots = new ArrayList<String>();
		
		// find all owl:Class entities and filter these which do not have a parent
		String getRootsQuery = 
				  "SELECT DISTINCT ?s WHERE " 
				+ "{"
				+ "  ?s <http://www.w3.org/2000/01/rdf-schema#subClassOf> <http://www.w3.org/2002/07/owl#Thing> . " 
				+ "  FILTER ( ?s != <http://www.w3.org/2002/07/owl#Thing> && ?s != <http://www.w3.org/2002/07/owl#Nothing> ) . " 
				+ "  OPTIONAL { ?s <http://www.w3.org/2000/01/rdf-schema#subClassOf> ?super . " 
				+ "  FILTER ( ?super != <http://www.w3.org/2002/07/owl#Thing> && ?super != ?s ) } . " 
				+ "}";
		
		Query query = QueryFactory.create( getRootsQuery );
		
		try ( QueryExecution qexec = QueryExecutionFactory.create( query, model ) ) 
		{
			ResultSet results = qexec.execSelect();
			while( results.hasNext() )
			{
				QuerySolution soln = results.nextSolution();
				RDFNode sub = soln.get("s"); 
				
				if( !sub.isURIResource() ) continue;
				
				roots.add( sub.toString() );
			}
		}
		
		return roots;
	}
	
	public static void traverseStart( OntModel model, String entity )
	{
		// if starting class available
		if( entity != null ) 
		{
			traverse( model, entity,  new ArrayList<String>(), 0  );
		}
		// get roots and traverse each root
		else
		{
			List<String> roots = getRoots( model );
		
			for( int i = 0; i < roots.size(); i++ )
			{
				traverse( model, roots.get( i ), new ArrayList<String>(), 0 );
			}
		}
	}
	
	public static void traverse( OntModel model, String entity, List<String> occurs, int depth )
	{
		if( entity == null ) return;
		
		String queryString 	= "SELECT ?s WHERE { "
						   	+ "?s <http://www.w3.org/2000/01/rdf-schema#subClassOf> <" + entity + "> . }" ;
		
		Query query = QueryFactory.create( queryString  );
		
		if ( !occurs.contains( entity ) ) 
		{
			// print depth times "\t" to retrieve an explorer tree like output
			for( int i = 0; i < depth; i++ ) { System.out.print("\t"); }
			// print out the URI
			System.out.println( entity );
			
			try ( QueryExecution qexec = QueryExecutionFactory.create( query, model ) ) 
			{
				ResultSet results = qexec.execSelect();
				while( results.hasNext() )
				{
					QuerySolution soln = results.nextSolution();
					RDFNode sub = soln.get("s"); 
					
					if( !sub.isURIResource() ) continue;
					
					String str = sub.toString();
					
	                // push this expression on the occurs list before we recurse to avoid loops
	                occurs.add( entity );
	                // traverse down and increase depth (used for logging tabs)
					traverse( model, str, occurs, depth + 1 );
	                // after traversing the path, remove from occurs list
	                occurs.remove( entity );
				}
			}
		}
		
	}
	
	 public static final String SOURCE = "./src/main/resources/data/";

	    // Pizza ontology namespace
	 public static final String PIZZA_NS = "http://www.co-ode.org/ontologies/pizza/pizza.owl#";
	
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
		
		/*  EJEMPLO PIZZA  
		
		 	OntModel m = getModel();
	        loadData( m );
	        String prefix = "prefix pizza: <" + PIZZA_NS + ">\n" +
	                        "prefix rdfs: <" + RDFS.getURI() + ">\n" +
	                        "prefix owl: <" + OWL.getURI() + ">\n";


	        showQuery( m,
	                   prefix +
	                   "select ?pizza where {?pizza a owl:Class ; " +
	                   "                            rdfs:subClassOf ?restriction.\n" +
	                   "                     ?restriction owl:onProperty pizza:hasTopping ;" +
	                   "                            owl:someValuesFrom pizza:PeperoniSausageTopping" +
	                   "}" );
		
		*/
		
	}
	
	
	    protected static OntModel getModel() {
	        return ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
	    }

	    protected static void loadData( Model m ) {
	        FileManager.get().readModel( m,  "/home/txmunoz/git/PlanesCuenca/PlanesCuenca/src/ontologia/pizza.owl" );
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

	/*	
		
		
		 String SOURCE = "http://www.opentox.org/api/1.1";
	        String NS = SOURCE + "#";
	       //create a model using reasoner
	        OntModel model1 = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM_MICRO_RULE_INF);
	       //create a model which doesn't use a reasoner
	        OntModel model2 = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM);
	        
	        // read the RDF/XML file
	        model1.read( SOURCE, "RDF/XML" );
	        model2.read( SOURCE, "RDF/XML" );
	        //prints out the RDF/XML structure
	        //qe.close();
	        System.out.println(" ");
	      
	        
//	        Model onto = ModelFactory.createDefaultModel();
//	     
//	        Model ontodata = onto.read(GenerateSparql.class.getResourceAsStream("camera.owl"), "RDF/XML");   
//	        

	    // Create a new query
	    String queryString =        
	      "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "+
	        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>  "+
	        "select ?uri "+
	        "where { "+
	         "?uri rdfs:subClassOf <http://www.opentox.org/api/1.1#Feature>  "+
	        "} \n ";
	    Query query = QueryFactory.create(queryString);

	    System.out.println("----------------------");

	    System.out.println("Query Result Sheet");

	    System.out.println("----------------------");

	    System.out.println("Direct&Indirect Descendants (model1)");

	    System.out.println("-------------------");

	   
	    // Execute the query and obtain results
	    QueryExecution qe = QueryExecutionFactory.create(query, model1);
	    ResultSet results =  qe.execSelect();

	    // Output query results    
	    ResultSetFormatter.out(System.out, results, query);

	    qe.close();
	    
	    System.out.println("----------------------");
	    System.out.println("Only Direct Descendants");
	    System.out.println("----------------------");
	    
	    // Execute the query and obtain results
	    //qe = QueryExecutionFactory.create(query, model2);
	    //results =  qe.execSelect();

	    // Output query results    
	    //ResultSetFormatter.out(System.out, results, query);  
	   qe.close();
	}
 */
}
