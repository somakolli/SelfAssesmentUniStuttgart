package parser;

/**
 * @author tjehr
 * Class to represent the Mapping for Times
 */
public class TimePair{

    private String time;
    private String id;

    /**
     * Constructor for a new Pair, internal use only
     * @param time the time-limit for a question
     * @param id the ID of the corresponding question
     */
    public TimePair(String time, String id) {
        this.time = time;
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
