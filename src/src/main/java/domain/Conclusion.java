package domain;

import java.util.HashMap;
import java.util.Objects;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Conclusion extends ContentObject implements Comparable<Conclusion> {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conclusion that = (Conclusion) o;
        return range == that.range;
    }

    @Override
    public int hashCode() {
        return Objects.hash(range);
    }

    @Override
    public int compareTo(Conclusion conclusion) {
        return Integer.compare(this.getRange(), conclusion.getRange());
    }
}
