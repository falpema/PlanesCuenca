package cargaCSV;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.commons.io.FileUtils;
import org.apache.jena.lang.csv.CSV2RDF;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import planesCuenca.CtrCargaDataToRdf;



@ManagedBean
@SessionScoped
public class cargaCSVtoRDF {

//	public static void main(String[] args) {
//		ejecutarCSV();
//	}
		

	public static void ejecutarCSV()
	{
		String userdir= "";
		try {
			userdir = cargaCSVtoRDF.class.getResource("/datosRestaurantesCuenca.geojson").toURI().getPath().substring(0, cargaCSVtoRDF.class.getResource("/datosRestaurantesCuenca.geojson").toURI().getPath().lastIndexOf("/"));
		} catch (URISyntaxException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		System.out.println(userdir);

		
		  CSV2RDF.init();//Initialise the CSV conversion engine in Jena
		  
		  Model model = ModelFactory.createDefaultModel();
			File initialFile = new File(userdir+ "/datossemanticabares.csv");
		    InputStream targetStream = null;
			try {
				targetStream = new FileInputStream(initialFile);
			} catch (FileNotFoundException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
		
	      model.read(targetStream, "http://maestriageti/barescuenca", "csv");
	     
		 

	       try {

			FileWriter out = new FileWriter(userdir+"/discoteca.rdf");

	              model.write(out,"RDFXML");

	       } catch (Exception e2) {

	              System.out.println("Error in the file output process!");

	              e2.printStackTrace();

	       }

	                    
	       File output = new File(userdir+"/discoteca.rdf");

	       File tempFile = new File(userdir+"/temp.nt");

	       BufferedReader reader = null;

	       BufferedWriter writer = null;

	       try {

	              reader = new BufferedReader(new FileReader(userdir+"/discoteca.rdf"));

	              writer = new BufferedWriter(new FileWriter(userdir+"/temp.nt"));

	              String currentLine;

	              //Delete triples from the old file by skipping it while reading the input N-Triple   file from the last step, otherwise write the triple to a new temp file!

	              while ((currentLine = reader.readLine()) != null) {

	                    if (currentLine.contains("http://w3c/future-csv-vocab/row")) {

	                           continue;

	                    } else {

	                           writer.write(currentLine);

	                           writer.newLine();

	                    }

	              }

	              writer.close();

	              reader.close();

	             

	              PrintWriter printer = new PrintWriter(output);

	              printer.print("");

	              printer.close();

	             

	              //copy content from temp file to final output file, overwriting it.

	              FileUtils.copyFile(tempFile, output);
	              

	       } catch (FileNotFoundException e1) {

	              // TODO Auto-generated catch block

	              e1.printStackTrace();

	       } catch (IOException e) {

	              // TODO Auto-generated catch block

	              e.printStackTrace();

	       } 
	       
	   System.out.println("Termino de realizar la carga "+ userdir+"/discoteca.rdf");
		
	}
	
	
	
	
}
