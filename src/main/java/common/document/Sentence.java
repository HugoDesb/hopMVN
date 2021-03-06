package common.document;

import MWExtraction.dope.NGram;
import tagging.RNNTagger.RNNTag;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class Sentence {

    //Global unique id
    private UUID id;

    private String text;

    private ArrayList<RNNTag> tokens;

    private ArrayList<NGram> ngrams;

    public Sentence(String text) {
        this.id = UUID.randomUUID();
        this.text = text;
        tokens = new ArrayList<>();
        ngrams = new ArrayList<>();
    }

    /**
     * SETTER for tokens
     * @param tokens tokens
     */
    public void setTokens(ArrayList<RNNTag> tokens) {
        this.tokens = tokens;
    }

    /**
     * SETTER for n-grams
     * @param ngrams n-grams
     */
    public void setNgrams(ArrayList<NGram> ngrams) {
        this.ngrams = ngrams;
    }

    /**
     * GETTER for id
     * @return id
     */
    public UUID getId() {
        return id;
    }

    /**
     * GETTER for text
     * @return text
     */
    public String getText() {
        return text;
    }

    /**
     * GETTER for tokens
     * @return tokens
     */
    public ArrayList<RNNTag> getTokens() {
        return tokens;
    }

    /**
     * GETTER for ngrams
     * @return ngrams
     */
    public ArrayList<NGram> getNgrams() {
        return ngrams;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sentence sentence = (Sentence) o;
        return Objects.equals(text, sentence.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }
}
