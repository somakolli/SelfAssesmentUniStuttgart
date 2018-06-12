package parser;

import domain.Answer;
import domain.Question;
import domain.TimeQuestion;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tjehret
 * <p>
 * Class to make simple Pair of ID and Question
 */
public class Pair {
    private String id;
    private Question question;
    private TimeQuestion timeQuestion;
    private String time;
    private String mediapath;

    /**
     * Constructor for a simple Question pair. <br/>
     * e.g. the Mapping of Question <-> Answer via ID <br/>
     * ! FOR INTERNAL USE ONLY !
     *
     * @param question the question to be registered
     * @param id       the question's ID
     */
    public Pair(Question question, String id) {
        this.question = question;
        this.id = id;
    }

    /**
     * Constructor for a simple TIME-Question pair <br/>
     * e.g. the Mapping of TIME-Question <-> Answer via ID <br/>
     * ! FOR INTERNAL USE ONLY !
     *
     * @param timeQuestion the TIME-question to be registered
     * @param id           the TIME-questions OD
     */
    public Pair(TimeQuestion timeQuestion, String id) {
        this.timeQuestion = timeQuestion;
        this.id = id;
    }

    /**
     * Constructor for a simple Time pair <br/>
     * e.g. the Mapping of Time <-> TIME-Question OR Mediapath <-> (TIME-)question via ID
     *
     * @param timeMedia the time OR mediapath to be stored
     * @param id        the ID of the corresponding TIME-question
     * @param isMedia   a Boolean to switch between MEDIAPATH and TIME
     */
    public Pair(String timeMedia, String id, boolean isMedia) {
        if (isMedia) {
            this.mediapath = timeMedia;
        } else {
            this.time = timeMedia;
        }
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
     *
     * @param answer the new answer to be added
     */
    public void addAnswer(Answer answer) {
        if (this.question != null) {
            List<Answer> newAnswers = this.question.getAnswers();
            newAnswers.add(answer);
            this.question.setAnswers(newAnswers);
        } else {
            List<Answer> newAnswers = this.timeQuestion.getAnswers();
            newAnswers.add(answer);
            this.timeQuestion.setAnswers(newAnswers);
        }
    }

    /**
     * Adds a new Mediapath
     *
     * @param newPath the new Mediapath
     */
    public void addMediaPaths(String newPath) {
        if (this.question != null) {
            List<String> newPaths = this.question.getMediaPaths();
            newPaths.add(newPath);
            this.question.setMediaPaths(newPaths);
        } else {
            List<String> newPaths = this.timeQuestion.getMediaPaths();
            newPaths.add(newPath);
            this.timeQuestion.setMediaPaths(newPaths);
        }
    }

    public TimeQuestion getTimeQuestion() {
        return timeQuestion;
    }

    public String getTime() {
        return time;
    }

    public String getMediapath() {
        return mediapath;
    }
}
