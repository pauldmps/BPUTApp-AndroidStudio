package com.paulshantanu.bputapp;

import java.util.ArrayList;

/**
 * Created by Shantanu Paul on 06-11-2015.
 */
public class Schedule {


private ArrayList<String> semester = new ArrayList<String>();
private ArrayList<String> branch = new ArrayList<String>();
private ArrayList<String> index = new ArrayList<String>();
private ArrayList<String> date = new ArrayList<String>();
private ArrayList<String> sitting = new ArrayList<String>();
private ArrayList<String> code = new ArrayList<String>();
private ArrayList<String> subject = new ArrayList<String>();


    public ArrayList<String> getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index.add(index);
    }

    public ArrayList<String> getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date.add(date);
    }

    public ArrayList<String> getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code.add(code);
    }

    public ArrayList<String> getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject.add(subject);
    }

    public ArrayList<String> getSitting() {
        return sitting;
    }

    public void setSitting(String sitting) {
        this.sitting.add(sitting);
    }

    public ArrayList<String> getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester.add(semester);
    }

    public ArrayList<String> getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch.add(branch);
    }
}
