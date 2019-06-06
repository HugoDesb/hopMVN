package Main;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import BioTex.Execution;
import BuildListToValidate.BuildFilterManyLists;
import Object.CandidatTerm;
import Wrapper.OutputHandler;
import Wrapper.Preparation;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author juanlossio
 * @author hugodesb
 */
public class Principal {

    /**
     * @param args the command line arguments
     */
    static ArrayList<CandidatTerm> list_candidat_terms_validated = new ArrayList<>();

    public static void main(String[] args) {

        // TODO : Arguments :
        //

        /*
         * Variables to find: the Pattern List, DataSetReference for ValidationMesh, and file where the Tagger Tool is installed
         */

        String source_patterns = "./res/patterns"; // constante
        //TODO : Soon to be moved to config file
        String source_dataset_reference = "./res/dataSetReference"; // constante
        String source_stop_words = "./res/stopWords"; // constante
        String source_tagger = "/home/sesstim/Téléchargements/TreeTagger"; // config
        int typeSource = 2;

        //String [] measures = {"C_value", "L_value"};
        //String [] measures = {"LIDF_value", "F-OCapi_A", "F-OCapi_M", "F-OCapi_S", "F-TFIDF-C_A", "F-TFIDF-C_M", "F-TFIDF-C_S", "TFIDF_A", "TFIDF_M", "TFIDF_S","Okapi_A", "Okapi_M", "Okapi_S"};
        String [] measures = {"F-OCapi_M"};

        for (int i = 0; i < measures.length; i++) {
            /*
             * Variable that saves the extracted terms
             */
            String source_OUTPUT = "OUTPUT__"+measures[i];
            File folder = new File("./res/"+source_OUTPUT+"/");
            if (!folder.exists()){
                if (!folder.mkdirs()) {
                    // Directory creation failed
                    source_OUTPUT = "./res";
                }else{
                    source_OUTPUT = "./res/"+source_OUTPUT;
                }
            }else {
                source_OUTPUT = "./res/"+source_OUTPUT; //Mettre le dossier où vous voulez que les fichiers se sauvegardent
            }

            /*
             * File to be analized for the term extraction
             */
            //String file_to_be_analyzed = "/home/sesstim/IdeaProjects/hopMVN/files/out_test.txt";
            String sourceFile = "/home/sesstim/IdeaProjects/hopMVN/files/10irp04_reco_diabete_type_2.txt";
            String file_to_be_analyzed = Preparation.makeEachLineADocument(sourceFile);



            /*
             * Language : english, french, spanish
             * number_patrons : number of first pattern to take into account
             * typeTerms : all (single word + multi words terms),
             * 			   multi (multi words terms)
             * measure = 15 possible measures
             * tool_Tagger: TreeTagger by default
             */
            String type_of_terms = "all"; // all    multi
            String language = "french"; // english french spanish
            int frequency_min_of_terms = 1; // frequency minimal to extract the terms  (for big corpus is better to use more than 10.

            list_candidat_terms_validated = Execution.main_execution(
                    language, //english french spanish
                    200, // nombre de patrons
                    type_of_terms,
                    measures[i], // For one document       :   L_value     C_value
                    // For a set of documents :   LIDF_value  F-OCapi_A   F-OCapi_M   F-OCapi_S   F-TFIDF-C_A     F-TFIDF-C_M     F-TFIDF-C_S
                    //                            TFIDF_A     TFIDF_M     TFIDF_S     Okapi_A     Okapi_M     Okapi_S
                    typeSource,/* 1 = single file (only for L_value  or C_value)
                     2 = set of files (for LIDF-value **strong recommended** or any other measure)
                */
                    frequency_min_of_terms,
                    file_to_be_analyzed,
                    "TreeTagger",
                    source_patterns,
                    source_dataset_reference,
                    source_tagger,
                    source_OUTPUT
            );

            BuildFilterManyLists.createList(list_candidat_terms_validated,source_stop_words,source_OUTPUT,type_of_terms,language);
            OutputHandler.write(sourceFile, source_OUTPUT+"/t4gram["+measures[i]+"].csv",
                    source_OUTPUT+"/t3gram["+measures[i]+"].csv",
                    source_OUTPUT+"/t2gram["+measures[i]+"].csv",
                    source_OUTPUT+"/t1gram["+measures[i]+"].csv",
                    source_OUTPUT+"/out.txt");
            System.out.println("Fin de l'exécution");
        }
    }

}
