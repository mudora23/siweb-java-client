package com.siweb.model;

import org.json.JSONObject;

public class Section implements TableViewModel {

    private final int id;
    private final String code;
    private final Course course;
    private final User lecturer;

    public Section(JSONObject jsonSection) {
        this.id = jsonSection.getInt("id");
        this.code = jsonSection.getString("section_code");
        this.course = CourseModel.getInstance().add(jsonSection.getJSONObject("course"), false);
        this.lecturer = UserModel.getInstance().add(jsonSection.getJSONObject("lecturer"), false);
    }

    public int getId() {
        return id;
    }
    public String getCode() {
        return code;
    }
    public Course getCourse() {
        return course;
    }
    public Semester getSemester() { return course.getSemester(); }
    public User getLecturer() {
        return lecturer;
    }

    @Override
    public String toString() {
        return this.course + "-" + this.code;
    }

    public String getValText() {
        return id + "";
    }

}
