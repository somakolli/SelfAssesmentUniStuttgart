package parser.annotatedClasses;


import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tjehr
 * Class to represent the annotated Question-Objects for XML parsing
 * This class does not (yet) contain a Hashmap Method
 */

@XmlRootElement
@XmlType(propOrder = { "question", "points", "time", "mediaPaths", "answers" })
public class AQuestion {
    private String question = "";
    private List<AAnswer> answers = new ArrayList<>();
    private List<String> mediaPaths = new ArrayList<>();
    private int points = 0;
    // time == 0 means this question has NO TIME LIMIT
    private int time = 0;

    /**
     * standard constructor
     */
    public AQuestion() {
    }

    public String getQuestion() {
        return question;
    }

    @XmlElement
    public void setQuestion(String question) {
        this.question = question;
    }

    public List<AAnswer> getAnswers() {
        return answers;
    }

    @XmlElement
    public void setAnswers(List<AAnswer> answers) {
        this.answers = answers;
    }

    public List<String> getMediaPaths() {
        return mediaPaths;
    }

    @XmlElement
    public void setMediaPaths(List<String> mediaPaths) {
        this.mediaPaths = mediaPaths;
    }

    public int getPoints() {
        return points;
    }

    @XmlElement
    public void setPoints(int points) {
        this.points = points;
    }

    public int getTime() {
        return time;
    }

    @XmlElement
    public void setTime(int time) {
        this.time = time;
    }
}
