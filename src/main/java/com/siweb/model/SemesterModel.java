package com.siweb.model;
import org.json.JSONObject;



public class SemesterModel extends ObservableModel<Semester> {

    // Declares variables
    private static final SemesterModel instance = new SemesterModel();

    // Returns the instance of the controller
    public static SemesterModel getInstance(){
        return instance;
    }

    private SemesterModel(){}

    public Semester add(JSONObject jsonSemester, Boolean isAddToObservableList) {
        Semester semester = new Semester(jsonSemester);
        modelsMap.put(semester.getId(), semester);
        if(isAddToObservableList)
            obsList.add(semester);
        return semester;
    }


}

