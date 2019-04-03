package MWExtraction;

import tagging.RNNTagger.RNNTag;

import java.util.Objects;

public class TriGram extends BiGram{

    private RNNTag third;

    public TriGram(RNNTag first, RNNTag second, RNNTag third) {
        super(first, second);
        this.third = third;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TriGram triGram = (TriGram) o;
        return super.equals(triGram) &&
                Objects.equals(third.getLemma(), triGram.third.getLemma());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), third);
    }

    public RNNTag getThird() {
        return third;
    }
}
