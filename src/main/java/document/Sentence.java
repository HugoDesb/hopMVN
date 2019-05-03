package document;

import tagging.RNNTagger.RNNTag;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Sentence {

    //Global unique id
    private UUID id;

    private String text;

    private List<RNNTag> tokens;

    public Sentence(String text) {
        this.id = UUID.randomUUID();
        this.text = text;
        tokens = new ArrayList<>();
    }



    public void setTokens(List<RNNTag> tokens) {
        this.tokens = tokens;
    }

    public UUID getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public List<RNNTag> getTokens() {
        return tokens;
    }
}
