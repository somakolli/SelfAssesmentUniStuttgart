import java.util.Objects;

public class TemplateVariable {
    private String object;
    private String property;

    public TemplateVariable() {
    }

    public TemplateVariable(String object, String property) {
        this.object = object;
        this.property = property;
    }


    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    @Override
    public String toString() {
        return "{{" + this.object + "." + this.property + "}}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TemplateVariable that = (TemplateVariable) o;
        return Objects.equals(object, that.object) &&
                Objects.equals(property, that.property);
    }

    @Override
    public int hashCode() {

        return Objects.hash(object, property);
    }
}
