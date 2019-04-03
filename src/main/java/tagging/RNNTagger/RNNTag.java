package tagging.RNNTagger;

public class RNNTag{

    private String word;
    private String tag;
    private String lemma;

    public RNNTag(String line) {
        String [] args = line.split("\\t");
        if(args.length <3){
            throw new IllegalArgumentException("The line doesn't match standard RNNTag line. Line is : '"+line+"' and args number is : "+args.length);
        }
        this.word = args[0];
        this.tag = args[1];
        this.lemma = args[2];
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
