package common.ihm;

public class FrameElement {

    private int index;
    private String role;

    public FrameElement(String frame, String role) {
        this.frame = frame;
        this.role = role;
    }

    public String getFrame() {
        return frame;
    }

    public String getRole() {
        return role;
    }
}
