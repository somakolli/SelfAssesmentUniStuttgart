package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Answer implements SAObject {
    private String content = "";
    //private List<String> mediaPath = new ArrayList<>();
    private Boolean isCorrect = false;

    
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

    public HashMap<String, String> getStringProperties() {
        HashMap<String, String> stringVariables = new HashMap<>();
        stringVariables.put("content", content);
        return stringVariables;
    }
}
