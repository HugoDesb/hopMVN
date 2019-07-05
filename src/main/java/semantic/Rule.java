package semantic;

import java.util.*;

public class Rule {

    private Sentence sentence;
    private Set<FrameNetTag> premise;
    private Set<FrameNetTag> conclusion;
    private ArrayList<FrameNetPattern> correspondingPatterns;


    Rule(Set<FrameNetTag> premise, Set<FrameNetTag> conclusion, ArrayList<FrameNetPattern> correspondingPattern, Sentence sentence) {
        this.sentence = sentence;
        this.premise = premise;
        this.conclusion = conclusion;
        this.correspondingPatterns = correspondingPattern;
    }

    Rule(Sentence sentence){
        this.sentence = sentence;
        this.premise = new HashSet<>();
        this.conclusion = new HashSet<>();
        this.correspondingPatterns = new ArrayList<>();
    }

    public void addMatch(FrameNetTag premise, FrameNetTag conclusion, FrameNetPattern fnp){
        this.premise.add(premise);
        this.conclusion.add(conclusion);
        correspondingPatterns.add(fnp);
    }

    public void addMatch(Set<FrameNetTag> premise, Set<FrameNetTag> conclusion, FrameNetPattern fnp){
        this.premise.addAll(premise);
        this.conclusion.addAll(conclusion);
        correspondingPatterns.add(fnp);
    }

    public boolean isEmpty(){
        return correspondingPatterns.size() == 0;
    }


    public Set<FrameNetTag> getPremise() {
        return premise;
    }

    public Set<FrameNetTag> getConclusion() {
        return conclusion;
    }

    public ArrayList<FrameNetPattern> getCorrespondingPatterns() {
        return correspondingPatterns;
    }

    private Set<String> simplifyFrameNetTagList(Set<FrameNetTag> tokens){
        Set<String> out = new HashSet<>();

        ArrayList<FrameNetTag> sorted = new ArrayList<>(tokens);
        Collections.sort(sorted, FrameNetTag.indexComparator);

        StringBuilder builder = new StringBuilder();
        int last = -2;
        for (int i = 0; i < sorted.size(); i++) {
            if(last == -2){
                last = sorted.get(i).getIndex()-1;
            }

            if(last == sorted.get(i).getIndex()-1){
                builder.append(sorted.get(i).getWord());
            }else{
                out.add(builder.toString());
                builder = new StringBuilder();
                last = sorted.get(i).getIndex()-1;
            }
        }
        return out;
    }

    public String toStringBeautifulOutput(){
        Set<String> outPremises = simplifyFrameNetTagList(this.premise);
        Set<String> outConlusions = simplifyFrameNetTagList(this.conclusion);

        return "Rule{\n" +
                "\tSentence : "+sentence+
                "\n\tIF : " + outPremises +
                "\n\t, THEN : " + outConlusions +
                "\n\t, correspondingPatterns= '" + correspondingPatterns + '\'' +
                "\n}";

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
