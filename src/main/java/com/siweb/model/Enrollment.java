package com.siweb.model;

import org.json.JSONObject;

public class Enrollment implements TableViewModel {
    private final int id;
    private final double finalGrade;
    private final User user;
    private final Section section;

    public Enrollment(JSONObject jsonEnrollment) {
        this.id = jsonEnrollment.getInt("id");
        this.finalGrade = jsonEnrollment.getDouble("final_grade");
        this.user = UserModel.getInstance().add(jsonEnrollment.getJSONObject("user"), false);
        this.section = SectionModel.getInstance().add(jsonEnrollment.getJSONObject("section"), false);
    }

    public int getId() {
        return id;
    }
    public double getFinalGrade() {
        return finalGrade;
    }
    public User getUser() {
        return user;
    }
    public Section getSection() {
        return section;
    }

    public String getSectionCode(){
        return  getSection().getCode();
    }

    public String getCourseCode(){
        return  getSection().getCourse().getCode();
    }

    public String getCourseName(){
        return  getSection().getCourse().getName();
    }

    public String getLectureFullName(){
        String firstName =  getSection().getLecturer().getFirstName();
        String lastName =  getSection().getLecturer().getLastName();
        return firstName + " " + lastName;
    }

    public String getStudentFullName(){
        String firstName =  getUser().getFirstName();
        String lastName =  getUser().getLastName();
        return firstName + " " + lastName;
    }

    public String getStudentUserName(){
        return  getUser().getUserName();
    }

    @Override
    public String toString() {
        return this.user + " (" + this.section + ", grade:" + this.finalGrade + ")";
    }

    public String getValText() {
        return id + "";
    }

}
