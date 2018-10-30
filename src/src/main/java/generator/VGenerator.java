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
import java.util.*;

public class VGenerator implements VGeneratorInterface {

    private String previewTemplate;

    public String getPreviewTemplate() {
        return previewTemplate;
    }

    public VGenerator(){
        generatePreviewTemplate();
    }

    /**
     * @param question A question which Content will transformed to HTML and inserted to the template
     * @param template A template which describes the Structure of the HTML
     * @return HTML-String which is dervide from the question and template
     */
    private String generateQuestion(Question question, String template){
        //convert Markdown to HTML and remove linebreaks
        Question questionCopy = new Question(question);
        questionCopy.setContent(MarkdownHelper.markdownToHtml(questionCopy.getContent()).replace("\n", "").replace("\r", ""));
        ArrayList<Answer> answers = new ArrayList<>();
        
        for (Answer answer :
                questionCopy.getAnswers()) {
            Answer answerCopy = new Answer(answer);
            answerCopy.setContent(MarkdownHelper.markdownToHtml(question.getContent()).replace("\n", "").replace("\r", ""));
            answers.add(answerCopy);
        }
        questionCopy.setAnswers(answers);
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
        for (Question question:
             saRoot.getQuestions()) {
            String templatePath = question.isSingleChoice() ? "templates/questions/SCquestion.tpl" : "templates/questions/question.tpl";
            String template = fh.getFileFromResources(templatePath);
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
        questionCopy.setContent(MarkdownHelper.markdownToHtml(question.getContent()));
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

    private void generatePreviewTemplate(){
        FileHelper fh = new FileHelper();
        String template = fh.getFileFromResources("preview/index.tpl");
        Velocity.init();
        Context context = new VelocityContext();
        context.put("bottomContentStyleCss" , fh.getFileFromResources( "website/css/bottomContentStyle.css"));
        context.put("colorCss" , fh.getFileFromResources( "website/css/color.css"));
        context.put("containerStyleCss" , fh.getFileFromResources( "website/css/containerStyle.css"));
        context.put("evaluationStyleCss" , fh.getFileFromResources( "website/css/evaluationStyle.css"));
        context.put("mediaQuestionStyleCss" , fh.getFileFromResources( "website/css/mediaQuestionStyle.css"));
        context.put("multiCheckboxStyleCss" , fh.getFileFromResources( "website/css/multiCheckboxStyle.css"));
        context.put("overviewStyleCss" , fh.getFileFromResources( "website/css/overviewStyle.css"));
        context.put("topContentStyleCss" , fh.getFileFromResources( "website/css/topContentStyle.css"));
        context.put("bootstrapCss", fh.getFileFromResources( "website/css/bootstrap.min.css"));
        StringWriter writer = new StringWriter();
        Velocity.evaluate(context, writer, "preview", template);
        previewTemplate = writer.toString();
    }

    public String getQuestionHtml(Question question){
        Question questionCopy = new Question(question);
        questionCopy.setContent(MarkdownHelper.markdownToHtml(question.getContent()));
        ArrayList<Answer> answers = new ArrayList<>();
        for (Answer answer :
                questionCopy.getAnswers()) {
            Answer answerCopy = new Answer(answer);
            answerCopy.setContent(MarkdownHelper.markdownToHtml(answer.getContent()));
            answers.add(answerCopy);
        }
        questionCopy.setAnswers(answers);
        FileHelper fh = new FileHelper();
        Velocity.init();
        Context context = new VelocityContext();
        context.put("question", questionCopy);
        StringWriter writer = new StringWriter();
        Velocity.evaluate(context, writer, "questionPreview", previewTemplate);
        return writer.toString();
    }
    public String getCategoryHtml(Category category){
        return MarkdownHelper.markdownToHtml(category.getContent());
    }

    public String getConclusionHtml(Conclusion conclusion){
        return MarkdownHelper.markdownToHtml(conclusion.getContent());
    }
}
