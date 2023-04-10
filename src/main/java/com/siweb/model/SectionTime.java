package com.siweb.model;

import org.json.JSONObject;

public class SectionTime implements TableViewModel {
    private final int id;
    private final Section section;
    private final TimeSlot timeSlot;

    public SectionTime(JSONObject jsonSectionTime){
        this.id = jsonSectionTime.getInt("id");
        this.section = SectionModel.getInstance().add(jsonSectionTime.getJSONObject("section"), false);
        this.timeSlot = TimeSlotModel.getInstance().add(jsonSectionTime.getJSONObject("time_slot"), false);
    }

    public int getId() {
        return id;
    }
    public Section getSection() {
        return section;
    }
    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    @Override
    public String toString() {
        return this.section + "-" + this.timeSlot;
    }

    public String getValText() {
        return id + "";
    }

}
