package generator;

import domain.Category;
import domain.Conclusion;
import domain.Question;
import domain.SARoot;

import java.io.IOException;

public interface VGeneratorInterface {
    void createZipArchive(SARoot saRoot, String path) throws IOException;
    String getQuestionHtml(Question question);
    String getCategoryHtml(Category category);
    String getConclusionHtml(Conclusion conclusion);
}
