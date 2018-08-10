import domain.Answer;
import domain.Question;
import domain.SARoot;
import generator.VGeneratorInterface;
import generator.VGenrator;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;

public class VGeneratorTest {
    private final String testTemplatePath = "question.vm";

    @Test
    public void testFileGenreation() throws IOException {
        FileWriter fw = new FileWriter("test");
        fw.write("Hallo");


        Question question = new Question();
        question.setQuestion("Frage");
        Answer answer = new Answer();
        answer.setContent("Antwort1");
        Answer answer1 = new Answer();
        answer1.setContent("Antwort2");
        question.getAnswers().add(answer);
        question.getAnswers().add(answer1);

        VGenrator generator = new VGenrator();
        String generatedCode = generator.generateQuestion(question,
                "<h1>$question.getQuestion()<h1>" +
                        "<ul>" +
                        "#foreach($answer in $question.getAnswers())"+
                        "<li>$answer.getContent()</li>"+
                                "#end"+
                        "</ul>");

        Assert.assertEquals("<h1>Frage<h1>" +
                "<ul>" +
                "<li>Antwort1</li>"+
                "<li>Antwort2</li>"+
                "</ul>", generatedCode);

    }
}
