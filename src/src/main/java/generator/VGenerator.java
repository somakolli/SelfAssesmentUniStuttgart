package generator;

import domain.*;
import helper.FileHelper;
import helper.MarkdownHelper;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.zeroturnaround.zip.ByteSource;
import org.zeroturnaround.zip.FileSource;
import org.zeroturnaround.zip.ZipEntrySource;
import org.zeroturnaround.zip.ZipUtil;
import parser.Parser;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VGenerator implements VGeneratorInterface {

    public VGenerator(){

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

    private HashMap<String, String> generateCategoriesJS(HashMap<Category, ArrayList<Question>> categoryMap){
        HashMap<String, String> filesContentMap = new HashMap<>();
        FileHelper fh = new FileHelper();
        String template = fh.getFileFromResources("templates/scripts/categories.tpl");
        Velocity.init();
        Context context = new VelocityContext();
        context.put("categoryMap", categoryMap);
        StringWriter writer = new StringWriter();
        Velocity.evaluate(context, writer, "categoryMap", template);
        filesContentMap.put("scripts/categories.js", writer.toString());
        return filesContentMap;
    }

    private HashMap<String, String> generateQCountJS(int questionCount){
        HashMap<String, String> filesContentMap = new HashMap<>();
        FileHelper fh = new FileHelper();
        String template = fh.getFileFromResources("templates/scripts/QCount.tpl");
        Velocity.init();
        Context context = new VelocityContext();
        context.put("questionCount", questionCount);
        StringWriter writer = new StringWriter();
        Velocity.evaluate(context, writer, "categoryMap", template);
        filesContentMap.put("scripts/QCount.js",writer.toString());
        return filesContentMap;
    }

    private HashMap<String, String> generateQuestions(SARoot saRoot) throws IOException {
        HashMap<String, String> filesContentMap = new HashMap<>();
        FileHelper fh = new FileHelper();
        String template = fh.getFileFromResources("templates/questions/question.tpl");
        for (Question question:
             saRoot.getQuestions()) {
            filesContentMap.put("questions/"+question.getId() + ".json", generateQuestion(question, template));
        }
        return filesContentMap;
    }

    private HashMap<String, String> generateSolution(SARoot saRoot){
        HashMap<String, String> filesContentMap = new HashMap<>();
        FileHelper fh = new FileHelper();
        String template = fh.getFileFromResources("templates/scripts/solution.tpl");
        StringBuilder solution = new StringBuilder();
        for (Question question:
                saRoot.getQuestions()) {
            for (Answer answer: question.getAnswers()){
                solution.append(answer.getCorrect() ? "1" : "0");
            }
        }
        Velocity.init();
        Context context = new VelocityContext();
        context.put("solution", solution.toString());
        StringWriter writer = new StringWriter();
        Velocity.evaluate(context, writer, "solution", template);
        filesContentMap.put("scripts/solution.js",writer.toString());
        return filesContentMap;
    }

    private HashMap<String, String> generateConclusion(SARoot saRoot){
        HashMap<String, String> filesContentMap = new HashMap<>();
        FileHelper fh = new FileHelper();
        String template = fh.getFileFromResources("templates/questions/conclusion.tpl");
        Velocity.init();
        Context context = new VelocityContext();
        context.put("conclusion", saRoot.getConclusions());
        StringWriter writer = new StringWriter();
        Velocity.evaluate(context, writer, "conclusion", template);
        filesContentMap.put("questions/conclusion.json",writer.toString());
        return filesContentMap;
    }

    public static void main(String[] args) throws IOException {
        VGenerator vGenerator = new VGenerator();
        FileHelper fileHelper = new FileHelper();
        SARoot saRoot = Parser.getRootFromString(fileHelper.getFileFromResources("test.xml"));
        saRoot.getConclusions().add(new Conclusion(20, "very Bad"));
        saRoot.getConclusions().add(new Conclusion(50, "not to bad"));
        vGenerator.createZipArchive(saRoot, "website.zip");
    }

    private HashMap<String, String> getFilesContentMap(SARoot saRoot) throws IOException {
        HashMap<String, String> filesContentMap = new HashMap<>();
        filesContentMap.putAll(generateQuestions(saRoot));
        filesContentMap.putAll(generateSolution(saRoot));
        filesContentMap.putAll(generateCategoriesJS(saRoot.getCategoryQuestionMap()));
        filesContentMap.putAll(generateQCountJS(saRoot.getQuestions().size()));
        return filesContentMap;
    }

    public void createZipArchive(SARoot saRoot, String path) throws IOException {
        File websiteFile = new File(path);
        ZipUtil.pack(new File(getClass().getClassLoader().getResource("website").getFile()), websiteFile);
        ArrayList<ZipEntrySource> entries = new ArrayList<>();
        for (HashMap.Entry<String, String> entry : getFilesContentMap(saRoot).entrySet()){
            entries.add(new ByteSource(entry.getKey(), entry.getValue().getBytes()));
        }
        ZipEntrySource[] entriesArray = entries.toArray(new ZipEntrySource[entries.size()]);
        ZipUtil.addOrReplaceEntries(websiteFile, entriesArray);
    }
}
