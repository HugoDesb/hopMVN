package pretreatement.Extractor;

import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import pretreatement.Extractor.Extractors.Extractor;
import pretreatement.Extractor.Extractors.ExtractorHAS;
import pretreatement.Extractor.Extractors.ExtractorNICE;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExtractorPDF {

    /**
     * Extract text from pdf File
     * @param file the source file
     * @param extractHighlight extract only highlighted text ?
     * @return an array of text blocks
     */
    public static ArrayList<String> extract(File file, boolean extractHighlight, String type) {
        Extractor extractor;

        if(extractHighlight){
            return extractHighlight(file);
        }else{
            switch(type){
                case "HAS":
                    extractor = new ExtractorHAS(file);
                    return extractor.extract();
                case "NICE":
                    extractor = new ExtractorNICE(file);
                    return extractor.extract();
                default:
                    return extractStandard(file);
            }
        }
    }

    /**
     * Extract text from pdf file (standard)
     * @param file the source file
     * @return an array of text blocks
     */
    private static ArrayList<String> extractStandard(File file) {
        ArrayList<String> hop = new ArrayList<>();
        PDFParser parser;
        try {
            parser = new PDFParser(new RandomAccessFile(file, "r"));
            parser.parse();
            PDDocument contentDocument = parser.getPDDocument();

            PDFTextStripper textStripper = new PDFTextStripper();
            String contentText = textStripper.getText(contentDocument);
            hop.add(contentText);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return hop;
    }

    /**
     * Extract hilighted text blocks from pdf file
     * @param file the source file
     * @return an array of text blocks
     */
    private static ArrayList<String> extractHighlight(File file) {
        ArrayList<String> hop = new ArrayList<>();

        try {
            PDDocument pddDocument = PDDocument.load(file);
            for (PDPage page : pddDocument.getDocumentCatalog().getPages()) {
                List<PDAnnotation> la = page.getAnnotations();

                // If no annotations in this page
                if (la.size() == 0) {
                    continue;
                }
                for (PDAnnotation pdfAnnot : la) {
                    if(pdfAnnot.getSubtype().equals("Highlight")) {

                        PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                        stripper.setSortByPosition(true);

                        PDRectangle rect = pdfAnnot.getRectangle();
                        float x = rect.getLowerLeftX() - 1;
                        float y = rect.getUpperRightY() - 1;
                        float width = rect.getWidth() + 2;
                        float height = rect.getHeight() + rect.getHeight() / 4;
                        int rotation = page.getRotation();
                        if (rotation == 0) {
                            PDRectangle pageSize = page.getMediaBox();
                            y = pageSize.getHeight() - y;
                        }

                        Rectangle2D.Float awtRect = new Rectangle2D.Float(x, y, width, height);
                        stripper.addRegion(Integer.toString(0), awtRect);
                        stripper.extractRegions(page);
                        hop.add(stripper.getTextForRegion(Integer.toString(0)));
                    }
                }
            }
            pddDocument.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return hop;
    }
}
