package MWExtraction.dope;

import tagging.RNNTagger.RNNTag;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class NGram {

    private int capacity;
    private int N;
    private UUID sentenceId;
    private ArrayList<RNNTag> grams;

    private Metrics metrics;

    /**
     * Create a N-Gram with specific N
     * @param N
     */
    public NGram(int N, UUID sentenceId) {
        if(N<= 0){
            throw new InvalidParameterException("getN must be > 0. Because A Ngrams must have at least one element");
        }
        this.N = N;
        this.capacity = 0;
        grams = new ArrayList<>();
        this.sentenceId = sentenceId;
    }

    /**
     * Create a Ngram with specific getN and grams
     * @param grams
     */
    public NGram(ArrayList<RNNTag> grams, UUID sentenceId) {
        if(1 <= grams.size()){
            throw new InvalidParameterException("grams must at least contain 1-gram");
        }
        this.capacity = grams.size();
        this.N = grams.size();
        this.grams = grams;
        this.sentenceId = sentenceId;
    }

    /**
     * Returns whether longerGram contains this NGram
     * @param longerGram
     * @return
     */
    public boolean isIn(NGram longerGram){
        if(longerGram.getN() <= N){
            throw new InvalidParameterException("The parameter NGram must shorter than "+(N -1));
        }
        return longerGram.contains(this);
    }

    /**
     * Returns
     * @param shorterNGram
     * @return
     */
    public boolean contains(NGram shorterNGram){
        if(shorterNGram.getN() >= N){
            throw new InvalidParameterException("The parameter NGram must shorter than "+(N -1));
        }
        NGram temp;
        boolean ret = false;
        for (int i = 0; i<=(N -shorterNGram.N); i++) {
            temp = new NGram(shorterNGram.N, shorterNGram.sentenceId);
            boolean hop = true;
            for (int j = 0; j < shorterNGram.N -1; j++) {
                hop = hop && get(j+i).equals(shorterNGram.get(j));
            }
            ret = ret || hop;
        }
        return ret;
    }

    /**
     * Adds the next Gram
     * @param gram the gram to add
     * @return tyhe success
     */
    public void addGram(RNNTag gram){
        if(capacity == N){
            throw new IllegalArgumentException("The NGram of where n="+ N +" is already at full capacity");
        }else{
            grams.add(gram);
            capacity++;
        }
    }

    /**
     * Autogenerated equals function
     * @param o object
     * @return T/F
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NGram nGram = (NGram) o;
        boolean ngramsEquals = true;
        System.out.println(this.toString());
        for (int i = 0; i<N; i++) {
            if(grams.get(i).getLemma().equals(nGram.get(i).getLemma()));
        }

        return N == nGram.N && ngramsEquals;
    }

    /**
     * HashCode (super)
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(capacity, N, grams);
    }

    /**
     * GETTER for the sentenceId
     * @return The sentence id
     */
    public UUID getSentenceId() {
        return sentenceId;
    }

    /**
     * Get the element at the specified index
     * @param i index
     * @return the according element
     */
    public RNNTag get(int i){
        if(i<0 || i>= N){
            //System.out.println(this.toString());
            throw new IndexOutOfBoundsException("0<= i < "+N+" but i="+i);
        }

        return grams.get(i);
    }

    /**
     * GETTER for N
     * @return N
     */
    public int getN(){
        return N;
    }

    @Override
    public String toString() {
        return "NGram{" +
                ", N=" + N +
                ", grams=" + grams +
                '}';
    }
}
