package generator;

import domain.Category;
import domain.Question;
import domain.SARoot;
import helper.FileHelper;
import helper.MarkdownHelper;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import parser.Parser;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VGenerator implements VGeneratorInterface {
    private String path = "\\website";

    public VGenerator(String path) {
        this.path = path;
    }

    public VGenerator(){

    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String generateQuestion(Question question, String template) throws IOException {
        //convert Markdown to HTML and remove linebreaks
        question.setQuestion(MarkdownHelper.markdownToHtml(question.getQuestion()).replace("\n", "").replace("\r", ""));

        Velocity.init();
        Context context = new VelocityContext();
        context.put("question", question);
        StringWriter writer = new StringWriter();
        Velocity.evaluate(context, writer, "question", template);
        return writer.toString();
    }

    public void generateCategoriesJS(HashMap<Category, ArrayList<Question>> categoryMap){
        FileHelper fh = new FileHelper();
        String template = fh.getFileFromResources("templates/scripts/categories.tpl");
        Velocity.init();
        Context context = new VelocityContext();
        context.put("categoryMap", categoryMap);
        StringWriter writer = new StringWriter();
        Velocity.evaluate(context, writer, "categoryMap", template);
        fh.writeFileToResources(getPath() + "/questions/categories.js", writer.toString());
    }

    public void generateQCountJS(int questionCount){
        FileHelper fh = new FileHelper();
        String template = fh.getFileFromResources("templates/scripts/QCount.tpl");
        Velocity.init();
        Context context = new VelocityContext();
        context.put("questionCount", questionCount);
        StringWriter writer = new StringWriter();
        Velocity.evaluate(context, writer, "categoryMap", template);
        fh.writeFileToResources(getPath() + "/questions/QCount.js",writer.toString());
    }

    public void generateQuestions(SARoot saRoot) throws IOException {
        FileHelper fh = new FileHelper();
        String template = fh.getFileFromResources("templates/questions/question.tpl");
        for (Question question:
             saRoot.getQuestions()) {
            fh.writeFileToResources(getPath() + "/questions/"+question.getId() + ".json",generateQuestion(question, template));
        }
    }

    public static void main(String[] args) throws IOException {
        VGenerator vGenerator = new VGenerator();
        FileHelper fileHelper = new FileHelper();
        SARoot saRoot = Parser.getRootFromString(fileHelper.getFileFromResources("test.xml"));
        vGenerator.generateQuestions(saRoot);
        vGenerator.generateCategoriesJS(saRoot.getCategoryQuestionMap());
        vGenerator.generateQCountJS(saRoot.getQuestions().size());
    }
}
