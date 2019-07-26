package semantic;

import common.Pair;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class RulesGenerator {

    private SemanticOpenSesameTagging sentences;
    private FrameNetPatterns frameNetPatterns;
    private String topic;

    private ArrayList<Rule> generatedRules;

    RulesGenerator(SemanticOpenSesameTagging sentences, FrameNetPatterns patterns, String topic) {
        this.sentences = sentences;
        this.frameNetPatterns = patterns;
        this.topic = topic;
    }

    public void generateRules(){
        Map<String, Pair<ArrayList<Integer>, ArrayList<Integer>>> map = new HashMap<>();
        ArrayList<Rule> rules = new ArrayList<>();
        ArrayList<Sentence> sentences = this.sentences.getSentences();
        Iterator<Sentence> it = sentences.iterator();
        Sentence sentence;

        while(it.hasNext()){
            sentence = it.next();
            //System.out.println("--"+sentence.getSentence());
            if(sentence.getSentence().equals("measure blood pressure at least annually in an adult with type 2 diabetes without previously diagnosed hypertension or renal disease")){
                System.out.println("STOOOOOOOOP");
                System.out.println("kjgk");
            }
            Rule r = new Rule(sentence, topic);
            for (FrameNetPattern pattern: frameNetPatterns.getFrameNetPatterns()) {

                ArrayList<Integer> matchedPremise = findMatchingWordsIndexes(pattern.getPremises(), sentence);
                ArrayList<Integer> matchedConclusion = findMatchingWordsIndexes(pattern.getConclusions(), sentence);

                if((matchedPremise.size()!=0 || pattern.getPremises().size()==0) && (matchedConclusion.size()!=0|| pattern.getConclusions().size()==0)){
                    List<Word> addToPremises = sentence.getSentence(matchedPremise);
                    List<Word> addToConclusions = sentence.getSentence(matchedConclusion);
                    r.addMatchPremise(addToPremises);
                    r.addMatchConclusion(addToConclusions);

                    if(addToPremises.size()!=0){
                        //System.out.println(pattern.toString());
                        String s = "";
                        for (Word w: addToPremises) {
                            s+=" "+w.getText();
                        }
                        //System.out.println(s);
                        r.addMatchPattern(pattern, addToPremises);
                    }
                    if(addToConclusions.size()!=0){
                        //System.out.println(pattern.toString());
                        String s = "";
                        for (Word w: addToConclusions) {
                            s+=" "+w.getText();
                        }
                        //System.out.println(s);
                        r.addMatchPattern(pattern, addToConclusions);
                    }

                    //r.addMatchPattern(pattern);
                }
            }
            r.computeStrings();
            rules.add(r);
        }

        generatedRules = rules;
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
                Integer targetIndex = sentence.getTargetMatch(pattern);
                Set<Integer> tmp3 = new HashSet<>(sentence.matches(pattern));
                tmp3 = includePartOfSentence(tmp3, sentence);
                tmp3 = filterResults(tmp3, pattern, sentence);
                if(toAdd.size()==0){
                    toAdd = tmp3;
                }else{
                    if(includeTarget(tmp3, targetIndex, sentence, pattern)){
                        toAdd.add(targetIndex);
                    }
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
    }

    private Set<Integer> includePartOfSentence(Set<Integer> tmp3, Sentence sentence) {
        ArrayList<ArrayList<Integer>> hop = separateChunks(tmp3);

        Set<Integer> ret = new HashSet<>();
        for (ArrayList<Integer> integers : hop) {
            int firstIndex = integers.get(0);
            if(sentence.getWord(firstIndex).getLemma().equals("of")){
                if(firstIndex>=2 && sentence.getWord(firstIndex-1).getPos_tag().equals("NN")){
                    ret.add(firstIndex-1);

                }
                ret.addAll(integers);
            }else{
                if(integers.get(0)>=3
                        && sentence.getWord(firstIndex-2).getPos_tag().equals("NN")
                        && sentence.getWord(firstIndex-1).getLemma().equals("of")){
                    ret.add(firstIndex-2);
                    ret.add(firstIndex-1);
                }
                ret.addAll(integers);
            }
        }
        return ret;
    }

    private boolean includeTarget(Set<Integer> indexes, Integer targetIndex, Sentence sentence, Pattern pattern){
        boolean targetIsAfter = false;
        boolean targetIsBefore = false;

        for (Integer index : indexes) {
            if(index == targetIndex-1){
                targetIsAfter = true;
            }
            if(index == targetIndex+1){
                targetIsBefore = true;
            }
        }
        boolean ret = false;
        if(targetIsAfter){
            ret = ret || includeTargetBasedOnFrame(pattern);
        }
        if(targetIsBefore){
            ret = ret || includeTargetBasedOnFrame(pattern);
            ret = sentence.getWord(targetIndex).getPos_tag().equals("VB") ||
                    sentence.getWord(targetIndex).getPos_tag().equals("JJ") ||
                    sentence.getWord(targetIndex).getPos_tag().equals("VBD") ||
                    sentence.getWord(targetIndex).getPos_tag().equals("VBG") ||
                    sentence.getWord(targetIndex).getPos_tag().equals("VBP") ||
                    sentence.getWord(targetIndex).getPos_tag().equals("VBZ") ||
                    sentence.getWord(targetIndex).getPos_tag().equals("VBN");
        }

        return ret;
    }

    private boolean includeTargetBasedOnFrame(Pattern pattern) {
        if(pattern.getFrame().equals("Preventing_or_letting")){
            return true;
        }
        return false;
    }

    private ArrayList<ArrayList<Integer>> separateChunks(Set<Integer> indexes){
        ArrayList<Integer> hop = new ArrayList<>(indexes);
        Collections.sort(hop);
        ArrayList<ArrayList<Integer>> ret = new ArrayList<>();

        if(indexes.size()!=0){
            int last = hop.get(0);
            int j = 0;
            ret.add(new ArrayList<>());
            ret.get(j).add(last);
            for (int i = 1; i < hop.size(); i++) {
                if(last+1 == hop.get(i)){
                    ret.get(j).add(hop.get(i));
                    last = hop.get(i);
                }else{
                    ret.add(new ArrayList<>());
                    j++;
                    ret.get(j).add(hop.get(i));
                    last = hop.get(i);
                }
            }
        }

        return ret;
    }

    private Set<Integer> filterResults(Set<Integer> tmp3, Pattern pattern, Sentence sentence) {
        Set<Integer> ret = new HashSet<>();

        if(tmp3.size()==0){
            return ret;
        }

        ArrayList<ArrayList<Integer>> hop2 = separateChunks(tmp3);

        for (ArrayList<Integer> chunk: hop2) {
            if(chunk.size()==1){
                Word w = sentence.getWord(chunk.get(0));
                //Do not add when is a single EX pos_tag
                if(w.getPos_tag().equals("EX")){
                    //not adding to the new set
                }else{
                    ret.add(chunk.get(0));
                }
            }else{
                //Not yet any conditions
                ret.addAll(chunk);
            }
        }
        return ret;
    }


    public ArrayList<Rule> getGeneratedRules() {
        return generatedRules;
    }

    public void combineMultiWordsExpression(String mweFile) {
        findMWE(mweFile);

        // Foreach rule found
        for (Rule r : getGeneratedRules()) {
            if(getGeneratedRules().indexOf(r) == 22){
                System.out.println("STOOOOOOOOOOOP");
            }
            // foreach found mwe in this sentence
            Set<String> new_set_premises = r.getPremisesToStrings();
            Set<String> new_set_conclusions = r.getConclusionsToStrings();
            for (String mwe : r.getMwe()) {

                new_set_premises = getCombinedWithMWE(new_set_premises, mwe);
                new_set_conclusions = getCombinedWithMWE(new_set_conclusions, mwe);

            }
            r.setPremisesString(new_set_premises);
            r.setConclusionsString(new_set_conclusions);
        }
    }

    private Set<String> getCombinedWithMWE(Set<String> old , String mwe){

        Set<String> new_set = new HashSet<>();

        for (String p: old) {

            boolean found = false;

            // check if the mwe contains the premise
            if(mwe.contains(p)){
                new_set.add(mwe);
                found = true;
            } else if(p.contains(p)){
                new_set.add(p);
                found = true;
            } else if(!found){

                // check if the mwe partially overlap the premise
                String [] splittedMWE = mwe.split("\\s");

                // The max length of the overlap window is mwe.length-1 or 3 (max ngram is 4)
                int max = (splittedMWE.length >= 4)? 3 : splittedMWE.length-1;

                for(int i = max; i>=1; i--){

                    // find whether its in the head
                    String head = extractHead(p, i);
                    if(mwe.contains(head)){
                        p = p.replaceAll(head, mwe);
                        new_set.add(p);
                        found = true;
                    }

                    //find whether its in the tail
                    String tail = extractTail(p, i);
                    if(mwe.contains(tail)){
                        p = p.replaceAll(tail, mwe);
                        new_set.add(p);
                        found = true;
                    }
                }
            }

            // none have been found, let it as it is
            if(!found){
                new_set.add(p);
            }

        }
        return new_set;
    }

    private String extractHead(String s, int length){
        s = s.trim().toLowerCase();
        String [] hop = s.split("\\s");

        if(length > hop.length){
            return s;
        }else{

            String ret = "";
            for (int i = 0; i < length; i++) {
                ret += hop[i];
            }
            return ret.trim();
        }
    }

    private String extractTail(String s, int length){
        s = s.trim().toLowerCase();
        String [] hop = s.split("\\s");
        if(length > hop.length){
            return s;
        }else{
            String ret = "";
            for (int i = 0; i < length; i++) {
                ret += hop[hop.length-(i+1)];
            }
            return ret.trim();
        }
    }

    private void findMWE(String mweFile) {

        try {
            BufferedReader bf = new BufferedReader(new FileReader(mweFile));
            String line = bf.readLine();
            boolean end = false;
            int currentSentence = 0;

            String previous = "##########END##########";
            Rule r = null;

            while (line != null) {
                int a = getRuleByNumber(Integer.parseInt(line.trim()));
                if(a>=0){
                    bf.readLine();
                    line = bf.readLine();
                    while (line != null && !line.equals("##########END##########")) {
                        if (!line.matches("^T[1-4]$")) {
                            getGeneratedRules().get(a).addMWE(line.split("\\t")[0]);
                        }
                        line = bf.readLine();
                    }
                }else{
                    while (line != null && !line.equals("##########END##########")) {
                        line = bf.readLine();
                    }
                }
                line = bf.readLine();

            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public int getRuleByNumber(int i){
        for (Rule r : getGeneratedRules()) {
            if(r.getSentence().getSentenceNumber() == i){
                return getGeneratedRules().indexOf(r);
            }
        }
        return -1;
    }



    /**
     * MAIN -----> read and analyse results from Open-Sesame to create rules.txt
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
