package parser;

import domain.*;

import java.io.File;
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
public class parser implements parserInterface {

	private File file;
	private List<Question> generatedQuestions = new ArrayList<>();
	private List<Answer> generatedAnswers = new ArrayList<>();
	private SARoot rootElement = new SARoot();

	/**
	 * standard constructor, sets the default Path
	 */
	public parser() {

		this.file = new File("src/test/testJAXB.xml");
	}

	/**
	 * console interaction to set a new Path for the XML source
	 */
	@Override
	public void init() {
		// Scanner to read from Console
		System.out.println("starting parser...");
		Scanner scanner = new Scanner(System.in);
		System.out.println("Do you want to specify a new path for the XML source file?");
		System.out.println("[Y/N]");
		if (scanner.nextLine().toUpperCase().equals("Y")) {
			System.out.println("Enter new path:");
			String newPath = scanner.nextLine();
			try {
				this.file = new File(newPath);
				scanner.close();
			} catch (Exception e) {
				scanner.close();
				e.printStackTrace();
				System.out.println("File has not been changed!");
			}
		}

	}

	/**
	 * starts the parser
	 */
	@Override
	public void startParser() {
		// Console interaction currently deactivated
		// init();

		try {
			// setup marshaller
			JAXBContext jaxbContext = JAXBContext.newInstance(SARoot.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			// start marshaller to read XML file
			SARoot rootElement = (SARoot) jaxbUnmarshaller.unmarshal(this.file);
			// generate Lists for Objects
			this.generatedQuestions = generateQuestionList(rootElement);
			this.generatedAnswers = generateAnswerList(rootElement);
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Stores All questions (including the answers/...) in a XML file
	 *
	 * @param questions List of all Questions
	 * @param path      path for the new XML file
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

	/**
	 * generated a List of Questions: order is the same as in the XML file
	 *
	 * @param root the root Element of the XML file
	 * @return List of all Questions
	 */
	@Override
	public List<Question> generateQuestionList(SARoot root) {
		List<Question> newQuestions = new ArrayList<>();
		newQuestions.addAll(root.getQuestions());
		return newQuestions;
	}

	/**
	 * generated a List of Answers: order is the same as in the XML file
	 *
	 * @param root the root Element of the XML file
	 * @return List of all Answers
	 */
	@Override
	public List<Answer> generateAnswerList(SARoot root) {
		List<Answer> newAnswers = new ArrayList<>();
		for (Question q : root.getQuestions()) {
			newAnswers.addAll(q.getAnswers());
		}
		return newAnswers;
	}

	public List<Question> getGeneratedQuestions() {
		return generatedQuestions;
	}

	public void setGeneratedQuestions(List<Question> generatedQuerstions) {
		this.generatedQuestions = generatedQuerstions;
	}

	public List<Answer> getGeneratedAnswers() {
		return generatedAnswers;
	}

	public void setGeneratedAnswers(List<Answer> generatedAnswers) {
		this.generatedAnswers = generatedAnswers;
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
