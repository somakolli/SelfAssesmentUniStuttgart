package generator;

import domain.Question;
import domain.SARoot;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class VGenrator implements VGeneratorInterface {
    @Override
    public String generateQuestion(Question question, String template) throws IOException {

        Velocity.init();

        Context context = new VelocityContext();

        context.put("question", question);

        StringWriter writer = new StringWriter();

        Velocity.evaluate(context, writer, "question", template);

        return writer.toString();
    }
}
