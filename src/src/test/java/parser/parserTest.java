package parser;

import domain.Answer;
import domain.Question;
import domain.SARoot;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class parserTest {
    private parser parser = new parser();
    private Question testQuestion = new Question();
    private Question testQuestion2 = new Question();
    private Answer testAnswer1 = new Answer();
    private Answer testAnswer2 = new Answer();
    private List<Answer> testAnswers = new ArrayList<>();
    private List<String> testMedia = new ArrayList<>();
    private String testOutputPath = "src/test/testOutputJAXB.xml";

    @Before
    public void setUp() throws Exception {
        // init Mediapaths
        testMedia.add("defaultpath");
        testMedia.add("defaultpath2");
        // init Answers
        testAnswer1.setContent("My first answer");
        testAnswer1.setMediaPath(testMedia);
        testAnswer2.setContent("My second answer");
        testAnswer2.setMediaPath(testMedia);
        testAnswers.add(testAnswer1);
        testAnswers.add(testAnswer2);
        // init Questions
        testQuestion.setQuestion("testQuestion");
        testQuestion.setPoints(5);
        testQuestion.setTime(60);
        testQuestion.setAnswers(testAnswers);
        testQuestion.setMediaPaths(testMedia);
    }


    @Test
    public void testStartParser() throws Exception {

        parser.startParser();

        //check Questions (question/points/time)
        Assert.assertEquals(testQuestion.getQuestion(), parser.getGeneratedQuestions().get(0).getQuestion());
        Assert.assertEquals(testQuestion2.getQuestion(), parser.getGeneratedQuestions().get(1).getQuestion());
        Assert.assertEquals(testQuestion.getPoints(), parser.getGeneratedQuestions().get(0).getPoints());
        Assert.assertEquals(testQuestion2.getPoints(), parser.getGeneratedQuestions().get(1).getPoints());
        Assert.assertEquals(testQuestion.getTime(), parser.getGeneratedQuestions().get(0).getTime());
        Assert.assertEquals(testQuestion2.getTime(), parser.getGeneratedQuestions().get(1).getTime());

        //check Answers (this check is really lazy...)
        //content
        Assert.assertEquals(testQuestion.getAnswers().get(0).getContent(), parser.getGeneratedAnswers().get(0).getContent());
        Assert.assertEquals(testQuestion.getAnswers().get(1).getContent(), parser.getGeneratedAnswers().get(1).getContent());
        //media Path
        Assert.assertEquals(testQuestion.getAnswers().get(0).getMediaPath().get(0), parser.getGeneratedAnswers().get(0).getMediaPath().get(0));
        Assert.assertEquals(testQuestion.getAnswers().get(0).getMediaPath().get(1), parser.getGeneratedAnswers().get(0).getMediaPath().get(1));
        Assert.assertEquals(testQuestion.getAnswers().get(1).getMediaPath().get(0), parser.getGeneratedAnswers().get(1).getMediaPath().get(0));
        Assert.assertEquals(testQuestion.getAnswers().get(1).getMediaPath().get(1), parser.getGeneratedAnswers().get(1).getMediaPath().get(1));

        //System.out.println("finished parsing.");
    }

    /**
     * the new test file will be created at: src/test/testOutputJAXB.xml
     * @throws Exception -
     */
    @Test
    public void testWriteObjects() throws Exception {
        // setup List of test-questions
        List<Question> testQuestions = new ArrayList<>();
        testQuestions.add(testQuestion);
        testQuestions.add(testQuestion2);
        SARoot newRoot = new SARoot();
        newRoot.setQuestions(testQuestions);
        // let parser create a new XML file
        //System.out.println("A new test file has been created at: "+testOutputPath);
        parser.writeObjectsToXML(newRoot, testOutputPath);

    }

    @After
    public void tearDown() throws Exception {
    }
}