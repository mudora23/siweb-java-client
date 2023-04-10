package com.siweb.model;
import org.json.JSONObject;


public class EnrollmentModel extends ObservableModel<Enrollment> {

    // Declares variables
    private static final EnrollmentModel instance = new EnrollmentModel();

    // Returns the instance of the controller
    public static EnrollmentModel getInstance(){
        return instance;
    }

    private EnrollmentModel(){}

    public Enrollment add(JSONObject jsonEnrollment, Boolean isAddToObservableList) {
        Enrollment enrollment = new Enrollment(jsonEnrollment);
        modelsMap.put(enrollment.getId(), enrollment);
        if(isAddToObservableList)
            obsList.add(enrollment);
        return enrollment;
    }


}

