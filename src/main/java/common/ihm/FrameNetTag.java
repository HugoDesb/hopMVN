package common.ihm;

public class FrameNetTag {

    private int index;
    private String word, lemma, POSTag, target, frame, frameElement;

    public FrameNetTag(int index, String word, String lemma, String POSTag, String target, String frame, String frameElement) {
        this.index = index;
        this.word = word;
        this.lemma = lemma;
        this.POSTag = POSTag;
        this.target = target;
        this.frame = frame;
        this.frameElement = frameElement;
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
