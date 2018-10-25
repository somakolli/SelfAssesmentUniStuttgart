package domain;

import javax.xml.bind.annotation.XmlElement;

public class Conclusion {
    private int range;
    private String content;

    public Conclusion() {
    }

    public Conclusion(int range, String content) {
        this.range = range;
        this.content = content;
    }

    public Conclusion(Conclusion other) {
        this.range = other.range;
        this.content = other.content;
    }

    public int getRange() {
        return range;
    }

    @XmlElement
    public void setRange(int range) {
        this.range = range;
    }

    public String getContent() {
        return content;
    }

    @XmlElement
    public void setContent(String content) {
        this.content = content;
    }
}
