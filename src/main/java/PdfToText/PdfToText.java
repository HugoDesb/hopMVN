package PdfToText;


import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import common.TextDocument;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
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
        return new TextDocument(withPDFBox(file, getTargetTXTPath(file)));
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
     * Write the pdf in a text file
     * @param pdf
     * @param txt
     * @return the txt file
     * @throws IOException
     */
    private static File withPDFBox(File pdf, File txt) throws IOException {
        String parsedText;
        PDFParser parser = new PDFParser(new RandomAccessFile(pdf, "r"));
        parser.parse();
        COSDocument cosDoc = parser.getDocument();
        PDFTextStripper pdfStripper = new PDFTextStripper();
        PDDocument pdDoc = new PDDocument(cosDoc);


        readSummary(pdDoc);

        pdfStripper.setPageStart("Page Debut\n");
        pdfStripper.setPageEnd("Page End\n");
        parsedText = pdfStripper.getText(pdDoc);
        System.out.println(parsedText);
        PrintWriter pw = new PrintWriter(txt);
        pw.print(parsedText);
        pw.close();
        return txt;
    }

    private static SummaryHAS readSummary(PDDocument pdDoc) throws IOException {
        PDPage page = pdDoc.getPage(2);
        PDDocument doc = new PDDocument();
        doc.addPage(page);
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(doc);
        return new SummaryHAS(text);
    }

    public static void main(String [] args) throws IOException {
        convert(new File("./files/10irp04_reco_diabete_type_2.pdf"));
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