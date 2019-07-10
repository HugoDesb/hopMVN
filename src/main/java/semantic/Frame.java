package semantic;

import java.util.ArrayList;
import java.util.Collections;

public class Frame {

    private ArrayList<FrameElement> frameElements;



    private String name;
    private int indexTarget;


    public Frame(String name, int indexTarget, ArrayList<FrameElement> frameElements) {
        this.name = name;
        this.indexTarget = indexTarget;
        this.frameElements = frameElements;
    }

    /**
     * Returns the frame element matching the name (if not found, returns null
     * @param frameElements a list of frame element to search in
     * @param name the name of the frame element to find
     * @return a FrameElement or null
     */
    private FrameElement getFrameElementIfExists(ArrayList<FrameElement> frameElements, String name){
        for (FrameElement fe : frameElements) {
            if(name.equals(fe.getName())){
                return fe;
            }
        }
        return null;
    }

    public ArrayList<Integer> getRange(){
        ArrayList<Integer> ranger = new ArrayList<>();

        for (FrameElement fe :frameElements) {
            ranger.addAll(fe.getRange());
        }
        ranger.add(indexTarget);

        Collections.sort(ranger);

        return ranger;
    }

    /**
     * Returns all indexes for words concerned by the specified frameElement
     * If the frameElement doesn't exists, an empty ArrayList is returned
     * @param frameElementName the FrameNet name of the Frame Element
     * @return an ArrayList of concerned words index's
     */
    public ArrayList<Integer> getFrameElementIndexes(String frameElementName){
        ArrayList<Integer> ret = new ArrayList<>();
        for (FrameElement fe : frameElements) {
            if(fe.getName().equals(frameElementName)){
                ret = fe.getRange();
            }
        }
        if(ret.size()!=0){
            Collections.sort(ret);
            if(ret.get(ret.size()-1)+1 == indexTarget){
                ret.add(indexTarget);
            }
        }

        return ret;
    }

    /**
     * Getter for the frame name
     * @return the frame name
     */
    public String getFrameName() {
        return name;
    }

    /**
     * Getter for the frame elements
     * @return a list of FrameElements
     */
    public ArrayList<FrameElement> getFrameElements() {
        return frameElements;
    }


    /**
     * Getter for the chunks
     * @return a list of Chunk
     */
    /*
    ArrayList<Chunk> getChunks() {
        return createChunks();
    }
    */

    /**
     * Create chunks based on the tokens
     * @return a list of Chunk
     */
    /*
    private ArrayList<Chunk> createChunks() {
        ArrayList<Chunk> chunks = new ArrayList<>();

        String sentenceFrame = "";
        ArrayList<Chunk.Builder> buildingList = new ArrayList<>();
        Chunk.Builder builder = new Chunk.Builder(tokens.get(0));
        for (int i = 1; i < tokens.size(); i++) {
            FrameNetTag token = tokens.get(i);

            if(!token.getFrame().equals("_")){
                sentenceFrame = token.getFrame();
                this.frameName = sentenceFrame;
                this.targetIndex = i;

            }

            if (builder.isOfSameType(token)) {
                builder.appendText(token.getWord());
            } else {
                buildingList.add(builder);
                //new Chunk
                builder = new Chunk.Builder(token);
            }
        }
        buildingList.add(builder);

        for (Chunk.Builder b: buildingList) {
            b.addSentenceFrame(sentenceFrame);
            b.addSentenceFull(getSentence());
            chunks.add(b.build());
        }
        return chunks;
    }
    */


    /**
     * Getter for the target index
     * @return the target index
     */
    public int getTargetIndex(){
        return indexTarget;
    }

    public static class Builder {

        private ArrayList<FrameElement.Builder> builders;
        private String name;
        private int indexTarget;

        public Builder() {
            builders = new ArrayList<>();
        }

        public void setName(String name){
            this.name = name;
        }

        public void setIndexTarget(int indexTarget){
            this.indexTarget = indexTarget;
        }

        public void addFrameElement(String name, int index){

            boolean exists = false;
            for (FrameElement.Builder builder: builders) {
                if(builder.same(name)){
                    exists = true;
                    builder.addIndex(index);
                }
            }
            if(!exists){
                builders.add(new FrameElement.Builder(name, index));
            }
        }

        public Frame build(){
            ArrayList<FrameElement> frameElements = new ArrayList<>();
            for (FrameElement.Builder builder: builders) {
                frameElements.add(builder.build());
            }
            return new Frame(name, indexTarget, frameElements);
        }

    }
}
