package semantic;

import java.util.ArrayList;

/**
 * Class representing Frame Elements annotation
 */
public class FrameElement {
    private String name;
    private ArrayList<Integer> indexes;
    private ArrayList<FrameNetTag> tokens;

    /**
     * Constructor
     * @param token the first token
     */
    FrameElement(FrameNetTag token) {
        tokens = new ArrayList<>();
        tokens.add(token);
        this.name = token.getFrameElement();
        this.indexes = new ArrayList<>();
        indexes.add(token.getIndex());
    }

    /**
     * Adds a token to this frame element
     * @param token the token
     */
    void addToken(FrameNetTag token){
        tokens.add(token);
        indexes.add(token.getIndex());
    }

    /**
     * Getter for the name of the Frame Element
     * @return the text
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the indexes in the sentence of this frame element
     * @return a list of Integers
     */
    public ArrayList<Integer> getRange() {
        return indexes;
    }

    /**
     * Getter for the text concerned
     * @return the text
     */
    public String getText() {
        StringBuilder sentence = new StringBuilder();
        for (FrameNetTag tags : tokens) {
            sentence.append(tags.getWord()).append(" ");
        }
        return sentence.toString().trim();
    }

    @Override
    public String toString() {
        return "FrameElement{" +
                "name='" + name + '\'' +
                ", indexes=" + indexes +
                ", tokens=" + tokens +
                '}';
    }

}
