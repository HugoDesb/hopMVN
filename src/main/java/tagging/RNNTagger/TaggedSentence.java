package tagging.RNNTagger;

import java.util.ArrayList;

public class TaggedSentence {

    private ArrayList<RNNTag> taggedTokens;

    public TaggedSentence() {
        this.taggedTokens = new ArrayList<>();
    }

    public void addTaggedToken(RNNTag token){
        taggedTokens.add(token);
    }

    public ArrayList<RNNTag> getTokens(){
        return taggedTokens;
    }
}
