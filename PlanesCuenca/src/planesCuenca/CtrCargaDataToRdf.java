package planesCuenca;
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
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;

import org.apache.commons.io.FileUtils;
import org.apache.jena.lang.csv.CSV2RDF;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
import org.json.simple.parser.ParseException;
import org.primefaces.push.inject.ServletContextInjectable;

import cargaCSV.cargaCSVtoRDF;
import ontologia.ProcesaOwl;





/**
* load restaurants of geojson to rdf
* @author FABIAN PENALOZA mail falpema@gmail.com
*/
@ManagedBean
@ViewScoped
public class CtrCargaDataToRdf {
	
	public static void main(String[] args) {
		createandLoadOWLfromData();
}
	
	
	private String titulo = "Json TO Rdf";

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public static void createandLoadOWLfromData(){
		 String resp = json2rdf() ;//crear el rdf restaurantes a partir del geojson
		 cargaCSVtoRDF.ejecutarCSV(); // crea rdf bares desde csv
		 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se completo la creacion de Rdfs de fuentes heterogeneas."));
		 //crear rdf de la floreria
		 ProcesaOwl.cargaRdfRestaurants();
		 ProcesaOwl.carga();  //cargar los discos
		 try {
			ProcesaOwl.cargattl();//carga florerias
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Cargar Rdfs en Ontologica correcta."));
		 System.out.println("ontologia cargada en resources/ontologia_general_cargada.owl");
	}
	
	/**
	 * Crear el rdf a partir del geojson	 * falpema
	 * @return
	 */
	private static String json2rdf() {
		String resp = "fail";
		JSONParser parser = new JSONParser();
		 JSONObject output;
	
		String userdir= System.getProperty("user.dir");
		  System.out.println("Working Directory = " + userdir
	              );
		  ///Users/macbookpro/git/PlanesCuenca/PlanesCuenca/src/resourcesfp
		  Path p = Paths.get("resources/datosRestaurantesCuenca.geojson");
		  InputStream res =
				    CtrCargaDataToRdf.class.getResourceAsStream("/datosRestaurantesCuenca.geojson");
		  System.out.println(p.toString());
		  
		  try {
			System.out.println(CtrCargaDataToRdf.class.getResource("/datosRestaurantesCuenca.geojson").toURI().getPath());
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		  
		  
		try {
			Object obj = parser.parse(new FileReader(CtrCargaDataToRdf.class.getResource("/datosRestaurantesCuenca.geojson").toURI().getPath()));
			userdir = CtrCargaDataToRdf.class.getResource("/datosRestaurantesCuenca.geojson").toURI().getPath().substring(0, CtrCargaDataToRdf.class.getResource("/datosRestaurantesCuenca.geojson").toURI().getPath().lastIndexOf("/")) ;
			System.out.println(userdir);
			//parse csv
		  	 output = new JSONObject(obj.toString());
		       JSONArray docs = output.getJSONArray("features");
		
	            File file=new File(userdir+ "/tmprestaurants.csv");
	            String csv = CDL.toString(docs);
	            FileUtils.writeStringToFile(file, csv);
	            cargarGsonToRdf();
			//parse csv
		//	String str = obj.toString();
		//	JSONObject json = new JSONObject(str);
		//	String xml = XML.toString(json);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		

	return resp;

		
	}

	private static String cargarGsonToRdf() {
		String userdir="";
		try {
			userdir = CtrCargaDataToRdf.class.getResource("/datosRestaurantesCuenca.geojson").toURI().getPath().substring(0, CtrCargaDataToRdf.class.getResource("/datosRestaurantesCuenca.geojson").toURI().getPath().lastIndexOf("/"));
		} catch (URISyntaxException e4) {
			// TODO Auto-generated catch block
			e4.printStackTrace();
		}
		
		String resp = "";

		CSV2RDF.init();// Initialise the CSV conversion engine in Jena

		Model model = ModelFactory.createDefaultModel();

		//model.read(userdir+ "/tmprestaurants.csv",
		//		"http://maestriageti/restaurantecuenca", "csv");
		///recursos/datossemanticabares.csv"
		File initialFile = new File(userdir+ "/tmprestaurants.csv");
	    InputStream targetStream = null;
		try {
			targetStream = new FileInputStream(initialFile);
		} catch (FileNotFoundException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		model.read(targetStream,
				"http://maestriageti/restaurantecuenca", "csv");

		try {

			FileWriter out = new FileWriter(userdir+ "/restaurante.rdf");

			model.write(out, "RDFXML");

		} catch (Exception e2) {

			System.out.println("Error in the file output process!");

			e2.printStackTrace();

		}

		File output = new File(userdir+ "/restaurante.rdf");

		File tempFile = new File(userdir+ "/temp.nt");

		BufferedReader reader = null;

		BufferedWriter writer = null;

		try {

			reader = new BufferedReader(
					new FileReader(userdir+ "/restaurante.rdf"));

			writer = new BufferedWriter(
					new FileWriter(userdir+ "/temp.nt"));

			String currentLine;

			// Delete triples from the old file by skipping it while reading the input
			// N-Triple file from the last step, otherwise write the triple to a new temp
			// file!

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

			// copy content from temp file to final output file, overwriting it.

			FileUtils.copyFile(tempFile, output);

		} catch (FileNotFoundException e1) {

			// TODO Auto-generated catch block

			e1.printStackTrace();

		} catch (IOException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		}

		System.out.println("Termino de realizar la carga rdf restaurante en "+userdir+ "/restaurante.rdf");
		resp="Termino de realizar la carga rdf restaurante "+ userdir+ "/restaurante.rdf";
		return resp;
	}

}
