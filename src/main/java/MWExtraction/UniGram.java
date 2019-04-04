package MWExtraction;

import org.jetbrains.annotations.NotNull;
import tagging.RNNTagger.RNNTag;

import java.util.ArrayList;
import java.util.Objects;

public class UniGram implements Comparable, Gram{

    private RNNTag first;
    protected double CValue;

    public UniGram(RNNTag first) {
        this.first = first;
    }

    public RNNTag getFirst() {
        return first;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UniGram uniGram = (UniGram) o;
        return Objects.equals(first.getLemma(), uniGram.first.getLemma());
    }

    @Override
    public int hashCode() {
        return Objects.hash(first);
    }

    /**
     * Compare by the C-Value
     * @param o (Object)
     * @return The comparison between the CValue
     */
    public int compareTo(@NotNull Object o) {
        if(o instanceof UniGram){
            UniGram that = (UniGram) o;
            Double CValThis = CValue;
            return CValThis.compareTo(that.getCValue());
        }else{
            throw new IllegalArgumentException("The argument isn't a UniGram");
        }
    }

    /**
     * Getter for C-Value
     * @return
     */
    public double getCValue() {
        return CValue;
    }

    /**
     * Setter for C-Value
     * @param CValue
     */
    public void setCValue(double CValue) {
        this.CValue = CValue;
    }

    @Override
    public ArrayList<RNNTag> getAllTokens() {
        ArrayList<RNNTag> ret = new ArrayList<>();
        ret.add(first);
        return ret;
    }

    @Override
    public boolean in(Gram g) {
        return false;
    }
}
