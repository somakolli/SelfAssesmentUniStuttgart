import Domain.Question;

public class Generator implements GeneratorInterface{
    public String generateQuestion(Question question, String template) {
        return template.replace("{{question.content}}", question.getContent());
    }
}
