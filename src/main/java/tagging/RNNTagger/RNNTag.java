package tagging.RNNTagger;

public class RNNTag{

    private String word;
    private String tag;
    private String lemma;

    public RNNTag(String word, String tag, String lemma) {
        this.word = word;
        this.tag = tag;
        this.lemma = lemma;
    }

    public String getWord(){
        return word;
    }

    public String getLemma(){
        return lemma;
    }

    public String getTag() {
        return tag;
    }
}
