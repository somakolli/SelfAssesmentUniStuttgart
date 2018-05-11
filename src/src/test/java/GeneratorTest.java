import Domain.Answer;
import Domain.Question;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class GeneratorTest {

    @Test
    public void generateQuestion() {
        Generator generator = new Generator();
        String template = "{{question.content}}";
        ArrayList<Answer> answers = new ArrayList<>();

        Question question = new Question("Frage 1", answers);
        String result = generator.generateQuestion(question, template);
        Assert.assertEquals("Frage 1", result);

        template = "<h1>{{question.content}}</h1>";
        result = generator.generateQuestion(question, template);
        Assert.assertEquals("<h1>Frage 1</h1>", result);

        Answer answer = new Answer("Answer 1");
        template = "<h1>{{question.content}}<h1>\n" +
                    "<ul>\n" +
                      "{% for answer in question.answers%}\n" +
                      "<li>{{answer.content}}</li>\n" +
                      "{% end for}" +
                    "</ul>";

    }
}