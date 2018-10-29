package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Answer extends ContentObject {
    private int id;
    //private List<String> mediaPath = new ArrayList<>();
    private Boolean isCorrect = false;

    public Answer(){
    }

    public Answer(int id, String content, Boolean isCorrect) {
        super(content);
        this.id = id;
        this.isCorrect = isCorrect;
    }

    public Answer(Answer other) {
        super(other.getContent());
        this.id = other.id;
        this.isCorrect = other.isCorrect;
    }

    public Boolean getCorrect() {
		return this.isCorrect;
	}
    
    @XmlElement
	public void setCorrect(Boolean isCorrect) {
		this.isCorrect = isCorrect;
	}

    public int getId() {
        return id;
    }

    @XmlElement
    public void setId(int id) {
        this.id = id;
    }

    public HashMap<String, String> getStringProperties() {
        HashMap<String, String> stringVariables = new HashMap<>();
        stringVariables.put("content", getContent());
        return stringVariables;
    }
}
