package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(propOrder = {"category", "question", "points", "time", "answers"})
public class Question implements SAObject {
    private int id;
    private String question = "";
    private List<Answer> answers = new ArrayList<>();
    //private List<String> mediaPaths = new ArrayList<>();
    private int points = 0;
    //wen die zeit 0 ist dann ist es keine
    private int time = 0;
    private Category category = new Category();

    public Question() {
    }


    public int getTime() {
        return time;
    }

    @XmlElement
    public void setTime(int time) {
        this.time = time;
    }

    public Question(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    @XmlElement
    public void setQuestion(String question) {
        this.question = question;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    @XmlElement
    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

//    public List<String> getMediaPaths() {
//        return mediaPaths;
//    }

//    @XmlElement
//    public void setMediaPaths(List<String> mediaPaths) {
//        this.mediaPaths = mediaPaths;
//    }

    public int getPoints() {
        return points;
    }

    @XmlElement
    public void setPoints(int points) {
        this.points = points;
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
        stringVariables.put("question", this.question);
        return stringVariables;
    }

    public HashMap<String, List<Answer>> getListProperties() {
        HashMap<String, List<Answer>> listVariables = new HashMap<>();
        listVariables.put("answers", this.answers);
        return listVariables;
    }


	/**
	 * @return the category
	 */
	public Category getCategory() {
		return this.category;
	}

	
	/**
	 * @param category the category to set
	 */
	@XmlElement
	public void setCategory(Category category) {
		this.category = category;
	}


}
