package tagging;

import java.util.ArrayList;

public class TaggedText {

    private ArrayList<TaggedSentence> sentences;

    public TaggedText(ArrayList<TaggedSentence> sentences) {
        this.sentences = sentences;
    }

    class Builder {

        private ArrayList<TaggedSentence> sentences;

        public Builder() {
            this.sentences = new ArrayList<>();
        }

        public void addSentence(TaggedSentence sentence){
            sentences.add(sentence);
        }

        public TaggedText build(){
            return new TaggedText(sentences);
        }
    }
}
