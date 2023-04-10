package com.siweb.model;

import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Semester implements TableViewModel {

    private final int id;
    private final String year;
    private final int semester;
    private final LocalDate dateStart;
    private final LocalDate dateEnd;

    public Semester(JSONObject jsonSemester) {
        this.id = jsonSemester.getInt("id");
        this.year = jsonSemester.getString("year");
        this.semester = jsonSemester.getInt("semester");
        this.dateStart = LocalDate.parse(jsonSemester.getString("date_start"));
        this.dateEnd = LocalDate.parse(jsonSemester.getString("date_end"));
    }

    public int getId() {
        return id;
    }
    public String getYear() {
        return year;
    }
    public int getSemester() {
        return semester;
    }
    public LocalDate getDateStart() {
        return dateStart;
    }
    public LocalDate getDateEnd() {
        return dateEnd;
    }

    @Override
    public String toString() {
        return this.year + "-" + this.semester;
    }

    public String getValText() {
        return id + "";
    }

}
