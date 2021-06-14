package Application.PDF_Maker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import Application.Data.Database;
import Application.Data.DepositInfo;
import Application.Data.Info;
import Application.Data.WithdrawalInfo;
import Application.Utility.Utils;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFFile {

    private int counter_for_files_W = 0; // to count if files already exist and when creating them, that they don't get
    private int counter_for_files_I = 0;
    private String fileName;
    private static String fileNameFull;
    private boolean isWithdraw;
    private Document document;

    public void createWithdrawInfoDeposit(String fileName, String withdrawOrInfo) {
        this.fileName = fileName;
        if (withdrawOrInfo.equalsIgnoreCase("withdraw")) {
            isWithdraw = true;
            fileAlreadyExistsReader();
            try {
                createPDF();

                document.open();
                Chunk c1 = new Chunk(Info.getBank());
                Paragraph p1 = new Paragraph(c1);
                document.add(p1);
                document.add(new Paragraph(" "));
                document.add(new Paragraph(" "));
                document.add(new Paragraph(
                        "---------------------------------------------------------------------------------------------------------------------------------"));
                document.add(new Paragraph(" "));
                Paragraph p2 = new Paragraph();
                p2.add("Bezug " + WithdrawalInfo.getInstance().getCurrency());
                document.add(p2);

                document.add(new Paragraph(" "));
                document.add(new Paragraph(" "));
                document.add(new Paragraph(" "));
                document.add(new Paragraph(" "));

                PdfPTable t1 = new PdfPTable(2);
                for (int aw = 0; aw < 1; aw++) {
                    PdfPCell cell1 = createAddCell("Kartentyp:", t1);
                    PdfPCell cell2 = createAddCell(Info.getCardtype(), t1);
                    PdfPCell cell3 = createAddCell("Kartennummer:", t1);
                    PdfPCell cell4 = createAddCell(Info.getCardNr(), t1);
                    PdfPCell cell5 = createAddCell("IBAN:", t1);
                    PdfPCell cell6 = createAddCell(Info.getIBAN(), t1);
                    PdfPCell cell7 = createAddCell("Bezugsbetrag:", t1);
                    PdfPCell cell8 = createAddCell(Utils.formatMoney(WithdrawalInfo.getInstance().getAmount()) + " "
                            + WithdrawalInfo.getInstance().getCurrency(), t1);
                }
                document.add(t1);
                document.add(new Paragraph(" "));
                document.add(new Paragraph(" "));
                document.add(new Paragraph(
                        "---------------------------------------------------------------------------------------------------------------------------------"));
                document.add(new Paragraph(" "));

                Paragraph p3 = new Paragraph();
                p3.add("Datum: " + Utils.getCurrentTimeStamp("dd.MM.yyyy HH:mm"));
                document.add(p3);
                document.close();

            } catch (Exception e) {
                System.out.println(e);
            }

        } else if (withdrawOrInfo.equalsIgnoreCase("information")) {
            fileAlreadyExistsReader();
            try {
                createPDF();

                document.open();
                Chunk c1 = new Chunk(Info.getBank());
                Paragraph p1 = new Paragraph(c1);
                p1.setSpacingBefore(213);
                document.add(p1);
                document.add(new Paragraph(" "));
                document.add(new Paragraph(" "));
                document.add(new Paragraph(
                        "---------------------------------------------------------------------------------------------------------------------------------"));
                document.add(new Paragraph(" "));
                Paragraph p2 = new Paragraph();
                p2.add("Kontoinformation-Beleg");
                document.add(p2);

                document.add(new Paragraph(" "));
                document.add(new Paragraph(" "));
                document.add(new Paragraph(" "));
                document.add(new Paragraph(" "));

                PdfPTable t1 = new PdfPTable(2);
                for (int aw = 0; aw < 1; aw++) {
                    PdfPCell cell1 = createAddCell("Kartentyp:", t1);
                    PdfPCell cell2 = createAddCell(Info.getCardtype(), t1);
                    PdfPCell cell3 = createAddCell("Kartennummer:", t1);
                    PdfPCell cell4 = createAddCell(Info.getCardNr(), t1);
                    PdfPCell cell5 = createAddCell("IBAN:", t1);
                    PdfPCell cell6 = createAddCell(Info.getIBAN(), t1);
                    PdfPCell cell7 = createAddCell("Kontostand:", t1);
                    PdfPCell cell8 = createAddCell(Utils.formatMoney(Database.getBalance(Info.getAccountID())) + " CHF",
                            t1);
                }
                document.add(t1);
                document.add(new Paragraph(" "));
                document.add(new Paragraph(" "));
                document.add(new Paragraph(
                        "---------------------------------------------------------------------------------------------------------------------------------"));
                document.add(new Paragraph(" "));

                Paragraph p3 = new Paragraph();
                p3.add("Datum: " + Utils.getCurrentTimeStamp("dd.MM.yyyy HH:mm"));
                document.add(p3);
                document.close();

            } catch (Exception e) {
                System.out.println(e);
            }
        } else {

            if (withdrawOrInfo.equalsIgnoreCase("deposit")) {
                isWithdraw = true;
                fileAlreadyExistsReader();
                try {
                    createPDF();

                    document.open();
                    Chunk c1 = new Chunk(Info.getBank());
                    Paragraph p1 = new Paragraph(c1);
                    document.add(p1);
                    document.add(new Paragraph(" "));
                    document.add(new Paragraph(" "));
                    document.add(new Paragraph(
                            "---------------------------------------------------------------------------------------------------------------------------------"));
                    document.add(new Paragraph(" "));
                    Paragraph p2 = new Paragraph();
                    p2.add("Einzahlung " + DepositInfo.getInstance().getCurrency());
                    document.add(p2);

                    document.add(new Paragraph(" "));
                    document.add(new Paragraph(" "));
                    document.add(new Paragraph(" "));
                    document.add(new Paragraph(" "));

                    PdfPTable t1 = new PdfPTable(2);
                    for (int aw = 0; aw < 1; aw++) {

                        PdfPCell cell1 = createAddCell("Kartentyp:", t1);
                        PdfPCell cell2 = createAddCell(Info.getCardtype(), t1);
                        PdfPCell cell3 = createAddCell("Kartennummer:", t1);
                        PdfPCell cell4 = createAddCell(Info.getCardNr(), t1);
                        PdfPCell cell5 = createAddCell("IBAN:", t1);
                        PdfPCell cell6 = createAddCell(Info.getIBAN(), t1);
                        PdfPCell cell7 = createAddCell("Einzahlungsbetrag:", t1);
                        PdfPCell cell8 = createAddCell(Utils.formatMoney(DepositInfo.getInstance().getAmount()) + " "
                                + DepositInfo.getInstance().getCurrency(), t1);
                    }
                    document.add(t1);
                    document.add(new Paragraph(" "));
                    document.add(new Paragraph(" "));
                    document.add(new Paragraph(
                            "---------------------------------------------------------------------------------------------------------------------------------"));
                    document.add(new Paragraph(" "));

                    Paragraph p3 = new Paragraph();
                    p3.add("Datum: " + Utils.getCurrentTimeStamp("dd.MM.yyyy HH:mm"));
                    document.add(p3);
                    document.close();

                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
    }

    private PdfPCell createAddCell(String string, PdfPTable table) {
        PdfPCell cell = new PdfPCell(new Phrase(string));
        cell.setUseVariableBorders(true);
        cell.setBorderColor(BaseColor.WHITE);
        table.addCell(cell);
        return cell;
    }

    private void createPDF() {
        try {
            if (isWithdraw) {
                fileNameFull = fileName + "_" + counter_for_files_W + ".pdf";
            } else {
                fileNameFull = fileName + "_" + counter_for_files_I + ".pdf";
            }
            document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(fileNameFull));
        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        }
    }

    // Trying to find a file, which has already that exact name
    public void fileAlreadyExistsReader() {
        try {
            List<File> filesInFolder;
            filesInFolder = Files.walk(Paths.get(System.getProperty("user.dir"))).filter(Files::isRegularFile)
                    .map(Path::toFile).collect(Collectors.toList());
            for (File iterable_element : filesInFolder) {
                if (iterable_element.getName().contains(fileName)) {
                    if (isWithdraw) {
                        counter_for_files_W = Integer.parseInt(iterable_element.getName().replaceAll("[^0-9]", ""));
                        counter_for_files_W += 1;
                    } else {
                        counter_for_files_I = Integer.parseInt(iterable_element.getName().replaceAll("[^0-9]", ""));
                        counter_for_files_I += 1;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static File getFileName() {
        try {
            List<File> filesInFolder;
            filesInFolder = Files.walk(Paths.get(System.getProperty("user.dir"))).filter(Files::isRegularFile)
                    .map(Path::toFile).collect(Collectors.toList());
            for (File iterable_element : filesInFolder) {
                if (iterable_element.getName().contains(fileNameFull)) {
                    return iterable_element;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
