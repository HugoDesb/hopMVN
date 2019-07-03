package semantic;

import java.util.ArrayList;
import java.util.List;

//ALL FRAMES, ONE SENTENCE
public class Sentence {

    //private String sentence;
    private int sentenceNumber;
    private ArrayList<Frame> frames; //same for all frames
    private ArrayList<FrameNetTag> tokens;

    Sentence() {
        this.tokens = new ArrayList<>();
        frames = new ArrayList<>();
    }

    public FrameGraph buildFrameGraph(){
        return null;
    }

    void addFrameIdentification(Frame fs, int sentenceNumber){
        frames.add(fs);
        this.sentenceNumber = sentenceNumber;
    }
    void addFrameIdentification(List<List<String>> lines){
        for (List<String> line :lines) {
            tokens.add(new FrameNetTag(line));
        }
        Frame m = findFrame(tokens);

        frames.add(findFrameElements(tokens, m));
    }

    private Frame findFrame(ArrayList<FrameNetTag> tokens) {
        for (FrameNetTag token: tokens) {
            if(!token.getFrame().equals("_")){
                return new Frame(token);
            }
        }
        return null;
    }

    private Frame findFrameElements(ArrayList<FrameNetTag> tokens, Frame frame) {
        ArrayList<FrameElement> frameElements = new ArrayList<>();
        for (FrameNetTag token: tokens) {
            if(!token.getFrameElement().equals("O")){
                frame.addToFrame(token);
            }
        }
        return frame;
    }

    /**
     * Gets the full sentence
     * @return the sentence
     */
    public String getSentence(){
        StringBuilder sentence = new StringBuilder();
        for (FrameNetTag tags : tokens) {
            sentence.append(tags.getWord()).append(" ");
        }
        return sentence.toString().trim();
    }

    public int getSentenceNumber(){
        return tokens.get(0).getSentenceNumber();
    }

    ArrayList<Frame> getFrames() {
        return frames;
    }
}
