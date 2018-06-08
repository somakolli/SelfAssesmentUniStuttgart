package parser;

import domain.Answer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * @author tjehret
 * The Inferface for the Parser Class \n
 * Provides Methods to parse the Configuration File and convert it's elements into java objects
 */
public interface ParserInterface {

    String askForPath();

    File convertPath(String path);

    /**
     * starts this Parser
     */
    void startParser();

    /**
     * parses the configFile given as a XML-File !  <br/>
     * if successful, generated a List of all Objects found in the configFile
     *
     * @param configFile the XML - File to be parsed
     * @return The List of generated Objects
     * @throws FileNotFoundException if the File cannot be resolved
     */
    Properties parse(File configFile) throws FileNotFoundException;

    /**
     * generates one Object from the parsed configFile
     *
     * @param type       the Type (Class name) of the new object
     * @param attributes all attributes for the new object
     * @return The generated Object
     */
    void generateObject(String type, String attributes, String id);

    /**
     * Tries to add the given answer to a Question, if it exists
     *
     * @param answer The new answer that might be added
     * @param id     QuestionsId
     */
    void lookupAndChange(Answer answer, String id);


    /**
     * Extracts the ID of a given Key, e.g. the internal Mapping constant
     *
     * @param key the Key to retrieve the ID from
     * @return the ID of the Key
     */
    String getId(String key);

    /**
     * Extracts the actual Type of a given Key
     *
     * @param key the Key to retrieve the Type from
     * @return the Type of the Key
     */
    String getType(String key);
}