package com.paperreview.paperreview.controllers;
import com.paperreview.paperreview.interfaces.ControlledScreen;


public class HomeControl implements ControlledScreen {

    private MainControl mainControl;

    @Override
    public void setMainController(MainControl mainControl) {
        this.mainControl = mainControl;
    }

    public void initialize() {
    }
}
