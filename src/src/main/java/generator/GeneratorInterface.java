package generator;

import domain.Question;

import java.util.List;

public interface GeneratorInterface {

    String generateQuestion(Question question, String template);

    List<TemplateForLoop> getTemplateForLoops();

    List<TemplateObject> getTemplateObjects();

    void replaceStringContent(TemplateObject object, String variableName, Question question);

    String documentToString();

}
