package pretreatement.Extractor;


import document.SummaryHAS;
import document.TextDocument;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Static class for converting a pdf to a txt file
 */
public class PdfToText {

    /**
     * public access to the converter
     * @return A TextDocument instance
     * @throws IOException
     */
    public static TextDocument convert(File file) throws IOException {
        return withPDFBox(file, getTargetTXTPath(file));
    }

    /**
     * Write the pdf in a text file
     * @param pdf
     * @param txt
     * @return the txt file
     * @throws IOException
     */
    public static TextDocument withPDFBox(File pdf, File txt) throws IOException {
        PDFParser parser = new PDFParser(new RandomAccessFile(pdf, "r"));
        parser.parse();
        COSDocument cosDoc = parser.getDocument();
        PDDocument pdDoc = new PDDocument(cosDoc);

        TextDocument.Builder builder = readContent(pdDoc);
        builder.setFile(txt);

        return builder.build();
    }

    private static SummaryHAS readSummary(PDDocument pdDoc) throws IOException {
        PDPage page = pdDoc.getPage(2);
        PDDocument doc = new PDDocument();
        doc.addPage(page);
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(doc);
        return new SummaryHAS(text);
    }

    private static TextDocument.Builder readContent(PDDocument doc) throws IOException {
        //choose relevant pages
        SummaryHAS summary = readSummary(doc);
        PDDocument contentDocument = new PDDocument();
        int [] boundaries = summary.getContentBoundaries();
        for(int i = boundaries[0]; i<boundaries[1]; i++){
            contentDocument.addPage(doc.getPage(i));
        }

        PDFTextStripper textStripper = new PDFTextStripper();
        String contentText = textStripper.getText(contentDocument);

        return exctractContent(contentText);

    }

    private static TextDocument.Builder exctractContent(String contentText) {
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
                        builder.addLine(toAdd + ".");
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

    /**
     * Create a file
     * @return
     */
    private static File getTargetTXTPath(File file){
        String filename = file.getName().split("\\.")[0];
        String p = Paths.get(file.getPath()).getParent().toString()+"/"+filename+".txt";
        return new File(p);
    }


}