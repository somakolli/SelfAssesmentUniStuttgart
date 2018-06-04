package domain;

import java.util.HashMap;

public class Answer {
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public HashMap<String, String> getStringVariables(){
        HashMap<String, String> stringVariables = new HashMap<>();
        stringVariables.put("content", content);
        return stringVariables;
    }
}
