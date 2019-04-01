package MWExtraction;

import tagging.RNNTagger.RNNTag;

import java.util.Objects;

public class Gram {

    private RNNTag first;

    public Gram(RNNTag first) {
        this.first = first;
    }

    public RNNTag getFirst() {
        return first;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gram gram = (Gram) o;
        return Objects.equals(first.getLemma(), gram.first.getLemma());
    }

    @Override
    public int hashCode() {
        return Objects.hash(first);
    }
}
