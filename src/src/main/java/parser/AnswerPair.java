package parser;

import domain.Answer;

/**
 * @author tjehr
 * Class to represent the Mapping of Answers
 */
public class AnswerPair {
    private Answer answer;
    private String id;

    /**
     * Constructor for a new Pair, internal use only
     * @param answer the Answer for a question
     * @param id the ID of the corresponding question
     */
    public AnswerPair(Answer answer, String id){
        this.answer = answer;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }
}
