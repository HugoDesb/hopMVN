package MWExtraction;

import tagging.RNNTagger.RNNTag;

import java.util.Objects;

public class BiGram extends Gram{

    private RNNTag second;

    public BiGram(RNNTag first, RNNTag second) {
        super(first);
        this.second = second;
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
        return super.equals(o) && second.getLemma().equals(biGram)
                Objects.equals(second.getLemma(), biGram.second.getLemma());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getFirst(), second);
    }


    public RNNTag getSecond() {
        return second;
    }
}
