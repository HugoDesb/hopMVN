package pretreatement.Extractor;

import common.document.TextDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import pretreatement.Extractor.HAS.SummaryHAS;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static pretreatement.Extractor.PdfToText.getTargetTXTPath;
import static pretreatement.Extractor.PdfToText.withPDFBox;

public class ExtractorHAS {



    public static TextDocument extract(File file) {
        try {
            return (TextDocument) withPDFBox(file, getTargetTXTPath(file));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Extract the Summary of an HAS reco file
     * @param pdDoc
     * @return
     * @throws IOException
     */
    public   static SummaryHAS readSummary(PDDocument pdDoc) throws IOException {
        PDPage page = pdDoc.getPage(2);
        PDDocument doc = new PDDocument();
        doc.addPage(page);
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(doc);
        return new SummaryHAS(text);
    }

    public static String getTitle(PDDocument doc) throws IOException {
        PDFTextStripper stripper = new PDFTextStripper();
        stripper.setStartPage(1);
        stripper.setEndPage(1);
        String text = stripper.getText(doc);
        String [] hip = text.split("\\n");
        for (int i = 0; i<hip.length; i++) {
            if(hip[i].contains("RECOMMANDATION DE BONNE PRATIQUE")){
                int j = i+1;
                boolean started = false;
                String title = "";
                while(!hip[j].trim().isEmpty() || !started){
                    if(!hip[j].trim().isEmpty()){
                        started = true;
                        title += hip[j]+" ";
                    }
                    j++;
                }
                return title.trim();
            }
        }
        return "";
    }

    public static Map<String, String> getAbbrevs(PDDocument doc, int page) throws IOException {
        Map<String, String> hop = new HashMap<>();

        PDFTextStripper stripper = new PDFTextStripper();
        stripper.setStartPage(page+1);
        stripper.setEndPage(page+1);
        String text = stripper.getText(doc);

        boolean start = false;
        for (String line: text.split("\\n")) {
            if(start && !line.isEmpty()){
                String [] elements = line.split(" ");
                String value = elements[1];
                for (int i = 2; i < elements.length; i++) {
                    value += " "+elements[i];
                }
                hop.put(elements[0], value );
            }
            if(line.contains("Libellé") && !start){
                start = true;
            }
        }

        return hop;
    }

    protected TextDocument.Builder readContent(PDDocument doc) throws IOException {
        //choose relevant pages
        SummaryHAS summary = readSummary(doc);
        PDDocument contentDocument = new PDDocument();
        int [] boundaries = summary.getContentBoundaries();
        for(int i = boundaries[0]; i<boundaries[1]; i++){
            contentDocument.addPage(doc.getPage(i));
        }

        PDFTextStripper textStripper = new PDFTextStripper();
        String contentText = textStripper.getText(contentDocument);

        return extractContent(contentText);
    }

    private static TextDocument.Builder extractContent(String contentText) {
        String [] textLines = contentText.split("\n");

        TextDocument.Builder builder = new TextDocument.Builder();

        String toAdd = "";

        for (String line : textLines) {
            String [] sentences = line.split("\\.\\s");
            for (String sentence: sentences) {
                //ligne non vide
                if(!sentence.equals("")){
                    // First character is a UPPERCASE
                    if(sentence.matches("^[ABCDEFGHIJKLMNOPQRSTUVWXYZÉÈÊÔŒÎÏËÇÆÂÀÙŸ].*")){
                        // We can consider it's a new sentence
                        //  add previous line
                        // TODO : be a Sentence : builder.addLine(toAdd + ".");
                        //store new line
                        toAdd = sentence;
                        // first character is NOT an UPPERCASE
                    } else {
                        toAdd += sentence;
                    }
                }
            }
        }
        return builder;
    }

}
