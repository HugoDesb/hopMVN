package MWExtraction;


import config.Config;
import tagging.RNNTagger.RNNTag;
import tagging.RNNTagger.TaggedSentence;

import java.util.ArrayList;
import java.util.Map;

public class MWEExtractor {

    private Map<UniGram, Integer> uniGram;
    private int totalNumberOfUnigrams;
    private Map<BiGram, Integer> biGram;
    private int totalNumberOfBigrams;
    private Map<TriGram, Integer> triGram;
    private int totalNumberOfTrigrams;

    private SyntacticFilter filterMWE;

    public MWEExtractor(SyntacticFilter filterMWE){
        totalNumberOfUnigrams = 0;
        totalNumberOfBigrams = 0;
        totalNumberOfTrigrams = 0;
        this.filterMWE = filterMWE;
    }

    /**
     * Extract all n-grams
     * @param taggedSentences
     */
    public void extractGrams(ArrayList<TaggedSentence> taggedSentences){
        for (TaggedSentence ts : taggedSentences) {
            ArrayList<RNNTag> tokens = ts.getTokens();
            extractUniGrams(tokens);
            extractBiGrams(tokens);
            extractTriGrams(tokens);
        }
    }

    public void computeC_Value(){
        ArrayList<UniGram> outputlist = new ArrayList<>();

        //  Compute for Trirams
        for (TriGram a : triGram.keySet()) {
            double cValue = Math.log(3)/Math.log(2) * triGram.get(a)/totalNumberOfTrigrams;
            a.setCValue(cValue);
        }

        // Compute for Bigrams
        for(BiGram b : biGram.keySet()){
            ArrayList<UniGram> substrings = b.getSubStrings();


        }

        //  extract strings using linguistic filter;          --DONE
        //  remove tags from strings;                         --DONE
        //  remove strings below frequency threshold;         --DONE
        //  filter rest of strings through stop-list;         --DONE

        // for all strings 'a' of maximum length :
        for (TriGram a : triGram.keySet()) {
            //  calculate C-value(a)=log2(|a|) * f(a);
            double cValue = Math.log(triGram.get(a))/Math.log(2);
            //  if C-value(a) >= Threshold
            if(cValue>= Config.C_VALUE_THERSHOLD){
                //  add 'a' to output list
                outputlist.add(a);
                //  for all sub strings b
                for (UniGram g: a.getSubStrings()) {
                    //  revise t(b);
                    //  revise c(b);
                }
            }
        }
        // for all smaller strings 'a' in descending order :
            //  if 'a' appears for the first time :
                //  C-value(a)=log2(|a|) * f(a)
            //  else
                //  C-value(a)=log2(|a|) (f(a) 1c(a)t(a)ifC-value(a)
                // Threshold add a to output list;
                // for all sub strings b
                    // revise t(b);
                    // revise c(b)
    }

    /**
     * Extract all Uni-Grams of the sentence
     * @param tokens
     */
    public void extractUniGrams(ArrayList<RNNTag> tokens){
        for(int i = 0; i<tokens.size(); i++){
            totalNumberOfUnigrams++;
            if(isAccepted(tokens.get(i))){
                UniGram g = new UniGram(tokens.get(i));
                if(uniGram.containsKey(g)){
                    uniGram.replace(g, uniGram.get(g)+1);
                }else{
                    uniGram.put(g, 1);
                }
            }
        }
    }

    /**
     * Extract all Bi-Grams of the sentence
     * @param tokens
     */
    public void extractBiGrams(ArrayList<RNNTag> tokens){
        for(int i = 0; i<tokens.size()-1; i++){
            totalNumberOfBigrams++;
            if(isAccepted(tokens.get(i), tokens.get(i+1))){
                BiGram g = new BiGram(tokens.get(i), tokens.get(i+1));
                if(biGram.containsKey(g)){
                    biGram.replace(g, biGram.get(g)+1);
                }else{
                    biGram.put(g, 1);
                }
            }
        }
    }

    /**
     * Extract all Tri-Grams of the sentence
     * @param tokens
     */
    public void extractTriGrams(ArrayList<RNNTag> tokens){
        for(int i = 0; i<tokens.size()-2; i++){
            totalNumberOfTrigrams++;
            if(isAccepted(tokens.get(i), tokens.get(i+1), tokens.get(i+2))){
                TriGram g = new TriGram(tokens.get(i), tokens.get(i+1), tokens.get(i+2));
                if(triGram.containsKey(g)){
                    triGram.replace(g, triGram.get(g)+1);
                }else{
                    triGram.put(g, 1);
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
        for (String [] filter : filterMWE.getForGram()) {
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
        for (String [] filter : filterMWE.getForBiGram()) {
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
        for (String [] filter : filterMWE.getForTriGram()) {
            if(token1.getTag().matches(filter[0]) && token2.getTag().matches(filter[1]) && token3.getTag().matches(filter[2])){
                ret = false;
                break;
            }
        }
        return ret;
    }

}
