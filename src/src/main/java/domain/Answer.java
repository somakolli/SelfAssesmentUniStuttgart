package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Answer implements SAObject {
    private int id;
    private String content = "";
    //private List<String> mediaPath = new ArrayList<>();
    private Boolean isCorrect = false;

    public Answer(){
    }

    public Answer(int id, String content, Boolean isCorrect) {
        this.id = id;
        this.content = content;
        this.isCorrect = isCorrect;
    }

    public Answer(Answer other) {
        this.id = other.id;
        this.content = other.content;
        this.isCorrect = other.isCorrect;
    }

    public Boolean getCorrect() {
		return this.isCorrect;
	}
    
    @XmlElement
	public void setCorrect(Boolean isCorrect) {
		this.isCorrect = isCorrect;
	}
    
    public String getContent() {
        return content;
    }

    @XmlElement
    public void setContent(String content) {
        this.content = content;
    }

//    public List<String> getMediaPath() {
//        return mediaPath;
//    }

//    @XmlElement
//    public void setMediaPath(List<String> mediaPath) {
//        this.mediaPath = mediaPath;
//    }

    public int getId() {
        return id;
    }

    @XmlElement
    public void setId(int id) {
        this.id = id;
    }

    public HashMap<String, String> getStringProperties() {
        HashMap<String, String> stringVariables = new HashMap<>();
        stringVariables.put("content", content);
        return stringVariables;
    }
}
