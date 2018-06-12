package parser;

import domain.Answer;
import domain.Question;
import domain.TimeQuestion;

import java.io.*;
import java.util.*;

/**
 * @author tjehr
 * Class for the Parser, currently unstable.
 */
public class Parser implements ParserInterface {

    //private final String terminate = "<endConfig>";
    private File file;
    private HashMap<String, Object> domain;
    private Properties properties;
    private List<Question> generatedQuestions;
    private List<Answer> generatedAnswers;
    private List<TimeQuestion> generatedTimeQuestions;
    private List<Pair> timeBuffer;
    private List<Pair> mediaBuffer;
    private List<Pair> pairs;

    /**
     * Constructor for the Parser. Domain is added manually!
     *
     * @param configFile the File to read from
     */
    public Parser(File configFile) {
        this.pairs = new ArrayList<>();
        this.timeBuffer = new ArrayList<>();
        this.mediaBuffer = new ArrayList<>();
        this.generatedAnswers = new ArrayList<>();
        this.generatedQuestions = new ArrayList<>();
        this.generatedTimeQuestions = new ArrayList<>();
        // domain-List will contain one Object of each Class from out domain
        this.domain = new HashMap<>();
        this.domain.put("Answer", new Answer());
        this.domain.put("Question", new Question());
        this.domain.put("TimeQuestion", new TimeQuestion());
        this.file = configFile;
    }

    @Override
    public String askForPath() {
        // TODO implement functionality...
        return null;
    }

    @Override
    public File convertPath(String path) {
        // TODO implement functionality...
        return null;
    }


    /**
     * starts this Parser
     */
    @Override
    public void startParser() {
        System.out.println("Parser started...");
        try {

            properties = parse(this.file);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("Critical Failure. EXITING!");
        }
        //iterate over all Keys ( questions/answers )
        for (String key : properties.stringPropertyNames()) {
            // OBJECT
            //System.out.println(properties.getProperty(key));
            //System.out.println(properties.stringPropertyNames());
            generateObject(getType(key), properties.getProperty(key), getId(key));
        }
        System.out.println("Objects added:" + (generatedQuestions.size() + generatedAnswers.size() + generatedTimeQuestions.size()));
    }


    /**
     * parses the configFile given as a XML-File !  <br/>
     * if successful, Data is stored in properties
     *
     * @param configFile the XML - File to be parsed
     * @return The List of generated Objects
     * @throws FileNotFoundException if the File cannot be resolved
     */
    @Override
    public Properties parse(File configFile) throws FileNotFoundException {
        //Keys must be unique!
        InputStream stream = new FileInputStream(configFile);
        Properties props = new Properties();
        try {
            props.loadFromXML(stream);
            stream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return props;
    }


    /**
     * generates one Object from the parsed configFile
     *
     * @param type       the Type (Class name) of the new object
     * @param attributes all attributes for the new object
     * @return The generated Object
     */
    @Override
    public void generateObject(String type, String attributes, String id) {
        //containsKey uses .equals()
        //System.out.println("Type: "+type+" Attributes: "+attributes+ " id: "+id);
        switch (type) {
            case "Answer":
                Answer newAnswer = new Answer();
                newAnswer.setContent(attributes);
                //the ID is calculated in getID(Key)
                lookupAndChange(newAnswer, id);
                generatedAnswers.add(newAnswer);
                break;
            case "Question":
                Question newQuestion = new Question();
                //the ID is calculated in getID(Key)
                register(newQuestion, id);
                newQuestion.setQuestion(attributes);
                generatedQuestions.add(newQuestion);
                break;
            case "TimeQuestion":
                TimeQuestion newTimeQuestion = new TimeQuestion();
                register(newTimeQuestion, id);
                newTimeQuestion.setQuestion(attributes);
                generatedTimeQuestions.add(newTimeQuestion);
                break;
            case "Time":
                lookupAndChangeTime(attributes, id);
                break;
            case "Mediapath":
                lookupAndChangeMedia(attributes, id);
                break;


        }
    }


    /**
     * Method to store all Questions
     *
     * @param question the new question to be stored
     */
    private void register(Question question, String id) {
        this.pairs.add(new Pair(question, id));

    }

    /**
     * Method to store all TimeQuestions
     *
     * @param question the new question to be stored
     */
    private void register(TimeQuestion question, String id) {
        this.pairs.add(new Pair(question, id));
        for (Pair time : timeBuffer) {
            if (time.getId().equals(id)) {
                //System.out.println("Setting time, question");
                question.setTime(Integer.valueOf(time.getTime()));
                //System.out.println(question.getTime());
            }
        }
    }

    /**
     * Tries to add the given answer to a Question, if it exists
     *
     * @param answer The new answer that might be added
     * @param id     QuestionsId
     */
    public void lookupAndChange(Answer answer, String id) {
        for (Pair pair : pairs) {
            if (pair.getId().equals(id)) {
                pair.addAnswer(answer);

            }
        }
    }

    /**
     * search corresponding TIME-QUESTION and set the time <br/>
     * if there is no question jet, the time will be stored in timeBuffer
     *
     * @param time the time to set
     * @param id   the ID of the corresponding TIME-QUESTION
     */
    public void lookupAndChangeTime(String time, String id) {
        boolean set = false;
        for (Pair pair : pairs) {
            if (pair.getId().equals(id)) {
                //System.out.println(Integer.valueOf(time));
                pair.getTimeQuestion().setTime(Integer.valueOf(time));
                set = true;
            }
        }
        if (!set) {
            timeBuffer.add(new Pair(time, id, false));
        }
    }

    /**
     * search corresponding QUESTION and update it's Mediapath <br/>
     * if there is no question jet, the Mediapath will be stored in
     *
     * @param mediaPath the new Mediapath will be stored in mediapathBuffer
     * @param id        the ID of the QUESTION
     */
    public void lookupAndChangeMedia(String mediaPath, String id) {
        boolean set = false;
        for (Pair pair : pairs) {
            if (pair.getId().equals(id)) {
                pair.addMediaPaths(mediaPath);
                set = true;
            }
        }
        if (!set) {
            mediaBuffer.add(new Pair(mediaPath, id, true));
        }
    }

    /**
     * Extracts the ID of a given Key, e.g. the internal Mapping constant
     *
     * @param key the Key to retrieve the ID from
     * @return the ID of the Key
     */
    public String getId(String key) {
        int start = key.indexOf('_');
        int end = key.indexOf('.');
        //System.out.println(key);
        if (key.contains(".")) {
            return key.substring(start + 1, end);
        } else {
            return key.substring(start + 1, key.length());
        }
    }

    /**
     * Extracts the actual Type of a given Key
     *
     * @param key the Key to retrieve the Type from
     * @return the Type of this Key
     */
    public String getType(String key) {
        int end = key.indexOf('_');
        return key.substring(0, end);
    }

    private Question getQuestionById(String id) {
        for (Pair p : pairs) {
            if (p.getId().equals(id)) {
                return p.getQuestion();
            }
        }
        return null;
    }

    public List<Question> getGeneratedQuestions() {
        return this.generatedQuestions;
    }

    public List<Answer> getGeneratedAnswers() {
        return this.generatedAnswers;
    }

    public Properties getProperties() {
        return this.properties;
    }

    public List<TimeQuestion> getGeneratedTimeQuestions() {
        return generatedTimeQuestions;
    }
}
