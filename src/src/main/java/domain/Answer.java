package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Answer implements SAObject{
    private String content = "";
    private List<String> mediaPath = new ArrayList<>();

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(List<String> mediaPath) {
        this.mediaPath = mediaPath;
    }

    public HashMap<String, String> getStringProperties(){
        HashMap<String, String> stringVariables = new HashMap<>();
        stringVariables.put("content", content);
        return stringVariables;
    }
}
