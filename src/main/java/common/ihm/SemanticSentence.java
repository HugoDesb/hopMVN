package common.ihm;

import java.util.ArrayList;

public class SemanticSentence {

    private String sentence;
    private ArrayList<FrameSentence> tokens; //same for all sentences


    public String getText(int index){
        return tokens.get(index).get(1);
    }

    public String getLemma(int index){
        return tokens.get(index).get(3);
    }

    public String getPOSTag(int index){
        return tokens.get(index).get(5);
    }

    public String getTarget
}
