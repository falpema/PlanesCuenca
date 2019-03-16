package cargaCSV;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.xml.sax.SAXException;

import no.acando.xmltordf.Builder;

public class CargaXmltoRdf {
	public static void ejecutarXML() throws ParserConfigurationException, SAXException, IOException
	{
		String userdir= System.getProperty("user.dir");		
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(userdir+"/resources/florerias_cuenca.xml"));
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(userdir+"/src/recursos/florerias_cuenca.ttl"));

        Builder.getAdvancedBuilderStream().build().convertToStream(in, out);
	}
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException 
	{
		ejecutarXML();
	}
}
