package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Answer {
    private String content = "";
    private List<String> mediaPath = new ArrayList<>();

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
