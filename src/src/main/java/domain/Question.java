package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Question implements SAObject {
    private String question = "";
    private List<Answer> answers = new ArrayList<>();
    private List<String> mediaPaths = new ArrayList<>();
    private int points = 0;

    public Question(){
    }

    public Question(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public List<String> getMediaPaths() {
        return mediaPaths;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public HashMap<String, String> getStringProperties(){
        HashMap<String, String> stringVariables = new HashMap<>();
        stringVariables.put("question", this.question);
        return stringVariables;
    }
    public HashMap<String, List<Answer>> getListProperties(){
        HashMap<String, List<Answer>> listVariables = new HashMap<>();
        listVariables.put("answers", this.answers);
        return listVariables;
    }
}
