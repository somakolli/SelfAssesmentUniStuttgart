import org.jsoup.nodes.Element;

import java.util.Objects;

public class TemplateObject {
    private String content = "";
    private String variableName = "";
    private Element htmlElement;

    public Element getHtmlElement() {
        return htmlElement;
    }

    public void setHtmlElement(Element htmlElement) {
        this.htmlElement = htmlElement;
    }

    public String getContent() {
        return content;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TemplateObject that = (TemplateObject) o;
        return Objects.equals(content, that.content) &&
                Objects.equals(variableName, that.variableName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(content, variableName);
    }
}
