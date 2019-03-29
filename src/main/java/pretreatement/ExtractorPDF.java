package pretreatement;

import document.TextDocument;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class ExtractorPDF {

    public TextDocument extract(File file) {
        return null;
    }

    protected TextDocument.Builder readContent(PDDocument pdDocument) throws IOException {
        return null;
    }

    /**
     * Create a file
     * @return
     */
    protected File getTargetTXTPath(File file){
        String filename = file.getName().split("\\.")[0];
        String p = Paths.get(file.getPath()).getParent().toString()+"/"+filename+".txt";
        return new File(p);
    }

    /**
     * Write the pdf in a text file
     * @param pdf
     * @param txt
     * @return the txt file
     * @throws IOException
     */
    protected TextDocument withPDFBox(File pdf, File txt) throws IOException {
        PDFParser parser = new PDFParser(new RandomAccessFile(pdf, "r"));
        parser.parse();
        COSDocument cosDoc = parser.getDocument();
        PDDocument pdDoc = new PDDocument(cosDoc);

        TextDocument.Builder builder = readContent(pdDoc);
        builder.setFile(txt);

        return builder.build();
    }

}
