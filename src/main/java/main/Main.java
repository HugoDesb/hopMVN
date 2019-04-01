package main;

import pretreatement.InitialFilter;
import document.TextDocument;

import java.io.*;

import static pretreatement.PdfToText.convert;

public class Main {

    public static final String pathReco = "./files/recos_txt/";
    public static final String pathRecoAdjusted = "./files/recos_txt_adjusted/";
    public static final String pathRecoTagged = "./files/recos_txt_adjusted/";

    public static void main(String [] args){

        try {
            TextDocument td1 = convert(new File("./files/10irp04_reco_diabete_type_2.pdf"));
            td1 = InitialFilter.filter(td1);
            td1.writeFile();

            TextDocument td2 = convert(new File("./files/fiche_memo_hta__mel.pdf"));
            td2 = InitialFilter.filter(td2);
            td2.writeFile();
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
