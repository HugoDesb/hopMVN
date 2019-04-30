package tagging.RNNTagger;

import java.security.InvalidParameterException;
import java.util.Objects;

public class RNNTag{

    private String word;
    private String tag;
    private String lemma;

    public boolean lemmaEquals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RNNTag rnnTag = (RNNTag) o;
        return Objects.equals(lemma, rnnTag.lemma);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RNNTag rnnTag = (RNNTag) o;
        return Objects.equals(lemma, rnnTag.lemma);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word, tag, lemma);
    }

    /**
     * Constructor
     * @param line a line of RNNTagger returns
     */
    RNNTag(String line) {
        String [] args = line.split("\\t");
        if(args.length <3){
            throw new IllegalArgumentException("The line doesn't match standard RNNTag line. Line is : '"+line+"' and args number is : "+args.length);
        }
        this.word = args[0];
        this.tag = args[1];
        this.lemma = args[2];
    }

    /**
     * Gets the word token
     * @return the original word token
     */
    public String getWord(){
        return word;
    }

    /**
     * Gets the lemma
     * @return the lemma
     */
    public String getLemma(){
        return lemma;
    }

    /**
     * Gets the tag
     * @return the string representation for RNNTagger of the tag
     */
    public String getTag() {
        return tag;
    }

    /**
     * Gets the ith element of the tag. If there's no ith, then an empty string is returned
     * @param i ith element of the tag.
     * @return The String for the ith tag, empty if it doesn't exists
     */
    public String getDerivative(int i){
        if(i<0 || i>3){
            throw new InvalidParameterException("i must be between 0 and 3 included");
        }
        String [] derivatives = tag.split("\\.");
        if(i < derivatives.length){
            return derivatives[i];
        }else{
            return "";
        }
    }

    /**
     * Obtain the value for asked data
     * @param dataName the name of the asked data
     * @return asked value (empty string if asked data null)
     */
    public String getData(String dataName) {
        switch (dataName){
            case "Word" :
                return getWord();
            case "D1" :
                return getDerivative(0);
            case "D2" :
                return getDerivative(1);
            case "D3" :
                return getDerivative(2);
            case "D4" :
                return getDerivative(3);
            case "Lemma" :
                return getLemma();
            default:
                return "";
        }
    }

    @Override
    public String toString() {
        return "RNNTag{" +
                "word='" + word + '\'' +
                ", tag='" + tag + '\'' +
                ", lemma='" + lemma + '\'' +
                '}';
    }
}
