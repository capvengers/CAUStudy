package com.example.caustudy;

public class CreateStudy {

    public String study_name = "";
    public String category = "";
    public String study_leader_id = "";
    public String organization = "";


    public CreateStudy() {

    }

    public CreateStudy(String study_name, String category, String study_leader_id, String organization) {
        this.study_name = study_name;
        this.category = category;
        this.study_leader_id = study_leader_id;
        this.organization = organization;
    }
}