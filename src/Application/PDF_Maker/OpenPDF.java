package Application.PDF_Maker;

import java.awt.Desktop;
import java.io.File;

public class OpenPDF {

    public OpenPDF(PDFFile f) {
        try {
            File pdfFile = PDFFile.getFileName();
            if (pdfFile.exists()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(pdfFile);
                } else {
                    System.out.println("Awt Desktop is not supported!");
                }
            } else {
                System.out.println("File does not exist");
            }
            System.out.println("Done");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
