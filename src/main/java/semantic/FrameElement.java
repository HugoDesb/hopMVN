package semantic;

import java.util.ArrayList;

/**
 * Class representing Frame Elements annotation
 */
public class FrameElement {
    private String name;
    private ArrayList<Integer> indexes;
    private String text;

    /**
     * constructor
     * @param name the frame element name
     * @param word the word
     * @param index and his index in the sentence
     */
    public FrameElement(String name, String word, int index) {
        this.name = name;
        this.indexes = new ArrayList<>(); indexes.add(index);
        this.text = word;
    }

    /**
     * Adds a token to this frame element
     * @param word the word
     * @param index and his index in the sentence
     */
    public void addToken(String word, int index){
        this.text += " "+word;
        indexes.add(index);
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
    public ArrayList<Integer> getIndexes() {
        return indexes;
    }

    /**
     * Getter for the text concerned
     * @return the text
     */
    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "FrameElement{" +
                "name='" + name + '\'' +
                ", indexes=" + indexes +
                ", text='" + text + '\'' +
                '}';
    }
}
