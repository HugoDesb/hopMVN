package semantic;

import common.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Set;

public class RulesGenerator {

    private SemanticOpenSesameTagging sentences;
    private FrameNetPatterns patterns;
    private String topic;

    private RulesGenerator(SemanticOpenSesameTagging sentences, FrameNetPatterns patterns, String topic) {
        this.sentences = sentences;
        this.patterns = patterns;
        this.topic = topic;
    }

    private ArrayList<Rule> generateRules(){
        ArrayList<Rule> rules = new ArrayList<>();
        for(int i = 0; i<sentences.getSentences().size(); i++){
            Rule r = new Rule(sentences.getSentences().get(i), topic);
            for (FrameNetPattern pattern : patterns.getFrameNetPatterns()) {
                Pair<Set<FrameNetTag>, Set<FrameNetTag>> hop = sentences.getSentences().get(i).matches(pattern);

                if(!hop.getValue2().isEmpty()){
                    r.addMatch(hop.getValue1() , hop.getValue2(), pattern);
                }
            }
            if(!r.isEmpty()) {
                rules.add(r);
            }
        }
        return rules;
    }

    private void writeResults(ArrayList<Rule> rules) {
        try {
            PrintStream ps = new PrintStream(new File("./files/OUTPUT/SemanticOS/rules.txt"));
            //PrintWriter pw = new PrintWriter(new File("./files/tmp.txt"));
            for (Rule r : rules) {
                ps.println(r.toStringOutput());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void main(String [] args){
        SemanticOpenSesameTagging sost = new SemanticOpenSesameTagging(new File(
                "/home/sesstim/Telechargements/open-sesame/open-sesame/logs/myargid/predicted-args.conll"
        ));
        FrameNetPatterns fnp = new FrameNetPatterns(new File("./files/SemanticAnalysis/patterns"));

        RulesGenerator rg = new RulesGenerator(sost, fnp, "type 2 diabetes");

        ArrayList<Rule> hop = rg.generateRules();

        rg.writeResults(hop);
    }


}
