package parser.annotatedClasses;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tjehr
 * Class to represent the required XML root Element
 */
@XmlRootElement
public class AQuestions {
    private List<AQuestion> questions = new ArrayList<>();

    public AQuestions() {

    }

    public List<AQuestion> getQuestions() {
        return questions;
    }

    @XmlElement
    public void setQuestions(List<AQuestion> questions) {
        this.questions = questions;
    }
}

