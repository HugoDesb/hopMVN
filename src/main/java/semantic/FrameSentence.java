package semantic;

import java.util.ArrayList;
import java.util.List;

public class FrameSentence {

    private String frameName;
    private int targetIndex;
    private ArrayList<FrameElement> frameElements;

    private ArrayList<FrameNetTag> tokens;
    private ArrayList<Chunk> chunks;

    public FrameSentence(List<List<String>> lines) {
        this.frameElements = new ArrayList<>();
        this.tokens = new ArrayList<>();
        for (List<String> line :lines) {
            tokens.add(new FrameNetTag(line));
        }
        chunks = createChunks();
        frameElements = parse();
        for (FrameElement fe :frameElements) {
            System.out.println(frameName+" : "+fe);
        }
    }

    /**
     * Gets all frame elements for this sentence
     * @return a list of FrameElemnts
     */
    private ArrayList<FrameElement> parse(){
        ArrayList<FrameElement> frameElements = new ArrayList<>();

        for (FrameNetTag token: tokens) {
            if(!token.getFrame().equals("_")){
                this.frameName = token.getFrame();
                this.targetIndex = token.getIndex();
            }else if(!token.getFrameElement().equals("O")){
                FrameElement fe = getFrameElementIfExists(frameElements, token.getFrameElement());
                if(fe != null){
                    fe.addToken(token.getWord(), token.getIndex());
                }else{
                    fe = new FrameElement(token.getFrameElement(), token.getIndex(), token.getWord());
                    frameElements.add(fe);
                }
            }
        }
        return frameElements;
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
        return frameName;
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
    public ArrayList<Chunk> getChunks() {
        return chunks;
    }

    /**
     * Create chunks based on the tokens
     * @return a list of Chunk
     */
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

    /**
     * Gets the full sentence
     * @return the sentence
     */
    public String getSentence(){
        String sentence = "";
        for (FrameNetTag tags : tokens) {
            sentence += tags.getWord()+" ";
        }
        return sentence.trim();
    }

    /**
     * Getter for the tokens
     * @return a list of tokens
     */
    public ArrayList<FrameNetTag> getTokens() {
        return tokens;
    }

    /**
     * Getter for the target index
     * @return the target index
     */
    public int getTargetIndex(){
        return targetIndex;
    }

    /**
     * Getter for the sentence number in the corpus
     * @return the sentence number
     */
    public int getSentenceNumber() {
        return tokens.get(0).getSentenceNumber();
    }
}
