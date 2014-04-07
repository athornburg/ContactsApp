package io.contactsapp.app.model;

import java.io.Serializable;

/**
 * Created by alexthornburg on 4/5/14.
 */
public class Phone implements Serializable{
    private String home;
    private String mobile;
    private String work;

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }
}
