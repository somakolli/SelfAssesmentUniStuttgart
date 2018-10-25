package domain;

import java.util.HashMap;

import javax.xml.bind.annotation.XmlElement;

public class Conclusion implements SAObject {
    private int range;
    private String content;

    public Conclusion() {
    }

    public Conclusion(int range, String content) {
        this.range = range;
        this.content = content;
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

	@Override
	public HashMap<String, String> getStringProperties() {
		// TODO Auto-generated method stub
		return null;
	}
}
