package MWExtraction.Wrapper;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// TODO rename
public class OutputHandler {

    private static final Double THRESHOLD = 0.5;
    private static final int COUNT_THRESHOLD = 2;

    private ArrayList<Sentence> allSentences;
    private Set<String> basicRules;

    public OutputHandler() {
        this.allSentences = new ArrayList<>();
        this.basicRules = new HashSet<>();
    }

    public void write(String outputSentencesFP, String outputBasicRulesFP){
        File outputSentencesFile = new File(outputSentencesFP);
        File outputBasicRulesFile = new File(outputBasicRulesFP);

        FileWriter fwS, fwR = null;
        try {
            fwS = new FileWriter(outputSentencesFile);
            for (Sentence s: allSentences) {
                fwS.write(s.toString());
            }
            fwS.close();

            fwR = new FileWriter(outputBasicRulesFile);
            for (String s : basicRules) {
                fwR.write(s+"\n");
            }
            fwR.close();
        }catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void buildOutput(String sourceFile, String t4gramsCSVFile,
                                     String t3gramsCSVFile, String t2gramsCSVFile,
                                     String t1gramsCSVFile) {

        ArrayList<Sentence> allSentences = new ArrayList<>();
        int i = 0;
        try {
            for (String s: getSentences(sourceFile)) {
                s = s.toLowerCase();
                Sentence sentence = new Sentence(s, i);

                s = recognizeMWE(t4gramsCSVFile, 4, s, sentence);

                // 3-mots
                s = recognizeMWE(t3gramsCSVFile, 3, s, sentence);

                // 2-mots
                s = recognizeMWE(t2gramsCSVFile, 2, s, sentence);

                // 1-mots
                recognizeMWE(t1gramsCSVFile, 1, s, sentence);

                allSentences.add(sentence);
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.allSentences = allSentences;
        this.basicRules = createBasicRules();
    }

    private static String recognizeMWE(String gramsCSVFile, int length, String s, Sentence sentence) throws Exception {
        for (List<String> csvLine : MultiColumnCSVSort.compareHetopThenMeasure(gramsCSVFile)) {
            if(s.contains(csvLine.get(0)) && Double.parseDouble(csvLine.get(2))> THRESHOLD){
                //int length, String text, boolean validated, double measure
                sentence.addMWE(length, csvLine.get(0), (Integer.parseInt(csvLine.get(1))==1), Double.parseDouble(csvLine.get(2)));
                //fw.buildOutput(csvLine.get(0) + "\t"+ csvLine.get(1)+ "\t"+ csvLine.get(2)+"\n");
                s = s.replace(csvLine.get(0).subSequence(0, csvLine.get(0).length()), "");
                //s.replaceAll(csvLine.get(0).replaceAll("\\(", "");
            }
        }
        return s;
    }


    public static ArrayList<String> getSentences(String filePath){
        ArrayList<String> sentences = new ArrayList<>();

        try{
            File sourceFile = new File(filePath);
            BufferedReader bf = new BufferedReader(new FileReader(sourceFile));

            String line = bf.readLine();
            while(line != null){
                if(!line.equals("##########END##########")){
                    sentences.add(line);
                }
                line = bf.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sentences;
    }

    private Set<String> createBasicRules() {
        Set<String> basicRules = new HashSet<>();
        basicRules = firstRule(basicRules);
        basicRules = secondRule(basicRules);
        basicRules = thirdRule(basicRules);
        return basicRules;
    }

    public Set<String> thirdRule(Set<String> basicRules){
        for (Sentence s : allSentences) {
            int i = 1;
            boolean existsInBigger = false;
            for (int j = 2; j < 4; j++) {
                for (MWE ti : s.getAllMWEFor(1)) {
                    boolean canbeAdded = oneWordExistsInBiggerNWords(s, ti) && (existsAsFirstOneWord(s, ti) || oneWordExistsInOtherOneWordsExpression(ti));
                    if(canbeAdded){
                        basicRules.add(ti.getText());
                    }
                }
            }
        }
        return basicRules;
    }

    private boolean existsAsFirstOneWord(Sentence s, MWE ti) {
        for (MWE otherT1: s.getAllMWEFor(1)) {
            if(otherT1.getMeasure()>ti.getMeasure()){
                return false;
            }
        }
        return true;
    }

    private boolean oneWordExistsInOtherOneWordsExpression(MWE ti) {
        for (Sentence s : allSentences) {
            for (MWE t1prime: s.getAllMWEFor(1)) {
                if(t1prime.getText().toLowerCase().equals(ti.getText().toLowerCase())){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean oneWordExistsInBiggerNWords(Sentence s, MWE nWord){
        boolean existsInBigger = false;
        for (MWE tj : s.getAllMWEFor(2)) {
            if (tj.getText().toLowerCase().contains(nWord.getText().toLowerCase())) {
                existsInBigger = true;
            }
        }
        for (MWE tj : s.getAllMWEFor(3)) {
            if (tj.getText().toLowerCase().contains(nWord.getText().toLowerCase())) {
                existsInBigger = true;
            }
        }
        for (MWE tj : s.getAllMWEFor(4)) {
            if (tj.getText().toLowerCase().contains(nWord.getText().toLowerCase())) {
                existsInBigger = true;
            }
        }
        return existsInBigger;
    }

    private Set<String> secondRule(Set<String> basicRules){
        for (Sentence s : allSentences) {
            for (int i = 1; i < 4; i++) {
                for (int j = i; j < 4; j++) {
                    for (MWE ti : s.getAllMWEFor(i)) {
                        int count = 0;
                        for (MWE tj : s.getAllMWEFor(j)) {
                            if (tj.getText().toLowerCase().contains(ti.getText().toLowerCase())) {
                                count++;
                            }
                        }
                        if (count >= COUNT_THRESHOLD) {
                            basicRules.add(ti.getText());
                        }
                    }
                }
            }
        }
        return basicRules;
    }

    private Set<String> firstRule(Set<String> basicRules){
        for (Sentence s : allSentences) {

            if(s.getMWECountFor(4)==1 && s.getMWECountFor(3)==0 && s.getMWECountFor(2)==0){
                System.out.println("## get 4");
                basicRules.add(s.getAllMWEFor(4).get(0).getText());
            }else if(s.getMWECountFor(4)==0 && s.getMWECountFor(3)==1 && s.getMWECountFor(2)==0){
                System.out.println("## get 3");
                basicRules.add(s.getAllMWEFor(3).get(0).getText());
            }else if(s.getMWECountFor(4)==0 && s.getMWECountFor(3)==0 && s.getMWECountFor(2)==1){
                System.out.println("## get 2");
                basicRules.add(s.getAllMWEFor(2).get(0).getText());
            }
        }
        return basicRules;
    }

    public void combineMeasures(String source_OUTPUT) {
        for (int i = 1; i<=4; i++) {
            String ocapi = source_OUTPUT+"F-OCapi_A/t"+i+"gram.csv";
            String tfidf = source_OUTPUT+"TFIDF_A/t"+i+"gram.csv";
            String output= source_OUTPUT+"t"+i+"gram.csv";
            writeInFile(output, combineTwo(ocapi, tfidf));
        }
    }

    public void writeInFile(String filepath, List<List<String>> lines){
        File f = new File(filepath);
        try {
            PrintWriter pw = new PrintWriter(f);
            String s;
            for (List<String> line: lines) {
                s = "";
                for (String cell : line) {
                    s += cell+"\t";
                }
                System.out.println("Write ["+s+"]");
                pw.println(s);
            }
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<List<String>> combineTwo(String sourceOcapi, String sourceTFIDF){
        List<List<String>> hop = new ArrayList<>();

        List<List<String>> ocapiCSV = MultiColumnCSVSort.readCsv(sourceOcapi);
        List<List<String>> tfidfCSV = MultiColumnCSVSort.readCsv(sourceTFIDF);

        for (List<String> lineOcapi: ocapiCSV) {
            for (List<String> linetfidf: tfidfCSV) {
                if(lineOcapi.get(0).equals(linetfidf.get(0))){
                    System.out.println("lineOcapi : "+lineOcapi.get(2));
                    System.out.println("linetfidf : "+linetfidf.get(2));
                    double newValue = Double.parseDouble(lineOcapi.get(2)) - (1 - Double.parseDouble(linetfidf.get(2)));
                    lineOcapi.set(2, Double.toString(newValue));
                    hop.add(lineOcapi);
                }
            }
        }

        return hop;
    }
}
