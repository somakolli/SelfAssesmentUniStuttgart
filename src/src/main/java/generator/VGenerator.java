package generator;

import domain.*;
import helper.FileHelper;
import helper.MarkdownHelper;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.zeroturnaround.zip.ByteSource;
import org.zeroturnaround.zip.ZipEntrySource;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class VGenerator implements VGeneratorInterface {

    public VGenerator(){

    }

    /**
     * @param question A question which Content will transformed to HTML and inserted to the template
     * @param template A template which describes the Structure of the HTML
     * @return HTML-String which is dervide from the question and template
     */
    private String generateQuestion(Question question, String template){
        //convert Markdown to HTML and remove linebreaks
        question.setQuestion(MarkdownHelper.markdownToHtml(question.getQuestion()).replace("\n", "").replace("\r", ""));
        for (Answer answer :
                question.getAnswers()) {
            answer.setContent(MarkdownHelper.markdownToHtml(question.getQuestion()).replace("\n", "").replace("\r", ""));
        }
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

    private HashMap<String, String> generateQuestions(SARoot saRoot) {
        HashMap<String, String> filesContentMap = new HashMap<>();
        FileHelper fh = new FileHelper();
        String template = fh.getFileFromResources("templates/questions/question.tpl");
        for (Question question:
             saRoot.getQuestions()) {
        filesContentMap.put("questions/"+question.getId() + ".json", generateQuestion(new Question(question), template));
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

    private HashMap<String, String> getFilesContentMap(SARoot saRoot){
        HashMap<String, String> filesContentMap = new HashMap<>();
        filesContentMap.putAll(generateConclusion(saRoot));
        filesContentMap.putAll(generateQuestions(saRoot));
        filesContentMap.putAll(generateSolution(saRoot));
        filesContentMap.putAll(generateCategoriesJS(saRoot.getCategoryQuestionMap()));
        filesContentMap.putAll(generateQCountJS(saRoot.getQuestions().size()));
        return filesContentMap;
    }

    private Question parseMarkdown(Question question){
        Question questionCopy = new Question(question);
        questionCopy.setQuestion(MarkdownHelper.markdownToHtml(question.getQuestion()));
        for (Answer answer :
                questionCopy.getAnswers()) {
            answer.setContent(MarkdownHelper.markdownToHtml(answer.getContent()));
        }
        return questionCopy;
    }

    public void createZipArchive(SARoot saRoot, String path){
        File websiteFile = new File(path);
        ZipUtil.pack(new File(Objects.requireNonNull(getClass().getClassLoader().getResource("website")).getFile()), websiteFile);
        ArrayList<ZipEntrySource> entries = new ArrayList<>();
        for (HashMap.Entry<String, String> entry : getFilesContentMap(new SARoot(saRoot)).entrySet()){
            entries.add(new ByteSource(entry.getKey(), entry.getValue().getBytes()));
        }
        ZipEntrySource[] entriesArray = entries.toArray(new ZipEntrySource[0]);
        ZipUtil.addOrReplaceEntries(websiteFile, entriesArray);
    }

    public String getQuestionHtml(Question question){
        Question questionCopy = new Question(question);
        questionCopy.setQuestion(MarkdownHelper.markdownToHtml(question.getQuestion()));
        for (Answer answer :
                questionCopy.getAnswers()) {
            answer.setContent(MarkdownHelper.markdownToHtml(answer.getContent()));
        }
        FileHelper fh = new FileHelper();
        String indexTemplate = fh.getFileFromResources("preview/index.tpl");
        Velocity.init();
        Context context = new VelocityContext();
        context.put("question", questionCopy);
        StringWriter writer = new StringWriter();
        Velocity.evaluate(context, writer, "questionPreview", indexTemplate);
        return writer.toString();
    }
    public String getCategoryHtml(Category category){
        return MarkdownHelper.markdownToHtml(category.getContent());
    }

    public String getConclusionHtml(Conclusion conclusion){
        return MarkdownHelper.markdownToHtml(conclusion.getContent());
    }
}
