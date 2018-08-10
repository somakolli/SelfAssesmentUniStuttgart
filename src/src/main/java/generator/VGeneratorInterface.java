package generator;

import domain.Question;
import domain.SARoot;

import java.io.IOException;
import java.util.List;

public interface VGeneratorInterface {
    String generateQuestion(Question question, String template) throws IOException;
}
