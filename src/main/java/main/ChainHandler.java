package main;

import MWExtraction.Main.Principal;
import common.document.TextDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import pretreatement.Extractor.PdfToSentences;
import semantic.MainSemantic;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to have here the whole chain processes
 */
public class ChainHandler {

    public static void fullChainStandard(File pdfFile){
        if(!pdfFile.exists()){
            throw new IllegalArgumentException("The file given doesn't exists");
        }

        // MainSemantic 1 --- PDF TO SENTENCES
        //TODO : extractor instance with common.config file or common.config Object (better) and whether it's an expert extraction
        //TextDocument textDocument = PdfToSentences.extract(pdfFile.getPath(), false);

        //textDocument.writeFile();

        // MainSemantic 2 --- TAG THE SENTENCES
        //RNNTagger tagger = new RNNTagger();
        //tagger.tag(textDocument);

        // MainSemantic 3 --- Extract Multi-words expressions
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

            // MainSemantic 4 --- Branch 1 --- Compute C-value for all MWE -- branch 1
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


    }

    public static List<Map<String, String>> processXml(File xmlFile){
        List<Map<String, String>> hop = new ArrayList<>();

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        Document doc;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            Node base = doc.getElementsByTagName("task").item(0);
            String config_file = base.getAttributes().getNamedItem("config_file").getNodeName();
            NodeList nList = doc.getElementsByTagName("file");
            for (int i = 0; i<nList.getLength(); i++) {
                Map<String, String> hi = new HashMap<>();
                String absolutePath = nList.item(i).getAttributes().getNamedItem("absolutePath").getNodeValue();
                hi.put("absolutePath", absolutePath);
                String name = nList.item(i).getAttributes().getNamedItem("name").getNodeValue();
                hi.put("name", name);
                String topic = nList.item(i).getAttributes().getNamedItem("topic").getNodeValue();
                hi.put("topic", topic);
                String language = nList.item(i).getAttributes().getNamedItem("language").getNodeValue();
                hi.put("language", language);
                String type = nList.item(i).getAttributes().getNamedItem("type").getNodeValue();
                hi.put("type", type);
                String isExpertFile = nList.item(i).getAttributes().getNamedItem("isExpertFile").getNodeValue();
                hi.put("isExpertFile", isExpertFile);
                hi.put("config_file", config_file);
                hop.add(hi);
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return hop;
    }

    /**
     * Sequentially execute a file specified in the xml file
     * @param xmlFile
     * @param doPretreatment
     * @param doMWE
     * @param doSemantics
     */
    public static void treatAllDeclaredFiles(File xmlFile, boolean doPretreatment, boolean doMWE, boolean doSemantics) {

        List<Map<String, String>> xml = processXml(xmlFile);

        for (Map<String, String> set: xml) {

            if(doPretreatment){
                TextDocument td = pretreatmentModule(set, true);
            }

            if(doMWE){
                multiWordExtractor(set);
            }

            if(doSemantics){
                launchOpenSesame(set);
            }
        }
    }

    private static void launchOpenSesame(Map<String, String> set) {
        MainSemantic main = new MainSemantic(set);
        main.run(false, true);
    }


    /**
     * Apply the pretreatment module for the specified pdf file in set
     *
     * @param set
     * @param writeInTemporaryFile write in a text file the selected sentences
     * @return The list of sentences
     */
    public static TextDocument pretreatmentModule(Map<String, String> set, boolean writeInTemporaryFile){
        TextDocument textDocument = null;
        try {
            textDocument = PdfToSentences.extract(set.get("config_file"), set.get("absolutePath"), set.get("name"), set.get("type"), set.get("isExpertFile").equals("true"), set.get("language"));
            if(writeInTemporaryFile){
                textDocument.writeFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return textDocument;
    }

    /**
     *
     * @param set
     */
    public static void multiWordExtractor(Map<String, String> set){
        // find mwe in all files
        Principal.main(set.get("config_file"), set.get("name"), set.get("language"));

        //Combine output -- generate output
        Principal.combine(set.get("config_file"), set.get("name"));
    }

}
