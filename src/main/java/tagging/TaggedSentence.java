package tagging;

import java.util.ArrayList;

public class TaggedSentence {

    private ArrayList<Token> tokens;

    public TaggedSentence(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }

    class Builder{
        private ArrayList<Token> tokens;

        public Builder() {
            this.tokens = new ArrayList<>();
        }

        public void addToken(String line){
            String [] infos = line.split("\\t");
            if(infos.length == 3){
                tokens.add(new Token(infos[0], new POSInfo(infos[1]), infos[2]));
            }
        }

        public TaggedSentence build(){
            return new TaggedSentence(tokens) ;
        }
    }
}