package semantic;

import java.util.ArrayList;
import java.util.Objects;

public class Pattern {

    private String frame;
    private ArrayList<String> frameElements;

    public Pattern(String frame, ArrayList<String> frameElements) {
        this.frame = frame;
        this.frameElements = frameElements;
    }

    public String getFrame() {
        return frame;
    }

    public ArrayList<String> getFrameElements() {
        return frameElements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pattern pattern = (Pattern) o;
        return Objects.equals(frame, pattern.frame) &&
                Objects.equals(frameElements, pattern.frameElements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(frame, frameElements);
    }
}
