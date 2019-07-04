package semantic;

import java.util.ArrayList;

public class Rule {

    private Sentence sentence;
    private ArrayList<FrameNetTag> premise;
    private ArrayList<FrameNetTag> conclusion;
    private ArrayList<FrameNetPattern> correspondingPatterns;


    Rule(ArrayList<FrameNetTag> premise, ArrayList<FrameNetTag> conclusion, ArrayList<FrameNetPattern> correspondingPattern, Sentence sentence) {
        this.sentence = sentence;
        this.premise = premise;
        this.conclusion = conclusion;
        this.correspondingPatterns = correspondingPattern;
    }

    Rule(Sentence sentence){
        this.sentence = sentence;
        this.premise = new ArrayList<>();
        this.conclusion = new ArrayList<>();
        this.correspondingPatterns = new ArrayList<>();
    }

    public void addMatch(FrameNetTag premise, FrameNetTag conclusion, FrameNetPattern fnp){
        this.premise.add(premise);
        this.conclusion.add(conclusion);
        correspondingPatterns.add(fnp);
    }

    public void addMatch(ArrayList<FrameNetTag> premise, ArrayList<FrameNetTag> conclusion, FrameNetPattern fnp){
        this.premise.addAll(premise);
        this.conclusion.addAll(conclusion);
        correspondingPatterns.add(fnp);
    }


    public ArrayList<FrameNetTag> getPremise() {
        return premise;
    }

    public ArrayList<FrameNetTag> getConclusion() {
        return conclusion;
    }

    public ArrayList<FrameNetPattern> getCorrespondingPatterns() {
        return correspondingPatterns;
    }

    @Override
    public String toString() {
        return "Rule{\n" +
                "\tSentence : "+sentence+
                "\n\tIF : " + premise +
                "\n\t, THEN : " + conclusion +
                "\n\t, correspondingPatterns= '" + correspondingPatterns + '\'' +
                "\n}";
    }
}
