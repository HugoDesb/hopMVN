package pretreatement.Extractor.Extractors;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public abstract class Extractor {

    public File f;

    public Extractor(File f){
        this.f = f;
    }

    public String getSummaryText(int pageNumber) throws IOException {
        PDDocument pdDoc = PDDocument.load(this.f);
        PDPage page = pdDoc.getPage(pageNumber);
        PDDocument doc = new PDDocument();
        doc.addPage(page);
        PDFTextStripper stripper = new PDFTextStripper();
        return stripper.getText(doc);
    }


    public abstract ArrayList<String> extract();
}
