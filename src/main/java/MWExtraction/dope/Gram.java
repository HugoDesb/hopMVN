package MWExtraction.dope;

import tagging.RNNTagger.RNNTag;

import java.util.ArrayList;

public interface Gram {

    /**
     * Returns whether the Gram is nested in the bigger N-gram
     * @param g
     * @return
     */
    boolean in(Gram g);

    ArrayList<RNNTag> getAllTokens();


}
