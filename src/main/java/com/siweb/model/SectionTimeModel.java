package com.siweb.model;
import org.json.JSONObject;


public class SectionTimeModel extends ObservableModel<SectionTime> {

    // Declares variables
    private static final SectionTimeModel instance = new SectionTimeModel();

    // Returns the instance of the controller
    public static SectionTimeModel getInstance(){
        return instance;
    }

    private SectionTimeModel(){}

    public SectionTime add(JSONObject jsonSection, Boolean isAddToObservableList) {
        SectionTime sectionTime = new SectionTime(jsonSection);
        modelsMap.put(sectionTime.getId(), sectionTime);
        if(isAddToObservableList)
            obsList.add(sectionTime);
        return sectionTime;
    }


}

