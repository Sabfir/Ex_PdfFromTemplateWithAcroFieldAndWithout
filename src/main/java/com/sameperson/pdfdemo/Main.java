package com.sameperson.pdfdemo;

import com.sameperson.pdfdemo.util.PDFGenerator;

public class Main {
    private static final String BARCODE_VALUE = "0100100000012";
    private static final String FIRST_NAME_VALUE = "Mike";
    private static final String LAST_NAME_VALUE = "Tyson";

    public static void main(String[] args) {

        PDFGenerator generator = new PDFGenerator();
        generator.createFromTemplateWithFields(BARCODE_VALUE, FIRST_NAME_VALUE, LAST_NAME_VALUE);
        generator.createFromTemplateWithCoordinates(BARCODE_VALUE+1, FIRST_NAME_VALUE, LAST_NAME_VALUE);
        generator.create(BARCODE_VALUE+2, FIRST_NAME_VALUE, LAST_NAME_VALUE);
    }
}
