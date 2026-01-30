package model;

import view.ViewMain;

public class ModelMain {

    public void printTest() {

        final ViewMain test = new ViewMain();

        System.out.println("awesome " + test.testInt(12));
    }
}
