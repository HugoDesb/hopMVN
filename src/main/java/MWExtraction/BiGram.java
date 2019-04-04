package MWExtraction;

import tagging.RNNTagger.RNNTag;

import java.util.ArrayList;
import java.util.Objects;

public class BiGram extends UniGram implements Gram{

    private RNNTag second;

    public BiGram(RNNTag first, RNNTag second) {
        super(first);
        this.second = second;
    }

    public ArrayList<UniGram> getSubStrings(){
        ArrayList<UniGram> ret = new ArrayList<>();
        ret.add(new UniGram(getFirst()));
        ret.add(new UniGram(getSecond()));
        return ret;
    }

    public boolean in(TriGram gram){
        return 
    }

    @Override
    public String toString() {
        return "BiGram{" +
                "first=" + super.getFirst() +
                ", second=" + second +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BiGram biGram = (BiGram) o;
        return super.equals(biGram) &&
                Objects.equals(second.getLemma(), biGram.second.getLemma());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.second);
    }


    public RNNTag getSecond() {
        return second;
    }

    @Override
    public ArrayList<RNNTag> getAllTokens() {
        ArrayList<RNNTag> ret = super.getAllTokens();
        ret.add(second);
        return ret;
    }
}
