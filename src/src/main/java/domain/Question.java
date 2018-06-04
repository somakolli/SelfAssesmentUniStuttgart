package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Question {
    private String question = "";
    private List<Answer> answers = new ArrayList<>();

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

    public HashMap<String, String> getStringVariables(){
        HashMap<String, String> stringVariables = new HashMap<>();
        stringVariables.put("question", this.question);
        return stringVariables;
    }
    public HashMap<String, List<Answer>> getListVariables(){
        HashMap<String, List<Answer>> listVariables = new HashMap<>();
        listVariables.put("answers", this.answers);
        return listVariables;
    }
}
