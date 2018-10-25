package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


//@XmlType(propOrder = {"categories", "questions"})
@XmlRootElement
public class SARoot {

	private List<Category> categories = new ArrayList<>();

    private List<Question> questions = new ArrayList<>();

    private List<Conclusion> conclusions = new ArrayList<>();

	public List<Conclusion> getConclusions() {
		return conclusions;
	}

	@XmlElement(name = "conclusion")
	public void setConclusions(List<Conclusion> conclusions) {
		this.conclusions = conclusions;
	}

	public SARoot() {
	}

	public SARoot(SARoot other) {
		this.categories = other.categories;
		this.questions = other.questions;
		this.conclusions = other.conclusions;
	}

	public List<Question> getQuestions() {
    	int i = 0;
		for (Question question :
				questions) {
			question.setId(i);
			i++;
		}
        return questions;
    }

    @XmlElement(name="question")
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

	/**
	 * @return the categories
	 */
	public List<Category> getCategories() {
		return categories;
	}

	/**
	 * @param categories the categories to set
	 */
	@XmlElement
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public HashMap<Category, ArrayList<Question>> getCategoryQuestionMap(){
		HashMap<Category, ArrayList<Question>> categoryQuestionMap = new HashMap<Category, ArrayList<Question>>();

		for (Question question:
			 questions) {
			categoryQuestionMap.putIfAbsent(question.getCategory(), new ArrayList<>());
			categoryQuestionMap.get(question.getCategory()).add(question);
		}

		return categoryQuestionMap;
	}

}
