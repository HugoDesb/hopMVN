package MWExtraction;

import tagging.RNNTagger.RNNTag;

import java.util.Objects;

public class TriGram {

    private RNNTag first;
    private RNNTag second;
    private RNNTag third;

    public TriGram(RNNTag first, RNNTag second, RNNTag third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TriGram triGram = (TriGram) o;
        return Objects.equals(first.getLemma(), triGram.first.getLemma()) &&
                Objects.equals(second.getLemma(), triGram.second.getLemma()) &&
                Objects.equals(third.getLemma(), triGram.third.getLemma());
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second, third);
    }

    public RNNTag getFirst() {
        return first;
    }

    public RNNTag getSecond() {
        return second;
    }

    public RNNTag getThird() {
        return third;
    }
}
