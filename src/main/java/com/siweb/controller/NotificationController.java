package com.siweb.controller;

import javafx.stage.Popup;
import javafx.stage.PopupWindow;

import java.util.ArrayList;
import java.util.List;


public class NotificationController {

    private static final NotificationController instance = new NotificationController();
    private List<Popup> popupList = new ArrayList<Popup>();
    private NotificationController(){}

    // Returns the instance of the controller
    public static NotificationController getInstance(){
        return instance;
    }

    // WIP, not finished


}
