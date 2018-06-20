package parser;

import domain.*;

import java.util.List;

/**
 * @author tjehr
 * Interface for the parser
 */
public interface parserInterface {
    /**
     * starts this parserTry, source File may be given via Console <br/>
     * will create a JAXB Marshaller to handle the file.
     */

    /**
     * console interaction to set a new Path for the XML source
     */
    public void init();

    /**
     * starts the parser
     */
    public void startParser();

    /**
     * generated a List of Questions: order is the same as in the XML file
     *
     * @param root the root Element of the XML file
     * @return List of all Questions
     */
    public List<Question> generateQuestionList(SARoot root);

    /**
     * generated a List of Answers: order is the same as in the XML file
     *
     * @param root the root Element of the XML file
     * @return List of all Answers
     */
    public List<Answer> generateAnswerList(SARoot root);

    /**
     * Stores All questions (including the answers/...) in a XML file
     *
     * @param root SARoot that contains the List of all Questions
     * @param path      path for the new XML file
     */
    public void writeObjectsToXML(SARoot root, String path);
}
