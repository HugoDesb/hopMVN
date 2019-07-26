package pretreatement.Extractor.Extractors;

import common.document.TextDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import pretreatement.Extractor.Summaries.SummaryNICE;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ExtractorNICE extends Extractor {

    public ExtractorNICE(File f) {
        super(f);
    }

    /*
    public static TextDocument extract(File file) {
        try {
            return (TextDocument) withPDFBox(file, getTargetTXTPath(file));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    */

    public ArrayList<String> extract() {
        ArrayList<String> hop = new ArrayList<>();
        PDFParser parser;
        try {
            /**
             * Get text from appropriate document range (without annexes and summary)
             * Also reads abbrev to replace them in the text
             */
            parser = new PDFParser(new RandomAccessFile(super.f, "r"));
            parser.parse();
            PDDocument contentDocument = PDDocument.load(super.f);
            SummaryNICE summary = readSummary(contentDocument);
            PDFTextStripper textStripper = new PDFTextStripper();
            textStripper.setStartPage(summary.getContentBoundaries()[0]);
            textStripper.setEndPage(summary.getContentBoundaries()[1]);

            //Strip text
            String text = textStripper.getText(contentDocument);

            text = text.replaceAll("\\[(([Nn]ew\\s[0-9]*)|([0-9]*))\\]", " ");
            text = text.replaceAll("([0-9]+\\.)+[0-9]+", "\n");
            text = text.replaceAll("(\\n.*©.*\\n)", "\n");
            text = text.replaceAll("(Page\\s[0-9]+\\sof\\s[0-9]+)", " ");
            text = text.replaceAll("[0-9]\\s([A-Z])", "$0");


            String [] lineByLine = text.split("\\n");

            hop = new ArrayList<>(Arrays.asList(lineByLine));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hop;
    }

    /**
     * Extract the Summary of an NICE reco file
     * @param pdDoc
     * @return
     * @throws IOException
     */
    public SummaryNICE readSummary(PDDocument pdDoc) throws IOException {
        String text = getSummaryText(2);
        return new SummaryNICE(text);
    }

    protected TextDocument.Builder readContent(PDDocument doc) throws IOException {
        //choose relevant pages
        SummaryNICE summary = readSummary(doc);
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
