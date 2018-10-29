package domain;

import javax.xml.bind.annotation.XmlElement;
import java.util.HashMap;

public abstract class ContentObject implements SAObject {
    private String content = "";

    public ContentObject() {
    }

    public ContentObject(String content) {
        this.content = content;
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
        return null;
    }
}
