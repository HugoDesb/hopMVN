package semantic;

import java.util.ArrayList;

//ALL FRAMES, ONE SENTENCE
public class SemanticSentence {

    //private String sentence;
    private int sentenceNumber;
    private ArrayList<FrameSentence> frames; //same for all frames

    public FrameGraph buildFrameGraph(){
        return null;
    }

    public SemanticSentence() {
        frames = new ArrayList<>();
    }

    public void addFrameIdentification(FrameSentence fs, int sentenceNumber){
        frames.add(fs);
        this.sentenceNumber = sentenceNumber;
    }

    public boolean isSameSentence(FrameSentence fs){
        return fs.getSentenceNumber()==sentenceNumber;
    }

    public ArrayList<FrameSentence> getFrames() {
        return frames;
    }
}
