package com.sameperson.pdfdemo.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;

import java.io.File;
import java.io.IOException;

public class PDFGenerator {
    private static final float POINTS_PER_MM = 1 / (10 * 2.54f) * 72;
    private PDFont font;
    private static final String TEMP_FOLDER_PROPERTY = "java.io.tmpdir";
    private static final String FILE_TYPE = ".pdf";
    private static final String PDF_TEMPLATE_WITH_ACRO = "template/demo-template-acro.pdf";
    private static final String PDF_TEMPLATE = "template/demo-template.pdf";
    private static final String BARCODE_KEY = "barcode";
    private static final String FIRST_NAME_KEY = "firstName";
    private static final String LAST_NAME_KEY = "lastName";

    public PDFGenerator() {
        font = PDType1Font.HELVETICA;
    }

    public void createFromTemplateWithFields(String barcode, String firstName, String lastName) {
        try {
            File file = new File(getClass().getClassLoader().getResource(PDF_TEMPLATE_WITH_ACRO).getFile());
            PDDocument template = PDDocument.load(file);
            PDAcroForm acroForm = template.getDocumentCatalog().getAcroForm();
            if (acroForm != null) {
                PDTextField field = (PDTextField) acroForm.getField(BARCODE_KEY);
                field.setValue(barcode);

                field = (PDTextField) acroForm.getField(FIRST_NAME_KEY);
                field.setValue(firstName);

                field = (PDTextField) acroForm.getField(LAST_NAME_KEY);
                field.setValue(lastName);
            }
            String tempDir = System.getProperty(TEMP_FOLDER_PROPERTY);
            String fileOutput = tempDir + File.separator + barcode + FILE_TYPE;
            template.save(fileOutput);
            template.close();
            System.out.println("File from template with acro fields position saved to: " + fileOutput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createFromTemplateWithCoordinates(String barcode, String firstName, String lastName) {
        try {
            File file = new File(getClass().getClassLoader().getResource(PDF_TEMPLATE).getFile());
            PDDocument template = PDDocument.load(file);
            PDPage templatePage = template.getPage(0);
            PDPageContentStream content = new PDPageContentStream(template, templatePage,
                    PDPageContentStream.AppendMode.APPEND, true, true);

            //Barcode
            putText(content, font, 10, 10 * POINTS_PER_MM, 130f * POINTS_PER_MM, barcode + " (Barcode will be here)");
            //First name
            putText(content, font, 14, 50 * POINTS_PER_MM, 115.8f * POINTS_PER_MM, firstName);
            //Last name
            putText(content, font, 14, 50 * POINTS_PER_MM, 106.9f * POINTS_PER_MM, lastName);

            content.close();

            String tempDir = System.getProperty(TEMP_FOLDER_PROPERTY);
            String fileOutput = tempDir + File.separator + barcode + FILE_TYPE;
            template.save(fileOutput);
            template.close();
            System.out.println("File from template with coordinate position saved to: " + fileOutput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void create(String barcode, String firstName, String lastName) {
        try {
            new PDPage(new PDRectangle(297 * POINTS_PER_MM, 210 * POINTS_PER_MM));

            PDDocument document = new PDDocument();
            PDRectangle rectangle = new PDRectangle(210 * POINTS_PER_MM, 148 * POINTS_PER_MM);
            PDPage page = new PDPage(rectangle);

            document.addPage(page);
            PDPageContentStream content = new PDPageContentStream(document, page);

            //Barcode
            putText(content, font, 10, 10 * POINTS_PER_MM, 90 * POINTS_PER_MM, barcode + " (Barcode will be here)");
            //First name
            putText(content, font, 14, 15 * POINTS_PER_MM, 75 * POINTS_PER_MM, "First name: " + firstName);
            //Last name
            putText(content, font, 14, 15 * POINTS_PER_MM, 70 * POINTS_PER_MM, "Last name: " + lastName);

            content.close();

            String tempDir = System.getProperty(TEMP_FOLDER_PROPERTY);
            String fileOutput = tempDir + File.separator + barcode + FILE_TYPE;
            document.save(fileOutput);
            document.close();
            System.out.println("File without template saved to: " + fileOutput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setFont(PDFont font) {
        this.font = font;
    }

    private void putText(PDPageContentStream content, PDFont font, int size, float offsetX, float offsetY, String text) throws IOException {
        content.beginText();
        content.setFont(font, size);
        content.newLineAtOffset(offsetX, offsetY);
        content.showText(text);
        content.endText();
    }

}
