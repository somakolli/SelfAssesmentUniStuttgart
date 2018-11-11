package generator;

import domain.*;
import helper.FileHelper;
import helper.MarkdownHelper;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.zeroturnaround.zip.ByteSource;
import org.zeroturnaround.zip.ZipEntrySource;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.util.*;

public class VGenerator implements VGeneratorInterface {

    private String mediaPath = "";

    private String mCPreviewTemplate;

    private String sCPreviewTemplate;

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    private HashMap<String, byte[]> bytesMap = new HashMap<>();

    public VGenerator(){
         mCPreviewTemplate = generatePreviewTemplate("MCquestion.tpl");
         sCPreviewTemplate = generatePreviewTemplate("SCquestion.tpl");
    }

    /**
     * @param question A question which Content will transformed to HTML and inserted to the template
     * @param jsonTemplate A template which describes the Structure of the HTML
     */
    private void generateQuestion(Question question, String jsonTemplate, String htmlTemplate){
        //convert Markdown to HTML and remove linebreaks
        Question questionCopy = new Question(question);
        questionCopy.setContent(MarkdownHelper.markdownToHtml(questionCopy.getContent()));
        ArrayList<Answer> answers = new ArrayList<>();
        
        for (Answer answer :
                questionCopy.getAnswers()) {
            Answer answerCopy = new Answer(answer);
            answerCopy.setContent(MarkdownHelper.markdownToHtml((answerCopy.getContent())));
            answers.add(answerCopy);
        }
        questionCopy.setAnswers(answers);
        Velocity.init();
        Context context = new VelocityContext();
        context.put("question", questionCopy);
        StringWriter jsonWriter = new StringWriter();
        StringWriter htmlWriter = new StringWriter();
        Velocity.evaluate(context, jsonWriter, "question", jsonTemplate);
        Velocity.evaluate(context, htmlWriter, "question", htmlTemplate);
        bytesMap.put("questions/"+question.getId() + ".json", jsonWriter.toString().getBytes());
        bytesMap.put("questions/"+question.getId() + ".html", replaceImgAndVideoSrc(htmlWriter.toString()).getBytes());
    }

