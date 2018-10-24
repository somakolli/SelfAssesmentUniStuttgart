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

    public List<Question> getQuestions() {
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
