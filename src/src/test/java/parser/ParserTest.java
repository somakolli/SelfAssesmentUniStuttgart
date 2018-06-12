package parser;

import domain.Answer;
import domain.Question;
import domain.TimeQuestion;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;

public class ParserTest {
    private Parser parser;
    private File testFile;

    @Before
    public void setUp() throws Exception {
        testFile = new File("src/test/test.xml");
        parser = new Parser(testFile);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void parse() throws Exception {
        Properties testProps = new Properties();
        testProps.setProperty("Question_1", "exampleQuestion");
        testProps.setProperty("Answer_1.1", "Answer for question 1, maybe this is longer\n" +
                "    then one line....\n" +
                "        bliblablup");
        testProps.setProperty("Answer_1.2", "Second Answer for question 1");
        testProps.setProperty("TimeQuestion_2", "example for a timed Question");
        testProps.setProperty("Time_2", "60");
        testProps.setProperty("Answer_2.1", "The Answer for the timed question!");
        testProps.setProperty("Mediapath_1.1","src/path/blablabla/.../here/lives/media1");
        testProps.setProperty("Mediapath_1.2","src/path/blablabla/.../here/lives/media2");
        testProps.setProperty("Mediapath_1.3","src/path/blablabla/.../here/lives/media3");
        Properties actualProps = parser.parse(testFile);

        Assert.assertEquals(testProps.stringPropertyNames(), actualProps.stringPropertyNames());
        Assert.assertEquals(testProps.getProperty("Question_1"), actualProps.getProperty("Question_1"));
        Assert.assertEquals(testProps.getProperty("Answer_1.1"), actualProps.getProperty("Answer_1.1"));
        Assert.assertEquals(testProps.getProperty("Answer_1.2"), actualProps.getProperty("Answer_1.2"));

    }

    @Test
    public void generateObject() throws Exception {

        Answer testAnswer = new Answer();
        testAnswer.setContent("Answer_1.1");

        List<Answer> answerList = new ArrayList<>();
        answerList.add(testAnswer);

        Question testQuestion = new Question();
        testQuestion.setQuestion("Question_1");
        testQuestion.setAnswers(answerList);

        parser.generateObject("Question", "Question_1", "1");
        parser.generateObject("Answer", "Answer_1.1", "1");
        Assert.assertEquals(parser.getGeneratedQuestions().get(0).getQuestion(), testQuestion.getQuestion());
        Assert.assertEquals(parser.getGeneratedQuestions().get(0).getAnswers().get(0).getContent(), testAnswer.getContent());

    }

    @Test
    public void startParserTest() {
        Answer testAnswer1 = new Answer();
        testAnswer1.setContent("Answer for question 1, maybe this is longer\n" +
                "    then one line....\n" +
                "        bliblablup");
        Answer testAnswer2 = new Answer();
        testAnswer2.setContent("Answer for question 1, maybe this is longer\n" +
                "    then one line....\n" +
                "        bliblablup");

        Answer testAnswer3 = new Answer();
        testAnswer3.setContent("The Answer for the timed question!");

        List<Answer> answerList = new ArrayList<>();
        answerList.add(testAnswer1);
        answerList.add(testAnswer2);

        List<Answer> answerListTimed = new ArrayList<>();
        answerListTimed.add(testAnswer3);


        Question testQuestion = new Question();
        testQuestion.setQuestion("Question_1");
        testQuestion.setAnswers(answerList);

        TimeQuestion testTimeQuestion = new TimeQuestion();
        testTimeQuestion.setQuestion("example for a timed Question");
        testTimeQuestion.setAnswers(answerListTimed);
        int testTime = 666;

        List<String> testMediapaths = new ArrayList<>();
        testMediapaths.add("/src/path/blablabla/.../here/lives/media1");
        testMediapaths.add("/src/path/blablabla/.../here/lives/media2");
        testMediapaths.add("/src/path/blablabla/.../here/lives/media3");

        parser.startParser();
        for (int i = 0; i < testQuestion.getAnswers().size(); i++) {
            Assert.assertEquals(testQuestion.getAnswers().get(i).getContent(), testAnswer1.getContent());
        }
        for (int i = 0; i < testTimeQuestion.getAnswers().size(); i++) {
            Assert.assertEquals(testTimeQuestion.getAnswers().get(i).getContent(), testAnswer3.getContent());

        }
        for (int i = 0; i < parser.getGeneratedTimeQuestions().size(); i++) {
            Assert.assertEquals(parser.getGeneratedTimeQuestions().get(i).getTime(), testTime);
        }
        //Check Mediapaths
        for (int i = 0; i<parser.getGeneratedQuestions().get(0).getMediaPaths().size(); i++) {
            Assert.assertEquals(parser.getGeneratedQuestions().get(0).getMediaPaths().get(i), testMediapaths.get(i));
        }
    }

}