package com.siweb.model;
import org.json.JSONObject;



public class CourseModel extends ObservableModel<Course> {

    // Declares variables
    private static final CourseModel instance = new CourseModel();

    // Returns the instance of the controller
    public static CourseModel getInstance(){
        return instance;
    }

    private CourseModel(){}

    public Course add(JSONObject jsonCourse, boolean isAddToObservableList) {
        Course course = new Course(jsonCourse);
        modelsMap.put(course.getId(), course);
        if(isAddToObservableList)
            obsList.add(course);
        return course;
    }


}

