package main;

import java.io.File;

public class Main {

    public static void main(String [] args){


        /*
        TextDocument td1 = convert(new File("./files/depression_adulte_recommandations_version_mel expertisé.pdf"));
        td1 = InitialFilter.filter(td1);
        td1.writeFile();
        RNNTagger tagger = new RNNTagger();
        ArrayList<TaggedSentence> list = tagger.tag(td1);
        */

        //MainFrame mf = new MainFrame();


        File fileXML = new File("./files/files_to_treat.xml");
        ChainHandler.treatAllDeclaredFiles(fileXML, false, false, true);



        /*
        Levenshtein l = new Levenshtein();
        double distance = 0.0;
        int countZero = 0;
        for (String lineExpert : sentencesExpert) {
            System.out.println("======================================================");
            System.out.println("--> " + lineExpert);
            for (String lineExtracted : sentencesExtracted) {
                distance = l.distance(lineExpert, lineExtracted);
                if(distance < 50){
                    System.out.println(lineExtracted);
                    System.out.println("Levhenstein Distance : "+distance);
                    System.out.println("------------------------------------");
                    if(distance == 0) countZero++;
                }
            }
        }
        double precision = (double)countZero/sentencesExtracted.size();
        double rappel = (double)countZero/sentencesExpert.size();
        System.out.println("Total expert : "+ sentencesExpert.size());
        System.out.println("Total extract : "+ sentencesExtracted.size());
        System.out.println("Total right : "+ countZero);
        System.out.println("Précision : "+precision);
        System.out.println("Rappel : " + rappel);
        */

        /*
        TextDocument td2 = convert(new File("./files/fiche_memo_hta__mel.pdf"));
        td2 = InitialFilter.filter(td2);
        td2.writeFile();
        */

    }

    public static void showHelp(){
        System.out.println("Show Help - MainSemantic");
        System.out.println();
        System.out.println("No help has been written yet, as no command line functionnality has been written");
    }
}
