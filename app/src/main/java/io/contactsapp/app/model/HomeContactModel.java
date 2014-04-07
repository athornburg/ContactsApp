package io.contactsapp.app.model;

import java.io.Serializable;

/**
 * Created by alexthornburg on 4/4/14.
 */
public class HomeContactModel implements Serializable{
    private int id;
    private String name;
    private int employeeId;
    private String company;
    private String detailsURL;
    private String smallImageURL;
    private String birthdate;
    private Phone phone;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDetailsURL() {
        return detailsURL;
    }

    public void setDetailsURL(String detailsURL) {
        this.detailsURL = detailsURL;
    }

    public String getSmallImageURL() { return smallImageURL; }

    public void setSmallImageURL(String smallImageURL) {
        this.smallImageURL = smallImageURL;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public Phone getPhone() { return phone; }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }
}
