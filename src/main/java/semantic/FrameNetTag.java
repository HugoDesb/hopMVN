package semantic;

import java.util.List;

public class FrameNetTag {

    private int index, sentenceNumber;
    private String word, lemma, POSTag, target, frame, frameElement;

    public FrameNetTag(List<String> line) {
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

    public int getSentenceNumber() {
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

    public String getFrame() {
        return frame;
    }

    public String getFrameElement() {
        return frameElement;
    }


}
