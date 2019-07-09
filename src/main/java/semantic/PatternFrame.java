package semantic;

import java.util.Arrays;
import java.util.Objects;

public class PatternFrame {

    private String frame;
    private String[] frameElements;
    private boolean onlyWhenAlone;

    public PatternFrame(String pattern) {
        String [] hop1 = pattern.split("\\[");
        this.frame = hop1[0];
        String [] hop2 = hop1[1].split("]");
        this.frameElements = hop2[0].split("\\+");
        if(hop2.length == 2){
            if(hop2[1].equals("--")){
                this.onlyWhenAlone = true;
            }else{
                this.onlyWhenAlone = false;
            }
        }else{
            this.onlyWhenAlone = false;
        }
    }


    public boolean matches(FrameNetTag token){
        boolean ret = token.getFrame().equals(frame);
        boolean matchesOne = false;
        for (String fe : frameElements) {
            if(token.getFrameElement().equals(fe)){
                matchesOne = true;
            }
        }

        ret = ret && matchesOne;
        return ret;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatternFrame that = (PatternFrame) o;
        return Objects.equals(frame, that.frame) &&
                Arrays.equals(frameElements, that.frameElements);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(frame);
        result = 31 * result + Arrays.hashCode(frameElements);
        return result;
    }

    public String getFrame() {
        return frame;
    }

    public String[] getFrameElements() {
        return frameElements;
    }

    public boolean isOnlyWhenAlone() {
        return onlyWhenAlone;
    }
}
