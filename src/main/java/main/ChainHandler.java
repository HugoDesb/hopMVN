package main;

import MWExtraction.MWE;
import MWExtraction.MWEExtractor;
import MWExtraction.NGram;
import document.TextDocument;
import pretreatement.Extractor.PdfToSentences;
import tagging.RNNTagger.RNNTagger;

import java.io.File;
import java.io.IOException;
import java.util.Map;

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
            //TODO : extractor instance with config file or config Object (better) and whether it's an expert extraction
            TextDocument textDocument = PdfToSentences.extract("./files/depression_adulte_recommandations_version_mel expertisé.pdf", true);

            // Main 2 --- TAG THE SENTENCES
            RNNTagger tagger = new RNNTagger();
            tagger.tag(textDocument);

            // Main 3 --- Extract Multi-words expressions
            //TODO : config file ?
            MWEExtractor mweExtractor = new MWEExtractor(4);
            MWE mwe = mweExtractor.extractGrams(textDocument.getLines());

            // Main 4 --- Branch 1 --- Compute C-value for all MWE -- branch 1
            Map<NGram, Double> ngramCollocation = mwe.getCValueForAll();
            //PRINT
            //TODO : the print here isn't meant to stay
            for (NGram n: ngramCollocation.keySet()) {
                System.out.println(n.toString() +"__"+ngramCollocation.get(n));
            }

        }catch (IOException e){
            e.printStackTrace();
        }



    }

}
