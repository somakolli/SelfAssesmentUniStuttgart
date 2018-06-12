package parser;

/**
 * @author tjehr
 * Class to represent the Mapping of Mediapaths
 */
public class MediaPair{
    private String id;
    private String mediapath;

    /**
     * Constructor for a new Pair, internal use only
     * @param mediapath the media-path for a question
     * @param id the ID of the corresponding question
     */
    public MediaPair(String mediapath, String id) {
        this.id = id;
        this.mediapath = mediapath;
    }

    public String getId() {
        return id;
    }

    public String getMediapath() {
        return mediapath;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMediapath(String mediapath) {
        this.mediapath = mediapath;
    }
}
