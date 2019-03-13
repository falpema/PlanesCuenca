package ontologia;
import java.io.FileWriter;
import java.io.InputStream;

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
import org.apache.jena.util.iterator.ExtendedIterator;

import cargaCSV.cargaCSVtoRDF;

import org.apache.jena.util.FileManager;

public class ProcesaOwl {
	public static void carga() {
		
		String userdir= System.getProperty("user.dir");
		
		OntModel model = null;	 
		model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);	 
		model.read(userdir+ "/src/recursos/ontologia_ruta_final.owl","RDF/XML"); 
		 
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
		Consulta(model);
	}
	public static void Consulta(OntModel model) {
		//RESULTADOS CON SPARQL							 			
		String queryString = 
		   "PREFIX po: <http://www.semanticweb.org/usuario/ontologies/2019/2/ruta#> "+
		   "SELECT ?nf "+
		   "WHERE  "+
		   "{  "+
		   "   po:r1 po:tieneFloreria ?nombre. "+
		   "   ?nombre po:nombre ?nf "+
		   "}";		 		 
		Query query = QueryFactory.create(queryString);		 
		// Ejecutar la consulta y obtener los resultados
		QueryExecution qe = QueryExecutionFactory.create(query, model);		 
		try {
		   ResultSet results = qe.execSelect();
		   ResultSetFormatter.out(System.out, results, query) ;
		} finally { qe.close() ; }
	}
	
	public static void main(String[] args) 
	{
		carga();
	}
}
