package itext.mike;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class ListFonts {
	
	public static final String SRC = "sample-pdf/input/mike-pdf-ori.pdf";
    public static final String DEST = "sample-pdf/output/mike-pdf-edited.pdf";
 
    public static void main(String[] args) throws IOException, DocumentException 
    {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        processPDF(SRC, DEST);
    }
	
    public static void processPDF(String src, String dest) throws IOException, DocumentException 
    {
        PdfReader reader = new PdfReader(src);
        PdfDictionary dict = reader.getPageN(1);
        PdfObject object = dict.getDirectObject(PdfName.CONTENTS);
        
        if (object instanceof PRStream) 
        {
            PRStream stream = (PRStream)object;
            byte[] data = PdfReader.getStreamBytes(stream);
            String dd = new String(data);
            dd = dd.replace("BUDI TANAKA", "BUTA HANDOKO");
            stream.setData(dd.getBytes());
        }
        
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        stamper.close();
        reader.close();
    }
    
}
