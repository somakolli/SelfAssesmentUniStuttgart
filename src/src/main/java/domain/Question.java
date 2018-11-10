package domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@XmlRootElement
//@XmlType(propOrder = {"id", "category", "points", "time", "answers"})
public class Question extends ContentObject {
    private int id;

    private List<Answer> answers = new ArrayList<>();
    //private List<String> mediaPaths = new ArrayList<>();
    private int points = 1;
    //if the time is zero there will be no timer
    private int time = 0;
    private Category category = new Category();
    private boolean singleChoice = false;

    public Question() {
    }

    public Question(Question other) {
        super(other.getContent());
        this.id = other.id;
        this.answers = other.answers;
        this.points = other.points;
        this.time = other.time;
        this.category = other.category;
        this.singleChoice = other.singleChoice;
    }

    private long getNumberOfTrueAnswers(){
        //expresion to count all answers which are correct
        return answers.stream().filter(Answer::getCorrect).count();
    }

    public boolean isSingleChoice() {
        //if there is not exactly one answer correct it cannot be single choice
        return singleChoice && getNumberOfTrueAnswers() == 1;
    }

    public void setSingleChoice(boolean singleChoice) {
        this.singleChoice = singleChoice;
    }

    public int getTime() {
        return time;
    }

    @XmlElement
    public void setTime(int time) {
        this.time = time;
    }

    public Question(String content) {
        super(content);
    }

    public List<Answer> getAnswers() {
        int i = 0;
        for (Answer answer :
                answers) {
            answer.setId(i);
        }
        return answers;
    }


    @XmlElement(name = "answer")
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
        stringVariables.put("content", this.getContent());
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
