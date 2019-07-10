package semantic;

import common.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;

public class RulesGenerator {

    private SemanticOpenSesameTagging sentences;
    private FrameNetPatterns frameNetPatterns;
    private String topic;

    private RulesGenerator(SemanticOpenSesameTagging sentences, FrameNetPatterns patterns, String topic) {
        this.sentences = sentences;
        this.frameNetPatterns = patterns;
        this.topic = topic;
    }

    private ArrayList<Rule> generateRules(){
        Map<String, Pair<ArrayList<Integer>, ArrayList<Integer>>> map = new HashMap<>();
        ArrayList<Rule> rules = new ArrayList<>();
        ArrayList<Sentence> sentences = this.sentences.getSentences();
        Iterator<Sentence> it = sentences.iterator();
        Sentence sentence;

        while(it.hasNext()){
            sentence = it.next();
            Rule r = new Rule(sentence, topic);
            for (FrameNetPattern pattern: frameNetPatterns.getFrameNetPatterns()) {

                ArrayList<Integer> matchedPremise = findMatchingWordsIndexes(pattern.getPremises(), sentence);
                ArrayList<Integer> matchedConclusion = findMatchingWordsIndexes(pattern.getConclusions(), sentence);

                if((matchedPremise.size()!=0 || pattern.getPremises().size()==0) && (matchedConclusion.size()!=0|| pattern.getConclusions().size()==0)){
                    r.addMatchPremise(sentence.getSentence(matchedPremise));
                    r.addMatchConclusion(sentence.getSentence(matchedConclusion));
                    r.addMatchPattern(pattern);
                }
            }
            rules.add(r);
        }
        return rules;
    }

    private ArrayList<Integer> findMatchingWordsIndexes(ArrayList<Pattern> patterns, Sentence sentence){
        ArrayList<Integer> matched = new ArrayList<>();
        //ArrayList<Pattern> frameNetPatterns = frameNetPattern.getPremises();
        int max = patterns.size();
        while(max>=0){
            // try with all frameNetPatterns, then minus le last one, and again
            for (int i = 0; i < max; i++) {
                ArrayList<ArrayList<Integer>> hop = new ArrayList<>();
                ArrayList<Integer> tmp2 = new ArrayList<>();
                // Each frameNetPatterns separated by a comma
                List<Pattern> tmp = patterns.subList(0, i);
                for (Pattern p :tmp) {
                    tmp2 = sentence.matches(p);
                    if((tmp2.size() != 0) && !(frameNetPatterns.isInBlacklist(p) && max == 0)){
                        hop.add(tmp2);
                    }
                }

                if(hop.size()!=0){
                    matched = reduce(hop);
                    max = -1;
                }
            }

            max--;
        }

        return matched;
    }

    private ArrayList<Integer> reduce(ArrayList<ArrayList<Integer>> hop) {
        ArrayList<Integer> ret = new ArrayList<>();
        for (ArrayList<Integer> subhop: hop) {
            for (Integer i: subhop) {
                if(!ret.contains(i)){
                    ret.add(i);
                }
            }
        }
        Collections.sort(ret);
        return ret;
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
