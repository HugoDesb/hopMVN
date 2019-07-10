package semantic;

import common.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FrameNetPattern {

    private String line;
    private ArrayList<Pair<String, String>> frameAndRoles;

    private ArrayList<Pattern> premises;
    private ArrayList<Pattern> conclusions;

    private String destination;

    FrameNetPattern(String line){
        this.line = line;
        this.frameAndRoles = new ArrayList<>();
        this.premises = new ArrayList<>();
        this.conclusions = new ArrayList<>();
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
                premises.add(createPatternForOneframe(premise));
            }
        }

        if(tmp1.length > 1){
            for (String conclusion: tmp1[1].split("\\,")) {
                conclusions.add(createPatternForOneframe(conclusion));
            }
        }
    }

    public static Pattern createPatternForOneframe(String partPattern){
        //gets frame name
        String name = partPattern.split("\\[")[0];
        //get list of frameElements
        List<String> fes = Arrays.asList(partPattern.substring(0, partPattern.length() - 1).split("\\[")[1]);
        return new Pattern(name, new ArrayList<>(fes));
    }

    public String getLine() {
        return line;
    }

    public ArrayList<Pattern> getPremises() {
        return premises;
    }

    public ArrayList<Pattern> getConclusions() {
        return conclusions;
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
