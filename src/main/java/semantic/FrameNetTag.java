package semantic;

import java.util.Comparator;
import java.util.List;

public class FrameNetTag {

    public static Comparator<FrameNetTag> indexComparator = new Comparator<FrameNetTag>() {
        @Override
        public int compare(FrameNetTag t1, FrameNetTag t2) {

            return (t2.getIndex() < t1.getIndex() ? 1 :
                    (t2.getIndex() == t1.getIndex() ? 0 : -1));
        }
    };

    private int index, sentenceNumber;
    private String word, lemma, POSTag, target, frame, frameElement;

    FrameNetTag(List<String> line) {
        this.index = Integer.parseInt(line.get(0));
        this.word = line.get(1);
        this.lemma = line.get(3);
        this.POSTag = line.get(5);
        this.sentenceNumber = Integer.parseInt(line.get(6));
        this.target = line.get(12);
        this.frame = line.get(13);
        String[] fe = line.get(14).split("-");
        if(fe.length>1){
            this.frameElement = fe[1];
        }else{
            this.frameElement = fe[0];
        }
    }

    int getSentenceNumber() {
        return sentenceNumber;
    }

    public int getIndex() {
        return index;
    }

    public String getWord() {
        return word;
    }

    public String getLemma() {
        return lemma;
    }

    public String getPOSTag() {
        return POSTag;
    }

    public String getTarget() {
        return target;
    }

    String getFrame() {
        return frame;
    }

    String getFrameElement() {
        return frameElement;
    }

    @Override
    public String toString() {
        return "{" +
                "i=" + index +
                ", word='" + word + '\'' +
                ", lemma='" + lemma + '\'' +
                '}';
    }
}
