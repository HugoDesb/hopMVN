package main;

import document.TextDocument;
import pretreatement.InitialFilter;
import tagging.RNNTagger.RNNTagger;
import tagging.RNNTagger.TaggedSentence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static pretreatement.Extractor.PdfToText.convert;

public class Main {

    public static final String pathReco = "./files/recos_txt/";
    public static final String pathRecoAdjusted = "./files/recos_txt_adjusted/";
    public static final String pathRecoTagged = "./files/recos_txt_adjusted/";

    public static void main(String [] args){

        try {
            TextDocument td1 = convert(new File("./files/depression_adulte_recommandations_version_mel expertisé.pdf"));
            td1 = InitialFilter.filter(td1);
            td1.writeFile();
            RNNTagger tagger = new RNNTagger();
            ArrayList<TaggedSentence> list = tagger.tag(td1);


            /*
            TextDocument td2 = convert(new File("./files/fiche_memo_hta__mel.pdf"));
            td2 = InitialFilter.filter(td2);
            td2.writeFile();
            */
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showHelp(){
        System.out.println("Show Help - Main");
        System.out.println();
        System.out.println("No help has been written yet, as no command line functionnality has been written");
    }
}
