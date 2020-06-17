package com.nightonke.jellytogglebutton.ColorChangeTypes;


public enum ColorChangeType {

    RGB(0),
    HSV(1);

    int v;

    ColorChangeType(int v) {
        this.v = v;
    }
}
