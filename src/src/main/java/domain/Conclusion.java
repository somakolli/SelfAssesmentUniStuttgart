package domain;

import java.util.HashMap;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Conclusion extends ContentObject {
    private int range = 0;

    public Conclusion() {
    }

    public Conclusion(int range, String content) {
        super(content);
        this.range = range;
    }
    
    public Conclusion(Conclusion other) {
        super(other.getContent());
        this.range = other.range;
    }

    public int getRange() {
        return range;
    }

    @XmlElement
    public void setRange(int range) {
        this.range = range;
    }

	@Override
	public HashMap<String, String> getStringProperties() {
		// TODO Auto-generated method stub
		return null;
	}
}
