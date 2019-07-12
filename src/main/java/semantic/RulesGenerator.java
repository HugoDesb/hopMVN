package semantic;

import common.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.*;

public class RulesGenerator {

    private SemanticOpenSesameTagging sentences;
    private FrameNetPatterns frameNetPatterns;
    private String topic;

    RulesGenerator(SemanticOpenSesameTagging sentences, FrameNetPatterns patterns, String topic) {
        this.sentences = sentences;
        this.frameNetPatterns = patterns;
        this.topic = topic;
    }

    public ArrayList<Rule> generateRules(){
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

        if(patterns.size()==0){
            return new ArrayList<>();
        }

        int max = patterns.size();
        List<Pattern> partialPattern;
        ArrayList<Set<Integer>> hop = new ArrayList<>();

        int i = (frameNetPatterns.isInBlacklist(patterns.get(0))) ? 2 : 1;

        for (; i <= max; i++) {
            partialPattern = patterns.subList(0, i);

            Set<Integer> toAdd = new HashSet<>();
            for (Pattern pattern: partialPattern) {
                Set<Integer> tmp3 = new HashSet<>(sentence.matches(pattern));
                if(toAdd.size()==0){
                    toAdd = tmp3;
                }else{
                    toAdd.retainAll(tmp3);
                }
            }
            hop.add(toAdd);
        }

        for (int j = hop.size()-1; i>=0; i--) {
            if(hop.get(j).size()!=0){
                return new ArrayList<>(hop.get(j));
            }
        }

        return new ArrayList<>();

        /**
        while(max>=1){
            // try with all frameNetPatterns, then minus le last one, and again
            ArrayList<Set<Integer>> hop = new ArrayList<>();

            Set<Integer> toAdd = new HashSet<>();
            for (Pattern pattern: patterns) {
                Set<Integer> tmp3 = new HashSet<>(sentence.matches(pattern));
                if(toAdd.size()==0){
                    toAdd = tmp3;
                }else{
                    toAdd.retainAll(tmp3);
                }
            }
            hop.add(toAdd);


            for (int i = 0; i < max; i++) {
                ArrayList<Integer> tmp2 = new ArrayList<>();
                // Each frameNetPatterns separated by a comma
                List<Pattern> tmp = patterns.subList(0, i);
                for (Pattern p :tmp) {
                    tmp2 = sentence.matches(p);

                    if((tmp2.size() != 0) && !(frameNetPatterns.isInBlacklist(p) && max == 0)){
                        hop.add(tmp2);
                    }
                }
            }
            if(max == 1){
                matched = reduce(hop);
                max = -1;
            }
            max--;
        }
        return matched;
         */
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

    void writeResults(ArrayList<Rule> rules, String file) {
        //File f = new File(file);
        (new File(Paths.get(file).getParent().toString())).mkdirs();
        //f.mkdirs();
        try {
            PrintStream ps = new PrintStream(new File(file));
            //PrintWriter pw = new PrintWriter(new File("./files/tmp.txt"));
            for (Rule r : rules) {
                ps.println(r.toStringOutput());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * MAIN -----> read and analyse results from Open-Sesame to create rules.txt.backup
     * @param args
     */
    /*
    public static void main(String [] args){
        SemanticOpenSesameTagging sost = new SemanticOpenSesameTagging(new File(
                "/home/sesstim/Telechargements/open-sesame/open-sesame/logs/myargid/predicted-args.conll"
        ));
        FrameNetPatterns fnp = new FrameNetPatterns(new File("./files/SemanticAnalysis/framenetpatternV2"));

        RulesGenerator rg = new RulesGenerator(sost, fnp, "type 2 diabetes");

        ArrayList<Rule> hop = rg.generateRules();

        rg.writeResults(hop);
    }
    */


}
