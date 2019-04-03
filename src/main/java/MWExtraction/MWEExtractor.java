package MWExtraction;


import tagging.RNNTagger.RNNTag;
import tagging.RNNTagger.TaggedSentence;

import java.util.ArrayList;
import java.util.Map;

public class MWEExtractor {

    private Map<Gram, Integer> uniGram;
    private Map<BiGram, Integer> biGram;
    private Map<TriGram, Integer> triGram;

    private SyntacticFilter MWEFilter;

    public void extractUniGrams(ArrayList<TaggedSentence> taggedSentences){
        for (TaggedSentence sentence : taggedSentences) {
            ArrayList<RNNTag> tokens = sentence.getTokens();
            for(int i = 0; i<tokens.size(); i++){
                Gram g = new Gram(tokens.get(i));
                if(uniGram.containsKey(g)){
                    uniGram.replace(g, uniGram.get(g)+1);
                }else{
                    uniGram.put(g, 1);
                }
            }
        }
    }

    /**
     * Check whether the 1-gram is accepted (the filter accepts it).
     * @param token
     * @return
     */
    public boolean isAccepted(RNNTag token){
        boolean ret = true;
        for (String [] filter : MWEFilter.getForGram()) {
            if(token.getTag().matches(filter[0])){
                ret = false;
                break;
            }
        }
        return ret;
    }

    /**
     * Check whether the 2-gram is accepted (the filter accepts it).
     * @param token1
     * @param token2
     * @return
     */
    public boolean isAccepted(RNNTag token1, RNNTag token2){
        boolean ret = true;
        for (String [] filter : MWEFilter.getForBiGram()) {
            if(token1.getTag().matches(filter[0]) && token2.getTag().matches(filter[1])){
                ret = false;
                break;
            }
        }
        return ret;
    }

    /**
     * Check whether the 3-gram is accepted (the filter accepts it).
     * @param token1
     * @param token2
     * @param token3
     * @return
     */
    public boolean isAccepted(RNNTag token1, RNNTag token2, RNNTag token3){
        boolean ret = true;
        for (String [] filter : MWEFilter.getForTriGram()) {
            if(token1.getTag().matches(filter[0]) && token2.getTag().matches(filter[1]) && token3.getTag().matches(filter[2])){
                ret = false;
                break;
            }
        }
        return ret;
    }

}
