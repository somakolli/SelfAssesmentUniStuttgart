package generator;

import org.jsoup.nodes.Element;

import java.util.Objects;

public class TemplateForLoop {
    private String content;
    private String variableName;
    private String collectionName;
    private Element htmlElement;

    public TemplateForLoop() {
        content = "";
        variableName = "";
        collectionName = "";
    }

    public String getContent() {
        return content;
    }

    public Element getHtmlElement() {
        return htmlElement;
    }

    public void setHtmlElement(Element htmlElement) {
        this.htmlElement = htmlElement;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TemplateForLoop that = (TemplateForLoop) o;
        return Objects.equals(content, that.content) &&
                Objects.equals(variableName, that.variableName) &&
                Objects.equals(collectionName, that.collectionName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, variableName, collectionName);
    }
}
