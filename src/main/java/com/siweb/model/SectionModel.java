package com.siweb.model;
import org.json.JSONObject;


public class SectionModel extends ObservableModel<Section> {

    // Declares variables
    private static final SectionModel instance = new SectionModel();

    // Returns the instance of the controller
    public static SectionModel getInstance(){
        return instance;
    }

    private SectionModel(){}

    public Section add(JSONObject jsonSection, Boolean isAddToObservableList) {
        Section section = new Section(jsonSection);
        modelsMap.put(section.getId(), section);
        if(isAddToObservableList)
            obsList.add(section);
        return section;
    }


}

