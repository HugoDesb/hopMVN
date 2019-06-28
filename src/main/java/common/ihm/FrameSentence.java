package common.ihm;

import common.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FrameSentence {

    private String sentence;
    private ArrayList<FrameNetTag> tokens;

    public FrameSentence(ArrayList<FrameNetTag> tokens) {
        this.tokens = tokens;
        this.sentence = "";
        for (FrameNetTag tok :tokens) {
            sentence += tok.getWord();
            if(!tok.getFrame().equals("_")){
            }
        }
    }

    public ArrayList<FrameNetTag> getTokens() {
        return tokens;
    }

    public Pair<Integer, String> getTargetIndex(){
        for (FrameNetTag tok :tokens) {
            if(!tok.getFrame().equals("_")){
                return new Pair<>(tok.getIndex(), tok.getFrame());
            }
        }
        return null;
    }

    /**
     * Finds every
     * @return
     */
    public Map<Integer, String> getFrameElementsIndexes(){
        HashMap<Integer, String> hop = new HashMap<>();

        for (FrameNetTag tok :tokens) {
            if(!tok.getFrameElement().equals("O")){
                hop.put(tok.getIndex(), tok.getFrameElement());
            }
        }

        return hop;
    }


}
