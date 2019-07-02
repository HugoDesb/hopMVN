package semantic;

import java.awt.*;
import java.util.Random;

public class Chunk {
    private String sentenceFull;
    private String text;
    private String sentenceFrame;
    private boolean isFrame;
    private boolean isRole;
    private String subText;
    private int startIndex;
    private int endIndex;

    private Color color_bg;

    public Chunk(String text, boolean isFrame, boolean isRole, String subText, int startIndex, int endIndex, String sentenceFrame, String sentenceFull) {
        this.sentenceFull = sentenceFull;
        this.text = text;
        this.sentenceFrame = sentenceFrame;
        this.isFrame = isFrame;
        this.isRole = isRole;
        this.subText = subText;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    private Color getRandomColor() {
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        return new Color(r, g, b);
    }

    public String getSentenceFrame() {
        return sentenceFrame;
    }

    public boolean isFrame() {
        return isFrame;
    }

    public String getText() {
        return text;
    }

    public boolean isSpecial() {
        return isRole || isFrame;
    }

    public String getSubText() {
        return subText;
    }

    public Color getColor_bg() {
        if (isFrame) {
            color_bg = Color.BLACK;
        } else if (isRole) {
            color_bg = getRandomColor();
        } else {
            color_bg = new Color(Color.TRANSLUCENT);
        }
        return color_bg;
    }

    public String getSentenceFull() {
        return sentenceFull;
    }

    public void setSentenceFull(String sentenceFull) {
        this.sentenceFull = sentenceFull;
    }

    static class Builder{
        private String text;
        private boolean isFrame;
        private boolean isRole;
        private String subText;
        private Color color_bg;
        private int startIndex;
        private int endIndex;
        private String sentenceFrame;
        private String sentenceFull;

        public Builder(FrameNetTag token) {
            this.text = token.getWord();

            this.subText = "";

            this.isFrame = !token.getFrame().equals("_");
            if(isFrame) this.subText = token.getFrame();

            this.isRole = !token.getFrameElement().equals("O");
            if(isRole) this.subText = token.getFrameElement();

            this.startIndex = token.getIndex();
            this.endIndex = startIndex+1;
        }

        public void appendText(String text) {
            this.text += " " + text;
            endIndex += 1;
        }

        public boolean isOfSameType(FrameNetTag token){
            /*
            if(isFrame){
                if(!token.getFrame().equals(subText)) return false;
            }else if(isRole){
                if(!token.getFrameElement().equals(subText)) return false;
            }else{

            }*/
            return (token.getFrame().equals(subText) || token.getFrameElement().equals(subText));
        }

        public boolean isFrame() {
            return isFrame;
        }

        public void addSentenceFrame(String sentenceFrame){
            this.sentenceFrame = sentenceFrame;
        }

        public Chunk build(){
            return new Chunk(text, isFrame, isRole, subText, startIndex, endIndex, sentenceFrame, sentenceFull);
        }

        public void addSentenceFull(String sentence) {
            this.sentenceFull = sentence;
        }
    }
}