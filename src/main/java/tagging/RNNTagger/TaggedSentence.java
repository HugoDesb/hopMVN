package tagging.RNNTagger;

import java.util.ArrayList;

public class TaggedSentence<T> {

    private ArrayList<T> taggedTokens;

    public TaggedSentence() {
        this.taggedTokens = new ArrayList<>();
    }

    public void addTaggedToken(T token){
        taggedTokens.add(token);
    }

    public ArrayList<T> getTokens(){
        return taggedTokens;
    }
}
