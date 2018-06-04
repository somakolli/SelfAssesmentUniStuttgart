import domain.Question;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GeneratorTest {
    private final String testTemplatePath = "test.html";
    private Generator generator;
    private void createFile(String string, String path){
        try {
            FileWriter fileWriter = new FileWriter(testTemplatePath);
            fileWriter.write(string);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteFile(String path){
        File file = new File(testTemplatePath);
        file.delete();
    }

    @Before
    public void init() throws IOException {

        createFile("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Uni Stuttgart Self Assesment</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div sa-object=\"question\">\n" +
                "    <h1>{{question.content}}</h1>\n" +
                "    <ul sa-for=\"answer in question.answers\">\n" +
                "        <li>\n" +
                "            {{answer.content}}\n" +
                "        </li>\n" +
                "    </ul>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>",testTemplatePath);
        File file = new File(testTemplatePath);
        generator = new Generator(file);
    }

    @After
    public void cleanUp(){
        deleteFile(testTemplatePath);
    }

    @Test
    public void testGeneration(){

    }

    @Test
    public void getTemplateObjectsTest(){
        TemplateObject templateObject = new TemplateObject();
        templateObject.setContent("<h1>{{question.content}}</h1>\n" +
                "<ul sa-for=\"answer in question.answers\"> \n" +
                " <li> {{answer.content}} </li> \n" +
                "</ul>");
        templateObject.setVariableName("question");

        List<TemplateObject> expectedTemplateObjects = new ArrayList<>();
        expectedTemplateObjects.add(templateObject);

        List<TemplateObject> actualTemplateObjects = generator.getTemplateObjects();

        Assert.assertEquals(expectedTemplateObjects.size(), actualTemplateObjects.size());

        for(int i = 0; i < actualTemplateObjects.size(); i++){
            Assert.assertEquals(expectedTemplateObjects.get(i), actualTemplateObjects.get(i));
        }

    }

    @Test
    public void getTemplateForLoopsTest(){
        TemplateForLoop templateForLoop = new TemplateForLoop();
        templateForLoop.setContent("<li> {{answer.content}} </li>");
        templateForLoop.setCollectionName("question.answers");
        templateForLoop.setVariableName("answer");
        List<TemplateForLoop> exptectedTemplateForLoops = new ArrayList<>();
        exptectedTemplateForLoops.add(templateForLoop);

        File templateFile = new File("test.html");
        List<TemplateForLoop> actualTemplateForLoops = generator.getTemplateForLoops();
        Assert.assertEquals(exptectedTemplateForLoops.size(), actualTemplateForLoops.size());

        for (int i = 0; i< actualTemplateForLoops.size();i++){
            Assert.assertEquals(exptectedTemplateForLoops, actualTemplateForLoops);
        }
    }

    @Test
    public void variableReplacementTest(){
        String expectedDocument = "<!doctype html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Uni Stuttgart Self Assesment</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div sa-object=\"question\">\n" +
                "    <h1>Test</h1>\n" +
                "    <ul sa-for=\"answer in question.answers\">\n" +
                "        <li>\n" +
                "            {{answer.content}}\n" +
                "        </li>\n" +
                "    </ul>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";
        Question question = new Question();
        question.setQuestion("Test");
        List<TemplateObject> objects = generator.getTemplateObjects();
        TemplateObject templateQuestion = objects.get(0);
        generator.replaceStringContent(templateQuestion, "question", question);
        String actualDocument = generator.documentToString();

        Assert.assertEquals(expectedDocument, actualDocument);
    }
}
