package parser;

import domain.*;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * @author tjehr Class to parse a XML file (read and write possible)
 */
public class Parser implements IParser {

	private File file;
	private SARoot rootElement = new SARoot();
	private boolean manually = false;

	/**
	 * standard constructor, sets the default Path
	 */
	public Parser() {

		this.file = new File("src/test/testJAXB.xml");
	}

	/**
	 * constructor for manual use, user MUST specify a Path for XML Files
	 * @param manually - "true" to create a Standalone parser
	 */
	public Parser(final boolean manually) {
		this.manually = manually;
	}

	/**
	 * console interaction to set a new Path for the XML source
	 */
	@Override
	public void init() {
		// Scanner to read from Console
		Scanner scanner = new Scanner(System.in);
		boolean invalidPath = true;
		System.out.println("starting parser...");
		System.out.println("Please specify a new path for the XML source file:");
		while (invalidPath) {

			System.out.println("Enter new path:");

			String newPath = scanner.nextLine();
			try {
				this.file = new File(newPath);
				invalidPath = !this.file.canWrite();

			} catch (Exception e) {
				scanner.close();
				e.printStackTrace();
				System.out.println("File has not been changed!");
			}

			if (invalidPath) {
				System.out.println("The path you entered seems to be invalid or the File does not exist!");
				System.out.println("Please try again:");
			}
		}
		scanner.close();

	}

	/**
	 * starts the parser
	 */
	@Override
	public void startParser() {
		// Manually started parser will have a console Interaction
		if (manually) {
			init();
		}

		try {
			// setup marshaller
			JAXBContext jaxbContext = JAXBContext.newInstance(SARoot.class, Category.class, Question.class,
					Answer.class, Conclusion.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			// start marshaller to read XML file
			this.rootElement = (SARoot) jaxbUnmarshaller.unmarshal(this.file);
			// generate Lists for Objects

		} catch (JAXBException e) {
			e.printStackTrace();
		}

	}

	public static SARoot getRootFromString(String xml){
		JAXBContext jaxbContext = null;
		try {
			jaxbContext = JAXBContext.newInstance(SARoot.class, Category.class, Question.class,
					Answer.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			StringReader stringReader = new StringReader(xml);
			return (SARoot) jaxbUnmarshaller.unmarshal(stringReader);
		} catch (JAXBException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Stores All questions (including the answers/...) in a XML file
	 *
	 * @param root The root Object for the XML file
	 * @param file the (XML) File for storing
	 */
	@Override
	public void writeObjectsToXML(SARoot root, File file) {

		try {

			// setup Marshaller
			JAXBContext jaxbContext = JAXBContext.newInstance(SARoot.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			jaxbMarshaller.marshal(root, file);
			// the next Line will print the generated XML File to the console, for Debugging
			// only
			// jaxbMarshaller.marshal(newRoot, System.out);

		} catch (JAXBException e) {
			e.printStackTrace();
		}

	}


	public void setFile(File newFile) {
		this.file = newFile;
	}

	public void setRootelement(SARoot newRoot) {
		this.rootElement = newRoot;
	}

	public SARoot getRootelement() {
		return this.rootElement;
	}

}
