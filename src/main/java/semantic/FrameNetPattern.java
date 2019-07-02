package semantic;

import common.Pair;

import java.util.ArrayList;
import java.util.Objects;

public class FrameNetPattern {

    private String line;
    private ArrayList<Pair<String, String>> frameAndRoles;
    private String destination;

    public FrameNetPattern(String line){
        this.line = line;
        this.frameAndRoles = new ArrayList<>();
        deserialize(line);
    }

    /**
     * From a line from the patterns file, build a pattern
     * @param line
     */
    private void deserialize(String line){
        String [] tmp1 = line.split("-->");
        this.destination = tmp1[1];
        String [] tmp2 = tmp1[0].split(">");
        for (String frameAndRole: tmp2) {
            String frame = frameAndRole.split("\\[")[0];
            String role = frameAndRole.split("\\[")[1].substring(0, frameAndRole.split("\\[")[1].length()-1);
            frameAndRoles.add(new Pair<>(frame, role));
        }
    }

    public ArrayList<Pair<String, String>> getFrameAndRoles() {
        return frameAndRoles;
    }

    public String getDestination() {
        return destination;
    }

    @Override
    public String toString() {
        return line;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FrameNetPattern that = (FrameNetPattern) o;
        return Objects.equals(frameAndRoles, that.frameAndRoles) &&
                Objects.equals(destination, that.destination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(frameAndRoles, destination);
    }

    public boolean matches(Chunk chunk) {
        String leftLine = line.split("-->")[0];
        String createdLine = chunk.getSentenceFrame()+"["+chunk.getSubText()+"]";
        return createdLine.equals(leftLine);
    }
}
