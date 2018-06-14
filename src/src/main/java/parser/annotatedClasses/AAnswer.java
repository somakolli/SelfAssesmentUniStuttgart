package parser.annotatedClasses;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tjehr
 * Class to represent the annotated Answer-Objects for XML parsing
 */

@XmlRootElement
public class AAnswer {
    private String content = "";
    private List<String> mediaPath = new ArrayList<>();

    /**
     * standard constructor
     */
    public AAnswer() {

    }

    public String getContent() {
        return content;
    }

    @XmlElement
    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getMediaPath() {
        return mediaPath;
    }

    @XmlElement
    public void setMediaPath(List<String> mediaPath) {
        this.mediaPath = mediaPath;
    }
}
