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

    private Chunk(String text, boolean isFrame, boolean isRole, String subText, int startIndex, int endIndex, String sentenceFrame, String sentenceFull) {
        this.sentenceFull = sentenceFull;
        this.text = text;
        this.sentenceFrame = sentenceFrame;
        this.isFrame = isFrame;
        this.isRole = isRole;
        this.subText = subText;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    /**
     * Generates a random Color
     * @return a Color
     */
    private Color getRandomColor() {
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        return new Color(r, g, b);
    }

    /**
     * Getter for the full sentence
     * @return the sentence
     */
    String getSentenceFrame() {
        return sentenceFrame;
    }

    /**
     * Getter for isFrame
     * @return whether this chunk is of frame target type
     */
    public boolean isFrame() {
        return isFrame;
    }

    /**
     * Getter for the text of the chunk
     * @return the text of the chunk
     */
    public String getText() {
        return text;
    }

    /**
     * Gets whether this chunk is special
     * @return whether the chunk is of frame target or frame element type
     */
    public boolean isSpecial() {
        return isRole || isFrame;
    }

    /**
     * The SubText is equals to :
     *      - the frame's name IF it is a frame target
     *      - the frame element's name IF it is a frame element
     *      - nothing ELSE
     * @return the sub-text
     */
    String getSubText() {
        return subText;
    }

    /**
     * Getter for the background color of the chunk :
     *      - black for a frame target
     *      - random for a frame element
     *      - transparent else
     * @return the choosen color
     */
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

    String getSentenceFull() {
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

        Builder(FrameNetTag token) {
            this.text = token.getWord();

            this.subText = "";

            this.isFrame = !token.getFrame().equals("_");
            if(isFrame) this.subText = token.getFrame();

            this.isRole = !token.getFrameElement().equals("O");
            if(isRole) this.subText = token.getFrameElement();

            this.startIndex = token.getIndex();
            this.endIndex = startIndex+1;
        }

        void appendText(String text) {
            this.text += " " + text;
            endIndex += 1;
        }

        boolean isOfSameType(FrameNetTag token){
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

        void addSentenceFrame(String sentenceFrame){
            this.sentenceFrame = sentenceFrame;
        }

        Chunk build(){
            return new Chunk(text, isFrame, isRole, subText, startIndex, endIndex, sentenceFrame, sentenceFull);
        }

        void addSentenceFull(String sentence) {
            this.sentenceFull = sentence;
        }
    }
}