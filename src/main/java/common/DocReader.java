package common;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 *
 */
public class DocReader {

	private Filter filter = null;
	private TextDocument text;

	/**
	 * Constructeur par défaut
	 */
	public DocReader(TextDocument text) throws IOException {
		this.text = text;
	}

	/**
	 * Constructeur avec filtre
	 * @param filter
	 */
	public DocReader(TextDocument text, Filter filter) {
		this.text = text;
		this.filter = filter;
	}



	public static void readDocFile(String fileName) {

		try {
			File file = new File(fileName);
			FileInputStream fis = new FileInputStream(file.getAbsolutePath());

			HWPFDocument doc = new HWPFDocument(fis);

			WordExtractor we = new WordExtractor(doc);

			String[] paragraphs = we.getParagraphText();
			
			System.out.println("Total no of paragraph "+paragraphs.length);

			for (String para : paragraphs) {
				if (para.matches("\\A[A-Z]")){
					System.out.println(para);
				}
				//para = para.replaceAll("\n", " ");
				//para = para.replaceAll("\u000B", "");
				//para = para.replaceAll("\u0007", "");
				//para = para.replaceAll("\u000E","");

				//if(!para.matches("\\s")){
				//	System.out.println("start--"+para+"--end");
				//}
			}
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void readDocxFile(String fileName) {

		try {
			File file = new File(fileName);
			FileInputStream fis = new FileInputStream(file.getAbsolutePath());

			XWPFDocument document = new XWPFDocument(fis);

			List<XWPFParagraph> paragraphs = document.getParagraphs();
			
			System.out.println("Total no of paragraph "+paragraphs.size());
			for (XWPFParagraph para : paragraphs) {
				System.out.println(para.getText());
			}
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		readDocFile("./files/RecoDoc/10irp04_reco_diabete_type_2.doc");

	}
}
