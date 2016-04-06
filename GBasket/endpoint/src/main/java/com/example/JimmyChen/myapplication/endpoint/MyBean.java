package com.example.JimmyChen.myapplication.endpoint;

/** The object model for the data we are sending through endpoints */
public class MyBean {

    String email;
    String pw;
    String fname;
    String lname;

    public MyBean() {

    }

    private String myData;

    public String getData() {
        return myData;
    }

    public void setData(String data) {
        myData = data;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public MyBean(String email) {
        super();
        this.email = email;
    }

    public MyBean(String email, String pw) {
        super();
        this.email = email;
        this.pw = pw;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        for (int i = 0; i < email.length(); i++) {
            hash = hash*31 + email.charAt(i);
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MyBean other = (MyBean) obj;
        if (email.equals("")) {
            if (!other.email.equals(""))
                return false;
        } else if (!email.equals(other.email))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "User [email=" + email + ", fname=" + fname + ", lname="
                + lname + "]";
    }

}