package parser;

/**
 * @author tjehr
 * Class to represent the Mapping for Points
 */
public class PointPair {

    private String points;
    private String id;

    /**
     * Constructor for a new Pair, internal use only
     * @param points the points for a question
     * @param id the ID of the corresponding question
     */
    public PointPair(String points, String id){
        this.id = id;
        this.points = points;
    }

    public String getId() {
        return id;
    }

    public String getPoints() {
        return points;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}
