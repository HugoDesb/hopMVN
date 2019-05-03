package pretreatement.Extractor;

import document.SummaryHAS;
import document.TextDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

import static pretreatement.Extractor.PdfToText.getTargetTXTPath;
import static pretreatement.Extractor.PdfToText.withPDFBox;

public class ExtractorHAS extends ExtractorPDF {

    public TextDocument extract(File file) {
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
    private static SummaryHAS readSummary(PDDocument pdDoc) throws IOException {
        PDPage page = pdDoc.getPage(2);
        PDDocument doc = new PDDocument();
        doc.addPage(page);
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(doc);
        return new SummaryHAS(text);
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
