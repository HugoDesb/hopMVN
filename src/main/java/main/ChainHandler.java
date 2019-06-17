package main;

import common.document.Sentence;
import common.document.TextDocument;
import pretreatement.Extractor.PdfToSentences;
import tagging.RNNTagger.RNNTagger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class to have here the whole chain processes
 */
public class ChainHandler {

    public static void fullChainStandard(File pdfFile){
        if(!pdfFile.exists()){
            throw new IllegalArgumentException("The file given doesn't exists");
        }

        try{

            // Main 1 --- PDF TO SENTENCES
            //TODO : extractor instance with common.config file or common.config Object (better) and whether it's an expert extraction
            TextDocument textDocument = PdfToSentences.extract(pdfFile.getPath(), false);

            textDocument.writeFile();

            // Main 2 --- TAG THE SENTENCES
            RNNTagger tagger = new RNNTagger();
            tagger.tag(textDocument);

            // Main 3 --- Extract Multi-words expressions
            //TODO : common.config file ?
            /*
            MWEExtractor mweExtractor = new MWEExtractor(1, 4);
            MWE mwe = mweExtractor.generateGrams(textDocument);


            for (NGram n :mwe.getNGramsOfLength(4)) {
                System.out.println(n.toString());
            }
            for (NGram n :mwe.getNGramsOfLength(3)) {
                System.out.println(n.toString());
            }
            for (NGram n :mwe.getNGramsOfLength(2)) {
                System.out.println(n.toString());
            }
            for (NGram n :mwe.getNGramsOfLength(1)) {
                System.out.println(n.toString());
            }

            // Main 4 --- Branch 1 --- Compute C-value for all MWE -- branch 1
            Map<NGram, Double> ngramCollocation = mwe.getCValueForAll();
            //PRINT
            //TODO : the print here isn't meant to stay
            int count = 0;
            for (NGram n: ngramCollocation.keySet()) {
                count ++;
                System.out.println(count + "  ---------------------------------------------");
                System.out.println(n.toString());
                System.out.println(ngramCollocation.get(n));
                //if(count == 100) break;
            }
            String hop = "";
            */
        }catch (IOException e){
            e.printStackTrace();
        }



    }

    public static void mweExtractionModule(){

    }


    /**
     * Apply the pretreatment module for the specified pdf file
     * @param pdfFile the pdf file
     * @param isExpertFile if the given file has to be treated as highlighted (by an expert)
     * @param writeInTemporaryFile write in a text file the selected sentences
     * @return The list of sentences
     */
    public static ArrayList<Sentence> pretreatmentModule(File pdfFile, boolean isExpertFile, boolean writeInTemporaryFile){
        TextDocument textDocument = null;
        try {
            textDocument = PdfToSentences.extract(pdfFile.getPath(), isExpertFile);
            if(writeInTemporaryFile){
                textDocument.writeFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return textDocument.getLines();
    }

}
