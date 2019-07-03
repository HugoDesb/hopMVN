package semantic;

import java.util.ArrayList;

public class Frame {

    private ArrayList<FrameElement> frameElements;

    private FrameNetTag token;


    Frame(FrameNetTag token){
        this.token = token;
        frameElements = new ArrayList<>();
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

    /**
     * Getter for the frame name
     * @return the frame name
     */
    public String getFrameName() {
        return token.getFrame();
    }

    public void addToFrame(FrameNetTag token) {
        boolean added = false;
        for (FrameElement fetmp:frameElements) {
            if(token.getFrameElement().equals(fetmp.getName())){
                fetmp.addToken(token);
                added = true;
            }
        }
        if(!added) {
            frameElements.add(new FrameElement(token));
        }
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
        return token.getIndex();
    }
}
