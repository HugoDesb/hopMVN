package semantic;

import java.util.ArrayList;

/**
 * Class representing Frame Elements annotation
 */
public class FrameElement {
    private String name;
    private ArrayList<Integer> indexes;

    public FrameElement(String name, ArrayList<Integer> indexes) {
        this.name = name;
        this.indexes = indexes;
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

    @Override
    @Deprecated
    public String toString() {
        return "FrameElement{" +
                "name='" + name + '\'' +
                ", indexes=" + indexes +
                '}';
    }

    static class Builder{
        private String name;
        private ArrayList<Integer> indexes;


        public Builder(String name, int index) {
            this.name = name;
            this.indexes = new ArrayList<>();
            this.indexes.add(index);
        }

        public void addIndex(int index){
            this.indexes.add(index);
        }

        public boolean same(String otherName){
            return this.name.equals(otherName);
        }

        public FrameElement build(){
            return new FrameElement(name, indexes);
        }
    }

}
