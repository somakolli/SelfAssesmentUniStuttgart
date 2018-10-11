package domain;

import java.util.ArrayList;
import java.util.List;

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

    @XmlElement
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

}
