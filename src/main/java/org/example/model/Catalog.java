package org.example.model;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

public class Catalog {
    private List<Plant> plants;
    private String uuid;
    private Timestamp date;
    private String company;

    public List<Plant> getPlants() {
        return plants;
    }

    public String getUuid() {
        return uuid;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getCompany() {
        return company;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
