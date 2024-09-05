package itext.mike;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.parser.FilteredTextRenderListener;
import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.RegionTextRenderFilter;
import com.itextpdf.text.pdf.parser.RenderFilter;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import com.itextpdf.text.pdf.pdfcleanup.PdfCleanUpLocation;
import com.itextpdf.text.pdf.pdfcleanup.PdfCleanUpProcessor;

// https://www.javadoc.io/static/com.itextpdf/itextpdf/5.5.9/index.html?overview-summary.html
public class ChangeTextV2 {
	private static final String SRC = "sample-pdf/input/mike-pdf-ori.pdf";
    private static final String DEST = "sample-pdf/output/mike-pdf-edited-v2.pdf";
	private static final Rectangle EXTRACT_TEXT_RECTANGLE = new Rectangle(50f,550f,500f,445f);
	private static final Rectangle STAMPER_RECTANGLE = new Rectangle(50f,550f,500f,445f);

	/* 
	https://www.javadoc.io/static/com.itextpdf/itextpdf/5.5.9/index.html?overview-summary.html
	COURIER 
	HELVETICA 
	SYMBOL 
	TIMES_ROMAN 
	UNDEFINED 
	ZAPFDINGBATS 

	static int	BOLD
	static int	BOLDITALIC
	static int	DEFAULTSIZE
	static int	ITALIC
	static int	NORMAL
	static int	STRIKETHRU
	static int	UNDEFINED
	static int	UNDERLINE
	*/ 
	private static final Font FONT_NORMAL = new Font(FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.BLACK);

	public static void main(String[] args) throws IOException, DocumentException 
	{
		
		RenderFilter[] filter = {new RegionTextRenderFilter(EXTRACT_TEXT_RECTANGLE)};
		TextExtractionStrategy strategy;
		StringBuilder sb = new StringBuilder();
		PdfReader reader = new PdfReader(SRC);
		
		for (int i = 1; i <= reader.getNumberOfPages(); i++) {
		    strategy = new FilteredTextRenderListener(new LocationTextExtractionStrategy(), filter);
		    sb.append(PdfTextExtractor.getTextFromPage(reader, i, strategy));
		}
		
		System.out.println(sb.toString());
		
		processPDF(SRC,DEST);
		
	}
	
	public static void processPDF(String src, String dest) throws IOException, DocumentException 
	{
		
		PdfReader reader = new PdfReader(src);
	    PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
	    List<PdfCleanUpLocation> cleanUpLocations = new ArrayList<PdfCleanUpLocation>();
	    cleanUpLocations.add(new PdfCleanUpLocation(1, STAMPER_RECTANGLE, BaseColor.WHITE));
	    
	    PdfContentByte cb = stamper.getOverContent(1);
		ColumnText ct = new ColumnText(cb);
		ct.setSimpleColumn(140f, 490f, 500f, 445f);
		Paragraph pz = new Paragraph(new Phrase(0,"HERE IS THE DYNAMIC TEXT", FONT_NORMAL));
		ct.addElement(pz);
		ct.go();
	    
	    PdfCleanUpProcessor cleaner = new PdfCleanUpProcessor(cleanUpLocations, stamper);
	    cleaner.cleanUp();
	    stamper.close();
	    reader.close();
	    
	}

}
