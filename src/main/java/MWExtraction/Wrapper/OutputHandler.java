package Wrapper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// TODO rename
public class OutputHandler {

    private static final Double THRESHOLD = 0.0;

    public static void write(String sourceFile, String t4gramsCSVFile,
                             String t3gramsCSVFile, String t2gramsCSVFile,
                             String t1gramsCSVFile, String outputFilePath) {

        File outPutFile = new File(outputFilePath);
        FileWriter fw = null;
        try {
            fw = new FileWriter(outPutFile);

            for (String sentence: getSentences(sourceFile)) {
                String s = sentence.toLowerCase();

                // Write the sentence

                fw.write(s+"\n");
                // 4-mots
                fw.write("T4\n");
                recognizeMWE(t4gramsCSVFile, fw, s);

                // 3-mots
                fw.write("T3\n");
                recognizeMWE(t3gramsCSVFile, fw, s);

                // 2-mots
                fw.write("T2\n");
                recognizeMWE(t2gramsCSVFile, fw, s);

                // 1-mots
                fw.write("T1\n");
                recognizeMWE(t1gramsCSVFile, fw, s);

                fw.write("##########END##########\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private static void recognizeMWE(String gramsCSVFile, FileWriter fw, String s) throws Exception {
        for (List<String> csvLine : MultiColumnCSVSort.compareHetopThenMeasure(gramsCSVFile)) {
            if(s.contains(csvLine.get(0)) && Double.parseDouble(csvLine.get(2))> THRESHOLD){
                fw.write(csvLine.get(0) + "\t"+ csvLine.get(1)+ "\t"+ csvLine.get(2)+"\n");
                s = s.replace(csvLine.get(0).subSequence(0, csvLine.get(0).length()), "");
                //s.replaceAll(csvLine.get(0).replaceAll("\\(", "");
            }
        }
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
}
