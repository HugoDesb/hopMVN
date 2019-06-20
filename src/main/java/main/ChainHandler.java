package main;

import MWExtraction.Main.Principal;
import common.document.TextDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import pretreatement.Extractor.PdfToSentences;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * Class to have here the whole chain processes
 */
public class ChainHandler {

    public static void fullChainStandard(File pdfFile){
        if(!pdfFile.exists()){
            throw new IllegalArgumentException("The file given doesn't exists");
        }

        // Main 1 --- PDF TO SENTENCES
        //TODO : extractor instance with common.config file or common.config Object (better) and whether it's an expert extraction
        //TextDocument textDocument = PdfToSentences.extract(pdfFile.getPath(), false);

        //textDocument.writeFile();

        // Main 2 --- TAG THE SENTENCES
        //RNNTagger tagger = new RNNTagger();
        //tagger.tag(textDocument);

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


    }

    public static void treatAllDeclaredFiles(File xmlConfFile) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        Document doc;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(xmlConfFile);
            doc.getDocumentElement().normalize();
            Node base = doc.getElementsByTagName("task").item(0);

            String config_file = base.getAttributes().getNamedItem("config_file").getNodeName();

            NodeList nList = doc.getElementsByTagName("file");
            for (int i = 0; i<nList.getLength(); i++) {

                String file_path = nList.item(i).getAttributes().getNamedItem("absolutePath").getNodeValue();
                String name = nList.item(i).getAttributes().getNamedItem("name").getNodeValue();
                String topic = nList.item(i).getAttributes().getNamedItem("topic").getNodeValue();
                String language = nList.item(i).getAttributes().getNamedItem("language").getNodeValue();
                String type = nList.item(i).getAttributes().getNamedItem("type").getNodeValue();
                boolean isExpertFile = Boolean.parseBoolean(nList.item(i).getAttributes().getNamedItem("isExpertFile").getNodeValue());

                TextDocument td = pretreatmentModule(config_file, file_path,name, type, isExpertFile, true);
                multiWordExtractor(config_file, td, language);

            }


        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Apply the pretreatment module for the specified pdf file
     * @param file_path the path to the pdf file
     * @param isExpertFile if the given file has to be treated as highlighted (by an expert)
     * @param writeInTemporaryFile write in a text file the selected sentences
     * @return The list of sentences
     */
    public static TextDocument pretreatmentModule(String config_file, String file_path, String name, String type, boolean isExpertFile, boolean writeInTemporaryFile){
        TextDocument textDocument = null;
        try {
            textDocument = PdfToSentences.extract(config_file, file_path, name, type, isExpertFile);
            if(writeInTemporaryFile){
                textDocument.writeFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return textDocument;
    }

    public static void multiWordExtractor(String config_file, TextDocument textDocument, String language){
        Principal.main(config_file, textDocument.getFile(), language);
    }

}
