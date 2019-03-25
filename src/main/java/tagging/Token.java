package tagging;

public class Token {

    private String word;
    private POSInfo posInfo;
    private String lemma;

    public Token(String word, POSInfo posInfo, String lemma) {
        this.word = word;
        this.posInfo = posInfo;
        this.lemma = lemma;
    }

    public String getWord() {
        return word;
    }

    public POSInfo getPosInfo() {
        return posInfo;
    }

    public String getLemma() {
        return lemma;
    }
}
