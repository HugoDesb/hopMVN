package semantic;

public class PatternMatcher {

    private Sentence sentence;

    public PatternMatcher(Sentence sentence) {
        this.sentence = sentence;
    }

    public static void matches(FrameNetPattern fnp){
        matchesPremise(fnp);
        matchesConslusion(fnp);
    }

    public static void matchesPremise(FrameNetPattern fnp){

    }

    public static void matchesConslusion(FrameNetPattern fnp){

    }



}
