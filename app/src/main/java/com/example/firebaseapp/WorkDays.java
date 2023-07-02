package com.example.firebaseapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class WorkDays {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private String id;
    private String project_id;
    private String employee_id;
    private String name;
    private String start_time;
    private String end_time;
    private String created_at;
    private String totalHours;

    public WorkDays(String id, String project_id, String name, String start_time, String end_time) {
        this.id = id;
        this.project_id = project_id;
        this.name = name;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public WorkDays(String name, String start_time, String end_time, String employee_id) {
        this.name = name;
        this.start_time = start_time;
        this.end_time = end_time;
        this.employee_id = employee_id;
        this.created_at = dateFormat.format(new Date());
        this.totalHours = calculateHours(start_time, end_time);
    }
    public static String calculateHours(String startTime, String endTime) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        try {
            Date startDate = format.parse(startTime);
            Date endDate = format.parse(endTime);
            long diffInMilliseconds = endDate.getTime() - startDate.getTime();
            long diffInHours = TimeUnit.MILLISECONDS.toHours(diffInMilliseconds);
            return String.valueOf(diffInHours);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "0";
    }

    public String getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(String totalHours) {
        this.totalHours = totalHours;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
}
