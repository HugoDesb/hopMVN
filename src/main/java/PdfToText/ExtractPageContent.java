package PdfToText;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class ExtractPageContent {

    /** The original PDF that will be parsed. */
    public static final String PREFACE = "files/TF_ANX.pdf";
    /** The resulting text file. */
    public static final String RESULT = "files/TF_ANX3.txt";

    /**
     * Parses a PDF to a plain text file.
     * @param pdf the original PDF
     * @param txt the resulting text
     * @throws IOException
     */
    public void parsePdf(String pdf, String txt) throws IOException {
        PdfReader reader = new PdfReader(pdf);
        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
        PrintWriter out = new PrintWriter(new FileOutputStream(txt));
        TextExtractionStrategy strategy;
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
            out.println(strategy.getResultantText());
        }
        reader.close();
        out.flush();
        out.close();
    }

    public void extractsPdfLines(String PdfFile, String txtFile) throws IOException {
        try {
            StringBuffer buff = new StringBuffer();
            String ExtractedText = null;
            PdfReader reader = new PdfReader(PdfFile);
            PdfReaderContentParser parser = new PdfReaderContentParser(reader);
            PrintWriter out = new PrintWriter(new FileOutputStream(txtFile));
            TextExtractionStrategy strategy;

            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
                ExtractedText = strategy.getResultantText().toString();
                out.println(ExtractedText);
            }

            String[] LinesArray;
            LinesArray = buff.toString().split("\n");
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Main method.
     * @param    args    no arguments needed
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        //new ExtractPageContent().parsePdf(PREFACE, RESULT);
        new ExtractPageContent().extractsPdfLines(PREFACE, RESULT);
    }
}
