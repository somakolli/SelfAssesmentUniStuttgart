package parser;

import parser.annotatedClasses.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * @author tjehr
 * Class for experimental use only ! <br/>
 * just testing the behavior of JAXB
 */
public class parserTry {

    //change name to "main" for experiments
    public static void Notmain(String[] args){
        List<String> testPaths = new ArrayList<>();
        testPaths.add("defaultpath");
        testPaths.add("defautpath2");

        AAnswer testAnswer1 = new AAnswer();
        testAnswer1.setContent("My first answer");
        testAnswer1.setMediaPath(testPaths);

        AAnswer testAnswer2 = new AAnswer();
        testAnswer2.setContent("My second answer");
        testAnswer2.setMediaPath(testPaths);

        List<AAnswer> testAnswers = new ArrayList<>();
        testAnswers.add(testAnswer1);
        testAnswers.add(testAnswer2);

        AQuestion testQuestion = new AQuestion();
        testQuestion.setQuestion("testQuestion");
        testQuestion.setAnswers(testAnswers);
        testQuestion.setMediaPaths(testPaths);
        testQuestion.setPoints(5);
        testQuestion.setTime(60);

        List<AQuestion> listedQuestions = new ArrayList<>();
        listedQuestions.add(testQuestion);

        AQuestions testQuestions = new AQuestions();
        testQuestions.setQuestions(listedQuestions);


        try {

            File testFile = new File("src/test/testJAXB.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(AQuestions.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(testQuestions, testFile);
            jaxbMarshaller.marshal(testQuestions, System.out);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    // change to name to "main" for experiments...
    public static void Neithermain (String[] args){

            try {

                File file = new File("src/test/testJAXB.xml");
                JAXBContext jaxbContext = JAXBContext.newInstance(AQuestions.class);

                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
               AQuestions testQuestions = (AQuestions) jaxbUnmarshaller.unmarshal(file);
                System.out.println(testQuestions.getQuestions().get(0).getAnswers().get(1).getContent());

            } catch (JAXBException e) {
                e.printStackTrace();
            }
    }

}
