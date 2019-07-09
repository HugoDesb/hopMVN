package semantic;

import common.Pair;

import java.util.ArrayList;
import java.util.Objects;

public class FrameNetPattern {

    private String line;
    private ArrayList<Pair<String, String>> frameAndRoles;

    private ArrayList<String> premises;
    private ArrayList<String> conslusions;

    private String destination;

    FrameNetPattern(String line){
        this.line = line;
        this.frameAndRoles = new ArrayList<>();
        this.premises = new ArrayList<>();
        this.conslusions = new ArrayList<>();
        deserialize(line);
    }

    /**
     * From a line from the patterns file, build a pattern
     * @param line
     */
    private void deserialize(String line){
        this.line = "("+line+")";
        String [] tmp1 = line.split(";");
        if(!tmp1[0].isEmpty()){
            for (String premise: tmp1[0].split("\\,")) {
                premises.add(premise);
            }
        }

        if(tmp1.length > 1){
            for (String conslusion: tmp1[1].split("\\,")) {
                conslusions.add(conslusion);
            }
        }
    }

    public String getLine() {
        return line;
    }

    public ArrayList<String> getPremises() {
        return premises;
    }

    public ArrayList<String> getConslusions() {
        return conslusions;
    }

    public ArrayList<Pair<String, String>> getFrameAndRoles() {
        return frameAndRoles;
    }

    String getDestination() {
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
