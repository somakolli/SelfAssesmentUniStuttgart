package generator;

import domain.Answer;
import domain.Question;
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
        answer.setId(0);
        Answer answer1 = new Answer();
        answer1.setContent("Antwort2");
        answer1.setId(1);
        question.getAnswers().add(answer);
        question.getAnswers().add(answer1);

        VGenerator generator = new VGenerator();
        String generatedCode = generator.generateQuestion(question,
                "<h5>$question.getQuestion()</h5>\n" +
                        "<ul id=\"$question.getId()\" class=\"list-group list-group-flush\">\n" +
                        "    #foreach($answer in $question.getAnswers())\n" +
                        "    <li class=\"list-group-item\">\n" +
                        "        <div class=\"radio\"><label> <input type=\"radio\" id=\"$answer.getId()\" value=\"exampleAnswerValue\">$answer.getContent()</label> </div>\n" +
                        "    </li>\n" +
                        "    #end\n" +
                        "</ul>");

        Assert.assertEquals("<h1>Frage<h1>" +
                "<ul>" +
                "<li>Antwort1</li>"+
                "<li>Antwort2</li>"+
                "</ul>", generatedCode);

    }
}
