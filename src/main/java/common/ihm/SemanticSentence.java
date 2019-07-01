package common.ihm;

import java.util.ArrayList;

//ALL FRAMES, ONE SENTENCE
public class SemanticSentence {

    //private String sentence;
    private ArrayList<FrameSentence> frames; //same for all frames



    public SemanticSentence() {
        frames = new ArrayList<>();
    }

    public void addFrameIdentification(FrameSentence fs){
        frames.add(fs);
    }

    public boolean isSameSentence(FrameSentence fs){
        //when first frame
        if(frames.size() == 0){
            return true;
        }else{
            return frames.get(0).getSentence().equals(fs.getSentence());
        }
    }

    public ArrayList<FrameSentence> getFrames() {
        return frames;
    }
}
