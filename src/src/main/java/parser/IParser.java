package parser;

import domain.*;

import java.io.File;
import java.util.List;

/**
 * @author tjehr
 * Interface for the parser
 */
public interface IParser {

    /**
     * console interaction to set a new Path for the XML source
     */
    public void init();

    /**
     * starts the parser
     */
    public void startParser();

    /**
     * Stores All questions (including the answers/...) in a XML file
     *
	 * @param root The root Object for the XML file
	 * @param file the (XML) File for storing
     */
    public void writeObjectsToXML(SARoot root, File file);
}
