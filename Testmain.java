import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.RectangleReadOnly;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

public class Testmain {

	public static void main(String[] args) throws IOException, DocumentException {

		String PDF = "/path/to/input.pdf";
		Testmain main = new Testmain();
		String filename = main.createTwoUp(PDF);
		System.out.println("Output is [" + filename + "]");
	}

	private String createTwoUp(String originalPdfFile) throws IOException, DocumentException {
		String newFilename = "/path/to/input_2up.pdf";
		PdfReader reader = new PdfReader(originalPdfFile);
		Document doc = new Document(new RectangleReadOnly(842f, 595f), 0, 0, 0, 0);
		PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(newFilename));
		doc.open();
		int totalPages = reader.getNumberOfPages();
		for (int i = 1; i <= totalPages; i = i + 2) {
			doc.newPage();
			PdfContentByte cb = writer.getDirectContent();
			PdfImportedPage page = writer.getImportedPage(reader, i); // page #1

			float documentWidth = doc.getPageSize().getWidth() / 2;
			float documentHeight = doc.getPageSize().getHeight();
			if (i > 1) {
				documentHeight = documentHeight - 65f;
			}

			float pageWidth = page.getWidth();
			float pageHeight = page.getHeight();

			float widthScale = documentWidth / pageWidth;
			float heightScale = documentHeight / pageHeight;
			float scale = Math.min(widthScale, heightScale);

			// float offsetX = 50f;
			float offsetX = (documentWidth - (pageWidth * scale)) / 2;
			float offsetY = 0f;

			cb.addTemplate(page, scale, 0, 0, scale, offsetX, offsetY);

			if (i + 1 <= totalPages) {

				PdfImportedPage page2 = writer.getImportedPage(reader, i + 1); // page #2

				pageWidth = page.getWidth();
				pageHeight = page.getHeight();

				widthScale = documentWidth / pageWidth;
				heightScale = documentHeight / pageHeight;
				scale = Math.min(widthScale, heightScale);

				offsetX = ((documentWidth - (pageWidth * scale)) / 2) + documentWidth;

				cb.addTemplate(page2, scale, 0, 0, scale, offsetX, offsetY);
			}
		}

		doc.close();
		return newFilename;
	}
}
