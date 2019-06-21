package MWExtraction.Main;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import MWExtraction.BioTex.Execution;
import MWExtraction.BuildListToValidate.BuildFilterManyLists;
import MWExtraction.Object.CandidatTerm;
import MWExtraction.Wrapper.OutputHandler;
import MWExtraction.Wrapper.Preparation;
import common.config.Config;

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

    public static void main(String config_file, String name, String language) {

        Config config = Config.getInstance(config_file);

        //String [] measures = {"C_value", "L_value"};
        String [] measures = {"F-OCapi_A","TFIDF_A"};
        //String [] measures = {"F-OCapi_A", "F-OCapi_S", "F-OCapi_M", "TFIDF_A", "TFIDF_M", "TFIDF_S"};

        for (int i = 0; i < measures.length; i++) {
            /*
             * Set up target folder and make needed folder if necessary
             */
            String source_OUTPUT = config.getProp("mwe.output_folder")+name+"/"+measures[i]+"/";
            System.out.println("--------------------------------"+source_OUTPUT);
            (new File(source_OUTPUT)).mkdirs();

            // File to be analyzed for the term extraction
            String sourceFile = config.getProp("pretreatment.output_folder")+name+"/"+name+".txt";
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
            int frequency_min_of_terms = 1; // frequency minimal to extract the terms  (for big corpus is better to use more than 10.

            list_candidat_terms_validated = Execution.main_execution(
                    language, //english french
                    200, // nombre de patrons
                    type_of_terms,
                    measures[i], // For one document       :   L_value     C_value
                    // For a set of documents :   LIDF_value  F-OCapi_A   F-OCapi_M   F-OCapi_S   F-TFIDF-C_A     F-TFIDF-C_M     F-TFIDF-C_S
                    //                            TFIDF_A     TFIDF_M     TFIDF_S     Okapi_A     Okapi_M     Okapi_S
                    2,/* 1 = single file (only for L_value  or C_value)
                     2 = set of files (for LIDF-value **strong recommended** or any other measure)
                */
                    frequency_min_of_terms,
                    file_to_be_analyzed,
                    "TreeTagger",
                    config.getProp("mwe.patterns_path"),
                    config.getProp("mwe.dataset_reference_path"),
                    config.getProp("mwe.treetagger_path"),
                    source_OUTPUT
            );

            BuildFilterManyLists.createList(list_candidat_terms_validated,
                    config.getProp("mwe.stop_words_path"),
                    source_OUTPUT,
                    type_of_terms,
                    language);


            OutputHandler handler = new OutputHandler();

            handler.combineMeasures(config.getProp("mwe.output_folder")+name+"/");

            handler.buildOutput(sourceFile,
                    source_OUTPUT+"t4gram.csv",
                    source_OUTPUT+"t3gram.csv",
                    source_OUTPUT+"t2gram.csv",
                    source_OUTPUT+"t1gram.csv");
            handler.write(source_OUTPUT+"/outSentences.txt", source_OUTPUT+"/outBasicRules.txt");
            System.out.println("Fin de l'exécution");
        }
    }

    public static void combine(String config_file, String name) {
        String baseFolder = Config.getInstance(config_file).getProp("mwe.output_folder")+name+"/";


    }

    public static void writeOutput(){

    }
}
