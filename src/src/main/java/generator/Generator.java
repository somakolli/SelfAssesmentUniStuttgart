package generator;

import domain.Question;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Generator implements GeneratorInterface{

    Document document;

    public Generator(File templateFile) throws IOException {
        document = Jsoup.parse(templateFile, "UTF-8");
    }

    public String generateQuestion(Question question, String template) {
        return "";
    }

    @Override
    public List<TemplateForLoop> getTemplateForLoops() {
        List<TemplateForLoop> templateForLoops = new ArrayList<>();

        Elements forLoops = document.getElementsByAttribute("sa-for");
        for (Element forLoop : forLoops){
            TemplateForLoop templateForLoop = new TemplateForLoop();
            templateForLoop.setContent(forLoop.children().toString());
            String[] forLoopExpression = forLoop.attr("sa-for").split(" ");
            templateForLoop.setVariableName(forLoopExpression[0]);
            templateForLoop.setCollectionName(forLoopExpression[2]);
            templateForLoop.setHtmlElement(forLoop);
            templateForLoops.add(templateForLoop);
        }

        return templateForLoops;
    }


    @Override
    public List<TemplateObject> getTemplateObjects() {
        List<TemplateObject> templateObjects = new ArrayList<>();


        Elements objects = document.getElementsByAttribute("sa-object");
        for (Element object :
                objects) {
            TemplateObject templateObject = new TemplateObject();
            templateObject.setContent(object.children().toString());
            templateObject.setVariableName(object.attr("sa-object"));
            templateObject.setHtmlElement(object);
            templateObjects.add(templateObject);

        }

        return templateObjects;


    }

    @Override
    public void replaceStringContent(TemplateObject object, String variableName, Question question) {
        String innerObjectString = object.getHtmlElement().children().toString();
        List<TemplateVariable> templateVariables = getVariables(innerObjectString);
        String variableString = templateVariables.get(0).toString();
        System.out.print(variableString);
    }

    private List<TemplateVariable> getVariables(String innerObjectString) {
        List<TemplateVariable> variables = new ArrayList<>();
        char[] innerObjectChars = innerObjectString.toCharArray();
        String variable = "";
        for (int i = 0; i < innerObjectChars.length; i++){
            if(innerObjectChars[i] == '{' && innerObjectChars[i+1] == '{'){
                TemplateVariable templateVariable = new TemplateVariable();
                i += 2;
                while (true){
                    variable += innerObjectChars[i];
                    i++;
                    if(innerObjectChars[i] == '}' && innerObjectChars[i+1] == '}'){
                        String[] splitVariable = variable.split("\\.");
                        templateVariable.setObject(splitVariable[0]);
                        templateVariable.setProperty(splitVariable[1]);
                        variables.add(templateVariable);
                        variable = "";
                        break;
                    }
                }
            }
        }
        return variables;
    }

    @Override
    public String documentToString() {
        return document.toString();
    }


}
