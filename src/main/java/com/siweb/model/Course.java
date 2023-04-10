package com.siweb.model;

import org.json.JSONObject;

public class Course implements TableViewModel {
    private final int id;
    private final String code;
    private final String name;
    private final double credit;
    private final Semester semester;

    public Course(JSONObject jsonCourse){
        this.id = jsonCourse.getInt("id");
        this.code = jsonCourse.getString("course_code");
        this.name = jsonCourse.getString("course_name");
        this.credit = jsonCourse.getDouble("course_credit");
        this.semester = SemesterModel.getInstance().add(jsonCourse.getJSONObject("course_semester"), false);
    }

    public int getId() {
        return id;
    }
    public String getCode() {
        return code;
    }
    public String getName() {
        return name;
    }
    public double getCredit() {
        return credit;
    }
    public Semester getSemester() {
        return semester;
    }
    @Override
    public String toString() {
        return this.code + "-" + this.semester;
    }

    public String getValText() {
        return id + "";
    }

}
