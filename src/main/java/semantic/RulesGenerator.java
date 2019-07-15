package semantic;

import MWExtraction.Wrapper.MultiColumnCSVSort;
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



    public void combineWithMWE(String mweFolder) {

        // if comparing fails, at least we have the regular csv file
        List<List<String>> t2grams = MultiColumnCSVSort.readCsv(mweFolder+"t2gram.csv");
        List<List<String>> t3grams = MultiColumnCSVSort.readCsv(mweFolder+"t3gram.csv");
        List<List<String>> t4grams = MultiColumnCSVSort.readCsv(mweFolder+"t4gram.csv");
        try {
            t2grams = MultiColumnCSVSort.compareHetopThenMeasure(mweFolder+"t2gram.csv");
            t3grams = MultiColumnCSVSort.compareHetopThenMeasure(mweFolder+"t3gram.csv");
            t4grams = MultiColumnCSVSort.compareHetopThenMeasure(mweFolder+"t4gram.csv");
        } catch (Exception e) {
            e.printStackTrace();
        }


        for (Rule r : generatedRules) {
            ArrayList<String> premises = new ArrayList<>(r.getPremisesToStrings());
            for (String s: premises) {
                boolean foundT2Start = false;
                boolean foundT2End = false;

                boolean foundT3Start = false;
                boolean foundT3End = false;

                boolean foundT4Start = false;
                boolean foundT4End = false;


                //for (int i = 4; i > 1 ; i--) {


                String[] words = s.split("\\w");
                // Check if t2 exists at the start and/or at the end
                // if YES then don't try to find other m-w COMBINE
                String ret, query;
                if (!foundT2Start) {
                    query = words[0].trim();
                    ret = searchInCSV(t2grams, query, false, 0);
                    if (ret != null) {
                        premises.set(premises.indexOf(s), s.replaceAll(query, ret));
                        foundT2Start = true;
                    }
                }

                if (!foundT2End) {
                    query = words[words.length - 1].trim();
                    ret = searchInCSV(t2grams, query, true, 0);
                    if (ret != null) {
                        premises.set(premises.indexOf(s), s.replaceAll(query, ret));
                        foundT2End = true;
                    }
                }

                // Check if t3 exists at the start and the end
                // if YES then don't try to find other m-w
                // if NOT then try to find a mw ending or starting with respectively same first word and last word
                //      IF NOTHING try to find a mw starting or ending with respectively two same first and last words
                // both line above to be inverted
                if (!foundT2Start && !foundT3Start && words.length >= 2) {
                    query = words[0].trim() + " " + words[1].trim();
                    ret = searchInCSV(t3grams, query, true, 0);
                    if (ret != null) {
                        premises.set(premises.indexOf(s), s.replaceAll(query, ret));
                        foundT3Start = true;
                    } else {
                        query = words[0].trim();
                        ret = searchInCSV(t3grams, query, true, 0);
                        if (ret != null) {
                            premises.set(premises.indexOf(s), s.replaceAll(query, ret));
                            foundT3Start = true;
                        }
                    }

                    if (!foundT2End && !foundT3End && words.length >= 2) {
                        query = words[words.length - 2].trim() + " " + words[words.length - 1].trim();
                        ret = searchInCSV(t3grams, query, false, 0);
                        if (ret != null) {
                            premises.set(premises.indexOf(s), s.replaceAll(query, ret));
                            foundT3End = true;
                        }else{
                            query = words[words.length - 1].trim();
                            ret = searchInCSV(t3grams, query, false, 0);
                            if (ret != null) {
                                premises.set(premises.indexOf(s), s.replaceAll(query, ret));
                                foundT3Start = true;
                            }
                        }
                    }

                    // do the same for t4
                    // Check if t2 exists at the start and the end
                    // if YES then don't try to find other m-w
                    // if NOT then try to find a mw ending or starting with respectively first word and last word

                    if (!foundT2Start && !foundT3Start && foundT4Start && words.length >= 3) {
                        query = words[0].trim() + " " + words[1].trim() + " " + words[2].trim();
                        ret = searchInCSV(t4grams, query, true, 0);
                        if (ret != null) {
                            premises.set(premises.indexOf(s), s.replaceAll(query, ret));
                            foundT4Start = true;
                        } else {
                            query = words[0].trim() + " " + words[1].trim();
                            ret = searchInCSV(t4grams, query, true, 0);
                            if (ret != null) {
                                premises.set(premises.indexOf(s), s.replaceAll(query, ret));
                                foundT4Start = true;
                            }else{
                                query = words[0].trim();
                                ret = searchInCSV(t4grams, query, true, 0);
                                if (ret != null) {
                                    premises.set(premises.indexOf(s), s.replaceAll(query, ret));
                                    foundT4Start = true;
                                }
                            }
                        }

                        if (!foundT2End && !foundT3End && foundT4End && words.length >= 3) {
                            query = words[words.length - 3].trim() + " " + words[words.length - 2].trim() + " " + words[words.length - 1].trim();
                            ret = searchInCSV(t4grams, query, false, 0);
                            if (ret != null) {
                                premises.set(premises.indexOf(s), s.replaceAll(query, ret));
                                foundT4Start = true;
                            } else {
                                query = words[words.length - 2].trim() + " " + words[words.length - 1].trim();
                                ret = searchInCSV(t4grams, query, false, 0);
                                if (ret != null) {
                                    premises.set(premises.indexOf(s), s.replaceAll(query, ret));
                                    foundT4End = true;
                                }else{
                                    query = words[words.length - 1].trim();
                                    ret = searchInCSV(t4grams, query, false, 0);
                                    if (ret != null) {
                                        premises.set(premises.indexOf(s), s.replaceAll(query, ret));
                                        foundT4End = true;
                                    }
                                }
                            }
                        }

                    }
                    //}
                }
            }
        }
    }

    private String searchInCSV(List<List<String>> csvFile, String queryWords ,boolean atTheStart, int column){
        for (List<String> line: csvFile) {
            if(atTheStart){
                if(line.get(column).toLowerCase().indexOf(queryWords)!=-1){
                    return line.get(column).toLowerCase();
                }
            }else{
                int lastIndex = line.get(column).toLowerCase().indexOf(queryWords);
                lastIndex += queryWords.length();
                if(lastIndex == queryWords.length()){
                    return line.get(column).toLowerCase();
                }
            }
        }
        return null;
    }

    public ArrayList<Rule> getGeneratedRules() {
        return generatedRules;
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
