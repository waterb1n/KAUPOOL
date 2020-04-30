package com.water.kaupool;

public class member {
    private String member_id;
    private String member_pw;
    private String member_name;
    private String member_phone;
    private String member_gender;

    public member(String member_id, String member_pw, String member_name, String member_phone, String member_gender) {
        this.member_id = member_id;
        this.member_pw = member_pw;
        this.member_name = member_name;
        this.member_phone = member_phone;
        this.member_gender = member_gender;
    }

    public member() {
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getMember_pw() {
        return member_pw;
    }

    public void setMember_pw(String member_pw) {
        this.member_pw = member_pw;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getMember_phone() {
        return member_phone;
    }

    public void setMember_gender(String member_gender) {
        this.member_gender = member_gender;
    }

}
