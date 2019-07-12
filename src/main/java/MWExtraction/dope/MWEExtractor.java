package MWExtraction.dope;


import common.document.Sentence;
import common.document.TextDocument;
import tagging.RNNTagger.RNNTag;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class MWEExtractor {

    private Map<NGram, Integer> nGramWithCount;
    private Map<Integer, Integer> totalNumberNGrams;

    private Map<UniGram, Integer> uniGram;
    private int totalNumberOfUnigrams;
    private Map<BiGram, Integer> biGram;
    private int totalNumberOfBigrams;
    private Map<TriGram, Integer> triGram;
    private int totalNumberOfTrigrams;

    private MWEFilter filterMWE;

    private int minSize;
    private int maxSize;


    /**
     * Default constructor (minSize = 1, maxSize = 3)
     */
    private MWEExtractor(){
        this.minSize = 1;
        this.maxSize = 3;
    }

    /**
     * Constructor specifiyng minSize and max Size
     * @param minSize
     * @param maxSize
     */
    public MWEExtractor(int minSize, int maxSize) {
        this.minSize = minSize;
        this.maxSize = maxSize;
    }

    /**
     * Extract all n-grams. | where n in [1, maxSize]
     * @param doc the TextDocument object
     */
    public MWE generateGrams(TextDocument doc){
        MWE multiWordsExpressions = new MWE(maxSize);
        ArrayList<Sentence> sentences = doc.getLines();
        Iterator<Sentence> it = sentences.iterator();
        while(it.hasNext()){
            generateNGramsForSentence(it.next(), multiWordsExpressions);
        }
        return multiWordsExpressions;
    }

    /**
     * Generate all NGrams of all sizes (bound to the maxSize) for a sentence
     * @param sentence the sentence to generate ngrams for
     */
    public void generateNGramsForSentence(Sentence sentence, MWE mwe){
        UUID sentenceID = sentence.getId();
        ArrayList<RNNTag> tokens = sentence.getTokens();
        for (int n = 1; n <= maxSize ; n++) {
            for (int i = 0; i < tokens.size()-n; i++) {
                NGram ngram = new NGram(n, sentenceID);
                for (int j = 0; j < n; j++) {
                    ngram.addGram(tokens.get(i+j));
                }
                mwe.addNGram(ngram);
                //System.out.println("[MWEExtractor.java] Add Gram : "+ngram.toString());
            }
        }
    }

    private boolean isAccepted(NGram gram) {
        return true;
    }

}