    private void generateTempFile(){
        FileHelper fh = new FileHelper();
        String template = fh.getFileFromResources("templates/filenames.tpl");
        Velocity.init();
        Context context = new VelocityContext();
        List<String> filenames = null;
        try {
            filenames = fh.getFileMapFromResourceDirectory(new File(getClass().getClassLoader().getResource("website").getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        context.put("filenames", filenames);
        StringWriter writer = new StringWriter();
        Velocity.evaluate(context, writer, "categoryMap", template);
        bytesMap.put("filenames.txt", writer.toString().getBytes());
    }

    private void generateCategoriesJS(HashMap<Category, ArrayList<Question>> categoryMap){
        FileHelper fh = new FileHelper();
        String template = fh.getFileFromResources("templates/scripts/categories.tpl");
        Velocity.init();
        Context context = new VelocityContext();
        context.put("categoryMap", categoryMap);
        StringWriter writer = new StringWriter();
        Velocity.evaluate(context, writer, "categoryMap", template);
        bytesMap.put("scripts/categories.js", writer.toString().getBytes());
    }

    private void generateQCountJS(int questionCount){
        FileHelper fh = new FileHelper();
        String template = fh.getFileFromResources("templates/scripts/QCount.tpl");
        Velocity.init();
        Context context = new VelocityContext();
        context.put("questionCount", questionCount);
        StringWriter writer = new StringWriter();
        Velocity.evaluate(context, writer, "categoryMap", template);
        bytesMap.put("scripts/QCount.js",writer.toString().getBytes());
    }

    private void generateQuestions(SARoot saRoot) {
        FileHelper fh = new FileHelper();
        for (Question question:
             saRoot.getQuestions()) {
            String templatePath = "templates/questions/question.tpl";

            String jsonTemplate = fh.getFileFromResources(templatePath);
            String htmlTemplate;
            if (question.isSingleChoice()){
                htmlTemplate = fh.getFileFromResources("templates/questions/SCquestionHtml.tpl");
            }else {
                htmlTemplate = fh.getFileFromResources("templates/questions/questionHtml.tpl");
            }
            generateQuestion(new Question(question), jsonTemplate, htmlTemplate);
        }
    }

    private void generateSolution(SARoot saRoot){
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
        bytesMap.put("scripts/solution.js",writer.toString().getBytes());

    }

    private void generatePoints(SARoot saRoot){
        FileHelper fh = new FileHelper();
        String template = fh.getFileFromResources("templates/scripts/points.tpl");
        Velocity.init();
        Context context = new VelocityContext();
        context.put("questions", saRoot.getQuestions());
        StringWriter writer = new StringWriter();
        Velocity.evaluate(context, writer, "points", template);
        bytesMap.put("scripts/points.js",writer.toString().getBytes());
    }

    private void generateConclusion(SARoot saRoot){
        FileHelper fh = new FileHelper();
        String template = fh.getFileFromResources("templates/questions/conclusion.tpl");
        Velocity.init();
        Context context = new VelocityContext();
        context.put("conclusions", saRoot.getConclusions());
        StringWriter writer = new StringWriter();
        Velocity.evaluate(context, writer, "conclusions", template);
        bytesMap.put("questions/conclusion.json",writer.toString().getBytes());
    }

    private void fillByteMap(SARoot saRoot){
        generateConclusion(saRoot);
        generateQuestions(saRoot);
        generateSolution(saRoot);
        generateCategoriesJS(saRoot.getCategoryQuestionMap());
        generateQCountJS(saRoot.getQuestions().size());
        generatePoints(saRoot);
        addStaticFilesToByteMap();
    }

    private String replaceImgAndVideoSrcForTemplate(String htmlString){
        Document htmlDoc = Jsoup.parse(htmlString);
        for(Element video : htmlDoc.select("source[src]")){
            String filePath = "file:" + mediaPath + video.attr("src");
            video.attr("src", filePath);
        }
        for(Element img : htmlDoc.select("img[src]")){
            String filePath = "file:" + mediaPath + img.attr("src");
            img.attr("src", filePath);
        }
        return htmlDoc.toString();
    }

    private String replaceImgAndVideoSrc(String htmlString){
        Document htmlDoc = Jsoup.parse(htmlString);
        for(Element video : htmlDoc.select("source[src]")){
            File file = new File(mediaPath + video.attr("src"));
            String zipPath = "videos/" + file.getName();
            try {
                bytesMap.put(zipPath, Files.readAllBytes(file.toPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            video.attr("src", zipPath);
        }
        for(Element img : htmlDoc.select("img[src]")){
            File file = new File(mediaPath + img.attr("src"));
            String zipPath = "media/" + file.getName();
            try {
                bytesMap.put(zipPath, Files.readAllBytes(file.toPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            img.attr("src", zipPath);
        }
        return htmlDoc.body().children().toString();
    }

    private void addStaticFilesToByteMap(){
        FileHelper fh = new FileHelper();
        ArrayList<String> websiteFiles = new ArrayList<>();
        websiteFiles.add("images/unistuttgart_logo_deutsch.jpg");
        websiteFiles.add("questions/evaluation.json");
        websiteFiles.add("css/mediaQuestionStyle.css");
        websiteFiles.add("css/multiCheckboxStyle.css");
        websiteFiles.add("css/overviewStyle.css");
        websiteFiles.add("css/containerStyle.css");
        websiteFiles.add("css/topContentStyle.css");
        websiteFiles.add("css/bootstrap.min.css");
        websiteFiles.add("css/evaluationStyle.css");
        websiteFiles.add("css/color.css");
        websiteFiles.add("css/bottomContentStyle.css");
        websiteFiles.add("scripts/timer.js");
        websiteFiles.add("scripts/base64.js");
        websiteFiles.add("scripts/state.js");
        websiteFiles.add("scripts/evaluation.js");
        websiteFiles.add("scripts/load-question.js");
        websiteFiles.add("index.html");
        for (String websiteFileName :
                websiteFiles) {
            String filenamePath = "website/"+websiteFileName;
            System.out.println(filenamePath);
            bytesMap.put(websiteFileName, fh.bytesFromResources(filenamePath));

        }
    }

    public void createZipArchive(SARoot saRoot, String path){
        File websiteFile = new File(path);
        ArrayList<ZipEntrySource> entries = new ArrayList<>();
        bytesMap = new HashMap<>();
        fillByteMap(saRoot);

        for (HashMap.Entry<String, byte[]> entry : bytesMap.entrySet()){
            entries.add(new ByteSource(entry.getKey(), entry.getValue()));
        }
        ZipEntrySource[] entriesArray = entries.toArray(new ZipEntrySource[0]);
        ZipUtil.pack(entriesArray, websiteFile);
        bytesMap = new HashMap<>();
    }

    private String generatePreviewTemplate(String name){
        FileHelper fh = new FileHelper();
        String template = fh.getFileFromResources("preview/" + name);
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

        //just evaluate upper part so velocity does not remove foreachloop
        Velocity.evaluate(context, writer, "preview", template.substring(0,400));

        return writer.toString() + template.substring(400);
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
        Velocity.init();
        Context context = new VelocityContext();
        context.put("question", questionCopy);
        StringWriter writer = new StringWriter();
        if (question.isSingleChoice()) {
            Velocity.evaluate(context, writer, "questionPreview", sCPreviewTemplate);
        } else {
            Velocity.evaluate(context, writer, "questionPreview", mCPreviewTemplate);
        }
        //System.out.println(replaceImgAndVideoSrcForTemplate(writer.toString()));
        return replaceImgAndVideoSrcForTemplate(writer.toString());
    }
    public String getCategoryHtml(Category category){
        return MarkdownHelper.markdownToHtml(category.getContent());
    }

    public String getConclusionHtml(Conclusion conclusion){
        return MarkdownHelper.markdownToHtml(conclusion.getContent());
    }
}
