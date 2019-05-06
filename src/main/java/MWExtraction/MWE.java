package MWExtraction;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;

public class MWE {

    private ArrayList<ArrayList<NGram>> allMWE;
    private int maxLength;

    public MWE(int maxLength) {
        this.allMWE = new ArrayList<>();
        for (int i = 0; i<maxLength ; i++){
            allMWE.add(new ArrayList<NGram>());
        }
    }

    /**
     * Gets all MWE of specified getN
     * @param length the desired MWE getN
     * @return an array of NGram (each NGram is a MWE of specified getN)
     */
    public ArrayList<NGram> getNGramsOfLength(int length){
        if(length < 1|| getMaxLength() < length){
            throw new InvalidParameterException("getN should be between 1 and " + getMaxLength());
        }
        return allMWE.get(length-1);
    }

    public void addNGram(NGram gram){
        //System.out.println("Adds a gram");
        allMWE.get(gram.getN()-1).add(gram);
    }


    //TODO : DONE
    private HashMap<NGram, Double> computeFrequencies(){
        HashMap<NGram, Double> ret = new HashMap<>();
        //For each i in N (i-gram list)
        for (ArrayList<NGram> igramsOfSameLength: allMWE) {
            int totalOfSameLength = igramsOfSameLength.size();
            for (NGram currentNgram: igramsOfSameLength) {
                //System.out.println("Current NGram : "+currentNgram.toString());
                if(ret.keySet().contains(currentNgram)){
                    double value = ret.get(currentNgram);
                    ret.replace(currentNgram, value, value+1);
                }else{
                    ret.put(currentNgram, 1.0);
                }
            }

            for (NGram gram : igramsOfSameLength) {
                double value = ret.get(gram);
                ret.replace(gram, value, value/totalOfSameLength);
            }
        }
        System.out.println("Frequencies computed: "+ret.size());
        return ret;
    }

    /**
     * Compute the collocation value (aka c-value) for all ngrams.
     * @return A Map with the ngram and its collocation value
     */
    public Map<NGram, Double> getCValueForAll(){
        HashMap<NGram, Double> ret = new HashMap<>();
        HashMap<NGram, Double> frequencies = computeFrequencies();

        double collocationValue, w_A, sumFreq;

        for (int i = getMaxLength(); i > 0; i--) {
            for (NGram currentGram: frequencies.keySet()) {

                //System.out.println("###############################################################");
                //System.out.println("N = "+currentGram.getN());
                w_A = Math.log(currentGram.getN())/Math.log(2);
                //System.out.println("log2(|a|) = "+w_A);
                //Check if currentGram is nested and get all bigger NGram in that case
                HashSet<NGram> biggerNGramsContainingCurrentGram = getBiggerNGramsContaining(currentGram, frequencies.keySet());
                // if YES
                if(!biggerNGramsContainingCurrentGram.isEmpty()) {
                    // Sum frequencies of bigger ngrams
                    //System.out.println("NESTED = false");
                    sumFreq = 0.0;
                    for (NGram tmpGram: biggerNGramsContainingCurrentGram) {
                        sumFreq += frequencies.get(tmpGram);
                    }

                    //System.out.println("\tSum frequencies sum = "+sumFreq);

                    collocationValue = w_A * (frequencies.get(currentGram) - sumFreq/biggerNGramsContainingCurrentGram.size());
                    ret.put(currentGram, collocationValue);
                }else {
                    //System.out.println("NESTED = true");
                    collocationValue = w_A * frequencies.get(currentGram);
                    ret.put(currentGram, collocationValue);
                }
            }
        }
        System.out.println("C-value computed : "+ret.size());

        Map<NGram, Double> sorted = ret
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
        return sorted;
    }

    /**
     * Finds and returns any ngrams that contains the given one
     * @param currentGram the gram to search for.
     * @param gramsSet the set of ngrams to search in.
     * @return all ngrams that contains the given ngram
     */
    private HashSet<NGram> getBiggerNGramsContaining(NGram currentGram, Set<NGram> gramsSet) {
        HashSet<NGram> ret = new HashSet<>();

        for (NGram nGram : gramsSet) {
            if(nGram.getN() > currentGram.getN() && currentGram.isIn(nGram)){
                ret.add(nGram);
            }
        }

        return ret;
    }

    /**
     * Obtain the corresponding .arff File
     * @param dataPerWord following the pattern "Word D1 D2 D3 D4 Lemma" (one must choose which ones to keep)
     * @param minSize The minimum size of n-gram
     * @param maxSize The maximum size of n-gram
     * @return the .arff file
     */
    public File getFile(String dataPerWord, int minSize, int maxSize) {
        File tmpFile = new File("tmp_arff.arff");
        try {
            FileWriter fw = new FileWriter(tmpFile);
            writeArffFileHeader(fw, dataPerWord, minSize, maxSize);
            writeArffFileData(fw, dataPerWord, minSize, maxSize);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tmpFile;
    }

    /**
     * Write MWE header in given file writer
     * @param fw the fileWriter
     * @param dataPerWord following the pattern "Word D1 D2 D3 D4 Lemma" (one must choose which ones to keep)
     * @param minSize The minimum size of n-gram
     * @param maxSize The maximum size of n-gram
     * @throws IOException if the file writer fails
     */
    private void writeArffFileHeader(FileWriter fw, String dataPerWord, int minSize, int maxSize) throws IOException {
        fw.write("@relation MWE\n");

        String [] data = dataPerWord.split("\\s");
        for (int i = minSize; i <= maxSize; i++) {
            for(String dataElement : data){
                fw.write("@attribute "+ dataElement + "_"+(i-minSize)+" string\n");
            }
        }
    }

    /**
     * Write MWE data in given file writer
     * @param fw the fileWriter
     * @param dataPerWord following the pattern "Word D1 D2 D3 D4 Lemma" (one must choose which ones to keep)
     * @param minSize The minimum size of n-gram
     * @param maxSize The maximum size of n-gram
     * @throws IOException if the file writer fails
     */
    private void writeArffFileData(FileWriter fw, String dataPerWord, int minSize, int maxSize) throws IOException {
        fw.write("@data\n");

        String [] data = dataPerWord.split("\\s");
        for(int i = minSize-1; i<maxSize; i++){
            ArrayList<NGram> allForSpecificSize = allMWE.get(i);
            for (NGram ngram : allForSpecificSize) {
                for (int j = 0; j < ngram.getN(); j++) {
                    boolean lastword = false;
                    if(j == ngram.getN()-1){
                        lastword = true;
                    }
                    for (int k = 0; k < data.length; k++) {
                        String end = ",";
                        if(lastword && k == data.length-1){
                            end = "\n";
                        }
                        fw.write(ngram.get(j).getData(data[k])+end);
                    }
                }
            }
        }
        fw.close();
    }

    /**
     * Gets MWE max getN
     * @return the max getN
     */
    public int getMaxLength(){
        return allMWE.size();
    }
}
