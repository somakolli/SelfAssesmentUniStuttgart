package parser;

import domain.Answer;
import domain.Question;

import java.util.List;

/**
 * @author tjehret
 *
 * Class to make simple Pair of ID and Answer
 */
public class Pair {
    private String id;
    private Question question;

    public Pair(Question question, String id) {
        this.question = question;
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public Question getQuestion() {
        return this.question;
    }

    /**
     * Adds a new Answer
     * @param answer the new answer to be added
     */
    public void addAnswer(Answer answer){

        List<Answer> newAnswers =this.question.getAnswers();
        newAnswers.add(answer);
        this.question.setAnswers(newAnswers);
    }
}
