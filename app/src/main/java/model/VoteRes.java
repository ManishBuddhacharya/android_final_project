package model;

public class VoteRes {
    private int y;
    private String pos_name, label, path;

    public VoteRes(int y, String label, String path) {
        this.y = y;
        this.label = label;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getY() {
        return y;
    }

    public String getPos_name() {
        return pos_name;
    }

    public String getLabel() {
        return label;
    }
}
